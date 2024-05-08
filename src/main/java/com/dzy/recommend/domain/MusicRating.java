package com.dzy.recommend.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 音乐评分模型
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/6  11:41
 */
@Data
public class MusicRating {

    private Map<Long, Double> songRatings;

    public MusicRating() {
        songRatings = new HashMap<>();
    }

    public void addSongRating(Long songId, Double rating) {
        songRatings.put(songId, rating);
    }

}
