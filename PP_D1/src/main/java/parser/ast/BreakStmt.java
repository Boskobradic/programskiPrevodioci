package parser.ast;

import lexer.token.Token;

public class BreakStmt extends Stmt {
    public final Token keyword;

    public BreakStmt(Token keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "(break)";
    }
}
