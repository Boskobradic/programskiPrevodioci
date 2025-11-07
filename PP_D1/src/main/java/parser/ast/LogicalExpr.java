package parser.ast;

import lexer.token.Token;

public class LogicalExpr extends Expr {
    public final Expr left;
    public final Token operator;
    public final Expr right;

    public LogicalExpr(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return parenthesize(operator.lexeme, left, right);
    }
}
