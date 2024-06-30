package com.dzy.controller;

import com.dzy.common.BaseResponse;
import com.dzy.commonutils.ResponseUtil;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.dto.kafka.KafkaRealTimeSongRating;
import com.dzy.model.dto.recommend.RecommendSongQueryRequest;
import com.dzy.model.dto.userrating.SongRatingCreateRequest;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.model.vo.userinfo.UserLoginVO;
import com.dzy.service.SongService;
import com.dzy.service.UserInfoService;
import com.dzy.service.UserRatingService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/20  22:53
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Resource
    private UserRatingService userRatingService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private SongService songService;

    @Resource
    private KafkaTemplate<Long, KafkaRealTimeSongRating> kafkaTemplate;

    @Resource
    private RedisTemplate<String, List<SongIntroVO>> redisTemplate;

    /**
     * 离线推荐
     *
     * @return
     */
    @PostMapping("/offline/song")
    public BaseResponse<List<SongIntroVO>> offlineRecommendSong(@RequestBody RecommendSongQueryRequest recommendSongQueryRequest) {
        Long userId = recommendSongQueryRequest.getUserId();
        int n = recommendSongQueryRequest.getN();
        List<SongIntroVO> songIntroVOList = userRatingService.offlineRecommendSong(userId, n);
//        List<Long> recommendSongIdList = userRatingService.getRecommendSongIdList(userId, n);
//        List<SongIntroVO> songIntroVOList = recommendSongIdList.stream().map(recommendSongId -> songService.getSongIntroById(recommendSongId)).collect(Collectors.toList());
        System.out.println(userRatingService.constructUserMusicRatingMatrix());
        System.out.println(userRatingService.getTargetUserSimilarityMatrix(recommendSongQueryRequest.getUserId()));
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, songIntroVOList, "获取推荐列表成功");
    }

    /**
     * 实时推荐
     *
     * @return
     */
    @PostMapping("/realtime/song")
    public BaseResponse<List<SongIntroVO>> realtimeRecommendSong(@RequestBody SongRatingCreateRequest songRatingCreateRequest, HttpServletRequest request) {
        if (songRatingCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR, "创建请求参数为空");
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        //是否登录
        UserLoginVO loginUserVO = userInfoService.getUserInfoLoginState(request);
        if (loginUserVO == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        //是否是本人
        Long loginUserId = loginUserVO.getId();
        Long requestUserId = songRatingCreateRequest.getUserId();
        if (!loginUserId.equals(requestUserId)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户登录信息不一致");
        }
        Boolean isSongRatingCreate = userRatingService.createSongRating(songRatingCreateRequest);
        if (!isSongRatingCreate) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, "创建歌曲评分失败");
        }

        int n = songRatingCreateRequest.getN();
        Long userId = songRatingCreateRequest.getUserId();
        Long songId = songRatingCreateRequest.getSongId();
        Double songRating = songRatingCreateRequest.getSongRating();

        KafkaRealTimeSongRating kafkaRealTimeSongRating = new KafkaRealTimeSongRating();
        kafkaRealTimeSongRating.setUserId(userId);
        kafkaRealTimeSongRating.setSongId(songId);
        kafkaRealTimeSongRating.setSongRating(songRating);
        //kafkaTemplate.send("rating-song-topic", userId, kafkaRealTimeSongRating);

        //userRatingService.realtimeRecommendSong(userId, n);

        String redisKey = "mr:user:offlinerecommend:" + userId;
        List<SongIntroVO> realtimeRecommendSongIntroVOList = redisTemplate.opsForValue().get(redisKey);
        return ResponseUtil.success(StatusCode.RETRIEVE_SUCCESS, realtimeRecommendSongIntroVOList, "获取推荐列表成功");
    }

}
