package com.dzy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/14  17:47
 */
@RestController
@RequestMapping("/kafka")
@Slf4j
public class KafkaProducerController {

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @PostMapping( "/produce")
//    public String produce(@RequestBody Object obj) {
//
//        try {
//            String obj2String = JSONUtil.toJsonStr(obj);
//            kafkaTemplate.send(KafkaConfig.TOPIC_TEST, obj2String);
//            return "success";
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return "success";
//    }

}
