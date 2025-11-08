package parser.ast;

import lexer.token.Token;

public class UnaryExpr extends Expr {
    public final Token operator;
    public final Expr right;

    public UnaryExpr(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return parenthesize(operator.lexeme, right);
    }
}
