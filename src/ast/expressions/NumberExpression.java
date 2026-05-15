package ast.expressions;

public class NumberExpression extends Expression {

    public final int value;

    public NumberExpression(int value) {
        this.value = value;
    }
}
