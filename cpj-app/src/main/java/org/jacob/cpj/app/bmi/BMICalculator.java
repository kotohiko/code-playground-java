package org.jacob.cpj.app.bmi;

import org.jacob.cpj.common.CPJCommonConsoleInputReader;

import java.io.BufferedReader;

/**
 * @author Kotohiko
 * @since 10:36 Sep 12, 2024
 */
public class BMICalculator {

    public static void main(String[] args) {
        try (BufferedReader consoleInput = CPJCommonConsoleInputReader.consoleReader()) {
            String line;
            // 提示用户输入体重（千克）
            System.out.print("请输入您的体重（千克）: ");
            while ((line = consoleInput.readLine()) != null) {

                line = consoleInput.readLine();

            }
        } catch (Exception e) {
            System.out.println("发生异常");
        }


        double weight = scanner.nextDouble();

        // 提示用户输入身高（米）
        System.out.print("请输入您的身高（米）: ");
        double height = scanner.nextDouble();

        // 计算BMI
        double bmi = calculateBMI(weight, height);

        // 输出BMI值
        System.out.println("您的BMI值为: " + bmi);

        // 根据BMI值给出健康建议
        String advice = getHealthAdvice(bmi);
        System.out.println(advice);

    }

    /**
     * 计算BMI值
     *
     * @param weight 体重（千克）
     * @param height 身高（米）
     * @return BMI值
     */
    private static double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    /**
     * 根据BMI值给出健康建议
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