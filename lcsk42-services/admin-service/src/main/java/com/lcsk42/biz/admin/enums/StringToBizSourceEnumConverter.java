package com.lcsk42.biz.admin.enums;

import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBizSourceEnumConverter implements Converter<String, BizSourceEnum> {
    @Override
    public BizSourceEnum convert(@Nonnull String name) {
        return BizSourceEnum.fromName(name);
    }
}
