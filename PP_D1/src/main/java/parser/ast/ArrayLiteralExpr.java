package parser.ast;

import java.util.List;

public class ArrayLiteralExpr extends Expr {
    public final List<Expr> elements;

    public ArrayLiteralExpr(List<Expr> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return parenthesize("array-literal", elements.toArray(new ASTNode[0]));
    }
}
