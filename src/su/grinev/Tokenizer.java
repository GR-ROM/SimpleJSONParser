package su.grinev;

import java.util.LinkedList;
import java.util.List;

import static su.grinev.Token.TokenType.*;

public class Tokenizer {

    public List<Token> Tokenize(List<Character> characterList) {
        List<Token> tokens = new LinkedList<>();
        while (characterList.size() != 0) {
            char c = characterList.get(0);
            if (c == '{' || c == '}' || c == '[' || c == ']' || c == ':' || c == ',') {
                switch (c) {
                    case '{' -> tokens.add(new Token(TOKEN_OPEN_CURLY_BRACE, "{"));
                    case '}' -> tokens.add(new Token(TOKEN_CLOSE_CURLY_BRACE, "}"));
                    case '[' -> tokens.add(new Token(TOKEN_OPEN_SQUARE_BRACKET, "["));
                    case ']' -> tokens.add(new Token(TOKEN_CLOSE_SQUARE_BRACKET, "]"));
                    case ':' -> tokens.add(new Token(TOKEN_COLON, ":"));
                    case ',' -> tokens.add(new Token(TOKEN_COMMA, ","));
                }
                advanceList(characterList, 1);
            } else
            if (c == '"') {
                String parsedString = parseString(characterList);
                tokens.add(new Token(TOKEN_STRING, parsedString));
                advanceList(characterList, parsedString.length()+2);
            } else
            if (c == 'T' || c == 't') {
                String trueString = "true";
                tokens.add(new Token(Token.TokenType.TOKEN_BOOLEAN, trueString));
                advanceList(characterList, trueString.length());
            } else
            if (c == 'F' || c == 'f') {
                String falseString = "false";
                tokens.add(new Token(Token.TokenType.TOKEN_BOOLEAN, falseString));
                advanceList(characterList, falseString.length());
            } else
            if (c == 'N' || c == 'n') {
                String nullString = "null";
                tokens.add(new Token(Token.TokenType.TOKEN_NULL, nullString));
                advanceList(characterList, nullString.length());
            } else
            if (c >= '0' && c <= '9' || c == '-') {
                tokens.add(new Token(Token.TokenType.TOKEN_NUMBER, parseNumber(characterList)));
            }
        }
        return tokens;
    }

    private String parseString(List<Character> characterList) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 1; // remove the leading quote
        while (true) {
            if (characterList.size() == index) {
                // throw new NonTerminatedStringException(
            }
            char c = characterList.get(index++);
            if (c == '"') return stringBuilder.toString();
            // escape sequence processing
            if (c == '\\') {
                switch (characterList.get(index++)) {
                    case '"' -> stringBuilder.append('"');
                    case '\\' -> stringBuilder.append('\\');
                    case 'b' -> stringBuilder.append('\b');
                    case 'f' -> stringBuilder.append('\f');
                    case 'n' -> stringBuilder.append('\n');
                    case 'r' -> stringBuilder.append('\r');
                    case 't' -> stringBuilder.append('\t');
                    case 'u' -> stringBuilder.append((char) Integer.parseInt(characterList.get(index++)
                            + characterList.get(index++) + characterList.get(index++) + characterList.get(index++) + "", 16));
                }
            } else {
                stringBuilder.append(c); // put the plain character as is
            }
        }
    }

    private String parseNumber(List<Character> characterList) {
        StringBuilder number = new StringBuilder();
        boolean hasFraction = false;
        boolean hasExponent = false;
        while (true) {
            char c = characterList.get(0);
            if (Character.isDigit(c) || c == '-') {
                number.append(parseDigits(characterList));
            } else
            if (c == '.' && !hasFraction) {
                number.append('.');
                characterList.remove(0);
                number.append(parseDigits(characterList));
                hasFraction = true;
            } else
            if (c == 'E' || c == 'e' && !hasExponent) {
                c = characterList.remove(0);
                if (c == '-' || c == '+') {
                    number.append(c);
                }
                number.append(parseDigits(characterList));
                hasExponent = true;
            } else return number.toString();
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

    private void advanceList(List list, int count) {
        if (count > 0) {
            list.subList(0, count).clear();
        }
    }

}