package com.lcsk42.biz.admin.controller;

import com.lcsk42.biz.admin.common.enums.BizSourceEnum;
import com.lcsk42.biz.admin.domain.dto.AdminFileMetadataDTO;
import com.lcsk42.biz.admin.domain.vo.AdminFileVO;
import com.lcsk42.biz.admin.service.AdminFileService;
import com.lcsk42.frameworks.starter.common.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@RestController
@RequestMapping("/admin-file")
@RequiredArgsConstructor
@Tag(description = "Admin File Management", name = "AdminFileController")
public class AdminFileController {

    private final AdminFileService adminFileService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a file", description = "Endpoint to upload files for admin purposes")
    public AdminFileVO uploadFile(@RequestParam("file")
                                  @Parameter(description = "The file to upload", required = true)
                                  MultipartFile file,
                                  @RequestParam(name = "bizSource", required = false)
                                  @Parameter(description = "Business source of the file (e.g., DEFAULT, USER_UPLOAD)",
                                          example = "default")
                                  BizSourceEnum bizSource,
                                  @RequestParam(name = "publicRead", required = false)
                                  @Parameter(description = "Whether the file is publicly readable",
                                          example = "false")
                                  Boolean publicRead,
                                  @RequestParam(name = "batchId", required = false)
                                  @Parameter(description = "Batch ID for grouping files (auto-generated if not provided)")
                                  String batchId,
                                  @RequestParam(name = "id", required = false)
                                  @Parameter(description = "Custom ID for the file (auto-generated if not provided)")
                                  Long id,
                                  @RequestParam(name = "name", required = false)
                                  @Parameter(description = "Custom name for the file (falls back to original filename if not provided)")
                                  String name) {
        return adminFileService.upload(
                file,
                ObjectUtils.defaultIfNull(bizSource, BizSourceEnum.DEFAULT),
                ObjectUtils.defaultIfNull(publicRead, Boolean.FALSE),
                StringUtils.defaultIfBlank(batchId, IdUtil.generateCompactUuid()),
                ObjectUtils.defaultIfNull(id, IdUtil.getSnowflakeNextId()),
                StringUtils.defaultIfBlank(name, file.getOriginalFilename())
        );
    }

    @GetMapping("/download")
    @Operation(summary = "Download a file", description = "Endpoint to download files uploaded by admin")
    public void download(@RequestParam("id") Long id, HttpServletResponse response) {
        adminFileService.download(id, response);
    }

    @GetMapping("/pre-signed-download-url")
    @Operation(summary = "Generate Pre-signed Download URL", description = "Generates a pre-signed URL for downloading a file")
    public URL generatePreSignedDownloadUrl(@RequestParam("id") Long id) {
        return adminFileService.generatePreSignedDownloadUrl(id);
    }

    @PostMapping("/pre-signed-upload-url")
    @Operation(summary = "Generate Pre-signed Upload URL", description = "Generates a pre-signed URL for uploading a file")
    public URL generatePreSignedUploadUrl(
            @RequestParam(name = "bizSource", required = false)
            @Parameter(description = "Business source of the file (e.g., DEFAULT, USER_UPLOAD)",
                    example = "default")
            BizSourceEnum bizSource,
            @RequestParam(name = "publicRead", required = false)
            @Parameter(description = "Whether the file is publicly readable",
                    example = "false")
            Boolean publicRead,
            @RequestParam(name = "batchId", required = false)
            @Parameter(description = "Batch ID for grouping files (auto-generated if not provided)")
            String batchId,
            @RequestParam(name = "id", required = false)
            @Parameter(description = "Custom ID for the file (auto-generated if not provided)")
            Long id,
            @RequestParam(name = "name", required = false)
            @Parameter(description = "Custom name for the file (falls back to original filename if not provided)")
            String name
    ) {
        return adminFileService.generatePreSignedUploadUrl(
                ObjectUtils.defaultIfNull(bizSource, BizSourceEnum.DEFAULT),
                ObjectUtils.defaultIfNull(publicRead, Boolean.FALSE),
                StringUtils.defaultIfBlank(batchId, IdUtil.generateCompactUuid()),
                ObjectUtils.defaultIfNull(id, IdUtil.getSnowflakeNextId()),
                StringUtils.defaultIfBlank(name, "unnamed_file.unknown")
        );
    }

    @PutMapping("/file-metadata")
    @Operation(summary = "Update File Metadata", description = "Updates metadata for an existing file")
    public void updateFileMetadata(@RequestBody AdminFileMetadataDTO fileMetadataDTO) {
        adminFileService.updateFileMetadata(fileMetadataDTO);
    }

}
