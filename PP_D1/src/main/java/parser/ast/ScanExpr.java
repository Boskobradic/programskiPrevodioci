package parser.ast;

import lexer.token.Token;

public class ScanExpr extends Expr {
    public final Token keyword;

    public ScanExpr(Token keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "(scan)";
    }
}
