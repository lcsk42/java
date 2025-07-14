package com.lcsk42.frameworks.starter.common.convert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetType {
    private String stringValue;

    private Integer integerValue;

    private String longValue;

    private String localDateValue;

    private String localTimeValue;

    private String localDateTimeValue;
}
