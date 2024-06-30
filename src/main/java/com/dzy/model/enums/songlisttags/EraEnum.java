package com.dzy.model.enums.songlisttags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum EraEnum {

    ALL(-100, "全部"),
    ERA00(1, "00年代"),
    ERA90(2, "90年代"),
    ERA80(3, "80年代"),
    ERA70(4, "70年代"),
    ERA60(5, "60年代");

    private final Integer eraId;
    private final String eraTagName;

    EraEnum(Integer eraId, String eraTagName) {
        this.eraId = eraId;
        this.eraTagName = eraTagName;
    }

    public static String getEraTagNameById(Integer eraId) {
        if (eraId == null) {
            return null;
        }
        for (EraEnum era : EraEnum.values()) {
            if (eraId.equals(era.getEraId())) {
                return era.getEraTagName();
            }
        }
        return null;
    }

    public Integer getEraId() {
        return eraId;
    }

    public String getEraTagName() {
        return eraTagName;
    }

}
