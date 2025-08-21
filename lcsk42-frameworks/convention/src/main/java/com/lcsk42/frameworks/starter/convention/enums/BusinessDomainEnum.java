package com.lcsk42.frameworks.starter.convention.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessDomainEnum {

    COMMON("Common Module"),
    FEIGN("Feign Module"),
    SQL("SQL Module"),
    ADMIN_FILE("Admin File Module"),
    ;
    private final String description;
}
