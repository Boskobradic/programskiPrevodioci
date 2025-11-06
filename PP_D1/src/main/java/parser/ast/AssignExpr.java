package parser.ast;

import lexer.token.Token;

public class AssignExpr extends Expr {
    public final Token name;
    public final Expr value;

    public AssignExpr(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return parenthesize("= " + name.lexeme, value);
    }
}