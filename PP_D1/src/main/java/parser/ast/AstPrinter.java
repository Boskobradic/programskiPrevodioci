package parser.ast;

import parser.ast.*;
import java.util.List;

public class AstPrinter {

    public String print(Stmt stmt) {
        // Ako nije funkcija — ispisuje se u jednoj liniji (parser.toString())
        if (!(stmt instanceof FunctionStmt)) {
            return stmt.toString();
        }

        FunctionStmt f = (FunctionStmt) stmt;

        StringBuilder sb = new StringBuilder();
        sb.append("(def ").append(f.name.lexeme).append("\n");

        for (int i = 0; i < f.body.size(); i++) {
            Stmt s = f.body.get(i);
            sb.append("    ");  // indent

            if (s instanceof FunctionStmt) {
                // u slučaju funkcije u funkciji, rekurzivno i lepo
                sb.append(print(s));
            } else {
                // sve ostalo ide u jednoj liniji (parser ga sam formatira)
                sb.append(s.toString());
            }

            if (i < f.body.size() - 1)
                sb.append("\n");
        }

        sb.append("\n)");
        return sb.toString();
    }

    public String printAll(List<Stmt> statements) {
        StringBuilder sb = new StringBuilder();
        for (Stmt stmt : statements) {
            sb.append(print(stmt)).append("\n");
        }
        return sb.toString();
    }
}
