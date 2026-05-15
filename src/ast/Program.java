package ast;

import java.util.List;

public class Program {

    public final List<Module> modules;

    public Program(List<Module> modules) {
        this.modules = modules;
    }

    public FunctionDeclaration mainFunction() {
        FunctionDeclaration main = null;

        for (Module module : modules) {
            for (FunctionDeclaration function : module.functions) {
                if (function.isMain()) {
                    if (main != null) {
                        throw new RuntimeException("Program error: multiple main functions found");
                    }

                    main = function;
                }
            }
        }

        if (main == null) {
            throw new RuntimeException("Program error: no main function found");
        }

        return main;
    }
}
