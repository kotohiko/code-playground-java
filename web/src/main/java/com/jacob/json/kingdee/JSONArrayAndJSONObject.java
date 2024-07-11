package com.jacob.json.kingdee;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONArrayAndJSONObject {

    public static void main(String[] args) {
        String strJson = """
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
        Map<String, Object> respParamStruct = new HashMap<>() {{
            put("code", null);
            put("msg", null);
            put("time", null);
            put("data", new HashMap<>() {{
                put("title", null);
                put("heat", null);
            }});
        }};
        JSONObject jsonNeedToBeFlattened = JSONObject.parseObject(strJson);
        JSONObject flattenedJson = new JSONObject();
        getFlattenedJson(jsonNeedToBeFlattened, flattenedJson, respParamStruct);
        System.out.println(flattenedJson.toJSONString());
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
                    if (v == null) {
                        paramMap.putAll((Map<? extends String, ?>) valueOfJsonNeedToBeFlattened);
                    } else {
                        paramMap.putAll((LinkedHashMap) v);
                    }
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
