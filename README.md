# MiniGo Transpiler

MiniGo Transpiler is a small educational compiler project written in Java.
It transpiles a tiny subset of Go-like syntax into valid C code.

This is not a real Go compiler. It intentionally supports only a very small integer-based language so the compiler pipeline stays easy to understand.

The pipeline is:

```text
MiniGo source
-> Lexer
-> Tokens
-> Parser
-> AST
-> C Code Generator
-> output.c
```

## How To Run

Run all commands from the project directory:

```bash
cd .../MiniGoTranspiler
```

Compile the Java transpiler:

```bash
javac $(find src -name '*.java')
```

Run the transpiler using the default input file:

```bash
java -cp src Main
```

By default, this reads:

```text
input.go
```

And writes:

```text
output.c
```

You will also see the generated C code printed in the terminal.

## Run With Custom Files

You can pass an input MiniGo file and an output C file:

```bash
java -cp src Main input.go output.c
```

Example:

```bash
java -cp src Main examples/simple.go generated/simple.c
```

The first argument is the MiniGo input file.
The second argument is the generated C output file.

## Test The Generated C

If you have `clang` installed, check that the generated C is valid:

```bash
clang -fsyntax-only output.c
```

If there is no output, the C syntax is valid.

You can also compile and run it:

```bash
clang output.c -o output
./output
```

## Clean Java Build Files

The `javac` command creates `.class` files inside `src`.
To remove them:

```bash
find src -name '*.class' -delete
```

## Minimal Working Example

Write this in `input.go`:

```go
func main() {
    x := 5 + 3
    println(x)
}
```

Run:

```bash
javac $(find src -name '*.java')
java -cp src Main
```

Generated `output.c`:

```c
#include <stdio.h>

int main() {
    int x = (5 + 3);
    printf("%d\n", x);

    return 0;
}
```

## What Works

Version 1 supports only these MiniGo features.

### Main Function

Every program must start with exactly:

```go
func main() {
    // statements here
}
```

Only `func main()` is supported.

Works:

```go
func main() {
    println(1)
}
```

Does not work:

```go
func add() {
    println(1)
}
```

### Integer Variables

Variables are declared with `:=`.
All variables are generated as C `int`.

Works:

```go
func main() {
    x := 10
    y := x + 5
    println(y)
}
```

Generated C uses:

```c
int x = 10;
int y = (x + 5);
```

### Integer Literals

Only whole numbers are supported.

Works:

```go
func main() {
    x := 123
    println(x)
}
```

Does not work:

```go
func main() {
    x := 1.5
}
```

### Identifiers

Variable names can contain letters, digits, and underscores.
They must start with a letter.

Works:

```go
func main() {
    count_1 := 7
    println(count_1)
}
```

### Arithmetic

Supported arithmetic operators:

```text
+  -  *  /
```

Works:

```go
func main() {
    x := 5 + 3 * 2
    y := (5 + 3) * 2
    println(x)
    println(y)
}
```

The parser supports precedence:

```text
* and / happen before + and -
```

Generated C preserves expressions with parentheses.

### Comparisons

Supported comparison operators:

```text
<  >  ==  !=  <=  >=
```

Works:

```go
func main() {
    x := 10
    if x >= 5 {
        println(x)
    }
}
```

Comparisons are integer comparisons.

### Println

`println()` prints one integer expression.

Works:

```go
func main() {
    x := 10
    println(x)
    println(5 + 3)
}
```

Generated C uses:

```c
printf("%d\n", value);
```

Does not work:

```go
func main() {
    println("hello")
}
```

Strings are not supported.

### If Statements

`if` statements support a condition and a block.

Works:

```go
func main() {
    x := 10
    if x > 5 {
        println(x)
    }
}
```

Nested blocks also work:

```go
func main() {
    x := 10
    if x > 5 {
        if x != 0 {
            println(x)
        }
    }
}
```

Does not work:

```go
func main() {
    x := 10
    if x > 5 {
        println(x)
    } else {
        println(0)
    }
}
```

`else` is not supported.

### For Loops

MiniGo supports only simple condition-only `for` loops.
They are generated as C `while` loops.

Works:

```go
func main() {
    x := 1
    for x < 3 {
        println(x)
    }
}
```

Generated C:

```c
while ((x < 3)) {
    printf("%d\n", x);
}
```

Important: there is currently no assignment statement, so loops cannot update variables yet.
That means many loops will be infinite if you compile and run the generated C.

This works syntactically:

```go
func main() {
    x := 1
    for x < 3 {
        println(x)
    }
}
```

