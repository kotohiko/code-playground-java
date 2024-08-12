package kingdee.bos.util;

@FunctionalInterface
public interface PropertyProvider {

    SystemProperties get(String var1);
}
