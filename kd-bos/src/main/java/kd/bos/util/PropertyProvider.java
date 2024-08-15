package kd.bos.util;

@FunctionalInterface
public interface PropertyProvider {

    SystemProperties get(String var1);
}
