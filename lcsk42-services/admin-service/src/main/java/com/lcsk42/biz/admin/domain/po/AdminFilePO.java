package com.lcsk42.biz.admin.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lcsk42.biz.admin.enums.BizSourceEnum;
import com.lcsk42.frameworks.starter.file.enums.FileUploadType;
import com.lcsk42.frameworks.starter.mybatis.po.BasePO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("admin_file")
public class AdminFilePO extends BasePO {
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件大小（字节）")
    private Long size;

    @Schema(description = "文件存储桶")
    private String bucketName;

    @Schema(description = "文件存储路径")
    private String path;

    @Schema(description = "文件类型, 后缀")
    private String fileType;

    @Schema(description = "存储类型")
    private FileUploadType fileUploadType;

    @Schema(description = "业务来源")
    private BizSourceEnum bizSource;

    @Schema(description = "是否公开读")
    @TableField(value = "is_public_read")
    private Boolean publicRead;

    @Schema(description = "批次 Id")
    private String batchId;
}
