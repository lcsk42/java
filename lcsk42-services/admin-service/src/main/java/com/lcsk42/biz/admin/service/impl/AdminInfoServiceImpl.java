package com.lcsk42.biz.admin.service.impl;

import com.lcsk42.biz.admin.convert.AdminInfoConverter;
import com.lcsk42.biz.admin.domain.dto.AdminInfoDTO;
import com.lcsk42.biz.admin.domain.po.AdminInfoPO;
import com.lcsk42.biz.admin.domain.vo.AdminInfoVO;
import com.lcsk42.biz.admin.mapper.AdminInfoMapper;
import com.lcsk42.biz.admin.service.AdminInfoService;
import com.lcsk42.frameworks.starter.mybatis.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfoPO>
        implements AdminInfoService {
    @Override
    public AdminInfoVO upsert(AdminInfoDTO adminInfoDTO) {
        Long id = adminInfoDTO.getId();

        AdminInfoPO adminInfoPO = AdminInfoConverter.INSTANCE.toP(adminInfoDTO);

        if (Objects.isNull(id)) {
            save(adminInfoPO);
        } else {
            updateById(adminInfoPO);
        }

        return AdminInfoConverter.INSTANCE.toV(adminInfoPO);
    }
}
