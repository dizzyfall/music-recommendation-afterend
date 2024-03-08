package com.dzy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger+knife4j接口文档配置类
 *
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/2/15 19:21
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean(value = "defaultApi2")
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dzy.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api 信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("音乐推荐系统")
                .description("音乐推荐系统接口文档")
                .termsOfServiceUrl("https://github.com/dizzyfall")
                .version("1.0")
                .build();
    }
}
