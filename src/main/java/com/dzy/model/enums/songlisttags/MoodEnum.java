package com.dzy.model.enums.songlisttags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum MoodEnum {

    ALL(-100, "全部"),
    POP(1, "流行"),
    CPOP(2, "国风");

    private final Integer moodId;
    private final String moodTagName;

    MoodEnum(Integer moodId, String moodTagName) {
        this.moodId = moodId;
        this.moodTagName = moodTagName;
    }

    public static String getMoodTagNameById(Integer moodId) {
        if (moodId == null) {
            return null;
        }
        for (MoodEnum mood : MoodEnum.values()) {
            if (moodId.equals(mood.getMoodId())) {
                return mood.getMoodTagName();
            }
        }
        return null;
    }

    public Integer getMoodId() {
        return moodId;
    }

    public String getMoodTagName() {
        return moodTagName;
    }

}
