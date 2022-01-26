package su.grinev;

import java.util.*;

import static java.util.stream.Collectors.toCollection;
import static su.grinev.JSONValue.type.JSON_OBJECT;

public class JSONObject {

    private final Map<String, JSONValue> jsonValueMap = new LinkedHashMap<>();

    public Map<String, JSONValue> getJsonValueMap() {
        return jsonValueMap;
    }

    void put(String key, JSONValue value) {
        jsonValueMap.put(key, value);
    }

    public JSONValue getValueRecursively(String path) {
        List<String> pathList = Arrays.stream(path.split("/"))
                .filter(s -> s != "")
                .collect(toCollection(LinkedList::new));
        return traverse(pathList, this);
    }

    public JSONValue getValue(String name) {
        return jsonValueMap.get(name);
    }

    private JSONValue traverse(List<String> pathList, JSONObject currentObject) {
        String key = pathList.remove(0);
        if (pathList.size() > 0) {
            if (currentObject.jsonValueMap.get(key).getType() == JSON_OBJECT) {
                JSONObject jsonObject = (JSONObject)currentObject.jsonValueMap.get(key).getValue();
                return jsonObject.traverse(pathList, jsonObject);
            }
        } else {
            if (pathList.size() == 0) {
                return currentObject.jsonValueMap.get(key);
            }
        }
        return null;
    }
}
