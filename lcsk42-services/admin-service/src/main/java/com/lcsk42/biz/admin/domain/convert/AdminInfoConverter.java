package com.lcsk42.biz.admin.domain.convert;

import com.lcsk42.biz.admin.domain.dto.AdminInfoDTO;
import com.lcsk42.biz.admin.domain.po.AdminInfoPO;
import com.lcsk42.biz.admin.domain.vo.AdminInfoVO;
import com.lcsk42.frameworks.starter.common.convert.TriConverter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminInfoConverter extends TriConverter<AdminInfoPO, AdminInfoDTO, AdminInfoVO> {
    AdminInfoConverter INSTANCE = Mappers.getMapper(AdminInfoConverter.class);
}
