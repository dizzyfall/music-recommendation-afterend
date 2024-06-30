package com.dzy.model.enums.songlisttags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum ThemeEnum {

    ALL(-100, "全部"),
    POP(1, "流行"),
    CPOP(2, "国风");

    private final Integer themeId;
    private final String themeTagName;

    ThemeEnum(Integer themeId, String themeTagName) {
        this.themeId = themeId;
        this.themeTagName = themeTagName;
    }

    public static String getThemeTagNameById(Integer themeId) {
        if (themeId == null) {
            return null;
        }
        for (ThemeEnum theme : ThemeEnum.values()) {
            if (themeId.equals(theme.getThemeId())) {
                return theme.getThemeTagName();
            }
        }
        return null;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public String getThemeTagName() {
        return themeTagName;
    }

}
