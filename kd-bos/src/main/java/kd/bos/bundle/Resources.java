package kd.bos.bundle;

import com.jacob.sys.snowflake.kingdee.bos.lang.Lang;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Resources {

    private static final ResourceBundle NONEXISTENT_BUNDLE = new ResourceBundle() {
        public Enumeration<String> getKeys() {
            return null;
        }

        protected Object handleGetObject(String key) {
            return null;
        }

        public String toString() {
            return "NONEXISTENT_BUNDLE";
        }
    };
    private static final Map<String, ResourceBundle> bundleMap = new ConcurrentHashMap<>();

    public Resources() {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String getString(String project, String key, Object... args) {
        return getString(Lang.get().getLocale(), project, key, null, args);
    }

    public static String get(String project, String key, String desc, Object... args) {
        return get(Lang.get().getLocale(), project, key, desc, args);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String getString(Locale locale, String project, String key, String desc, Object... args) {
        return get(locale, project, key, desc, args);
    }

    public static String get(Locale locale, String project, String key, String desc, Object... args) {
        String resource = project + '_' + locale.toString();
        ResourceBundle bundle = bundleMap.get(resource);
        if (bundle == null) {
            bundle = bundleMap.computeIfAbsent(resource, (k) -> {
                try {
                    return ResourceBundle.getBundle("resources/" + project, locale);
                } catch (MissingResourceException var4) {
                    return NONEXISTENT_BUNDLE;
                }
            });
        }

        if (bundle == NONEXISTENT_BUNDLE) {
            return desc == null ? missBundle(key) : format(missBundle(desc), args);
        } else {
            String s = null;

            try {
                s = bundle.getString(key);
                return snowflake.kingdee.bos.utils.JavaVersion.isJava17OrLater()
                        ? format(s, args)
                        : format(new String(s.getBytes("ISO8859-1"), StandardCharsets.UTF_8), args);
            } catch (UnsupportedEncodingException var9) {
                return format(s, args);
            } catch (MissingResourceException var10) {
                return desc == null ? missBundleKey(key) : format(missBundleKey(desc), args);
            }
        }
    }

    public static String format(String s, Object... args) {
        if (args != null && args.length > 0) {
            try {
                return s.contains("%s") ? String.format(s, args) : MessageFormat.format(s, args);
            } catch (Exception var3) {
                return s + ':' + Arrays.toString(args);
            }
        } else {
            return s;
        }
    }

    public static String missBundle(String key) {
        return Boolean.getBoolean("lang.miss.mark") ? "!" + key : key;
    }

    public static String missBundleKey(String key) {
        return Boolean.getBoolean("lang.miss.mark") ? "!!" + key : key;
    }
}