package parser;

import lexer.Token;
import lexer.TokenType;

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

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Program parse() {
        List<Module> modules = new ArrayList<>();
        modules.add(parseModule());
        consume(TokenType.EOF);
        return new Program(modules);
    }

    public Module parseModule() {
        consume(TokenType.MODULE);
        String moduleName = consume(TokenType.IDENTIFIER).value;

        List<FunctionDeclaration> functions = new ArrayList<>();

        while (!match(TokenType.EOF)) {
            functions.add(parseFunction(moduleName));
        }

        return new Module(moduleName, functions);
    }

    private FunctionDeclaration parseFunction(String moduleName) {
        consume(TokenType.FUNC);
        String functionName = parseFunctionName();
        consume(TokenType.LPAREN);
        consume(TokenType.RPAREN);

        BlockStatement body = parseBlock();

        return new FunctionDeclaration(moduleName, functionName, body);
    }

    private String parseFunctionName() {
        if (match(TokenType.MAIN)) {
            return consume(TokenType.MAIN).value;
        }

        return consume(TokenType.IDENTIFIER).value;
    }

    private BlockStatement parseBlock() {
        List<Statement> statements = new ArrayList<>();

        consume(TokenType.LBRACE);

        while (!match(TokenType.RBRACE)) {
            if (match(TokenType.EOF)) {
                throw error("Expected '}' before end of file");
            }

            statements.add(parseStatement());
        }

        consume(TokenType.RBRACE);

        return new BlockStatement(statements);
    }

    private Statement parseStatement() {

        Token current = current();

        // x := 5
        if (
            current.type == TokenType.IDENTIFIER &&
            peek().type == TokenType.DECLARE
        ) {
            return parseVariableDeclaration();
        }

        if (
            current.type == TokenType.IDENTIFIER &&
            peek().type == TokenType.DOT
        ) {
            return parseFunctionCall();
        }

        // println(x)
        if (current.type == TokenType.PRINTLN) {
            return parsePrintStatement();
        }

        if (current.type == TokenType.IF) {
            return parseIfStatement();
        }

        if (current.type == TokenType.FOR) {
            return parseForStatement();
        }

        throw error("Unexpected token in statement: " + current.type);
    }

    private VariableDeclaration parseVariableDeclaration() {

        String name = consume(TokenType.IDENTIFIER).value;

        consume(TokenType.DECLARE);

        Expression value = parseExpression();

        return new VariableDeclaration(name, value);
    }

    private FunctionCallStatement parseFunctionCall() {
        String moduleName = consume(TokenType.IDENTIFIER).value;
        consume(TokenType.DOT);
        String functionName = consume(TokenType.IDENTIFIER).value;
        consume(TokenType.LPAREN);
        consume(TokenType.RPAREN);

        return new FunctionCallStatement(moduleName, functionName);
    }

    private PrintStatement parsePrintStatement() {

        consume(TokenType.PRINTLN);
        consume(TokenType.LPAREN);

        Expression expr = parseExpression();

        consume(TokenType.RPAREN);

        return new PrintStatement(expr);
    }

    private IfStatement parseIfStatement() {
        consume(TokenType.IF);

        Expression condition = parseExpression();
        BlockStatement body = parseBlock();

        return new IfStatement(condition, body);
    }

    private ForStatement parseForStatement() {
        consume(TokenType.FOR);

        Expression condition = parseExpression();
        BlockStatement body = parseBlock();

        return new ForStatement(condition, body);
    }

    private Expression parseExpression() {
        return parseComparison();
    }

    private Expression parseComparison() {
        Expression expr = parseAddition();

        while (
            match(TokenType.LESS) ||
            match(TokenType.GREATER) ||
            match(TokenType.EQUAL_EQUAL) ||
            match(TokenType.NOT_EQUAL) ||
            match(TokenType.LESS_EQUAL) ||
            match(TokenType.GREATER_EQUAL)
        ) {
            Token operator = current();
            pos++;
            Expression right = parseAddition();
            expr = new BinaryExpression(expr, operator.value, right);
        }

        return expr;
    }

    private Expression parseAddition() {
        Expression expr = parseMultiplication();

        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token operator = current();
            pos++;
            Expression right = parseMultiplication();
            expr = new BinaryExpression(expr, operator.value, right);
        }

        return expr;
    }

    private Expression parseMultiplication() {
        Expression expr = parsePrimary();

        while (match(TokenType.STAR) || match(TokenType.SLASH)) {
            Token operator = current();
            pos++;
            Expression right = parsePrimary();
            expr = new BinaryExpression(expr, operator.value, right);
        }

        return expr;
    }

    private Expression parsePrimary() {

        Token token = current();

        // Number
        if (token.type == TokenType.NUMBER) {
            pos++;
            return new NumberExpression(
                Integer.parseInt(token.value)
            );
        }

        // Identifier
        if (token.type == TokenType.IDENTIFIER) {
            pos++;
            return new IdentifierExpression(token.value);
        }

        if (token.type == TokenType.LPAREN) {
            consume(TokenType.LPAREN);
            Expression expression = parseExpression();
            consume(TokenType.RPAREN);
            return expression;
        }

        throw error("Unexpected token in expression: " + token.type);
    }

    private Token consume(TokenType expected) {

        Token token = current();

        if (token.type != expected) {
            throw error("Expected " + expected + " but got " + token.type);
        }

        pos++;
        return token;
    }

    private boolean match(TokenType type) {
        return current().type == type;
    }

    private Token current() {
        return tokens.get(pos);
    }

    private Token peek() {
        if (pos + 1 >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }

        return tokens.get(pos + 1);
    }

    private RuntimeException error(String message) {
        Token token = current();
        return new RuntimeException(
            "Parser error at line " + token.line + ", column " + token.column +
            ": " + message
        );
    }
}
