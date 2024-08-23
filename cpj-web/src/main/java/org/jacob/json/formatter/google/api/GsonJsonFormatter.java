package org.jacob.json.formatter.google.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 调用 Gson 接口
 */
public class GsonJsonFormatter {
    public static String formatJsonByGson(String unformattedJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(gson.fromJson(unformattedJson, Object.class));
    }
}
