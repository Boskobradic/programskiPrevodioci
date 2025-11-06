package parser.ast;

import lexer.token.Token;

public class VarDeclStmt extends Stmt {
    public final Token name;
    public final Token type;
    public final Expr initializer;

    public VarDeclStmt(Token name, Token type, Expr initializer) {
        this.name = name;
        this.type = type;
        this.initializer = initializer;
    }

    @Override
    public String toString() {
        return parenthesize("declare " + name.lexeme + " " + type.lexeme, initializer);
    }
}
