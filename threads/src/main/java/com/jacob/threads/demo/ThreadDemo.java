package com.jacob.threads.demo;

/**
 * 思考一下这里究竟是怎么回事？
 *
 * @author Jacob Suen
 * @since 13:52 8月 06, 2024
 */
public class ThreadDemo {
    public static void main(String[] args) {
        for (var i = 0; i < 10; ++i) {
            System.out.println("out");
            System.err.println("err");
        }
    }
}