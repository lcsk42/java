package com.lcsk42.biz.admin.domain.dto;

import com.lcsk42.frameworks.starter.mybatis.page.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoPageDTO extends PageRequest {
    private String username;
}
