import lexer.Lexer;
import lexer.Token;

import parser.Parser;

import ast.Module;
import ast.Program;

import codegen.CGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Path> inputPaths = new ArrayList<>();
        Path outputPath = Path.of("output.c");

        if (args.length == 0) {
            inputPaths.add(Path.of("input1.go"));
            inputPaths.add(Path.of("input2.go"));
        } else {
            for (String arg : args) {
                if (arg.endsWith(".c")) {
                    outputPath = Path.of(arg);
                } else {
                    inputPaths.add(Path.of(arg));
                }
            }
        }

        if (inputPaths.isEmpty()) {
            throw new RuntimeException("No MiniGo input files provided");
        }

        List<Module> modules = new ArrayList<>();

        for (Path inputPath : inputPaths) {
            String code = Files.readString(inputPath);

            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
            modules.add(parser.parseModule());
        }

        Program program = new Program(modules);

        CGenerator generator = new CGenerator();
        String cCode = generator.generate(program);

        Files.writeString(outputPath, cCode);
        System.out.println(cCode);
        System.out.println("Input MiniGo files: " + inputPaths);
        System.out.println("Generated C file: " + outputPath.toAbsolutePath());
    }
}
