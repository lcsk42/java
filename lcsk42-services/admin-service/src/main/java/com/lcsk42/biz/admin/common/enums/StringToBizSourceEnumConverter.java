package com.lcsk42.biz.admin.common.enums;

import com.lcsk42.frameworks.starter.convention.enums.BaseEnum;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBizSourceEnumConverter implements Converter<String, BizSourceEnum> {
    @Override
    public BizSourceEnum convert(@Nonnull String name) {
        return BaseEnum.fromValue(name, BizSourceEnum.class);
    }
}
