package ast.statements;

import java.util.List;

public class BlockStatement extends Statement {

    public final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }
}
