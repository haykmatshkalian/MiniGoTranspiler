package ast.expressions;

public class IdentifierExpression extends Expression {

    public final String name;

    public IdentifierExpression(String name) {
        this.name = name;
    }
}
