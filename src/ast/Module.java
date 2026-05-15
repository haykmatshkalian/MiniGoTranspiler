package ast;

import java.util.List;

public class Module {

    public final String name;
    public final List<FunctionDeclaration> functions;

    public Module(String name, List<FunctionDeclaration> functions) {
        this.name = name;
        this.functions = functions;
    }
}
