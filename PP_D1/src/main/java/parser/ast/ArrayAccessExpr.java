package parser.ast;

import lexer.token.Token;

public class ArrayAccessExpr extends Expr {
    public final Expr callee;
    public final Token bracket;
    public final Expr index;

    public ArrayAccessExpr(Expr callee, Token bracket, Expr index) {
        this.callee = callee;
        this.bracket = bracket;
        this.index = index;
    }

    @Override
    public String toString() {
        return parenthesize("get " + callee.toString(), index);
    }
}
