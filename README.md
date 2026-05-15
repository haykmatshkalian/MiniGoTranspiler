# MiniGo Transpiler

MiniGo Transpiler is a small educational compiler project written in Java.
It transpiles a tiny Go-like language into valid C code.

This is not a real Go compiler. MiniGo intentionally supports only a small subset of Go-like syntax so the compiler pipeline stays clean and understandable.

```text
MiniGo source files
-> Lexer
-> Tokens
-> Parser
-> AST
-> C Code Generator
-> output.c
```

## Quick Run

Run everything from the project directory:

```bash
cd /Users/hayk/Desktop/MiniGoTranspiler
make run
```

`make run` does the full flow:

```text
compile Java files
-> transpile input1.go and input2.go into output.c
-> check output.c with clang
-> compile output.c into ./output
-> run ./output
```

## Default Files

The default MiniGo input files are:

```text
input1.go
input2.go
```

The generated C file is:

```text
output.c
```

The compiled C executable is:

```text
output
```

## Make Commands

Show commands:

```bash
make help
```

Compile the Java transpiler:

```bash
make compile
```

Generate `output.c`:

```bash
make transpile
```

Check that generated C is valid:

```bash
make check-c
```

Build the generated C program:

```bash
make build-c
```

Run the generated C program:

```bash
make run-c
```

Do everything:

```bash
make run
```

Clean generated Java `.class` files and the compiled C executable:

```bash
make clean
```

## Manual Commands

You usually do not need these because `make run` does the work.

Manual full flow:

```bash
javac $(find src -name '*.java')
java -cp src Main input1.go input2.go output.c
clang -fsyntax-only output.c
clang output.c -o output
./output
```

## Module Example

MiniGo now supports multiple files using simple modules.

`input1.go`:

```go
module m1

func main() {
    ticketPrice := 45
    visitorCount := 6
    serviceFee := 12
    discountLimit := 250

    ticketSubtotal := ticketPrice * visitorCount
    estimatedTotal := ticketSubtotal + serviceFee
    averagePerVisitor := estimatedTotal / visitorCount

    println(ticketSubtotal)
    println(estimatedTotal)
    println(averagePerVisitor)

    if estimatedTotal >= discountLimit {
        discountAmount := estimatedTotal / 10
        finalTotal := estimatedTotal - discountAmount
        println(discountAmount)
        println(finalTotal)
    }

    if averagePerVisitor < ticketPrice {
        println(averagePerVisitor)
    }

    m2.printBudgetReport()
    m2.printSafetyReport()

    for estimatedTotal < 0 {
        println(estimatedTotal)
    }
}
```

`input2.go`:

```go
module m2

func printBudgetReport() {
    mealBudget := 80
    transportBudget := 35
    museumBudget := 60
    emergencyReserve := 25

    plannedExpenses := mealBudget + transportBudget + museumBudget
    requiredBudget := plannedExpenses + emergencyReserve

    println(plannedExpenses)
    println(requiredBudget)

    if requiredBudget > 150 {
        extraBuffer := requiredBudget - 150
        println(extraBuffer)
    }
}

func printSafetyReport() {
    expectedVisitors := 6
    staffMembers := 2
    visitorsPerStaff := expectedVisitors / staffMembers

    println(visitorsPerStaff)

    if visitorsPerStaff <= 3 {
        println(1)
    }
}
```

Run:

```bash
make run
```

Generated C shape:

```c
#include <stdio.h>

void m2_printBudgetReport();
void m2_printSafetyReport();

int main() {
    int m1_ticketPrice = 45;
    int m1_visitorCount = 6;
    int m1_ticketSubtotal = (m1_ticketPrice * m1_visitorCount);
    printf("%d\n", m1_ticketSubtotal);
    m2_printBudgetReport();
    m2_printSafetyReport();

    return 0;
}
```

Program output:

```text
270
282
47
28
254
175
200
50
3
1
```

## Module Rules

Every MiniGo file must start with a module declaration:

```go
module m1
```

Each module can contain functions:

```go
module m2

func helpMe() {
    println(123)
}
```

Exactly one function named `main` must exist across all input files:

```go
module m1

func main() {
    println(1)
}
```

Functions from another module are called with dot syntax:

```go
m2.printBudgetReport()
```

Generated C function names are prefixed:

```text
m2.printBudgetReport() -> m2_printBudgetReport()
```

Generated C variable names are also prefixed by module:

```text
x        in module m1 -> m1_x
mealBudget in module m2 -> m2_mealBudget
```

This avoids C name collisions when two modules use the same variable or function name.

## What Works

MiniGo currently supports these features.

### Modules

Works:

```go
module m1

func main() {
    m2.printBudgetReport()
}
```

```go
module m2

func helpMe() {
    println(10)
}
```

Does not work:

```go
package main
```

MiniGo uses `module`, not real Go `package`.

### Functions

Works:

```go
module m1

func main() {
    println(1)
}

func helper() {
    println(2)
}
```

Only functions with no parameters and no return values are supported.

Does not work:

```go
func add(a int, b int) int {
    return a + b
}
```

### Function Calls

Works:

```go
m2.printBudgetReport()
```

