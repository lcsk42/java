package com.lcsk42.biz.admin.mq.producer;

import com.lcsk42.biz.admin.constant.AdminRocketMQConstant;
import com.lcsk42.biz.admin.mq.event.GenerallyEvent;
import com.lcsk42.frameworks.starter.rocketmq.domain.BaseSendExtendDTO;
import com.lcsk42.frameworks.starter.rocketmq.producer.AbstractCommonSendProducerTemplate;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class GenerallyProducer extends AbstractCommonSendProducerTemplate<GenerallyEvent> {

    private final ConfigurableEnvironment environment;

    public GenerallyProducer(@Autowired RocketMQTemplate rocketMQTemplate, @Autowired ConfigurableEnvironment environment) {
        super(rocketMQTemplate);
        this.environment = environment;
    }

    @Override
    protected BaseSendExtendDTO buildBaseSendExtendParam(GenerallyEvent messageSendEvent) {
        return BaseSendExtendDTO.builder()
                .eventName(AdminRocketMQConstant.EVENT_NAME_GENERALLY_EVENT)
                .keys(messageSendEvent.getId())
                .topic(AdminRocketMQConstant.TOPIC_GENERALLY_EVENT_ASYNC)
                .tag(AdminRocketMQConstant.TAG_GENERALLY_EVENT_ASYNC)
                .sentTimeout(AdminRocketMQConstant.SEND_TIMEOUT)
                .delayLevel(messageSendEvent.getDelayLevelEnum())
                .build();
    }

    @Override
    protected Message<?> buildMessage(GenerallyEvent messageSendEvent, BaseSendExtendDTO requestParam) {
        return buildDefaultMessage(messageSendEvent, requestParam);
    }
}