But it runs forever because `x` never changes.

### Blocks

Blocks use braces:

```go
{
    println(1)
}
```

Blocks are supported inside:

```text
func main
if
for
```

## What Does Not Work

This project deliberately does not support full Go.

Unsupported features include:

- `package`
- `import`
- functions other than `main`
- function parameters
- function return values
- `return` in MiniGo source
- variable reassignment with `=`
- `else`
- `else if`
- `for` loops with init/update clauses
- `break`
- `continue`
- booleans
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
- real Go type inference
- Go standard library calls

## Syntax Rules

MiniGo is intentionally strict.

Statements do not need semicolons:

```go
func main() {
    x := 5
    println(x)
}
```

Parentheses are supported in expressions:

```go
func main() {
    x := (5 + 3) * 2
    println(x)
}
```

Conditions do not require parentheses:

```go
func main() {
    x := 5
    if x > 3 {
        println(x)
    }
}
```

This is also accepted because parentheses are valid expressions:

```go
func main() {
    x := 5
    if (x > 3) {
        println(x)
    }
}
```

## Full Example

`input.go`:

```go
func main() {
    x := 5 + 3 * 2
    println(x)
    if x >= 10 {
        println(x)
    }
    for x < 3 {
        println(x)
    }
}
```

Generated `output.c`:

```c
#include <stdio.h>

int main() {
    int x = (5 + (3 * 2));
    printf("%d\n", x);
    if ((x >= 10)) {
        printf("%d\n", x);
    }
    while ((x < 3)) {
        printf("%d\n", x);
    }

    return 0;
}
```

## Error Handling

The transpiler throws readable runtime errors when it finds invalid syntax or unsupported characters.

Example invalid input:

```go
func main() {
    x := 5 @ 3
}
```

Possible error:

```text
Lexer error at line 2, column 12: unexpected character '@'
```

Example parser error:

```go
func main() {
    println(5
}
```

Possible error:

```text
Parser error at line 3, column 1: Expected RPAREN but got RBRACE
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

### 1. Lexer

The lexer reads raw MiniGo source code and produces tokens.

Example:

```go
x := 5 + 3
```

Becomes tokens like:

```text
IDENTIFIER(x)
DECLARE(:=)
NUMBER(5)
PLUS(+)
NUMBER(3)
```

### 2. Parser

The parser reads tokens and builds an AST.
It uses recursive descent parsing.

Important parser methods include:

- `parseExpression()`
- `parseComparison()`
- `parseAddition()`
- `parseMultiplication()`
- `parsePrimary()`

### 3. AST

The AST represents the program as Java objects.

Examples:

- `Program`
- `VariableDeclaration`
- `PrintStatement`
- `IfStatement`
- `ForStatement`
- `BinaryExpression`
- `NumberExpression`
- `IdentifierExpression`

### 4. C Code Generator

The code generator walks the AST and produces C code.

Examples:

```go
println(x)
```

Becomes:

```c
printf("%d\n", x);
```

And:

```go
for x < 10 {
    println(x)
}
```

Becomes:

```c
while ((x < 10)) {
    printf("%d\n", x);
}
```

## Good Test Cases

### Arithmetic Precedence

```go
func main() {
    x := 5 + 3 * 2
    println(x)
}
```

Expected behavior:

```text
3 * 2 is grouped before 5 +
```

### Parentheses

```go
func main() {
    x := (5 + 3) * 2
    println(x)
}
```

Expected behavior:

```text
5 + 3 is grouped before * 2
```

### If Statement

```go
func main() {
    x := 10
    if x == 10 {
        println(x)
    }
}
```

### Nested If Statement

```go
func main() {
    x := 10
    if x > 0 {
        if x < 20 {
            println(x)
        }
    }
}
```

### Lexer Error

```go
func main() {
    x := 5 @ 3
}
```

Expected behavior:

```text
The lexer reports an invalid character.
```

### Parser Error

```go
func main() {
    println(5
}
```

Expected behavior:

```text
The parser reports a missing closing parenthesis.
```

## Current Limitations

The biggest current limitation is that variables can be declared but not reassigned.

This means you can write:

```go
x := 1
```

But you cannot write:

```go
x = x + 1
```

Because reassignment is not implemented yet.

This affects `for` loops because there is no way to update the loop variable inside the loop.

The next natural feature would be assignment statements:

```go
func main() {
    x := 1
    for x < 3 {
        println(x)
        x = x + 1
    }
}
```

That is not supported in the current version.
