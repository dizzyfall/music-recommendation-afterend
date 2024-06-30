package com.dzy.model.dto.kafka;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/31  13:15
 */
@Data
public class KafkaRealTimeSongRating implements Serializable {

    private static final long serialVersionUID = 7594786985327442680L;

    private Long userId;

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 歌曲评分
     */
    private Double songRating;

}
