package parser.ast;

public class LiteralExpr extends Expr {
    public final Object value;

    public LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "nil";
        return "(literal " + value.toString() + ")";
    }
}
