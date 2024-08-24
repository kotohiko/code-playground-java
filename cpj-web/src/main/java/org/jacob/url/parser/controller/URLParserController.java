package org.jacob.url.parser.controller;

import org.jacob.cpj.common.CPJCommonConsoleInputReader;
import org.jacob.cpj.common.CPJConstants;
import org.jacob.url.parser.service.URLParserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author Jacob Suen
 * @since 10:44 Aug 24, 2024
 */
public class URLParserController {
    public static void getInput() {
        try (BufferedReader consoleInput = CPJCommonConsoleInputReader.consoleReader()) {
            String urlStr;
            System.out.print("Please enter the url: ");
            while (true) {
                try {
                    urlStr = consoleInput.readLine();
                    if (urlStr == null || urlStr.trim().isEmpty()) {
                        System.out.println("Input is empty, please enter a valid URL.");
                        continue;
                    } else if (urlStr.equals("exit")) {
                        break;
                    }
                    var components = URLParserService.parseURL(urlStr);
                    System.out.println(CPJConstants.START_LINE);
                    System.out.println("Parsed URL components: ");
                    components.forEach((key, value) -> {
                        if (value instanceof Map) {
                            System.out.println(key + ":");
                            ((Map<?, ?>) value).forEach((k, v) -> System.out.println("  " + k + ": " + v));
                        } else {
                            System.out.println(key + ": " + value);
                        }
                    });
                    waitForTheAnotherInput();
                } catch (Exception e) {
                    System.out.println("An error occurred while parsing the URL. "
                            + "Please check your input and try again.");
                    System.out.print("Please enter the url: ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void waitForTheAnotherInput() {
        System.out.println(CPJConstants.DIVIDING_LINE);
        System.out.print("Please enter the url: ");
    }
}