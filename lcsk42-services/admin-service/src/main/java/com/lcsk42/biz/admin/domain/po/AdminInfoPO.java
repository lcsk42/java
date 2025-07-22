package com.lcsk42.biz.admin.domain.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lcsk42.frameworks.starter.mybatis.po.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@TableName("admin_info")
public class AdminInfoPO extends BasePO {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private Boolean enabled;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roles;
}
