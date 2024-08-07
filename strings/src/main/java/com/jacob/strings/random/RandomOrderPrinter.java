package com.jacob.strings.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jacob Suen
 * @since 11:38 Aug 07, 2024
 */
public class RandomOrderPrinter {

    public static void main(String[] args) {
        String input = "什么都懂等到以后人类准备完蛋的时候，把本皇女推选成世界总统，才能拯救人类";
        printStringInRandomOrder(input);
    }

    public static void printStringInRandomOrder(String input) {
        // 将字符串转换为List<Character>
        List<Character> charList = new ArrayList<>();
        for (char c : input.toCharArray()) {
            charList.add(c);
        }

        // 使用Collections.shuffle()随机打乱List
        Collections.shuffle(charList);

        // 遍历并打印List中的每个字符
        for (char c : charList) {
            System.out.print(c);
        }
        System.out.println(); // 在字符串打印完成后换行
    }
}