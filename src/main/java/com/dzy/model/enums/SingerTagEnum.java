package com.dzy.model.enums;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/27  15:18
 */
public enum SingerTagEnum {
    ALL(-100, "全部");


    private final Integer singerTagId;
    private final String singerTagName;

    SingerTagEnum(Integer singerTagId, String singerTagName) {
        this.singerTagId = singerTagId;
        this.singerTagName = singerTagName;
    }

    public Integer getSingerTagId() {
        return singerTagId;
    }

    public String getSingerTagName() {
        return singerTagName;
    }

}
