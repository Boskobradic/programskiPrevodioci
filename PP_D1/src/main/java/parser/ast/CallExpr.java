package parser.ast;

import java.util.List;

public class CallExpr extends Expr {
    public final Expr callee;
    public final List<Expr> arguments;

    public CallExpr(Expr callee, List<Expr> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return parenthesize("call " + callee.toString(), arguments.toArray(new ASTNode[0]));
    }
}
