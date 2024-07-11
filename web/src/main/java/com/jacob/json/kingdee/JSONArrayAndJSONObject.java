package com.jacob.json.kingdee;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jacob.json.utils.FormatJsonUtils;

import java.util.HashMap;
import java.util.Map;

public class JSONArrayAndJSONObject {

    public static void main(String[] args) {
        String bilibiliStrJson = """
                {
                    "code": 1,
                    "msg": "获取成功",
                    "time": "2024-07-10",
                    "data": [
                        {
                            "title": "B站的小伙伴们久等了，我来了！",
                            "heat": "145.5万",
                            "link": "https://www.bilibili.com/video/av1106028480/"
                        },
                        {
                            "title": "土松犬，信我，好的一批，好可爱，好喜欢",
                            "heat": "162.3万",
                            "link": "https://www.bilibili.com/video/av1506085090/"
                        },
                        {
                            "title": "《崩坏：星穹铁道》翡翠角色PV——「欲望收藏」",
                            "heat": "192.1万",
                            "link": "https://www.bilibili.com/video/av1456192687/"
                        }
                    ]
                }
                """;
        String weatherStrJson = """
                {
                  "coord": {
                    "lon": 10.99,
                    "lat": 44.34
                  },
                  "weather": [
                    {
                      "id": 800,
                      "main": "Clear",
                      "description": "clear sky",
                      "icon": "01d"
                    }
                  ],
                  "base": "stations",
                  "main": {
                    "temp": 304.74,
                    "feels_like": 307.71,
                    "temp_min": 302.27,
                    "temp_max": 305.42,
                    "pressure": 1014,
                    "humidity": 54,
                    "sea_level": 1014,
                    "grnd_level": 933
                  },
                  "visibility": 10000,
                  "wind": {
                    "speed": 1.16,
                    "deg": 82,
                    "gust": 2.1
                  },
                  "clouds": {
                    "all": 4
                  },
                  "dt": 1720708139,
                  "sys": {
                    "type": 2,
                    "id": 2004688,
                    "country": "IT",
                    "sunrise": 1720669357,
                    "sunset": 1720724407
                  },
                  "timezone": 7200,
                  "id": 3163858,
                  "name": "Zocca",
                  "cod": 200
                }
                """;
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
        JSONObject jsonNeedToBeFlattened = JSONObject.parseObject(weatherStrJson);
        JSONObject flattenedJson = new JSONObject();
        getFlattenedJson(jsonNeedToBeFlattened, flattenedJson, weatherRespParamStruct);
        System.out.println(FormatJsonUtils.formatJsonByGson(flattenedJson.toJSONString()));
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
