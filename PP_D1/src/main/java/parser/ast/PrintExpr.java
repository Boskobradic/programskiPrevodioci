package parser.ast;

public class PrintExpr extends Expr {
    public final Expr expression;

    public PrintExpr(Expr expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return parenthesize("print", expression);
    }
}
