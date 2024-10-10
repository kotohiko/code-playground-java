package org.jacob.serial.statictest;

import org.jacob.serial.constants.CpjSerialConstants;

import java.io.*;

/**
 * Demonstrates the serialization and deserialization of a class with a static variable.
 * The class is serialized to a file, and after modifying the static variable,
 * it is deserialized to observe if the static variable retains its modified value.
 * <p>
 * Note: Static variables are not part of an object's state and thus are not serialized.
 *
 * @author Kotohiko
 * @since 13:09 Oct 10, 2024
 */
public class StaticVarSerialTest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A static integer variable initialized to 1.
     */
    public static int num = 1;

    /**
     * Main method that demonstrates serialization and deserialization of the StaticVarSerialTest instance.
     * It writes the current instance to a file, modifies the static variable {@code num},
     * reads back the object from the file, and prints the value of {@code num} after deserialization.
     *
     * @param args Command line arguments (not used).
     */
    @SuppressWarnings("all")
    public static void main(String[] args) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(CpjSerialConstants.CPJ_STATIC_VAR_SERIAL_PATH);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(new StaticVarSerialTest());

            StaticVarSerialTest.num = 666;

            ObjectInputStream objectInputStream
                    = new ObjectInputStream(new FileInputStream(CpjSerialConstants.CPJ_STATIC_VAR_SERIAL_PATH));
            StaticVarSerialTest t = (StaticVarSerialTest) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(StaticVarSerialTest.num);
    }
}