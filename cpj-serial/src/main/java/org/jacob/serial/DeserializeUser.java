package org.jacob.serial;

import org.jacob.serial.constants.CpjSerialConstants;
import org.jacob.serial.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Demonstrates the deserialization of a User object.
 * This class reads a serialized User object from a file and prints the deserialized object.
 *
 * @author Kotohiko
 * @since 00:59 Oct 10, 2024
 */
public class DeserializeUser {

    /**
     * Main method that demonstrates the deserialization of a User object.
     * Reads a serialized User object from a specified file path and prints the deserialized object.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try (
                FileInputStream fileIn = new FileInputStream(CpjSerialConstants.CPJ_USER_SERIAL_PATH);
                ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            User user = (User) in.readObject();
            System.out.println("Deserialized User: " + user);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
