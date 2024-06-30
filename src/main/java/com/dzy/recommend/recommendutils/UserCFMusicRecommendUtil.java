package com.dzy.recommend.recommendutils;

import com.dzy.model.entity.UserRating;
import com.dzy.recommend.domain.MusicRating;
import com.dzy.recommend.domain.UserMusicRating;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤算法工具类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/4  23:25
 */
public class UserCFMusicRecommendUtil {

    public static Map<Long, MusicRating> constructUserMusicRatingMatrix() {
        UserMusicRating userMusicRatingMatrix = new UserMusicRating();
        UserMusicRatingDataLoader userMusicRatingDataLoader = new UserMusicRatingDataLoader();
        List<UserRating> userRatingList = userMusicRatingDataLoader.loadRatingData();
        Map<Long, List<UserRating>> userRatingMap = userRatingList.stream().collect(Collectors.groupingBy(UserRating::getUserId));
        for (Map.Entry<Long, List<UserRating>> userRatingEntry : userRatingMap.entrySet()) {
            MusicRating userRatings = new MusicRating();
            for (UserRating userRating : userRatingEntry.getValue()) {
                userRatings.addSongRating(userRating.getSongId(), userRating.getSongRating());
            }
            userMusicRatingMatrix.adduserMusicRating(userRatingEntry.getKey(), userRatings);
        }

        // 假设我们有以下用户评分数据  
//        MusicRating user1Ratings = new MusicRating();
//        user1Ratings.addSongRating(1L, 2.0);
//        user1Ratings.addSongRating(2L, 3.0);
//        user1Ratings.addSongRating(3L, 1.0);
//
//        MusicRating user2Ratings = new MusicRating();
//        user2Ratings.addSongRating(2L, 1.0);
//        user2Ratings.addSongRating(3L, 3.0);
//
//        MusicRating user3Ratings = new MusicRating();
//        user3Ratings.addSongRating(3L, 2.0);
//        user3Ratings.addSongRating(4L, 4.0);
//        user3Ratings.addSongRating(5L, 3.0);
//        user3Ratings.addSongRating(6L, 4.0);

        return userMusicRatingMatrix.getUserMusicRatings();
    }

    public static Set<Long> getCurrentSongIdSet() {
        Set<Long> currentSongIdSet = new HashSet<>();
        Map<Long, MusicRating> userMusicRatingMatrix = constructUserMusicRatingMatrix();
        for (Map.Entry<Long, MusicRating> userMusicRating : userMusicRatingMatrix.entrySet()) {
            Map<Long, Double> songRatings = userMusicRating.getValue().getSongRatings();
            for (Map.Entry<Long, Double> songRating : songRatings.entrySet()) {
                currentSongIdSet.add(songRating.getKey());
            }
        }
        return currentSongIdSet;
    }

    public static Set<Long> getCurrentUserIdSet() {
        Set<Long> currentUserIdSet = new HashSet<>();
        Map<Long, MusicRating> userMusicRatingMatrix = constructUserMusicRatingMatrix();
        for (Map.Entry<Long, MusicRating> userMusicRating : userMusicRatingMatrix.entrySet()) {
            currentUserIdSet.add(userMusicRating.getKey());
        }
        return currentUserIdSet;
    }

    public static int getUserNum() {
        Map<Long, MusicRating> userMusicRatingMatrix = constructUserMusicRatingMatrix();
        return userMusicRatingMatrix.size();
    }

    public static long getSongNum() {
        return getCurrentSongIdSet().size();
    }

    public static MusicRating getMusicRatingById(Long userId) {
        return constructUserMusicRatingMatrix().get(userId);
    }

    /**
     * 获取两个用户共同评价过的音乐Id
     *
     * @param user1MusicRating
     * @param user2MusicRating
     * @return java.util.HashSet<java.lang.Long>
     * @date 2024/5/6  0:14
     */
    public static HashSet<Long> getCommonSongIdSet(MusicRating user1MusicRating, MusicRating user2MusicRating) {
        HashSet<Long> commonSongIdSet = new HashSet<>(user1MusicRating.getSongRatings().keySet());
        commonSongIdSet.retainAll(user2MusicRating.getSongRatings().keySet());
        return commonSongIdSet;
    }

    public static HashSet<Long> getCommonSongIdSet(Long userId1, Long userId2) {
        MusicRating user1MusicRating = getMusicRatingById(userId1);
        MusicRating user2MusicRating = getMusicRatingById(userId2);
        return getCommonSongIdSet(user1MusicRating, user2MusicRating);
    }

    public static Map<Long, Double> getTargetUserSimilarityMatrix(Long targetUserId) {
        Map<Long, Double> userSimilarity = new HashMap<>();
        ArrayList<Long> currentUserIdList = new ArrayList<>(getCurrentUserIdSet());
        for (Long currentUserId : currentUserIdList) {
            if (!targetUserId.equals(currentUserId)) {
                double similarity = SimilarityCalculator.calculateCosineSimilarity(targetUserId, currentUserId);
                userSimilarity.put(currentUserId, similarity);
            }
        }
        return userSimilarity;
    }

    /**
     * 找到与目标用户最相似的N个用户
     *
     * @param targetUserId
     * @param n
     * @return java.util.List<java.lang.Long>
     * @date 2024/5/6  0:14
     */
    public static List<Long> findTopNSimilarUsers(Long targetUserId, int n) {
        Map<Long, Double> userSimilarity = getTargetUserSimilarityMatrix(targetUserId);
        //相似度排序
        List<Map.Entry<Long, Double>> similarityEntries = new ArrayList<>(userSimilarity.entrySet());
        similarityEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        List<Long> topNSimilarUsers = new ArrayList<>();
        for (int i = 0; i < Math.min(n, similarityEntries.size()); i++) {
            topNSimilarUsers.add(similarityEntries.get(i).getKey());
        }
        return topNSimilarUsers;
    }

    public static List<Long> getPredictSongIdList(Long userId) {
        Set<Long> hasPredictedSongIdSet = getMusicRatingById(userId).getSongRatings().keySet();
        Set<Long> currentSongIdSet = getCurrentSongIdSet();
        Set<Long> predictSongIdSet = new HashSet<>(currentSongIdSet);
        predictSongIdSet.removeAll(hasPredictedSongIdSet);
        return new ArrayList<>(predictSongIdSet);
    }

    public static double predictSongRating(Long targetUserId, Long songId, int n) {
        List<Long> topNSimilarUsers = findTopNSimilarUsers(targetUserId, n);
        Map<Long, Double> userSimilarity = getTargetUserSimilarityMatrix(targetUserId);
        double sim = 0.0;
        double simSum = 0.0;
        for (Long userId : topNSimilarUsers) {
            Double songRating = getMusicRatingById(userId).getSongRatings().getOrDefault(songId, 0.0);
            sim += userSimilarity.get(userId) * songRating;
            simSum += userSimilarity.get(userId);
        }
        return sim / simSum;
    }

    public static List<Map.Entry<Long, Double>> getSongRatingList(Long targetUserId, int n) {
        Map<Long, Double> songRatingMap = new HashMap<>();
        List<Long> predictSongIdList = getPredictSongIdList(targetUserId);
        for (Long songId : predictSongIdList) {
            double songRating = predictSongRating(targetUserId, songId, n);
            songRatingMap.put(songId, songRating);
        }
        return songRatingMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
    }

    public static List<Long> getRecommendSongIdList(Long targetUserId, int n) {
        List<Map.Entry<Long, Double>> songRatingList = getSongRatingList(targetUserId, n);
        return songRatingList.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

}
