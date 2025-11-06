package parser;

import lexer.token.Token;
import lexer.token.TokenType;
import parser.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            if (match(TokenType.NEWLINE)) continue;
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (peek().type == TokenType.IDENTIFIER && isType(peekNext().type)) {
                return varDeclaration();
            }
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        Token type = consumeType("Expect type name after variable name.");

        Expr initializer = null;
        if (match(TokenType.ASSIGN)) {
            initializer = expression();
        }

        consume(TokenType.NEWLINE, "Expect ';' after variable declaration or assignment.");
        return new VarDeclStmt(name, type, initializer);
    }

    private Stmt statement() {
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        throw error(peek(), "Expect a statement.");
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(TokenType.NEWLINE, "Expect ';' after value.");
        return new PrintStmt(value);
    }

    private Expr expression() {
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.INT_LIT, TokenType.LONG_LIT, TokenType.FLOAT_LIT, TokenType.DOUBLE_LIT, TokenType.STRING_LIT, TokenType.CHAR_LIT)) {
            return new LiteralExpr(previous().literal);
        }

        if (match(TokenType.TRUE, TokenType.FALSE)) {
            return new LiteralExpr(previous().type == TokenType.TRUE);
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        throw error(peek(), "Expect expression.");
    }

    // --- HELPER METHODS ---

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private Token consumeType(String message) {
        if (isType(peek().type)) return advance();
        throw error(peek(), message);
    }

    private boolean isType(TokenType type) {
        return type == TokenType.INT || type == TokenType.LONG || type == TokenType.FLOAT ||
                type == TokenType.DOUBLE || type == TokenType.BOOLEAN || type == TokenType.CHAR ||
                type == TokenType.ARRAY;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token peekNext() {
        if (current + 1 >= tokens.size()) return tokens.get(tokens.size() - 1);
        return tokens.get(current + 1);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        System.err.println("Parse Error at token " + token + ": " + message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == TokenType.NEWLINE) return;
            switch (peek().type) {
                case FUNCTION, IF, WHILE, PRINT, RETURN:
                    return;
            }
            advance();
        }
    }
}
