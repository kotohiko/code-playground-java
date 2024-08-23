package com.jacob.url.parser;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jacob Suen
 * @since 19:37 Aug 23, 2024
 */
public class URLParser {

    /**
     * 解析给定的 URL 并返回一个包含解析结果的 Map。
     *
     * @param urlStr 要解析的 URL 字符串
     * @return 包含解析结果的 Map
     */
    public static Map<String, Object> parseURL(String urlStr) {
        Map<String, Object> parsedComponents = new LinkedHashMap<>();
        try {
            var uri = URI.create(urlStr);
            var url = uri.toURL();

            parsedComponents.put("protocol", url.getProtocol());
            parsedComponents.put("host", url.getHost());
            parsedComponents.put("port", url.getPort() == -1 ? "default" : url.getPort());
            parsedComponents.put("path", url.getPath());
            parsedComponents.put("file", url.getFile());
            parsedComponents.put("ref", url.getRef());

            Map<String, String> queryParameters = parseQuery(url.getQuery());
            parsedComponents.put("query", queryParameters);

        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + urlStr);
            e.printStackTrace();
        }

        return parsedComponents;
    }

    /**
     * 解析查询字符串为键值对 Map。
     *
     * @param queryStr 查询字符串
     * @return 包含查询参数的 Map
     */
    private static Map<String, String> parseQuery(String queryStr) {
        var queryParameters = new LinkedHashMap<String, String>();
        if (queryStr != null) {
            var pairs = queryStr.split("&");
            for (var pair : pairs) {
                int idx = pair.indexOf("=");
                var key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
                var value = idx > 0 && pair.length() > idx + 1
                        ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8)
                        : null;
                queryParameters.put(key, value);
            }
        }
        return queryParameters;
    }

    public static void main(String[] args) {
        var urlStr = "https://www.example.com:8080/path/to/resource?param1=value1&param2=value2#fragment";
        Map<String, Object> components = parseURL(urlStr);

        System.out.println("Parsed URL Components:");
        components.forEach((key, value) -> {
            if (value instanceof Map) {
                System.out.println(key + ":");
                ((Map<?, ?>) value).forEach((k, v) -> System.out.println("  " + k + ": " + v));
            } else {
                System.out.println(key + ": " + value);
            }
        });
    }
}