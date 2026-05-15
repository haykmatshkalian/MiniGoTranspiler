package lexer;

public enum TokenType {
    // Keywords
    FUNC,
    MAIN,
    IF,
    FOR,
    PRINTLN,

    // Symbols
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,

    // Operators
    PLUS,
    MINUS,
    STAR,
    SLASH,

    LESS,
    GREATER,
    EQUAL_EQUAL,
    NOT_EQUAL,
    LESS_EQUAL,
    GREATER_EQUAL,

    DECLARE, // :=

    // Literals
    IDENTIFIER,
    NUMBER,

    EOF
}
