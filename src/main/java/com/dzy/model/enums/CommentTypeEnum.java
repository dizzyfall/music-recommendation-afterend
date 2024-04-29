package com.dzy.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/17  9:48
 */
public enum CommentTypeEnum {
    SONG_TYPE("歌曲", "song"),

    ALBUM_TYPE("专辑", "album"),

    SONGLIST_TYPE("歌单", "songlist");

    private final String commentType;
    private final String value;

    CommentTypeEnum(String commentType, String value) {
        this.commentType = commentType;
        this.value = value;
    }

    public static CommentTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (value.equals(commentTypeEnum.getValue())) {
                return commentTypeEnum;
            }
        }
        return null;
    }

    public String getCommentType() {
        return commentType;
    }

    public String getValue() {
        return value;
    }

}
