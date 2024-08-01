package com.jacob.snowflake.kingdee.bos.lang;


import com.jacob.snowflake.kingdee.sdk.annotation.SdkPublic;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jacob Suen
 * @since 13:46 Aug 01, 2024
 */
@SdkPublic
public enum Lang {

    zh_CN("zh-CN"),
    zh_TW("zh-TW"),
    en_US("en-US"),
    AF("AF"),
    AR("AR"),
    BG("BG"),
    CA("CA"),
    HR("HR"),
    CS("CS"),
    DA("DA"),
    NL("NL"),
    ET("ET"),
    FI("FI"),
    FR("FR"),
    DE("DE"),
    EL("EL"),
    HU("HU"),
    IS("IS"),
    ID("ID"),
    IT("IT"),
    JA("JA"),
    KO("KO"),
    LT("LT"),
    LV("LV"),
    MS("MS"),
    NO("NO"),
    PL("PL"),
    RU("RU"),
    SR("SR"),
    SK("SK"),
    SL("SL"),
    SV("SV"),
    TH("TH"),
    TR("TR"),
    UK("UK"),
    VI("VI"),
    PT("PT"),
    ES("ES"),
    MN("MN"),
    BN("BN");

    private final Locale locale;
    private static LangHolder holder;
    private static Map<String, Lang> enumMap;

    private Lang(String languageTag) {
        this.locale = Locale.forLanguageTag(languageTag);
        getEnumMap().put(this.name().toLowerCase(), this);
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getLangTag() {
        return this.locale.toLanguageTag();
    }

    private static synchronized Map<String, Lang> getEnumMap() {
        if (enumMap == null) {
            enumMap = new HashMap<>(3);
        }

        return enumMap;
    }

    public static Lang from(String lang) {
        Lang ret = null;
        if (lang != null) {
            ret = (Lang) enumMap.get(lang.toLowerCase());
        }

        if (ret == null) {
            ret = defaultLang();
        }

        return ret;
    }

    public static Lang from(Locale locale) {
        Lang[] var1 = values();

        for (Lang lang : var1) {
            if (lang.locale.equals(locale)) {
                return lang;
            }
        }

        return defaultLang();
    }

    public static Lang defaultLang() {
        Lang lang = (Lang) enumMap.get(System.getProperty("default_lang", zh_CN.toString()).toLowerCase());
        return lang == null ? zh_CN : lang;
    }

    public static void setHolder(LangHolder holder) {
        Lang.holder = holder;
    }

    public static Lang get() {
        Lang lang = holder == null ? defaultLang() : holder.get();
        return lang == null ? defaultLang() : lang;
    }
}