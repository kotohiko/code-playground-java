package kingdee.bos.util;

/**
 * @author Jacob Suen
 * @since 13:42 Aug 01, 2024
 */
public final class JavaVersion {

    private static final int majorJavaVersion = determineMajorJavaVersion();

    public JavaVersion() {
    }

    private static int determineMajorJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        return getMajorJavaVersion(javaVersion);
    }

    static int getMajorJavaVersion(String javaVersion) {
        int version = parseDotted(javaVersion);
        if (version == -1) {
            version = extractBeginningInt(javaVersion);
        }

        return version == -1 ? 6 : version;
    }

    private static int parseDotted(String javaVersion) {
        try {
            String[] e = javaVersion.split("[._]");
            int firstVer = Integer.parseInt(e[0]);
            return firstVer == 1 && e.length > 1 ? Integer.parseInt(e[1]) : firstVer;
        } catch (NumberFormatException var3) {
            return -1;
        }
    }

    private static int extractBeginningInt(String javaVersion) {
        try {
            StringBuilder e = new StringBuilder();

            for (int i = 0; i < javaVersion.length(); ++i) {
                char c = javaVersion.charAt(i);
                if (!Character.isDigit(c)) {
                    break;
                }

                e.append(c);
            }

            return Integer.parseInt(e.toString());
        } catch (NumberFormatException var4) {
            return -1;
        }
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    public static boolean isJava17OrLater() {
        return majorJavaVersion >= 17;
    }
}