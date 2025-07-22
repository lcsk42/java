package com.lcsk42.biz.admin.mq.consumer;

import com.lcsk42.biz.admin.constant.AdminRocketMQConstant;
import com.lcsk42.biz.admin.mq.event.GenerallyEvent;
import com.lcsk42.frameworks.starter.rocketmq.domain.MessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = AdminRocketMQConstant.TOPIC_GENERALLY_EVENT_ASYNC,
        selectorExpression = AdminRocketMQConstant.TAG_GENERALLY_EVENT_ASYNC,
        consumerGroup = AdminRocketMQConstant.CG_GENERALLY_EVENT_ASYNC
)
public class GenerallyConsumer implements RocketMQListener<MessageWrapper<GenerallyEvent>> {

    @Transactional
    @Override
    public void onMessage(MessageWrapper<GenerallyEvent> message) {
        log.info("Received message: {}", message);
        log.info("Used Time: {}", System.currentTimeMillis() - message.getTimestamp());
    }
}
