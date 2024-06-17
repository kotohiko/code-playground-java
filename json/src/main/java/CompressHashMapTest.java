import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompressHashMapTest {
    public static void main(String[] args) {
        String jsonString = "{"
                + "\"a\": {"
                + "    \"b\": {"
                + "        \"c\": 1,"
                + "        \"d\": [\"e\", \"f\"]"
                + "    },"
                + "    \"g\": 2"
                + "},"
                + "\"h\": [3, 4]"
                + "}";
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> flatMap = flatten(jsonObject);
        System.out.println(flatMap);
    }

    public static Map<String, Object> flatten(JSONObject jsonObject) {
        Map<String, Object> flatMap = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                // Recursively flatten nested JSON objects
                Map<String, Object> nestedMap = flatten((JSONObject) value);
                for (Map.Entry<String, Object> entry : nestedMap.entrySet()) {
                    flatMap.put(key + "." + entry.getKey(), entry.getValue());
                }
            } else if (value instanceof JSONArray jsonArray) {
                // Handle arrays by flattening each element (assuming they are not JSON objects)
                for (int i = 0; i < jsonArray.length(); i++) {
                    flatMap.put(key + "[" + i + "]", jsonArray.get(i));
                }
            } else {
                // Add non-JSON object values directly to the map
                flatMap.put(key, value);
            }
        }
        return flatMap;
    }
}
