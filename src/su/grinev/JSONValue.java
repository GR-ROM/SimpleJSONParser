package su.grinev;

import static su.grinev.JSONValue.type.JSON_STRING;

public class JSONValue {

    public static class type {
        public static final int JSON_OBJECT = 0;
        public static final int JSON_ARRAY = 1;
        public static final int JSON_NUMBER = 2;
        public static final int JSON_STRING = 3;
        public static final int JSON_BOOLEAN = 4;
        public static final int JSON_NULL = 5;
    }

    private Integer type;
    private Object value;

    public JSONValue(Integer type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (type == JSON_STRING) {
            return (String) value;
        }
        return "";
    }
}
