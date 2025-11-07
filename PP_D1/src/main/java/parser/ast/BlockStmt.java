package parser.ast;

import java.util.List;

public class BlockStmt extends Stmt {
    public final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return parenthesize("block", statements.toArray(new ASTNode[0]));
    }
}
