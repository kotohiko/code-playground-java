package org.jacob.url.parser.service;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jacob Suen
 * @since 19:37 Aug 23, 2024
 */
public class URLParserService {

    /**
     * Parse the given URL and return a {@code Map} containing the parsing results.
     *
     * @param urlStr the given URL
     * @return a {@code Map} containing the parsing results
     */
    public static Map<String, Object> parseURL(String urlStr) throws MalformedURLException {
        Map<String, Object> parsedComponents = new LinkedHashMap<>();
        var uri = URI.create(urlStr);
        URL url;
        url = uri.toURL();
        parsedComponents.put("protocol", url.getProtocol());
        parsedComponents.put("host", url.getHost());
        parsedComponents.put("port", url.getPort() == -1 ? "default" : url.getPort());
        parsedComponents.put("path", url.getPath());
        parsedComponents.put("file", url.getFile());
        parsedComponents.put("ref", url.getRef());

        var queryParameters = parseQuery(url.getQuery());
        parsedComponents.put("query", queryParameters);
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
                var key = idx > 0
                        ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8)
                        : pair;
                var value = idx > 0 && pair.length() > idx + 1
                        ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8)
                        : null;
                queryParameters.put(key, value);
            }
        }
        return queryParameters;
    }
}