Function calls are statements only.

Does not work:

```go
x := m2.printBudgetReport()
```

Return values are not supported yet.

### Integer Variables

Variables are declared with `:=`.
All variables become C `int`.

Works:

```go
module m1

func main() {
    x := 10
    y := x + 5
    println(y)
}
```

Generated C:

```c
int m1_x = 10;
int m1_y = (m1_x + 5);
```

### Integer Literals

Works:

```go
value := 123
```

Does not work:

```go
value := 1.5
```

Floats are not supported.

### Arithmetic

Supported operators:

```text
+  -  *  /
```

Works:

```go
result := ((20 + 4) * 2) - (20 / 4)
```

Operator precedence is supported:

```text
* and / bind tighter than + and -
```

### Comparisons

Supported comparison operators:

```text
<  >  ==  !=  <=  >=
```

Works:

```go
if x >= 10 {
    println(x)
}
```

Comparisons are integer-based in generated C.

### Println

`println` prints one integer expression:

```go
println(x)
println(5 + 3)
```

Generated C:

```c
printf("%d\n", m1_x);
```

Does not work:

```go
println("hello")
```

Strings are not supported.

### If Statements

Works:

```go
if x > 5 {
    println(x)
}
```

Nested `if` statements work:

```go
if x > 0 {
    if x < 100 {
        println(x)
    }
}
```

Does not work:

```go
if x > 5 {
    println(x)
} else {
    println(0)
}
```

`else` is not supported yet.

### For Loops

Only condition-only loops are supported:

```go
for x < 3 {
    println(x)
}
```

Generated C:

```c
while (m1_x < 3) {
    printf("%d\n", m1_x);
}
```

Important: assignment is not supported yet, so you cannot update `x` inside the loop.
If the loop condition starts true, the generated C program can run forever.

## What Does Not Work

MiniGo does not support:

- real Go `package`
- real Go `import`
- function parameters
- function return values
- `return`
- assignment with `=`
- `else`
- `break`
- `continue`
- booleans as a separate type
- strings
- floats
- arrays
- slices
- maps
- structs
- interfaces
- pointers
- methods
- goroutines
- channels
- comments
- Go standard library calls
- real Go type checking
- real Go runtime behavior

## Syntax Summary

A valid multi-file MiniGo program looks like this:

```go
module moduleName

func functionName() {
    variable := 123
    println(variable)
    otherModule.otherFunction()

    if variable > 0 {
        println(variable)
    }

    for variable < 0 {
        println(variable)
    }
}
```

One file somewhere must contain:

```go
func main() {
    // program starts here
}
```

## Error Handling

The transpiler throws readable errors for invalid characters, invalid syntax, missing `main`, duplicate `main`, duplicate generated function names, or unknown function calls.

Invalid character example:

```go
module m1

func main() {
    x := 5 @ 3
}
```

Possible error:

```text
Lexer error at line 4, column 12: unexpected character '@'
```

Unknown function example:

```go
module m1

func main() {
    m2.missing()
}
```

Possible error:

```text
Program error: unknown function call 'm2.missing()'
```

## Project Structure

```text
src/
 ├── lexer/
 │    ├── Lexer.java
 │    ├── Token.java
 │    └── TokenType.java
 │
 ├── parser/
 │    └── Parser.java
 │
 ├── ast/
 │    ├── FunctionDeclaration.java
 │    ├── Module.java
 │    ├── Program.java
 │    │
 │    ├── expressions/
 │    │    ├── BinaryExpression.java
 │    │    ├── Expression.java
 │    │    ├── IdentifierExpression.java
 │    │    └── NumberExpression.java
 │    │
 │    └── statements/
 │         ├── BlockStatement.java
 │         ├── ForStatement.java
 │         ├── FunctionCallStatement.java
 │         ├── IfStatement.java
 │         ├── PrintStatement.java
 │         ├── Statement.java
 │         └── VariableDeclaration.java
 │
 ├── codegen/
 │    └── CGenerator.java
 │
 └── Main.java
```

## How The Compiler Works

The lexer turns source text into tokens.

The parser reads tokens and builds AST objects such as `Module`, `FunctionDeclaration`, `VariableDeclaration`, `IfStatement`, and `BinaryExpression`.

The C generator walks the AST and emits C code. During generation it prefixes symbols:

```text
module m1 variable x       -> m1_x
module m2 function helpMe  -> m2_printBudgetReport
```

The generated C is then checked and compiled with `clang`.

## Good Test Cases

### Cross-Module Function Call

```go
module m1

func main() {
    m2.printBudgetReport()
}
```

```go
module m2

func helpMe() {
    println(42)
}
```

### Same Variable Name In Different Modules

```go
module m1

func main() {
    value := 1
    println(value)
    m2.printValue()
}
```

```go
module m2

func printValue() {
    value := 2
    println(value)
}
```

Generated C uses `m1_value` and `m2_value`.

### Missing Function Error

```go
module m1

func main() {
    m2.nope()
}
```

This should fail before C compilation.

## Current Best Next Features

Good next steps:

- assignment statements: `x = x + 1`
- `else`
- function parameters
- function return values
- comments
- simple booleans
- strings
