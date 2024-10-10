package org.jacob.serial;

import org.jacob.serial.constants.CpjSerialConstants;
import org.jacob.serial.entity.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Demonstrates the serialization of a User object.
 * This class creates a User object, serializes it to a file, and prints a message indicating
 * the success of the serialization.
 *
 * @author Kotohiko
 * @since 00:53 Oct 10, 2024
 */
public class SerializeUser {

    /**
     * Main method that demonstrates the serialization of a User object.
     * Creates a new User object with a name and password, then serializes it to a specified file path.
     * Prints a confirmation message upon successful serialization.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        User user = new User("Alice", "secretPassword");

        try (
                FileOutputStream fileOut = new FileOutputStream(CpjSerialConstants.CPJ_USER_SERIAL_PATH);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            out.writeObject(user);
            System.out.println("Serialized data is saved in user.ser");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
