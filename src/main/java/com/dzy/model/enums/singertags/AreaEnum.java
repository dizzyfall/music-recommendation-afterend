package com.dzy.model.enums.singertags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:36
 */
public enum AreaEnum {
    ALL(-100, "全部"),
    ML(1, "内地"),
    HT(2, "港台"),
    EU(3, "欧美"),
    JAN(4, "日本"),
    KR(5, "韩国"),
    OTHER(20, "其他");

    private final Integer areaId;
    private final String areaTagName;

    AreaEnum(Integer areaId, String areaTagName) {
        this.areaId = areaId;
        this.areaTagName = areaTagName;
    }

    public static String getAreaTagNameById(Integer areaId) {
        if (areaId == null) {
            return null;
        }
        for (AreaEnum area : AreaEnum.values()) {
            if (areaId.equals(area.getAreaId())) {
                return area.getAreaTagName();
            }
        }
        return null;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public String getAreaTagName() {
        return areaTagName;
    }

}
