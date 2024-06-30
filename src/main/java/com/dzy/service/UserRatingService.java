package com.dzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.dto.userrating.SongRatingCreateRequest;
import com.dzy.model.entity.UserRating;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.recommend.domain.MusicRating;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author DZY
 * @description 针对表【user_rating(用户评分表)】的数据库操作Service
 * @createDate 2024-05-20 22:41:29
 */
public interface UserRatingService extends IService<UserRating>, Serializable {

    /**
     * 创建歌曲评分
     *
     * @param songRatingCreateRequest
     * @return java.lang.Boolean
     * @date 2024/5/20  23:26
     */
    Boolean createSongRating(SongRatingCreateRequest songRatingCreateRequest);

    /**
     * 获取评分
     *
     * @param userId
     * @param songId
     * @return java.lang.Double
     * @date 2024/5/21  0:14
     */
    Double getSongRating(Long userId, Long songId);

    /**
     * 获取评分列表
     *
     * @param
     * @return java.util.List<com.dzy.model.entity.UserRating>
     * @date 2024/5/21  0:30
     */
    List<UserRating> getUserRatingList();

    Map<Long, MusicRating> constructUserMusicRatingMatrix();

    Map<Long, Double> getTargetUserSimilarityMatrix(Long targetUserId);

    List<Long> findTopNSimilarUsers(Long targetUserId, int n);

    List<Long> getRecommendSongIdList(Long targetUserId, int n);

    List<SongIntroVO> offlineRecommendSong(Long targetUserId, int n);

    List<SongIntroVO> realtimeRecommend(Long targetUserId, int n);

    void realtimeRecommendSong(Long userId, int n);

}
