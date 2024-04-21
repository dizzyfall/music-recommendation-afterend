package com.dzy.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/3/12  10:11
 */
public enum UserImageUploadEnum {

    USER_AVATAR("用户头像", "user_avatar"),
    USER_BACKGROUND("用户空间背景", "user_background");

    private final String key;

    private final String value;

    UserImageUploadEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static UserImageUploadEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (UserImageUploadEnum uploadEnum : UserImageUploadEnum.values()) {
            if (value.equals(uploadEnum.getValue())) {
                return uploadEnum;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
