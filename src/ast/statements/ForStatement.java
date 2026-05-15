package ast.statements;

import ast.expressions.Expression;

public class ForStatement extends Statement {

    public final Expression condition;
    public final BlockStatement body;

    public ForStatement(Expression condition, BlockStatement body) {
        this.condition = condition;
        this.body = body;
    }
}
