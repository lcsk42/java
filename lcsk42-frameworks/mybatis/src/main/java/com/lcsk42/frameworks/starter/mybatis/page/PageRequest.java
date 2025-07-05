package com.lcsk42.frameworks.starter.mybatis.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    public Page<Void> page() {
        return Page.of(current, size);
    }
}
