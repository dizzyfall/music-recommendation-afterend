package com.dzy.job.once;

import com.dzy.model.entity.UserRating;
import com.dzy.service.UserRatingService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/28  14:34
 */
public class UserRatingGenerator {

    @Resource
    private UserRatingService userRatingService;

    public void generateUserRatingToDB() {
        List<Double> songRatingList = Arrays.asList(0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            UserRating userRating = new UserRating();
            Long userId = (long) random.nextInt(200) + 1;
            userRating.setUserId(userId);
            Long songId = (long) random.nextInt(90) + 1;
            userRating.setSongId(songId);
            int songRatingIndex = random.nextInt(11);
            Double songRating = songRatingList.get(songRatingIndex);
            userRating.setSongRating(songRating);
            userRatingService.save(userRating);
        }
    }

}
