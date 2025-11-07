package parser.ast;

import lexer.token.Token;
import java.util.List;

public class FunctionStmt extends Stmt {
    public final Token name;
    public final Token returnType;
    public final List<Token> params;
    public final List<Stmt> body;

    public FunctionStmt(Token name, Token returnType, List<Token> params, List<Stmt> body) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.body = body;
    }

    @Override
    public String toString() {
        return "(def " + name.lexeme + " ...)";
    }
}
