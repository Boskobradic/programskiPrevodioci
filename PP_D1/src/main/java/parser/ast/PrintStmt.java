package parser.ast;

public class PrintStmt extends Stmt {
    public final Expr expression;

    public PrintStmt(Expr expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return parenthesize("print", expression);
    }
}
