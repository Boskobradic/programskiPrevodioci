package parser.ast;

public class WhileStmt extends Stmt {
    public final Expr condition;
    public final Stmt body;

    public WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return parenthesize("while", condition, body);
    }
}
