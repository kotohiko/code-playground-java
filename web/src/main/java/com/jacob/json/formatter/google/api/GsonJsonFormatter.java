package com.jacob.json.formatter.google.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonJsonFormatter {
    public static String formatJsonByGson(String unformattedJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(gson.fromJson(unformattedJson, Object.class));
    }
}
