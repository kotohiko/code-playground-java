package org.jacob.cpj.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * CPJ common console input reader.
 *
 * @author Jacob Suen
 * @since 22:49 Aug 23, 2024
 */
public class CpjCommonConsoleInputReader {
    public static BufferedReader consoleReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }
}