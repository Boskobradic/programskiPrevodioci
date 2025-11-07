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

    public Parser(List<Token> tokens) { this.tokens = tokens; }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(topLevelDeclaration());
        }
        return statements;
    }

    private Stmt topLevelDeclaration() {
        try {
            if (match(TokenType.FUNCTION)) return function("function");
            if (match(TokenType.MAIN)) return function("main");
            return declarationOrStatement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt declarationOrStatement() {
        if (isType(peek().type)) return varDeclaration();
        return statement();
    }

    private Stmt statement() {
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.RETURN)) return returnStatement();
        if (match(TokenType.BREAK)) return breakStatement();
        if (match(TokenType.LBRACE)) return blockStatement();
        return expressionStatement();
    }

    private Stmt blockStatement() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(declarationOrStatement());
        }
        consume(TokenType.RBRACE, "Expect '}' after block.");
        return new BlockStmt(statements);
    }

    private Stmt function(String kind) {
        Token returnType = null;
        if (!kind.equals("main")) {
            returnType = consumeType("Expect return type after 'function'.");
        }
        Token name = consume(TokenType.IDENTIFIER, "Expect " + kind + " name after return type.");

        consume(TokenType.LPAREN, "Expect '(' after function name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RPAREN)) {
            do {
                parameters.add(consumeType("Expect parameter type."));
                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name."));
            } while (match(TokenType.SEPARATOR_COMMA));
        }
        consume(TokenType.RPAREN, "Expect ')' after parameters.");

        consume(TokenType.LBRACE, "Expect '{' before function body.");
        List<Stmt> body = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            body.add(declarationOrStatement());
        }
        consume(TokenType.RBRACE, "Expect '}' after function body.");

        return new FunctionStmt(name, returnType, parameters, body);
        /*Token name = consume(TokenType.IDENTIFIER, "Expect " + kind + "
        name.");
        Token returnType = null;
        if (!kind.equals("main")) {
            returnType = consumeType("Expect return type for function.");
        }
        consume(TokenType.LPAREN, "Expect '(' after function name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RPAREN)) {
            do {
                parameters.add(consumeType("Expect parameter type."));
                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name."));
            } while (match(TokenType.SEPARATOR_COMMA));
        }
        consume(TokenType.RPAREN, "Expect ')' after parameters.");

        consume(TokenType.LBRACE, "Expect '{' before function body.");
        List<Stmt> body = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            body.add(declarationOrStatement());
        }
        consume(TokenType.RBRACE, "Expect '}' after function body.");

        return new FunctionStmt(name, returnType, parameters, body);*/
    }

    private Stmt ifStatement() {
        consume(TokenType.LPAREN, "Expect '(' after 'if'.");
        Expr condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after if condition.");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }
        return new IfStmt(condition, thenBranch, elseBranch);
    }

    private Stmt whileStatement() {
        consume(TokenType.LPAREN, "Expect '(' after 'while'.");
        Expr condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after while condition.");
        Stmt body = statement();
        return new WhileStmt(condition, body);
    }

    private Stmt returnStatement() {
        Token keyword = previous();
        Expr value = null;
        if (!check(TokenType.NEWLINE) && !check(TokenType.RBRACE)) {
            value = expression();
        }
        consume(TokenType.NEWLINE, "Expect ';' after return value.");
        return new ReturnStmt(keyword, value);
    }

    private Stmt varDeclaration() {
        Token type = consumeType("Expect type name for declaration.");
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name after type.");
        Expr initializer = null;
        if (match(TokenType.ASSIGN)) { initializer = expression(); }
        consume(TokenType.NEWLINE, "Expect ';' after variable declaration.");
        return new VarDeclStmt(name, type, initializer);
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(TokenType.NEWLINE, "Expect ';' after value.");
        return new PrintStmt(value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(TokenType.NEWLINE, "Expect ';' after expression.");
        return new ExprStmt(expr);
    }

    private Stmt breakStatement() {
        Token keyword = previous();
        consume(TokenType.NEWLINE, "Expect ';' after break.");
        return new BreakStmt(keyword);
    }

    private Expr expression() { return assignment(); }

    private Expr assignment() {
        Expr expr = logicalOr();
        if (match(TokenType.ASSIGN)) {
            Token equals = previous();
            Expr value = assignment();
            if (expr instanceof VariableExpr) {
                Token name = ((VariableExpr) expr).name;
                return new AssignExpr(name, value);
            } else if (expr instanceof ArrayAccessExpr) {
                return new AssignExpr(((VariableExpr)((ArrayAccessExpr)expr).callee).name, value);
            }
            throw error(equals, "Invalid assignment target.");
        }
        return expr;
    }

    private Expr logicalOr() {
        Expr expr = logicalAnd();
        while (match(TokenType.OR)) {
            Token operator = previous();
            Expr right = logicalAnd();
            expr = new LogicalExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr logicalAnd() {
        Expr expr = equality();
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.NEQ, TokenType.EQ)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = addition();
        while (match(TokenType.GT, TokenType.GE, TokenType.LT, TokenType.LE)) {
            Token operator = previous();
            Expr right = addition();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr addition() {
        Expr expr = multiplication();
        while (match(TokenType.ADD, TokenType.SUBTRACT)) {
            Token operator = previous();
            Expr right = multiplication();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr multiplication() {
        Expr expr = unary();
        while (match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.PERCENT)) {
            Token operator = previous();
            Expr right = unary();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.SUBTRACT)) {
            return primary();
        }
        return call();
    }

    private Expr call() {
        Expr expr = primary();

        while (true) {
            if (match(TokenType.LPAREN)) {
                expr = finishCall(expr);
            } else if (match(TokenType.LBRACKET)) {
                expr = finishArrayAccess(expr);
            } else {
                break;
            }
        }
        return expr;
    }

    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(TokenType.RPAREN)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.SEPARATOR_COMMA));
        }
        consume(TokenType.RPAREN, "Expect ')' after arguments.");
        return new CallExpr(callee, arguments);
    }

    private Expr finishArrayAccess(Expr callee) {
        Token bracket = previous();
        Expr index = expression();
        consume(TokenType.RBRACKET, "Expect ']' after array index.");
        return new ArrayAccessExpr(callee, bracket, index);
    }

    private Expr primary() {
        if (match(TokenType.INT_LIT, TokenType.LONG_LIT, TokenType.FLOAT_LIT, TokenType.DOUBLE_LIT, TokenType.STRING_LIT, TokenType.CHAR_LIT, TokenType.BOOL_LIT)) return new LiteralExpr(previous().literal);
        if (match(TokenType.SCAN)) return new ScanExpr(previous());
        if (match(TokenType.IDENTIFIER)) return new VariableExpr(previous());
        if (match(TokenType.LBRACKET)) return arrayLiteral();
        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return new GroupingExpr(expr);
        }
        throw error(peek(), "Expect expression.");
    }

    private Expr arrayLiteral() {
        List<Expr> elements = new ArrayList<>();
        if (!check(TokenType.RBRACKET)) {
            do {
                elements.add(expression());
            } while (match(TokenType.SEPARATOR_COMMA));
        }
        consume(TokenType.RBRACKET, "Expect ']' after array elements.");
        return new ArrayLiteralExpr(elements);
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
