package com.lcsk42.biz.admin.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminFileMetadataDTO {
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件大小（字节）")
    private Long size;
}
