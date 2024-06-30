package com.dzy.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/14  17:53
 */
@Component
@Slf4j
public class Kafka {

//    @KafkaListener(topics = KafkaConfig.TOPIC_TEST, groupId = KafkaConfig.GROUP_ID)
//    public void topic_test(List<String> messages, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        for (String message : messages) {
//            final JSONObject entries = JSONUtil.parseObj(message);
//            System.out.println(KafkaConfig.GROUP_ID + " 消费了： Topic:" + topic + ",Message:" + entries.getStr("data"));
//            //ack.acknowledge();
//        }
//    }

}
