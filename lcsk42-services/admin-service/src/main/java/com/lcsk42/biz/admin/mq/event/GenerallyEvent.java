package com.lcsk42.biz.admin.mq.event;

import com.lcsk42.frameworks.starter.rocketmq.enums.DelayLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerallyEvent {
    private String id;

    private LocalDateTime localDateTime;

    private Long longValue;

    private DelayLevelEnum delayLevelEnum = DelayLevelEnum.LEVEL_0S;
}
