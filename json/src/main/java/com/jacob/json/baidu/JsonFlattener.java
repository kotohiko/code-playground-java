package com.jacob.json.baidu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonFlattener {

    public static Map<String, Object> flattenJson(JsonNode jsonNode) {
        Map<String, Object> flattened = new HashMap<>();
        flattenNode("", jsonNode, flattened);
        return flattened;
    }

    private static void flattenNode(String prefix, JsonNode node, Map<String, Object> flattened) {
        var fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = prefix.isEmpty() ? field.getKey() : prefix + "." + field.getKey();
            if (field.getValue().isObject()) {
                flattenNode(key, field.getValue(), flattened);
            } else if (field.getValue().isArray()) {
                // 你可以根据需要处理数组，例如递归扁平化数组中的对象
                // 这里只是简单地将数组作为字符串添加
                flattened.put(key, field.getValue().toString());
            } else {
                flattened.put(key, field.getValue().asText());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String jsonString = "{ \"a\": 1, \"b\": { \"c\": 2, \"d\": { \"e\": 3 } } }";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonString);
        Map<String, Object> flattened = flattenJson(jsonNode);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flattened));
    }
}