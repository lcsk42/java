package com.lcsk42.frameworks.starter.rocketmq.producer;

import com.alibaba.fastjson2.JSON;
import com.lcsk42.frameworks.starter.rocketmq.domain.BaseSendExtendDTO;
import com.lcsk42.frameworks.starter.rocketmq.domain.MessageWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommonSendProducerTemplate<T> {
    private final RocketMQTemplate rocketMQTemplate;

    protected abstract BaseSendExtendDTO buildBaseSendExtendParam(T messageSendEvent);

    protected abstract Message<?> buildMessage(T messageSendEvent, BaseSendExtendDTO requestParam);

    protected Message<?> buildDefaultMessage(T messageSendEvent, BaseSendExtendDTO requestParam) {
        String keys = StringUtils.isEmpty(requestParam.getKeys()) ? UUID.randomUUID().toString() : requestParam.getKeys();
        return MessageBuilder
                .withPayload(MessageWrapper.of(requestParam.getKeys(), messageSendEvent))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, requestParam.getTag())
                .build();
    }

    public SendResult sendMessage(T messageSendEvent) {
        BaseSendExtendDTO baseSendExtendDTO = buildBaseSendExtendParam(messageSendEvent);
        SendResult sendResult;
        try {
            StringBuilder destinationBuilder = new StringBuilder(baseSendExtendDTO.getTopic());
            if (StringUtils.isNotBlank(baseSendExtendDTO.getTag())) {
                destinationBuilder.append(":").append(baseSendExtendDTO.getTag());
            }
            sendResult = rocketMQTemplate.syncSend(
                    destinationBuilder.toString(),
                    buildMessage(messageSendEvent, baseSendExtendDTO),
                    baseSendExtendDTO.getSentTimeout(),
                    baseSendExtendDTO.getDelayLevel().getLevel()
            );
            log.info(
                    "[{}] SendStatus：{}，MsgId：{}，MessageKeys：{}",
                    baseSendExtendDTO.getEventName(),
                    sendResult.getSendStatus(),
                    sendResult.getMsgId(),
                    baseSendExtendDTO.getKeys()
            );
        } catch (Throwable ex) {
            log.error(
                    "[{}] Message sending failed, message body：{}",
                    baseSendExtendDTO.getEventName(),
                    JSON.toJSONString(messageSendEvent),
                    ex);
            throw ex;
        }
        return sendResult;
    }
}
