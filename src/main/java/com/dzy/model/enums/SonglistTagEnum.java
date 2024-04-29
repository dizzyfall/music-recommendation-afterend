package com.dzy.model.enums;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/27  15:18
 */
public enum SonglistTagEnum {
    ALL(-100, "全部");

    private final Integer songlistTagId;
    private final String songlistTagName;

    SonglistTagEnum(Integer songlistTagId, String songlistTagName) {
        this.songlistTagId = songlistTagId;
        this.songlistTagName = songlistTagName;
    }

    public Integer getSonglistTagId() {
        return songlistTagId;
    }

    public String getSonglistTagName() {
        return songlistTagName;
    }

}
