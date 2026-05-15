SRC_DIR := src
INPUT := input.go
OUTPUT_C := output.c
OUTPUT_BIN := output
JAVA_MAIN := Main

.PHONY: all compile transpile check-c build-c run-c run clean help

all: run

compile:
	javac $$(find $(SRC_DIR) -name '*.java')

transpile: compile
	java -cp $(SRC_DIR) $(JAVA_MAIN) $(INPUT) $(OUTPUT_C)

check-c: transpile
	clang -fsyntax-only $(OUTPUT_C)

build-c: check-c
	clang $(OUTPUT_C) -o $(OUTPUT_BIN)

run-c: build-c
	./$(OUTPUT_BIN)

run: run-c

clean:
	find $(SRC_DIR) -name '*.class' -delete
	rm -f $(OUTPUT_BIN)

help:
	@echo "MiniGo Transpiler commands:"
	@echo "  make compile    Compile the Java transpiler"
	@echo "  make transpile  Generate output.c from input.go"
	@echo "  make check-c    Check generated C syntax with clang"
	@echo "  make build-c    Build generated C into ./output"
	@echo "  make run-c      Build and run generated C"
	@echo "  make run        Same as make run-c"
	@echo "  make clean      Remove Java .class files and ./output"
