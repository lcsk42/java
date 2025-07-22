package com.lcsk42.biz.admin.controller;

import com.lcsk42.biz.admin.mq.event.GenerallyEvent;
import com.lcsk42.biz.admin.mq.producer.GenerallyProducer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rocket-mq")
@RequiredArgsConstructor
@Tag(name = "RocketMQ Management", description = "Controller for managing RocketMQ operations")
public class RocketMQController {

    private final GenerallyProducer generallyProducer;

    @PostMapping
    public SendResult send(@RequestBody GenerallyEvent generallyEvent) {
        return generallyProducer.sendMessage(generallyEvent);
    }
}
