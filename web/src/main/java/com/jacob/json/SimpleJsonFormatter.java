package com.jacob.json;

import com.jacob.json.utils.FormatJsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleJsonFormatter {
    public static void main(String[] args) throws IOException {
        getInput();
    }

    private static void getInput() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            String ret = FormatJsonUtils.formatJsonByGson(line);
            System.out.println(ret);
        }
    }
}
