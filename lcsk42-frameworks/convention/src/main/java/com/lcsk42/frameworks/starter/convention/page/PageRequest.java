package com.lcsk42.frameworks.starter.convention.page;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * Pagination parameters
 */
@Data
@Builder
@FieldNameConstants
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
}
