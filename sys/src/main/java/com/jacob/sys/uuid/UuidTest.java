package com.jacob.sys.uuid;

import java.util.UUID;

/**
 * @author Jacob Suen
 * @since 13:27 8月 12, 2024
 */
public class UuidTest {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        String replace = uuid.toString().replace("-", "");
        System.out.println(replace);
        System.out.println(replace.length());
    }
}