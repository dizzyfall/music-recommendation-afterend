package com.dzy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/2/24  22:54
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        //1.创建配置
        Config config = new Config();
        //设置reis地址
        String rediaAddress = "redis://127.0.0.1:6379";
        //使用单机
        config.useSingleServer().setDatabase(1).setAddress(rediaAddress);

        //2.创建Redisson实例
        //Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
