package com.lcsk42.frameworks.starter.mybatis.service.impl;

import com.lcsk42.frameworks.starter.mybatis.mapper.BaseMapper;
import com.lcsk42.frameworks.starter.mybatis.po.BasePO;
import com.lcsk42.frameworks.starter.mybatis.service.IService;

public class ServiceImpl<M extends BaseMapper<T>, T extends BasePO>
        extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T>
        implements IService<T> {
}
