package parser.ast;

import lexer.token.Token;

public class ReturnStmt extends Stmt {
    public final Token keyword;
    public final Expr value;

    public ReturnStmt(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value == null) return "(return)";
        return parenthesize("return", value);
    }
}