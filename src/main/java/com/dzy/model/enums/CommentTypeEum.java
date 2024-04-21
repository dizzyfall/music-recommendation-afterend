package com.dzy.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/17  9:48
 */
public enum CommentTypeEum {
    SONG_TYPE("歌曲", "song"),

    ALBUM_TYPE("专辑", "album"),

    SONGLIST_TYPE("歌单", "songlist");

    private final String commentType;
    private final String value;

    CommentTypeEum(String commentType, String value) {
        this.commentType = commentType;
        this.value = value;
    }

    public static CommentTypeEum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (CommentTypeEum commentTypeEum : CommentTypeEum.values()) {
            if (value.equals(commentTypeEum.getValue())) {
                return commentTypeEum;
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
