package com.dzy.recommend.recommendutils;

import com.dzy.recommend.domain.MusicRating;

import java.util.HashSet;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/6  18:56
 */
public class SimilarityCalculator {

    public static Double calculateCosineSimilarity(MusicRating user1MusicRating, MusicRating user2MusicRating) {
        //获取两个用户共同评价过的音乐Id
        HashSet<Long> commonSongIdSet = UserCFMusicRecommendUtil.getCommonSongIdSet(user1MusicRating, user2MusicRating);
        //计算点积和每个用户的向量模
        double dotProduct = 0.0;
        double user1Norm = 0.0;
        double user2Norm = 0.0;
        for (Long songId : commonSongIdSet) {
            double rating1 = user1MusicRating.getSongRatings().getOrDefault(songId, 0.0);
            double rating2 = user2MusicRating.getSongRatings().getOrDefault(songId, 0.0);
            dotProduct += rating1 * rating2;
            user1Norm += Math.pow(rating1, 2);
            user2Norm += Math.pow(rating2, 2);
        }
        //计算余弦相似度
        if (user1Norm != 0.0 && user2Norm != 0.0) {
            return dotProduct / (Math.sqrt(user1Norm) * Math.sqrt(user2Norm));
        } else {
            //如果没有共同评价的项目，则相似度为0
            return 0.0;
        }
    }

    public static Double calculateCosineSimilarity(Long userId1, Long userId2) {
        MusicRating user1MusicRating = UserCFMusicRecommendUtil.getMusicRatingById(userId1);
        MusicRating user2MusicRating = UserCFMusicRecommendUtil.getMusicRatingById(userId2);
        //获取两个用户共同评价过的音乐Id
        HashSet<Long> commonSongIdSet = UserCFMusicRecommendUtil.getCommonSongIdSet(user1MusicRating, user2MusicRating);
        //计算点积和每个用户的向量模
        double dotProduct = 0.0;
        double user1Norm = 0.0;
        double user2Norm = 0.0;
        for (Long songId : commonSongIdSet) {
            double rating1 = user1MusicRating.getSongRatings().getOrDefault(songId, 0.0);
            double rating2 = user2MusicRating.getSongRatings().getOrDefault(songId, 0.0);
            dotProduct += rating1 * rating2;
            user1Norm += Math.pow(rating1, 2);
            user2Norm += Math.pow(rating2, 2);
        }
        //计算余弦相似度
        if (user1Norm != 0.0 && user2Norm != 0.0) {
            return dotProduct / (Math.sqrt(user1Norm) * Math.sqrt(user2Norm));
        } else {
            //如果没有共同评价的项目，则相似度为0
            return 0.0;
        }
    }

}
