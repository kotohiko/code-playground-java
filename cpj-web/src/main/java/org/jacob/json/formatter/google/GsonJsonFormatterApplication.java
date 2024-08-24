package org.jacob.json.formatter.google;

import org.jacob.cpj.common.CPJCommonConsoleInputReader;
import org.jacob.json.formatter.google.api.GsonJsonFormatter;

import java.io.BufferedReader;

/**
 * 简单 JSON 格式化工具，调用的是 {@link com.google.gson} 包相关 API
 */
public class GsonJsonFormatterApplication {
    public static void main(String[] args) {
        getInput();
    }

    private static void getInput() {
        try (BufferedReader consoleInput = CPJCommonConsoleInputReader.consoleReader()) {
            String line;
            while ((line = consoleInput.readLine()) != null) {
                String ret = GsonJsonFormatter.formatJsonByGson(line);
                System.out.println(ret);
            }
        } catch (Exception e) {
            System.out.println("发生异常");
            getInput();
        }
    }
}
