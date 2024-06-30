package com.dzy.recommend.recommendutils;

import com.dzy.commonutils.SpringContextUtil;
import com.dzy.model.entity.UserRating;
import com.dzy.service.UserRatingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/6  19:24
 */
@Service
public class UserMusicRatingDataLoader {

    public List<UserRating> loadRatingData() {
        UserRatingService userRatingService = SpringContextUtil.getBean(UserRatingService.class);
        return userRatingService.getUserRatingList();
    }

}
