package com.lcsk42.biz.admin.service;

import com.lcsk42.biz.admin.domain.dto.AdminInfoDTO;
import com.lcsk42.biz.admin.domain.po.AdminInfoPO;
import com.lcsk42.biz.admin.domain.vo.AdminInfoVO;
import com.lcsk42.frameworks.starter.mybatis.service.IService;

public interface AdminInfoService extends IService<AdminInfoPO> {
    AdminInfoVO upsert(AdminInfoDTO adminInfoDTO);
}
