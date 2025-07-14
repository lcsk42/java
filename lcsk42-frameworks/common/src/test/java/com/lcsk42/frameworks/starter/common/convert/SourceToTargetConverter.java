package com.lcsk42.frameworks.starter.common.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SourceToTargetConverter extends BiConverter<SourceType, TargetType> {
    SourceToTargetConverter INSTANCE = Mappers.getMapper(SourceToTargetConverter.class);
}
