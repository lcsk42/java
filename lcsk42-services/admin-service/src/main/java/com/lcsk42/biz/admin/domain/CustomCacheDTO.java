package com.lcsk42.biz.admin.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomCacheDTO {

    @Schema(description = "Cache key", example = "group:my-key")
    private String key;

    @Schema(description = "Cache value", example = "This is a cached value")
    private Object value;
}
