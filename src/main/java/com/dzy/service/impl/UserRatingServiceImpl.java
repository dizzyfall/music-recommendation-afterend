package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.UserRatingMapper;
import com.dzy.model.dto.userrating.SongRatingCreateRequest;
import com.dzy.model.entity.UserRating;
import com.dzy.model.vo.song.SongIntroVO;
import com.dzy.recommend.domain.MusicRating;
import com.dzy.recommend.domain.UserMusicRating;
import com.dzy.recommend.recommendutils.SimilarityCalculator;
import com.dzy.service.SongService;
import com.dzy.service.UserRatingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author DZY
 * @description 针对表【user_rating(用户评分表)】的数据库操作Service实现
 * @createDate 2024-05-20 22:41:29
 */
@Service
public class UserRatingServiceImpl extends ServiceImpl<UserRatingMapper, UserRating>
        implements UserRatingService, Serializable {

    private static final long serialVersionUID = 3132116418982145727L;

    @Resource
    private SongService songService;

    @Resource
    private RedisTemplate<String, List<SongIntroVO>> redisTemplate;

    /**
     * 创建歌曲评分
     *
     * @param songRatingCreateRequest
     * @return java.lang.Boolean
     * @date 2024/5/20  23:27
     */
    @Override
    public Boolean createSongRating(SongRatingCreateRequest songRatingCreateRequest) {
        if (songRatingCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long userId = songRatingCreateRequest.getUserId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Long songId = songRatingCreateRequest.getSongId();
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        Double songRating = songRatingCreateRequest.getSongRating();
        if (songRating == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setSongId(songId);
        userRating.setSongRating(songRating);
        boolean isSave = this.save(userRating);
        if (!isSave) {
            throw new BusinessException(StatusCode.CREATE_ERROR, "创建歌曲评分失败");
        }
        return true;
    }

    /**
     * 获取评分
     *
     * @param userId
     * @param songId
     * @return java.lang.Double
     * @date 2024/5/21  0:14
     */
    @Override
    public Double getSongRating(Long userId, Long songId) {
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        if (songId == null) {
            throw new BusinessException(StatusCode.PARAMS_NULL_ERROR);
        }
        QueryWrapper<UserRating> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("song_id", songId);
        UserRating userRating = this.getOne(queryWrapper);
//        if(userRating==null){
//            return null;
//        }
        return userRating.getSongRating();
    }

    /**
     * 获取评分列表
     *
     * @return java.lang.Double
     * @date 2024/5/21  0:14
     */
    @Override
    public List<UserRating> getUserRatingList() {
        List<UserRating> userRatingList = this.list();
        if (CollectionUtils.isEmpty(userRatingList)) {
            return new ArrayList<>();
        }
        return userRatingList;
    }

    @Override
    public Map<Long, MusicRating> constructUserMusicRatingMatrix() {
        UserMusicRating userMusicRatingMatrix = new UserMusicRating();
        List<UserRating> userRatingList = getUserRatingList();
        Map<Long, List<UserRating>> userRatingMap = userRatingList.stream().collect(Collectors.groupingBy(UserRating::getUserId));
        for (Map.Entry<Long, List<UserRating>> userRatingEntry : userRatingMap.entrySet()) {
            MusicRating userRatings = new MusicRating();
            for (UserRating userRating : userRatingEntry.getValue()) {
                userRatings.addSongRating(userRating.getSongId(), userRating.getSongRating());
            }
            userMusicRatingMatrix.adduserMusicRating(userRatingEntry.getKey(), userRatings);
        }
        return userMusicRatingMatrix.getUserMusicRatings();
    }

    public Set<Long> getCurrentSongIdSet() {
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

    public int getUserNum() {
        Map<Long, MusicRating> userMusicRatingMatrix = constructUserMusicRatingMatrix();
        return userMusicRatingMatrix.size();
    }

    public long getSongNum() {
        return getCurrentSongIdSet().size();
    }

    public MusicRating getMusicRatingById(Long userId) {
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
    public HashSet<Long> getCommonSongIdSet(MusicRating user1MusicRating, MusicRating user2MusicRating) {
        HashSet<Long> commonSongIdSet = new HashSet<>(user1MusicRating.getSongRatings().keySet());
        commonSongIdSet.retainAll(user2MusicRating.getSongRatings().keySet());
        return commonSongIdSet;
    }

    public Set<Long> getCurrentUserIdSet() {
        Set<Long> currentUserIdSet = new HashSet<>();
        Map<Long, MusicRating> userMusicRatingMatrix = constructUserMusicRatingMatrix();
        for (Map.Entry<Long, MusicRating> userMusicRating : userMusicRatingMatrix.entrySet()) {
            currentUserIdSet.add(userMusicRating.getKey());
        }
        return currentUserIdSet;
    }

    public HashSet<Long> getCommonSongIdSet(Long userId1, Long userId2) {
        MusicRating user1MusicRating = getMusicRatingById(userId1);
        MusicRating user2MusicRating = getMusicRatingById(userId2);
        return getCommonSongIdSet(user1MusicRating, user2MusicRating);
    }

    @Override
    public Map<Long, Double> getTargetUserSimilarityMatrix(Long targetUserId) {
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
    @Override
    public List<Long> findTopNSimilarUsers(Long targetUserId, int n) {
        Map<Long, Double> userSimilarity = getTargetUserSimilarityMatrix(targetUserId);
        //相似度排序
        List<Map.Entry<Long, Double>> similarityEntries = new ArrayList<>(userSimilarity.entrySet());
        similarityEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        List<Long> topNSimilarUsers = new ArrayList<>();
        for (int i = 0; i < Math.min(n, similarityEntries.size()); i++) {
            topNSimilarUsers.add(similarityEntries.get(i).getKey());
            System.out.println("与待推荐用户" + targetUserId + "的相似度：" + "其他用户id：" + similarityEntries.get(i).getKey() + "相似度：" + similarityEntries.get(i).getValue());
        }
        return topNSimilarUsers;
    }

    public List<Long> getPredictSongIdList(Long userId) {
        Set<Long> hasPredictedSongIdSet = getMusicRatingById(userId).getSongRatings().keySet();
        Set<Long> currentSongIdSet = getCurrentSongIdSet();
        Set<Long> predictSongIdSet = new HashSet<>(currentSongIdSet);
        predictSongIdSet.removeAll(hasPredictedSongIdSet);
        return new ArrayList<>(predictSongIdSet);
    }

    public double predictSongRating(Long targetUserId, Long songId, int n) {
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

    public List<Map.Entry<Long, Double>> getSongRatingList(Long targetUserId, int n) {
        Map<Long, Double> songRatingMap = new HashMap<>();
        List<Long> predictSongIdList = getPredictSongIdList(targetUserId);
        for (Long songId : predictSongIdList) {
            double songRating = predictSongRating(targetUserId, songId, n);
            System.out.println("评分预测：" + "歌曲id：" + songId + "歌曲预测评分：" + songRating);
            songRatingMap.put(songId, songRating);
        }
        return songRatingMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<Long> getRecommendSongIdList(Long targetUserId, int n) {
        List<Map.Entry<Long, Double>> songRatingList = getSongRatingList(targetUserId, n);
        return songRatingList.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public List<SongIntroVO> offlineRecommendSong(Long targetUserId, int n) {
        String redisKey = "mr:user:offlinerecommend:" + targetUserId;
        //查redis缓存
        List<SongIntroVO> offlineRecommendSongIntroVOList = redisTemplate.opsForValue().get(redisKey);
        //有缓存,直接返回
        if (offlineRecommendSongIntroVOList != null) {
            return offlineRecommendSongIntroVOList;
        }
        List<Map.Entry<Long, Double>> songRatingList = getSongRatingList(targetUserId, n);
        System.out.println(songRatingList);
        //获取离线推荐结果
        List<SongIntroVO> songIntroVOList = songRatingList.stream().map(songRating -> {
            Long songId = songRating.getKey();
            Double rating = songRating.getValue();
            SongIntroVO songIntro = songService.getSongIntroVOById(songId);
            songIntro.setRating(rating);
            return songIntro;
        }).collect(Collectors.toList());
        //没有缓存,加入redis缓存
        try {
            redisTemplate.opsForValue().set(redisKey, songIntroVOList, 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("redis set offlineRecommendSongIntroVOList error", e);
        }
        return songIntroVOList;
    }

    public List<SongIntroVO> realtimeRecommend(Long targetUserId, int n) {
        String redisKey = "mr:user:realtimerecommend:" + targetUserId;
        //查redis缓存
        List<SongIntroVO> realtimeRecommendSongIntroVOList = redisTemplate.opsForValue().get(redisKey);
        //有缓存,直接返回
        if (realtimeRecommendSongIntroVOList != null) {
            return realtimeRecommendSongIntroVOList;
        }
        List<Map.Entry<Long, Double>> songRatingList = getSongRatingList(targetUserId, n);
        System.out.println(songRatingList);
        //获取离线推荐结果
        List<SongIntroVO> songIntroVOList = songRatingList.stream().map(songRating -> {
            Long songId = songRating.getKey();
            Double rating = songRating.getValue();
            SongIntroVO songIntro = songService.getSongIntroVOById(songId);
            songIntro.setRating(rating);
            return songIntro;
        }).collect(Collectors.toList());
        //没有缓存,加入redis缓存
        try {
            redisTemplate.opsForValue().set(redisKey, songIntroVOList, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis set realtimeRecommendSongIntroVOList error", e);
        }
        return songIntroVOList;
    }

    //@KafkaListener(topics = "rating-song-topic", groupId = "music_recommendation_group")

    @Override

    public void realtimeRecommendSong(Long userId, int n) {
        String kafkaTopic = "rating-song-topic";
        String kafkaServers = "localhost:9092";

        //设置执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Long> longDataStreamSource = env.fromElements(userId);

        DataStream<String> recommendations = longDataStreamSource
                .process(new MyProcessFunction() {
                    @Override
                    public void processElement(Long userId, ProcessFunction<Long, String>.Context context, Collector<String> collector) throws Exception {
                        super.processElement(userId, context, collector);
                    }
                });
        // 执行作业
        try {
            env.execute("Realtime Music Recommendation Job");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


//        //配置Kafka消费者属性
//        Properties properties = new Properties();
//        properties.setProperty("bootstrap-servers", kafkaServers);
//        properties.setProperty("group-id", "music_recommendation_group");
//
//        //创建Kafka消费者
//        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
//                kafkaTopic, // Kafka主题
//                new SimpleStringSchema(), // 序列化/反序列化
//                properties);
//
//        //读取Kafka数据
//        DataStream<String> ratingStream = env.addSource(kafkaConsumer);
//
//        ratingStream.print();

//        //解析评分数据（假设评分数据是JSON格式）
//        DataStream<KafkaRealTimeSongRating> parsedRatings = ratingStream
//                .map(ratingJson -> {
//                    try {
//                        Gson gson = new Gson();
//                        return gson.fromJson(ratingJson, new TypeToken<KafkaRealTimeSongRating>() {
//                        }.getType());
//                    } catch (Exception e) {
//                        throw new RuntimeException("Failed to parse rating", e);
//                    }
//                });
//
//        DataStream<String> recommendations = parsedRatings
//                .keyBy("userId") // Key the stream by user ID
//                .process(new ProcessFunction<KafkaRealTimeSongRating, String>() {
//                    @Override
//                    public void processElement(KafkaRealTimeSongRating kafkaRealTimeSongRating, ProcessFunction<KafkaRealTimeSongRating, String>.Context context, Collector<String> collector) throws Exception {
//                        Long userId = kafkaRealTimeSongRating.getUserId();
//                        Long songId = kafkaRealTimeSongRating.getSongId();
//                        Double songRating = kafkaRealTimeSongRating.getSongRating();
//                        List<SongIntroVO> songIntroVOList = realtimeRecommend(userId, n);
//                        collector.collect("Recommendation for " + userId + ": " + songIntroVOList);
//                    }
//                });
//        // 执行作业
//        try {
//            env.execute("Realtime Music Recommendation Job");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Slf4j
    public static class MyProcessFunction extends ProcessFunction<Long, String> implements Serializable {
        @Resource
        private RedisTemplate<String, List<SongIntroVO>> redisTemplate;

        public List<SongIntroVO> realtimeRecommend(Long targetUserId, int n) {
            String redisKey = "mr:user:offlinerecommend:" + targetUserId;
            //查redis缓存
            List<SongIntroVO> realtimeRecommendSongIntroVOList = redisTemplate.opsForValue().get(redisKey);
            //有缓存,直接返回
//            if (realtimeRecommendSongIntroVOList != null) {
//                return realtimeRecommendSongIntroVOList;
//            }
            return realtimeRecommendSongIntroVOList;
        }

        @Override
        public void processElement(Long userId, ProcessFunction<Long, String>.Context context, Collector<String> collector) throws Exception {
            //List<SongIntroVO> songIntroVOList = realtimeRecommend(userId, 10);
            collector.collect("Recommendation for " + userId + ": ");
            System.out.println(userId);
        }
    }

}




