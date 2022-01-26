package su.grinev;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {

    private List<JSONValue> jsonValueList = new ArrayList<>();

    public void add(JSONValue value) {
        jsonValueList.add(value);
    }

}
