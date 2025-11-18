package application;

import lexer.Lexer;
import lexer.token.Token;
import lexer.token.TokenFormatter;
import parser.Parser;
import parser.ast.Stmt;
import parser.ast.AstPrinter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Application {

    /*
    Options (pored run i debug) -> Configuration Edit -> Working directory svoj resources folder
    Ime fajla kao arg komandne linije
     */

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java main.Application <source-file>");
            System.exit(64);
        }

        try {
            String code = Files.readString(Path.of(args[0]));

            System.out.println("--- Lexer Output ---");
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.scanTokens();
            System.out.println(TokenFormatter.formatList(tokens));

            System.out.println("\n--- Parser Output ---");
            Parser parser = new Parser(tokens);
            List<Stmt> syntaxTree = parser.parse();

            List<Stmt> validStatements = syntaxTree.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());


            AstPrinter printer = new AstPrinter();

            if (!validStatements.isEmpty()) {
                String formatted = printer.printAll(validStatements);
                System.out.println(formatted);
            } else {
                System.out.println("Parsing produced no valid statements.");
            }


        } catch (Exception e) {
            System.err.println("\nRuntime Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
