package parser.ast;

import lexer.token.Token;

public class VariableExpr extends Expr {
    public final Token name;

    public VariableExpr(Token name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "(var " + name.lexeme + ")";
    }
}
