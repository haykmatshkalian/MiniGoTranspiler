package ast;

import ast.statements.BlockStatement;

public class FunctionDeclaration {

    public final String moduleName;
    public final String name;
    public final BlockStatement body;

    public FunctionDeclaration(String moduleName, String name, BlockStatement body) {
        this.moduleName = moduleName;
        this.name = name;
        this.body = body;
    }

    public boolean isMain() {
        return name.equals("main");
    }
}
