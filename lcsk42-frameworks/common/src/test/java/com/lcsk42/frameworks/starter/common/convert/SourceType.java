package com.lcsk42.frameworks.starter.common.convert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceType {

    private String stringValue;

    private Integer integerValue;

    private Long longValue;

    private LocalDate localDateValue;

    private LocalTime localTimeValue;

    private LocalDateTime localDateTimeValue;

}
