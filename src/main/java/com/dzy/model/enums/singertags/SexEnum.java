package com.dzy.model.enums.singertags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum SexEnum {

    ALL(-100, "全部"),
    MALE(0, "男"),
    FEMALE(1, "女"),
    GROP(2, "组合");

    private final Integer sexId;
    private final String sexTagName;

    SexEnum(Integer sexId, String sexTagName) {
        this.sexId = sexId;
        this.sexTagName = sexTagName;
    }

    public static String getSexTagNameById(Integer sexId) {
        if (sexId == null) {
            return null;
        }
        for (SexEnum sex : SexEnum.values()) {
            if (sexId.equals(sex.getSexId())) {
                return sex.getSexTagName();
            }
        }
        return null;
    }

    public Integer getSexId() {
        return sexId;
    }

    public String getSexTagName() {
        return sexTagName;
    }

}
