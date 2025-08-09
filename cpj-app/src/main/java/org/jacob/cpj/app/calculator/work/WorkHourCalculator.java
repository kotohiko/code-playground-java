/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2025-2025. All rights reserved.
 */

package org.jacob.cpj.app.calculator.work;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author s60066131
 * @version 1.0
 * @since 14:15:10 Jan 10, 2025
 */
public class WorkHourCalculator {

    private static final Pattern TIME_FORMAT_PATTERN = Pattern.compile("^(\\d{1,2}:\\d{2}:\\d{2})$");

    private static final int TEN_HOURS_IN_SECONDS = 10 * 60 * 60;

    private static final String DEFAULT_TIME = "17:30:00";

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String startTimeStr = readInput("Please enter the start work time (format: HH:MM:SS): ", in);
                int startSeconds = convertTimeToSeconds(startTimeStr);

                String endTimeStr = readInput("Please enter the end work time (format: HH:MM:SS): ", in);
                if (endTimeStr.equalsIgnoreCase("default")) {
                    endTimeStr = DEFAULT_TIME;
                }
                int endSeconds = convertTimeToSeconds(endTimeStr);

                int totalSeconds = endSeconds - startSeconds;
                totalSeconds -= TEN_HOURS_IN_SECONDS;

                double resultMinutes = (double) totalSeconds / 60;
                String formattedMinutes = String.format("%.3f", resultMinutes);

                System.out.printf("The final result is: %s minutes, and it has been copied to the clipboard.\n",
                        formattedMinutes);

                copyToClipboard(formattedMinutes);
                System.out.println("===========================END LINE============================");
            } catch (IOException e) {
                System.err.println("An error occurred while reading input: " + e.getMessage());
            }
        }
    }

    private static String readInput(String prompt, BufferedReader in) throws IOException {
        System.out.print(prompt);
        return in.readLine();
    }

    private static int convertTimeToSeconds(String timeStr) {
        Matcher matcher = TIME_FORMAT_PATTERN.matcher(timeStr);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("时间格式不正确");
        }

        String[] timeParts = timeStr.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        if (hours == 17 && minutes >= 30) {
            hours = 18;
            minutes = 0;
            seconds = 0;
        }

        if (hours >= 0 && hours <= 5) {
            return (23 * 3600 + 60 * 60) + (hours * 3600 + minutes * 60 + seconds);
        }

        return hours * 3600 + minutes * 60 + seconds;
    }

    private static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}