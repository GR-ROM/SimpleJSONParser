package su.grinev;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;
import static su.grinev.Token.TokenType.*;

public class JSONParser {

    public static class Stage {
        public static final int EXPECT_KEY = 0;
        public static final int EXPECT_COLON = 1;
        public static final int EXPECT_VALUE = 2;
        public static final int READ_VALUE = 3;
    }

    private final Tokenizer tokenizer = new Tokenizer();

    public Object Parse(String JSONString) {
        List<Character> characterList = JSONString
                .chars()
                .filter(c -> c != ' ' && c != '\n' && c != '\r' && c != '\t')
                .mapToObj(c -> (char)c)
                .collect(toCollection(LinkedList::new));

        List<Token> tokenList = tokenizer.Tokenize(characterList);
        if (tokenList.remove(0).tokenType == TOKEN_OPEN_CURLY_BRACE) {
            return parseObject(tokenList);
        } else {
            if (tokenList.remove(0).tokenType == TOKEN_OPEN_SQUARE_BRACKET) {
                return parseArray(tokenList);
            }
        }
        return null;
    }

    private JSONObject parseObject(List<Token> tokenList) {
        JSONObject object = new JSONObject();
        int stage = 0;
        String name = "";
        while (tokenList.size() != 0) {
            Token token = tokenList.remove(0);
            switch (token.tokenType) {
                case TOKEN_EMPTY: {
                    continue;
                }
                case TOKEN_STRING: {
                    if (stage == Stage.EXPECT_KEY) {
                        name = token.value;
                        stage = Stage.EXPECT_COLON;
                        continue;
                    }
                    if (stage == Stage.EXPECT_VALUE) {
                        object.put(name, new JSONValue(JSONValue.type.JSON_STRING, token.value));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_NUMBER: {
                    if (stage == Stage.EXPECT_VALUE) {
                        object.put(name, new JSONValue(JSONValue.type.JSON_NUMBER, Float.parseFloat(token.value)));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_BOOLEAN: {
                    if (stage == Stage.EXPECT_VALUE) {
                        object.put(name, new JSONValue(JSONValue.type.JSON_BOOLEAN, Boolean.parseBoolean(token.value)));
                        stage = Stage.READ_VALUE;
                    }
                }
                case TOKEN_NULL: {
                    if (stage == Stage.EXPECT_VALUE) {
                        object.put(name, new JSONValue(JSONValue.type.JSON_NULL, "null"));
                        stage = Stage.READ_VALUE;
                    }
                }
                case TOKEN_COLON: {
                    if (stage == Stage.EXPECT_COLON) {
                        stage = Stage.EXPECT_VALUE;
                        continue;
                    }
                }
                case TOKEN_COMMA: {
                    if (stage == Stage.READ_VALUE) {
                        stage = Stage.EXPECT_KEY;
                        continue;
                    }
                }
                case TOKEN_OPEN_CURLY_BRACE: {
                    if (stage == Stage.EXPECT_VALUE) {
                        object.put(name, new JSONValue(JSONValue.type.JSON_OBJECT, parseObject(tokenList)));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_OPEN_SQUARE_BRACKET: {
                    if (stage == Stage.EXPECT_VALUE) {
                        object.put(name, new JSONValue(JSONValue.type.JSON_ARRAY, parseArray(tokenList)));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_CLOSE_CURLY_BRACE: {
                    if (stage == Stage.READ_VALUE) {
                        return object;
                    }
                }
            }
        }
        return object;
    }

    private JSONArray parseArray(List<Token> tokenList) {
        JSONArray array = new JSONArray();
        int stage = Stage.EXPECT_VALUE;
        while (tokenList.size() != 0) {
            Token token = tokenList.remove(0);
            switch (token.tokenType) {
                case TOKEN_EMPTY: {
                    continue;
                }
                case TOKEN_STRING: {
                    if (stage == Stage.EXPECT_VALUE) {
                        array.add(new JSONValue(JSONValue.type.JSON_STRING, token.value));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_NUMBER: {
                    if (stage == Stage.EXPECT_VALUE) {
                        array.add(new JSONValue(JSONValue.type.JSON_NUMBER, Float.parseFloat(token.value)));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_BOOLEAN: {
                    if (stage == Stage.EXPECT_VALUE) {
                        array.add(new JSONValue(JSONValue.type.JSON_BOOLEAN, Boolean.parseBoolean(token.value)));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_NULL: {
                    if (stage == Stage.EXPECT_VALUE) {
                        array.add(new JSONValue(JSONValue.type.JSON_NULL, "null"));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_OPEN_CURLY_BRACE: {
                    if (stage == Stage.EXPECT_VALUE) {
                        array.add(new JSONValue(JSONValue.type.JSON_OBJECT, parseObject(tokenList)));
                        stage = Stage.READ_VALUE;
                        continue;
                    }
                }
                case TOKEN_COMMA: {
                    if (stage == Stage.READ_VALUE) {
                        stage = Stage.EXPECT_KEY;
                        continue;
                    }
                }
                case TOKEN_CLOSE_SQUARE_BRACKET: {
                    if (stage == Stage.READ_VALUE) {
                        return array;
                    }
                }
            }
        }
        return array;
    }

}
