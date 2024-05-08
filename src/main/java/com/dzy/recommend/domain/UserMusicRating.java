package com.dzy.recommend.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户音乐评分模型
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/6  13:39
 */
@Data
public class UserMusicRating {
    private Map<Long, MusicRating> userMusicRatings;

    public UserMusicRating() {
        userMusicRatings = new HashMap<>();
    }

    public void adduserMusicRating(Long userId, MusicRating musicRating) {
        userMusicRatings.put(userId, musicRating);
    }

}
