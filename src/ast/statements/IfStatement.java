package ast.statements;

import ast.expressions.Expression;

public class IfStatement extends Statement {

    public final Expression condition;
    public final BlockStatement body;

    public IfStatement(Expression condition, BlockStatement body) {
        this.condition = condition;
        this.body = body;
    }
}
