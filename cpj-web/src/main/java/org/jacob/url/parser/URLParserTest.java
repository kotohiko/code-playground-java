package org.jacob.url.parser;

import org.jacob.cpj.common.CpjCommonConsoleInputReader;
import org.jacob.url.parser.service.URLParserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author Jacob Suen
 * @since 23:24 8月 23, 2024
 */
public class URLParserTest {
    public static void main(String[] args) {
        try (BufferedReader consoleInput = CpjCommonConsoleInputReader.consoleReader()) {
            System.out.print("Please enter the url: ");
            String urlStr;
            while ((urlStr = consoleInput.readLine()) != null) {
                var components = URLParserService.parseURL(urlStr);
                System.out.println("Parsed URL Components: ");
                components.forEach((key, value) -> {
                    if (value instanceof Map) {
                        System.out.println(key + ":");
                        ((Map<?, ?>) value).forEach((k, v) -> System.out.println("  " + k + ": " + v));
                    } else {
                        System.out.println(key + ": " + value);
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Cannot read your input contents, please check and try again.");
        }
    }
}