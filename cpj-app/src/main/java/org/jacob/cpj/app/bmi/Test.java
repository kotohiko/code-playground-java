package org.jacob.cpj.app.bmi;

/**
 * @author Kotohiko
 * @since 14:36 9月 30, 2024
 */
public class Test {
    public static void main(String[] args) {
        int value = getValue(2);
        System.out.println(value);
    }

    public static int getValue(int i) {
        int result = 0;
        switch (i) {
            case 1:
                result = result + i;
            case 2:
                result = result + i * 2;
            case 3:
                result = result + i * 3;
        }
        return result;
    }
}