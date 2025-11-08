package parser.ast;

import lexer.token.Token;

public class ScanExpr extends Expr {
    public final Token keyword;
    public final Expr expression;
    public ScanExpr(Token keyword, Expr expression) {
        this.keyword = keyword;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return parenthesize("scan", expression);
    }
}
