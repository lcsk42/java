package com.lcsk42.biz.admin.domain.vo;

import com.lcsk42.biz.admin.enums.BizSourceEnum;
import com.lcsk42.frameworks.starter.file.enums.FileUploadType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminFileVO {

    @Schema(description = "主键ID")
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
    private Boolean publicRead;

    @Schema(description = "批次 Id")
    private String batchId;
}
