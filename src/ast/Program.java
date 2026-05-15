package ast;

import ast.statements.Statement;
import ast.statements.BlockStatement;

import java.util.List;

public class Program {

    public final BlockStatement mainBlock;

    public Program(BlockStatement mainBlock) {
        this.mainBlock = mainBlock;
    }

    public List<Statement> statements() {
        return mainBlock.statements;
    }
}
