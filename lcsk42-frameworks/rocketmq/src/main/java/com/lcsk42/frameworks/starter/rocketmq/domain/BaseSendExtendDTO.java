package com.lcsk42.frameworks.starter.rocketmq.domain;

import com.lcsk42.frameworks.starter.rocketmq.enums.DelayLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class BaseSendExtendDTO {

    private String eventName;

    private String topic;

    private String tag;

    private String keys;

    private Long sentTimeout;

    private DelayLevelEnum delayLevel;
}
