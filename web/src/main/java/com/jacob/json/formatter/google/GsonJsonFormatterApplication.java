package com.jacob.json.formatter.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 简单 JSON 格式化工具，调用的是 {@link com.google.gson} 包相关 API
 */
public class GsonJsonFormatterApplication {
    public static void main(String[] args) throws IOException {
        getInput();
    }

    private static void getInput() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            String ret = com.jacob.json.formatter.google.api.GsonJsonFormatter.formatJsonByGson(line);
            System.out.println(ret);
        }
    }
}
