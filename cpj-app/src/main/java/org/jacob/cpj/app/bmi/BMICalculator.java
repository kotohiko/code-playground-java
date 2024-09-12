package org.jacob.cpj.app.bmi;

import org.jacob.cpj.common.CPJCommonConsoleInputReader;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Kotohiko
 * @since 10:36 Sep 12, 2024
 */
public class BMICalculator {

    public static void main(String[] args) {
        try (BufferedReader consoleInput = CPJCommonConsoleInputReader.consoleReader()) {
            String exitCommand = "exit";
            while (true) {
                try {
                    System.out.print("请输入您的体重（千克，输入 'exit' 退出）: ");
                    String weightInput = consoleInput.readLine();
                    if (weightInput.equalsIgnoreCase(exitCommand)) {
                        break;
                    }
                    double weight = Double.parseDouble(weightInput);

                    System.out.print("请输入您的身高（米）: ");
                    double height = Double.parseDouble(consoleInput.readLine());

                    double bmi = calculateBMI(weight, height);
                    System.out.println("您的BMI值为: " + bmi);
                    String advice = getHealthAdvice(bmi);
                    System.out.println(advice);
                } catch (IOException | NumberFormatException e) {
                    System.err.println("输入错误，请确保您输入的是有效的数字。");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算 BMI 值
     *
     * @param weight 体重（千克）
     * @param height 身高（米）
     * @return BMI值
     */
    private static double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    /**
     * 根据 BMI 值给出健康建议
     *
     * @param bmi BMI值
     * @return 健康建议
     */
    private static String getHealthAdvice(double bmi) {
        if (bmi < 18.5) {
            return "您的体重过轻，建议适当增加营养摄入。";
        } else if (bmi >= 18.5 && bmi < 24) {
            return "您的体重正常，继续保持良好的生活习惯。";
        } else if (bmi >= 24 && bmi < 28) {
            return "您的体重过重，建议注意饮食控制和适量运动。";
        } else if (bmi >= 28 && bmi < 32) {
            return "您的体重肥胖，建议采取减肥措施。";
        } else {
            return "您的体重严重肥胖，建议尽快就医寻求专业帮助。";
        }
    }
}