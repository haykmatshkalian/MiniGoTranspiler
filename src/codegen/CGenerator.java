package codegen;

import ast.Program;

import ast.statements.BlockStatement;
import ast.statements.ForStatement;
import ast.statements.IfStatement;
import ast.statements.Statement;
import ast.statements.VariableDeclaration;
import ast.statements.PrintStatement;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.expressions.NumberExpression;
import ast.expressions.IdentifierExpression;

public class CGenerator {

    public String generate(Program program) {

        StringBuilder sb = new StringBuilder();

        sb.append("#include <stdio.h>\n\n");
        sb.append("int main() {\n");

        generateStatements(program.mainBlock.statements, sb, 1);

        sb.append("\n    return 0;\n");
        sb.append("}\n");

        return sb.toString();
    }

    private void generateStatements(Iterable<Statement> statements, StringBuilder sb, int indentLevel) {
        for (Statement stmt : statements) {
            generateStatement(stmt, sb, indentLevel);
        }
    }

    private void generateStatement(Statement stmt, StringBuilder sb, int indentLevel) {
        if (stmt instanceof VariableDeclaration varDecl) {
            sb.append(indent(indentLevel))
                .append("int ")
                .append(varDecl.name)
                .append(" = ")
                .append(generateExpression(varDecl.value))
                .append(";\n");
            return;
        }

        if (stmt instanceof PrintStatement printStmt) {
            sb.append(indent(indentLevel))
                .append("printf(\"%d\\n\", ")
                .append(generateExpression(printStmt.expression))
                .append(");\n");
            return;
        }

        if (stmt instanceof IfStatement ifStmt) {
            sb.append(indent(indentLevel))
                .append("if (")
                .append(generateExpression(ifStmt.condition))
                .append(") {\n");
            generateBlock(ifStmt.body, sb, indentLevel);
            sb.append(indent(indentLevel)).append("}\n");
            return;
        }

        if (stmt instanceof ForStatement forStmt) {
            sb.append(indent(indentLevel))
                .append("while (")
                .append(generateExpression(forStmt.condition))
                .append(") {\n");
            generateBlock(forStmt.body, sb, indentLevel);
            sb.append(indent(indentLevel)).append("}\n");
            return;
        }

        throw new RuntimeException("Code generation error: unknown statement type");
    }

    private void generateBlock(BlockStatement block, StringBuilder sb, int parentIndentLevel) {
        generateStatements(block.statements, sb, parentIndentLevel + 1);
    }

    private String generateExpression(Expression expr) {

        if (expr instanceof NumberExpression num) {
            return String.valueOf(num.value);
        }

        if (expr instanceof IdentifierExpression id) {
            return id.name;
        }

        if (expr instanceof BinaryExpression binary) {
            return "(" +
                generateExpression(binary.left) +
                " " +
                binary.operator +
                " " +
                generateExpression(binary.right) +
                ")";
        }

        throw new RuntimeException("Code generation error: unknown expression type");
    }

    private String indent(int level) {
        return "    ".repeat(level);
    }
}
