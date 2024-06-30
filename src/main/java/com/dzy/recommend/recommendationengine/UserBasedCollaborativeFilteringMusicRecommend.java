package com.dzy.recommend.recommendationengine;

import com.dzy.recommend.recommendutils.UserCFMusicRecommendUtil;
import com.dzy.service.SongService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/6  13:17
 */
@Component
public class UserBasedCollaborativeFilteringMusicRecommend {

    @Resource
    private SongService songService;

    public List<Long> recommendSong(Long targetUserId, int n) {
        return UserCFMusicRecommendUtil.getRecommendSongIdList(targetUserId, n);
    }

}
