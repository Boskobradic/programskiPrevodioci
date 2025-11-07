package parser.ast;

public class GroupingExpr extends Expr {
    public final Expr expression;

    public GroupingExpr(Expr expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return parenthesize("group", expression);
    }
}
