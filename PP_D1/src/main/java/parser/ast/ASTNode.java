package parser.ast;

public abstract class ASTNode {

    protected String parenthesize(String name, ASTNode... nodes) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name.toLowerCase());
        for (ASTNode node : nodes) {
            builder.append(" ");
            builder.append(node.toString());
        }
        builder.append(")");
        return builder.toString();
    }
}
