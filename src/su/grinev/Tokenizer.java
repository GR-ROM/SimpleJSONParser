package su.grinev;

import java.util.LinkedList;
import java.util.List;

public class Tokenizer {

    public List<Token> Tokenize(List<Character> characterList) {
        List<Token> tokens = new LinkedList<>();
        while (characterList.size() != 0) {
            char c = characterList.get(0);
            if (c == '{') {
                tokens.add(new Token(Token.TokenType.TOKEN_OPEN_CURLY_BRACE, "{"));
                characterList.remove(0);
                continue;
            }
            if (c == '}') {
                tokens.add(new Token(Token.TokenType.TOKEN_CLOSE_CURLY_BRACE, "}"));
                characterList.remove(0);
                continue;
            }
            if (c == '[') {
                tokens.add(new Token(Token.TokenType.TOKEN_OPEN_SQUARE_BRACKET));
                characterList.remove(0);
                continue;
            }
            if (c == ']') {
                tokens.add(new Token(Token.TokenType.TOKEN_CLOSE_SQUARE_BRACKET));
                characterList.remove(0);
                continue;
            }
            if (c == ':') {
                tokens.add(new Token(Token.TokenType.TOKEN_COLON, ":"));
                characterList.remove(0);
                continue;
            }
            if (c == ',') {
                tokens.add(new Token(Token.TokenType.TOKEN_COMMA, ","));
                characterList.remove(0);
                continue;
            }
            if (c == '"') {
                tokens.add(new Token(Token.TokenType.TOKEN_STRING, parseString(characterList)));
                continue;
            }
            if (c == 'T' || c == 't') {
                tokens.add(new Token(Token.TokenType.TOKEN_BOOLEAN, "true"));
                continue;
            }
            if (c == 'F' || c == 'f') {
                tokens.add(new Token(Token.TokenType.TOKEN_BOOLEAN, "false"));
                continue;
            }
            if (c == 'N' || c == 'n') {
                tokens.add(new Token(Token.TokenType.TOKEN_NULL, "null"));
                continue;
            }
            if (c >= '0' && c <= '9' || c == '-') {
                tokens.add(new Token(Token.TokenType.TOKEN_NUMBER, parseNumber(characterList)));
                continue;
            }
        }
        return tokens;
    }

    private String parseString(List<Character> characterList) {
        StringBuilder stringBuilder = new StringBuilder();
        characterList.remove(0); // remove the leading quote
        while (true) {
            if (characterList.size() == 0) {
                // throw new NonTerminatedStringException(
            }
            char c = characterList.remove(0);
            if (c == '"') return stringBuilder.toString();
            // escape sequence processing
            if (c == '\\') {
                c = characterList.remove(0);
                if (c == '"') {
                    stringBuilder.append('"');
                }
                if (c == '\\') {
                    stringBuilder.append('\\');
                }
                if (c == 'b') {
                    stringBuilder.append('\b');
                }
                if (c == 'f') {
                    stringBuilder.append('\f');
                }
                if (c == 'n') {
                    stringBuilder.append('\n');
                }
                if (c == 'r') {
                    stringBuilder.append('\r');
                }
                if (c == 't') {
                    stringBuilder.append('\t');
                }
                if (c == 'u') { // u<HEX><HEX><HEX><HEX>
                    stringBuilder.append((char) Integer.parseInt(characterList.remove(0) + characterList.remove(0) + characterList.remove(0) + characterList.remove(0) + "", 16));
                }
            } else {
                stringBuilder.append(c); // put the plain character as is
            }
        }
    }

    private String parseNumber(List<Character> characterList) {
        String number = "";
        boolean hasFraction = false;
        boolean hasExponent = false;
        while (true) {
            char c = characterList.get(0);
            if (Character.isDigit(c) || c == '-') {
                number = number + parseDigits(characterList);
            } else
            if (c == '.' && !hasFraction) {
                number = number + '.';
                characterList.remove(0);
                number = number + parseDigits(characterList);
                hasFraction = true;
            } else
            if (c == 'E' || c == 'e' && !hasExponent) {
                c = characterList.remove(0);
                if (c == '-' || c == '+') {
                    number = number + c;
                }
                number = number + parseDigits(characterList);
                hasExponent = true;
            } else return number;
        }
    }

    private String parseDigits(List<Character> characterList) {
        String number = "";
        while (true) {
            char c = characterList.remove(0);
            if (Character.isDigit(c) || c == '-') {
                number = number + c;
            } else {
                characterList.add(0, c);
                return number;
            }
        }
    }

}