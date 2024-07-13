package com.jacob.json.kingdee;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jacob.json.utils.FormatJsonUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class JSONArrayAndJSONObject {

    private static final String OPEN_WEATHER_MAP = "openweathermap";

    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try (FileInputStream fileInput = new FileInputStream("web/src/main/resources/KDIiirsJsonCfgTest.yml")) {
            // 加载YAML文件到Java对象
            Map<String, Object> data = yaml.load(fileInput);
            // 假设我们知道结构，直接访问
            Map<String, Object> json = (Map<String, Object>) data.get(OPEN_WEATHER_MAP);
            Map<String, Object> bilibiliRespParamStruct = new HashMap<>() {{
                put("code", null);
                put("msg", null);
                put("time", null);
                put("data", new HashMap<>() {{
                    put("title", null);
                    put("heat", null);
                }});
            }};
            Map<String, Object> weatherRespParamStruct = new HashMap<>() {{
                put("base", null);
                put("timezone", null);
                put("id", null);
                put("coord", new HashMap<>() {{
                    put("lon", null);
                    put("lat", null);
                }});
                put("main", new HashMap<>() {{
                    put("temp", null);
                    put("feels_like", null);
                    put("temp_min", null);
                    put("temp_max", null);
                    put("pressure", null);
                    put("humidity", null);
                    put("sea_level", null);
                    put("grnd_level", null);
                }});
            }};
            JSONObject jsonNeedToBeFlattened = JSONObject.parseObject(JSON.toJSON(json).toString());
            JSONObject flattenedJson = new JSONObject();
            getFlattenedJson(jsonNeedToBeFlattened, flattenedJson, weatherRespParamStruct);
            System.out.println(FormatJsonUtils.formatJsonByGson(flattenedJson.toJSONString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 若遇到多层JSON，则压缩为一层给解析调用方
     *
     * @param jsonNeedToBeFlattened 源数据
     * @param flattenedJson         压缩后的数据
     * @param respParamStruct       DyO 获取的返回参数结构
     */
    private static void getFlattenedJson(Object jsonNeedToBeFlattened, JSONObject flattenedJson,
                                         Map<String, Object> respParamStruct) {
        respParamStruct.forEach((k, v) -> {
            if (jsonNeedToBeFlattened instanceof JSONObject && ((JSONObject) jsonNeedToBeFlattened).containsKey(k)) {
                Object valueOfJsonNeedToBeFlattened = ((JSONObject) jsonNeedToBeFlattened).get(k);
                if (valueOfJsonNeedToBeFlattened instanceof JSONObject) {
                    Map<String, Object> paramMap = new HashMap<>(16);
                    paramMap.putAll((Map<? extends String, ?>) valueOfJsonNeedToBeFlattened);
                    // 递归调用
                    getFlattenedJson(valueOfJsonNeedToBeFlattened, flattenedJson, paramMap);
                } else if (valueOfJsonNeedToBeFlattened instanceof JSONArray) {
                    JSONObject newFlattenedJson = new JSONObject();
                    Map<String, Object> paramMap = new HashMap<>(16);
                    for (Object jsonObject : ((JSONArray) valueOfJsonNeedToBeFlattened)) {
                        if (jsonObject instanceof JSONObject) {
                            // 遍历当前 JSONObject 的每一个键
                            for (String key : ((JSONObject) jsonObject).keySet()) {
                                // 获取当前键的值
                                Object val = ((JSONObject) jsonObject).get(key);
                                newFlattenedJson.merge(key, val, (a, b) -> a + "; " + b);
                                flattenedJson.putAll(newFlattenedJson);
                            }
                        }
                    }
                    paramMap.putAll((Map<? extends String, ?>) jsonNeedToBeFlattened);
                    // 递归调用
                    getFlattenedJson(valueOfJsonNeedToBeFlattened, flattenedJson, paramMap);
                } else {
                    flattenedJson.put(k, ((JSONObject) jsonNeedToBeFlattened).get(k));
                }
            } else if (jsonNeedToBeFlattened instanceof JSONArray) {
//                Object valueOfJsonNeedToBeFlattened = ((JSONArray) jsonNeedToBeFlattened).get(k);
//                JSONObject newFlattenedJson = new JSONObject();
//                Map<String, Object> paramMap = new HashMap<>(16);
//                for (Object jsonObject : ((JSONArray) valueOfJsonNeedToBeFlattened)) {
//                    if (jsonObject instanceof JSONObject) {
//                        // 遍历当前 JSONObject 的每一个键
//                        for (String key : ((JSONObject) jsonObject).keySet()) {
//                            // 获取当前键的值
//                            Object val = ((JSONObject) jsonObject).get(key);
//                            newFlattenedJson.merge(key, val, (a, b) -> a + ", " + b);
//                        }
//                        logger.info(newFlattenedJson.toJSONString());
//                    }
//                }
//                paramMap.putAll(newFlattenedJson);
//                // 递归调用
//                getFlattenedJson(valueOfJsonNeedToBeFlattened, flattenedJson, paramMap);
            }
        });
    }
}
