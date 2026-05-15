package ast.statements;

import ast.expressions.Expression;

public class VariableDeclaration extends Statement {

    public final String name;
    public final Expression value;

    public VariableDeclaration(String name, Expression value) {
        this.name = name;
        this.value = value;
    }
}
