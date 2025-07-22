package com.lcsk42.frameworks.starter.mybatis.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

/**
 * Pagination parameters
 */
@Data
@SuperBuilder
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    /**
     * Current page.
     */
    @Builder.Default
    private Long current = 1L;

    /**
     * Number of items displayed per page.
     */
    @Builder.Default
    private Long size = 10L;

    public <T> PageResponse<T> page() {
        return PageResponse.of(current, size);
    }
}
