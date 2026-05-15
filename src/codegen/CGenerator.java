package codegen;

import ast.FunctionDeclaration;
import ast.Module;
import ast.Program;

import ast.statements.BlockStatement;
import ast.statements.ForStatement;
import ast.statements.FunctionCallStatement;
import ast.statements.IfStatement;
import ast.statements.Statement;
import ast.statements.VariableDeclaration;
import ast.statements.PrintStatement;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.expressions.NumberExpression;
import ast.expressions.IdentifierExpression;

import java.util.HashSet;
import java.util.Set;

public class CGenerator {

    public String generate(Program program) {
        StringBuilder sb = new StringBuilder();

        validateFunctions(program);

        sb.append("#include <stdio.h>\n\n");

        FunctionDeclaration mainFunction = program.mainFunction();
        generatePrototypes(program, sb);
        generateMainFunction(mainFunction, sb);
        generateHelperFunctions(program, sb);

        return sb.toString();
    }

    private void validateFunctions(Program program) {
        Set<String> functions = new HashSet<>();

        for (Module module : program.modules) {
            for (FunctionDeclaration function : module.functions) {
                String cName = cFunctionName(function);

                if (!functions.add(cName)) {
                    throw new RuntimeException(
                        "Program error: duplicate function '" +
                        function.moduleName + "." + function.name + "'"
                    );
                }
            }
        }

        program.mainFunction();

        for (Module module : program.modules) {
            for (FunctionDeclaration function : module.functions) {
                validateFunctionCalls(function.body, functions);
            }
        }
    }

    private void validateFunctionCalls(BlockStatement block, Set<String> functions) {
        for (Statement stmt : block.statements) {
            if (stmt instanceof FunctionCallStatement callStmt) {
                String cName = cFunctionName(callStmt.moduleName, callStmt.functionName);

                if (!functions.contains(cName)) {
                    throw new RuntimeException(
                        "Program error: unknown function call '" +
                        callStmt.moduleName + "." + callStmt.functionName + "()'"
                    );
                }
            }

            if (stmt instanceof IfStatement ifStmt) {
                validateFunctionCalls(ifStmt.body, functions);
            }

            if (stmt instanceof ForStatement forStmt) {
                validateFunctionCalls(forStmt.body, functions);
            }
        }
    }

    private void generatePrototypes(Program program, StringBuilder sb) {
        for (Module module : program.modules) {
            for (FunctionDeclaration function : module.functions) {
                if (!function.isMain()) {
                    sb.append("void ")
                        .append(cFunctionName(function))
                        .append("();\n");
                }
            }
        }

        sb.append("\n");
    }

    private void generateMainFunction(FunctionDeclaration function, StringBuilder sb) {
        sb.append("int main() {\n");

        generateStatements(function.body.statements, sb, 1, function.moduleName);

        sb.append("\n    return 0;\n");
        sb.append("}\n\n");
    }

    private void generateHelperFunctions(Program program, StringBuilder sb) {
        for (Module module : program.modules) {
            for (FunctionDeclaration function : module.functions) {
                if (!function.isMain()) {
                    generateHelperFunction(function, sb);
                }
            }
        }
    }

    private void generateHelperFunction(FunctionDeclaration function, StringBuilder sb) {
        sb.append("void ")
            .append(cFunctionName(function))
            .append("() {\n");

        generateStatements(function.body.statements, sb, 1, function.moduleName);

        sb.append("}\n\n");
    }

    private void generateStatements(
            Iterable<Statement> statements,
            StringBuilder sb,
            int indentLevel,
            String currentModule
    ) {
        for (Statement stmt : statements) {
            generateStatement(stmt, sb, indentLevel, currentModule);
        }
    }

    private void generateStatement(
            Statement stmt,
            StringBuilder sb,
            int indentLevel,
            String currentModule
    ) {
        if (stmt instanceof VariableDeclaration varDecl) {
            sb.append(indent(indentLevel))
                .append("int ")
                .append(cVariableName(currentModule, varDecl.name))
                .append(" = ")
                .append(generateExpression(varDecl.value, currentModule))
                .append(";\n");
            return;
        }

        if (stmt instanceof PrintStatement printStmt) {
            sb.append(indent(indentLevel))
                .append("printf(\"%d\\n\", ")
                .append(generateExpression(printStmt.expression, currentModule))
                .append(");\n");
            return;
        }

        if (stmt instanceof FunctionCallStatement callStmt) {
            sb.append(indent(indentLevel))
                .append(cFunctionName(callStmt.moduleName, callStmt.functionName))
                .append("();\n");
            return;
        }

        if (stmt instanceof IfStatement ifStmt) {
            sb.append(indent(indentLevel))
                .append("if ")
                .append(generateCondition(ifStmt.condition, currentModule))
                .append(" {\n");
            generateBlock(ifStmt.body, sb, indentLevel, currentModule);
            sb.append(indent(indentLevel)).append("}\n");
            return;
        }

        if (stmt instanceof ForStatement forStmt) {
            sb.append(indent(indentLevel))
                .append("while ")
                .append(generateCondition(forStmt.condition, currentModule))
                .append(" {\n");
            generateBlock(forStmt.body, sb, indentLevel, currentModule);
            sb.append(indent(indentLevel)).append("}\n");
            return;
        }

        throw new RuntimeException("Code generation error: unknown statement type");
    }

    private void generateBlock(
            BlockStatement block,
            StringBuilder sb,
            int parentIndentLevel,
            String currentModule
    ) {
        generateStatements(block.statements, sb, parentIndentLevel + 1, currentModule);
    }

    private String generateExpression(Expression expr, String currentModule) {

        if (expr instanceof NumberExpression num) {
            return String.valueOf(num.value);
        }

        if (expr instanceof IdentifierExpression id) {
            return cVariableName(currentModule, id.name);
        }

        if (expr instanceof BinaryExpression binary) {
            return "(" +
                generateExpression(binary.left, currentModule) +
                " " +
                binary.operator +
                " " +
                generateExpression(binary.right, currentModule) +
                ")";
        }

        throw new RuntimeException("Code generation error: unknown expression type");
    }

    private String generateCondition(Expression expr, String currentModule) {
        String condition = generateExpression(expr, currentModule);

        if (condition.startsWith("(") && condition.endsWith(")")) {
            return condition;
        }

        return "(" + condition + ")";
    }

    private String cFunctionName(FunctionDeclaration function) {
        return cFunctionName(function.moduleName, function.name);
    }

    private String cFunctionName(String moduleName, String functionName) {
        return moduleName + "_" + functionName;
    }

    private String cVariableName(String moduleName, String variableName) {
        return moduleName + "_" + variableName;
    }

    private String indent(int level) {
        return "    ".repeat(level);
    }
}
