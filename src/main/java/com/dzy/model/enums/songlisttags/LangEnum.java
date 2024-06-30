package com.dzy.model.enums.songlisttags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum LangEnum {

    ALL(-100, "全部"),
    MANDARIN(0, "国语"),
    CANTONESE(1, "粤语"),
    ENGLISH(5, "英语");

    private final Integer langId;
    private final String langTagName;

    LangEnum(Integer langId, String langTagName) {
        this.langId = langId;
        this.langTagName = langTagName;
    }

    public static String getLangTagNameById(Integer langId) {
        if (langId == null) {
            return null;
        }
        for (LangEnum genre : LangEnum.values()) {
            if (langId.equals(genre.getLangId())) {
                return genre.getLangTagName();
            }
        }
        return null;
    }

    public Integer getLangId() {
        return langId;
    }

    public String getLangTagName() {
        return langTagName;
    }

}
