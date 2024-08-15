package kd.bos.util;

/**
 * @author Jacob Suen
 * @since 14:37 8月 12, 2024
 */
public class SystemProperties {

    private static PropertyProvider propertyProvider;

    public SystemProperties() {
    }

    public static void setPropertyProvider(PropertyProvider propertyProvider) {
        SystemProperties.propertyProvider = propertyProvider;
    }

    public static String get(String key) {
        return System.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        } else {
            value = System.getenv(key);
            return value != null ? value : defaultValue;
        }
    }

    public static String getWithEnv(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
        }

        return value;
    }

    public static String getWithEnv(String key, String defaultValue) {
        String withEnv = getWithEnv(key);
        if (withEnv == null) {
            withEnv = defaultValue;
        }

        return withEnv;
    }
}