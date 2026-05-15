package ast.statements;

public class FunctionCallStatement extends Statement {

    public final String moduleName;
    public final String functionName;

    public FunctionCallStatement(String moduleName, String functionName) {
        this.moduleName = moduleName;
        this.functionName = functionName;
    }
}
