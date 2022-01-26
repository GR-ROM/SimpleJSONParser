package su.grinev;

public class Token {

    public static class TokenType {
        public static final int TOKEN_EMPTY = 0;
        public static final int TOKEN_OPEN_CURLY_BRACE = 1;
        public static final int TOKEN_CLOSE_CURLY_BRACE = 2;
        public static final int TOKEN_OPEN_SQUARE_BRACKET = 3;
        public static final int TOKEN_CLOSE_SQUARE_BRACKET = 4;
        public static final int TOKEN_COLON = 5;
        public static final int TOKEN_COMMA = 6;
        public static final int TOKEN_NUMBER = 7;
        public static final int TOKEN_BOOLEAN = 8;
        public static final int TOKEN_NULL = 9;
        public static final int TOKEN_STRING = 10;
    }

    public int tokenType;
    public String value;

    public Token(int tokenType) {
        this.tokenType = tokenType;
    }

    public Token(int tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }
}
