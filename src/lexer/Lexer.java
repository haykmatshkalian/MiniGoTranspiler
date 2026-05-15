package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String input;
    private int pos = 0;
    private int line = 1;
    private int column = 1;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char current = input.charAt(pos);

            if (Character.isWhitespace(current)) {
                advance();
                continue;
            }

            // Numbers
            if (Character.isDigit(current)) {
                tokens.add(readNumber());
                continue;
            }

            // Identifiers / keywords
            if (Character.isLetter(current)) {
                tokens.add(readIdentifier());
                continue;
            }

            // :=
            if (current == ':' && peek() == '=') {
                tokens.add(makeToken(TokenType.DECLARE, ":="));
                advance();
                advance();
                continue;
            }

            if (current == '=' && peek() == '=') {
                tokens.add(makeToken(TokenType.EQUAL_EQUAL, "=="));
                advance();
                advance();
                continue;
            }

            if (current == '!' && peek() == '=') {
                tokens.add(makeToken(TokenType.NOT_EQUAL, "!="));
                advance();
                advance();
                continue;
            }

            if (current == '<' && peek() == '=') {
                tokens.add(makeToken(TokenType.LESS_EQUAL, "<="));
                advance();
                advance();
                continue;
            }

            if (current == '>' && peek() == '=') {
                tokens.add(makeToken(TokenType.GREATER_EQUAL, ">="));
                advance();
                advance();
                continue;
            }

            switch (current) {
                case '+':
                    tokens.add(makeToken(TokenType.PLUS, "+"));
                    break;

                case '-':
                    tokens.add(makeToken(TokenType.MINUS, "-"));
                    break;

                case '*':
                    tokens.add(makeToken(TokenType.STAR, "*"));
                    break;

                case '/':
                    tokens.add(makeToken(TokenType.SLASH, "/"));
                    break;

                case '(':
                    tokens.add(makeToken(TokenType.LPAREN, "("));
                    break;

                case ')':
                    tokens.add(makeToken(TokenType.RPAREN, ")"));
                    break;

                case '{':
                    tokens.add(makeToken(TokenType.LBRACE, "{"));
                    break;

                case '}':
                    tokens.add(makeToken(TokenType.RBRACE, "}"));
                    break;

                case '<':
                    tokens.add(makeToken(TokenType.LESS, "<"));
                    break;

                case '>':
                    tokens.add(makeToken(TokenType.GREATER, ">"));
                    break;

                default:
                    throw new RuntimeException(
                        "Lexer error at line " + line + ", column " + column +
                        ": unexpected character '" + current + "'"
                    );
            }

            advance();
        }

        tokens.add(makeToken(TokenType.EOF, ""));
        return tokens;
    }

    private char peek() {
        if (pos + 1 >= input.length()) {
            return '\0';
        }

        return input.charAt(pos + 1);
    }

    private void advance() {
        if (input.charAt(pos) == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        pos++;
    }

    private Token makeToken(TokenType type, String value) {
        return new Token(type, value, line, column);
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startColumn = column;

        while (
            pos < input.length() &&
            Character.isDigit(input.charAt(pos))
        ) {
            sb.append(input.charAt(pos));
            advance();
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine, startColumn);
    }

    private Token readIdentifier() {
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startColumn = column;

        while (
            pos < input.length() &&
            (
                Character.isLetterOrDigit(input.charAt(pos)) ||
                input.charAt(pos) == '_'
            )
        ) {
            sb.append(input.charAt(pos));
            advance();
        }

        String word = sb.toString();

        switch (word) {
            case "func":
                return new Token(TokenType.FUNC, word, startLine, startColumn);

            case "main":
                return new Token(TokenType.MAIN, word, startLine, startColumn);

            case "if":
                return new Token(TokenType.IF, word, startLine, startColumn);

            case "for":
                return new Token(TokenType.FOR, word, startLine, startColumn);

            case "println":
                return new Token(TokenType.PRINTLN, word, startLine, startColumn);

            default:
                return new Token(TokenType.IDENTIFIER, word, startLine, startColumn);
        }
    }
}
