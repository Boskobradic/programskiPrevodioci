
package lexer;

import lexer.token.Token;
import lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final ScannerCore sc;
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> KEYWORDS = Map.ofEntries(
            Map.entry("ale", TokenType.INT),
            Map.entry("pale", TokenType.LONG),
            Map.entry("pale_ale", TokenType.LONG),
            Map.entry("lager", TokenType.FLOAT),
            Map.entry("pilsner", TokenType.DOUBLE),
            Map.entry("malt", TokenType.BOOLEAN),
            Map.entry("helles", TokenType.TRUE),
            Map.entry("dunkel", TokenType.FALSE),
            Map.entry("glass", TokenType.CHAR),
            Map.entry("barrel", TokenType.ARRAY),
            Map.entry("pub", TokenType.MAIN),
            Map.entry("cheers", TokenType.RETURN),
            Map.entry("order", TokenType.SCAN),
            Map.entry("pour", TokenType.PRINT),
            Map.entry("bottle", TokenType.IF),
            Map.entry("tap", TokenType.ELSE),
            Map.entry("ili", TokenType.OR),
            Map.entry("brew", TokenType.WHILE),
            Map.entry("cork", TokenType.BREAK),
            Map.entry("function", TokenType.FUNCTION),
            Map.entry("if", TokenType.IF),
            Map.entry("or", TokenType.OR),
            Map.entry("else", TokenType.ELSE),
            Map.entry("return", TokenType.RETURN),
            Map.entry("is", TokenType.ASSIGN)
    );

    public Lexer(String source) {
        this.source = source;
        this.sc = new ScannerCore(source);
    }

    public List<Token> scanTokens() {
        while (!sc.isAtEnd()) {
            sc.beginToken();
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "\0", null, sc.getLine(), sc.getCol(), sc.getCol()));
        return tokens;
    }

    private void scanToken() {
        char c = sc.advance();

        switch (c) {
            case '(' -> add(TokenType.LPAREN);
            case ')' -> add(TokenType.RPAREN);
            case '[' -> add(TokenType.LBRACKET);
            case ']' -> add(TokenType.RBRACKET);
            case ',' -> add(TokenType.SEPARATOR_COMMA);
            case ':' -> add(TokenType.TYPE_COLON);
            case ';' -> add(TokenType.NEWLINE);
            case '+' -> add(TokenType.ADD);
            case '-' -> add(TokenType.SUBTRACT);
            case '*' -> add(TokenType.MULTIPLY);
            case '/' -> add(TokenType.DIVIDE);
            case '%' -> add(TokenType.PERCENT);
            case '{' -> add(TokenType.LBRACE);
            case '}' -> add(TokenType.RBRACE);
            case '<' -> add(sc.match('=') ? TokenType.LE : TokenType.LT);
            case '>' -> add(sc.match('=') ? TokenType.GE : TokenType.GT);
            case '=' -> add(TokenType.EQ);
            case '!' -> {
                if (sc.match('=')) add(TokenType.NEQ);
                else throw error("Unexpected '!'");
            }
            case '"' -> string();
            case '\'' -> character();
            case ' ', '\r', '\t', '\n' -> {}
            default -> {
                if (Character.isDigit(c)) number();
                else if (isIdentStart(c)) identifier();
                else throw error("Unexpected character");
            }
        }
    }

    private void number() {

        while (Character.isDigit(sc.peek())) sc.advance();

        boolean isFloatish = false;

        if (sc.peek() == '.' && Character.isDigit(sc.peekNext())) {
            isFloatish = true;
            sc.advance();
            while (Character.isDigit(sc.peek())) sc.advance();
        }


        if ((sc.peek() == 'e' || sc.peek() == 'E')) {
            isFloatish = true;
            sc.advance();
            if (sc.peek() == '+' || sc.peek() == '-') sc.advance();
            if (!Character.isDigit(sc.peek())) throw error("Malformed exponent in number");
            while (Character.isDigit(sc.peek())) sc.advance();
        }


        char suffix = sc.peek();
        if (suffix == 'l' || suffix == 'L') {
            sc.advance();
            String text = source.substring(sc.getStartIdx(), sc.getCur());
            addLiteralLong(text);
            return;
        } else if (suffix == 'f' || suffix == 'F') {
            sc.advance();
            String text = source.substring(sc.getStartIdx(), sc.getCur());
            addLiteralFloat(text);
            return;
        } else if (suffix == 'd' || suffix == 'D') {
            sc.advance();
            String text = source.substring(sc.getStartIdx(), sc.getCur());
            addLiteralDouble(text);
            return;
        }

        String text = source.substring(sc.getStartIdx(), sc.getCur());
        if (isFloatish) addLiteralDouble(text);
        else addLiteralInt(text);
    }

    private void identifier() {
        while (isIdentPart(sc.peek())) sc.advance();
        String text = source.substring(sc.getStartIdx(), sc.getCur());
        TokenType type = KEYWORDS.getOrDefault(text, TokenType.IDENTIFIER);
        if (type == TokenType.TRUE || type == TokenType.FALSE) {
            addLiteralBool(text);
        }  else {
            add(type, text);
        }
    }

    private boolean isIdentStart(char c) { return Character.isLetter(c) || c == '_'; }
    private boolean isIdentPart(char c)  { return isIdentStart(c) || Character.isDigit(c); }

    private void string() {

        while (!sc.isAtEnd() && sc.peek() != '"') {
            if (sc.peek() == '\\') {
                sc.advance();
                if (!sc.isAtEnd()) sc.advance();
            } else {
                sc.advance();
            }
        }
        if (sc.isAtEnd()) throw error("Unterminated string literal");
        sc.advance();
        String lexeme = source.substring(sc.getStartIdx(), sc.getCur());

        String raw = lexeme.substring(1, lexeme.length() - 1)
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
        tokens.add(new Token(TokenType.STRING_LIT, lexeme, raw,
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void character() {
        if (sc.isAtEnd()) throw error("Unterminated char literal");
        char value;
        if (sc.peek() == '\\') {
            sc.advance(); // backslash
            if (sc.isAtEnd()) throw error("Unterminated char escape");
            char esc = sc.advance();
            switch (esc) {
                case 'n' -> value = '\n';
                case 't' -> value = '\t';
                case '\'' -> value = '\'';
                case '\\' -> value = '\\';
                default -> value = esc;
            }
        } else {
            value = sc.advance();
        }
        if (sc.peek() != '\'') throw error("Unterminated char literal");
        sc.advance(); // closing '
        String lexeme = source.substring(sc.getStartIdx(), sc.getCur());
        tokens.add(new Token(TokenType.CHAR_LIT, lexeme, value,
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void add(TokenType type) {
        String lex = source.substring(sc.getStartIdx(), sc.getCur());
        tokens.add(new Token(type, lex, null,
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void add(TokenType type, String text) {
        tokens.add(new Token(type, text, null,
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void addLiteralInt(String literal) {
        tokens.add(new Token(TokenType.INT_LIT, literal, Integer.valueOf(literal),
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void addLiteralLong(String literalWithSuffix) {
        String lit = literalWithSuffix;
        if (lit.endsWith("L") || lit.endsWith("l")) lit = lit.substring(0, lit.length() - 1);
        tokens.add(new Token(TokenType.LONG_LIT, literalWithSuffix, Long.valueOf(lit),
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void addLiteralFloat(String literalWithSuffix) {
        String lit = literalWithSuffix;
        if (lit.endsWith("F") || lit.endsWith("f")) lit = lit.substring(0, lit.length() - 1);
        tokens.add(new Token(TokenType.FLOAT_LIT, literalWithSuffix, Float.valueOf(lit),
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void addLiteralDouble(String literalWithSuffix) {
        String lit = literalWithSuffix;
        if (lit.endsWith("D") || lit.endsWith("d")) lit = lit.substring(0, lit.length() - 1);
        tokens.add(new Token(TokenType.DOUBLE_LIT, literalWithSuffix, Double.valueOf(lit),
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void addLiteralBool(String literal) {
        tokens.add(new Token(TokenType.BOOL_LIT, literal, Boolean.valueOf(literal),
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private void addLiteralNull() {
        String lex = source.substring(sc.getStartIdx(), sc.getCur());
        tokens.add(new Token(TokenType.NULL_LIT, lex, null,
                sc.getStartLine(), sc.getStartCol(), sc.getCol() - 1));
    }

    private RuntimeException error(String msg) {
        String near = source.substring(sc.getStartIdx(), Math.min(sc.getCur(), source.length()));
        return new RuntimeException("LEXER > " + msg + " at " + sc.getStartLine() + ":" + sc.getStartCol() + " near '" + near + "'");
    }
}
