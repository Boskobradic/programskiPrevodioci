package parser.ast;

public class IfStmt extends Stmt {
    public final Expr condition;
    public final Stmt thenBranch;
    public final Stmt elseBranch; // Can be null

    public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public String toString() {
        if (elseBranch == null) {
            return parenthesize("if", condition, thenBranch);
        }
        return parenthesize("if-else", condition, thenBranch, elseBranch);
    }
}
