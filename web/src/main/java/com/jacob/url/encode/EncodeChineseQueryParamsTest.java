package com.jacob.url.encode;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EncodeChineseQueryParamsTest {

    public static void main(String[] args) {
        String url = "https://www.bilibili.com?";
        var map = new HashMap<String, String>() {{
            put("用户名", "jacob");
            put("pwd", "123456");
            put("验证码", "abcd");
        }};
        System.out.println(url + buildApiQueryParamString(map));
    }

    /**
     * 构建 Query 参数 URL 子串，并对参数中的中文进行转义
     *
     * @param queryParamMap Query 参数映射对象
     * @return Query 参数字符串
     */
    private static String buildApiQueryParamString(Map<String, String> queryParamMap) {
        var ret = new StringBuilder();
        queryParamMap.forEach((key, val) -> {
            key = URLEncoder.encode(key, StandardCharsets.UTF_8);
            val = URLEncoder.encode(val, StandardCharsets.UTF_8);
            ret.append(key).append("=").append(val).append("&");
        });
        ret.deleteCharAt(ret.length() - 1);
        return ret.toString();
    }
}
