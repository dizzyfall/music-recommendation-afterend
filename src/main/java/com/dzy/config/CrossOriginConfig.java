package com.dzy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.dzy.constant.URLConstant.ORIGIN_URL;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/4/21  22:17
 */
@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {

    static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(ORIGIN_URL)
                .allowCredentials(true)
                .allowedMethods(ORIGINS)
                .maxAge(3600);
    }

}
