package com.lcsk42.frameworks.starter.base.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Global filter execution order constants class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterOrderConstant {

    /**
     * Execution order sorting for user information propagation filter
     */
    public static final int USER_TRANSMIT_FILTER_ORDER = 100;
}