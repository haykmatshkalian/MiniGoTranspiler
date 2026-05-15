import lexer.Lexer;
import lexer.Token;

import parser.Parser;

import ast.Program;

import codegen.CGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of(args.length > 0 ? args[0] : "input.go");
        Path outputPath = Path.of(args.length > 1 ? args[1] : "output.c");

        String code = Files.readString(inputPath);

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        Program program = parser.parse();

        CGenerator generator = new CGenerator();
        String cCode = generator.generate(program);

        Files.writeString(outputPath, cCode);
        System.out.println(cCode);
        System.out.println("Generated C file: " + outputPath.toAbsolutePath());
    }
}
