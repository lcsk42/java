package com.lcsk42.biz.admin.service.impl;

import com.lcsk42.biz.admin.convert.AdminFileConverter;
import com.lcsk42.biz.admin.domain.dto.AdminFileMetadataDTO;
import com.lcsk42.biz.admin.domain.po.AdminFilePO;
import com.lcsk42.biz.admin.domain.vo.AdminFileVO;
import com.lcsk42.biz.admin.enums.BizSourceEnum;
import com.lcsk42.biz.admin.mapper.AdminFileMapper;
import com.lcsk42.biz.admin.service.AdminFileService;
import com.lcsk42.frameworks.starter.common.util.IdUtil;
import com.lcsk42.frameworks.starter.convention.errorcode.impl.FileErrorCode;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import com.lcsk42.frameworks.starter.file.config.FileUploadProperties;
import com.lcsk42.frameworks.starter.file.service.FileService;
import com.lcsk42.frameworks.starter.mybatis.service.impl.ServiceImpl;
import com.lcsk42.frameworks.starter.web.util.ServletUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminFileServiceImpl extends ServiceImpl<AdminFileMapper, AdminFilePO>
        implements AdminFileService {

    private final FileService fileService;

    private final FileUploadProperties fileUploadProperties;

    @Override
    public AdminFileVO upload(MultipartFile file,
                              BizSourceEnum bizSource,
                              Boolean publicRead,
                              String batchId,
                              Long id,
                              String fileName) {
        String path;
        String bucketName = getBucketName(publicRead);
        try {
            path = fileService.uploadFile(
                    file.getInputStream(),
                    fileName,
                    bucketName
            );
        } catch (IOException e) {
            throw new ServiceException();
        }
        AdminFilePO adminFilePO = AdminFilePO.builder()
                .id(id)
                .name(fileName)
                .size(file.getSize())
                .bucketName(bucketName)
                .path(path)
                .fileType(FilenameUtils.getExtension(fileName))
                .fileUploadType(fileUploadProperties.getFileUploadType())
                .bizSource(bizSource)
                .publicRead(publicRead)
                .batchId(StringUtils.isBlank(batchId) ? IdUtil.generateStandardUuid() : batchId)
                .build();

        save(adminFilePO);
        return AdminFileConverter.INSTANCE.toT(adminFilePO);
    }

    @Override
    public AdminFileVO uploadTempFile(MultipartFile file, String batchId) {
        String fileName = file.getName();
        String path;
        try {
            path = fileService.uploadTemporaryFile(file.getInputStream(), fileName);
        } catch (IOException e) {
            throw new ServiceException();
        }
        AdminFilePO adminFilePO = AdminFilePO.builder()
                .id(IdUtil.getSnowflakeNextId())
                .name(fileName)
                .size(file.getSize())
                .bucketName(fileUploadProperties.getBucketName())
                .path(path)
                .fileType(FilenameUtils.getExtension(fileName))
                .fileUploadType(fileUploadProperties.getFileUploadType())
                .bizSource(BizSourceEnum.DEFAULT)
                .publicRead(false)
                .batchId(StringUtils.isBlank(batchId) ? IdUtil.generateStandardUuid() : batchId)
                .build();

        save(adminFilePO);
        return AdminFileConverter.INSTANCE.toT(adminFilePO);
    }

    @Override
    public List<AdminFileVO> listByBatchId(String batchId) {
        return lambdaQuery()
                .eq(AdminFilePO::getBatchId, batchId)
                .orderByDesc(AdminFilePO::getCreateTime)
                .list()
                .stream()
                .map(AdminFileConverter.INSTANCE::toT)
                .toList();
    }

    @Override
    public AdminFileVO getById(Long id) {
        return lambdaQuery()
                .eq(AdminFilePO::getId, id)
                .oneOpt()
                .map(AdminFileConverter.INSTANCE::toT)
                .orElseGet(AdminFileVO::new);
    }

    @Override
    public void deleteById(Long id) {
        AdminFileVO adminFileVO = getById(id);
        fileService.deleteFile(adminFileVO.getPath(), adminFileVO.getBucketName());
        lambdaUpdate()
                .eq(AdminFilePO::getId, id)
                .remove();
    }

    @Override
    public void deleteByBatchId(String batchId) {

        lambdaQuery()
                .eq(AdminFilePO::getBatchId, batchId)
                .list()
                .forEach(adminFilePO -> fileService.deleteFile(adminFilePO.getPath(), adminFilePO.getBucketName()));

        lambdaUpdate()
                .eq(AdminFilePO::getBatchId, batchId)
                .remove();
    }

    @Override
    public void download(Long id, HttpServletResponse response) {
        AdminFileVO adminFileVO = getById(id);

        try (InputStream inputStream = fileService.downloadFile(adminFileVO.getPath(), adminFileVO.getBucketName())) {
            ServletUtil.write(response, inputStream, adminFileVO.getName(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
        } catch (IOException e) {
            throw FileErrorCode.IO_RUNTIME_EXCEPTION.toException();
        }

    }

    @Override
    public URL generatePreSignedDownloadUrl(Long id) {
        AdminFileVO adminFileVO = getById(id);
        return fileService.generatePreSignedDownloadUrl(adminFileVO.getPath(),
                adminFileVO.getBucketName(),
                Duration.ofDays(1));
    }

    @Override
    public URL generatePreSignedUploadUrl(BizSourceEnum bizSource,
                                          Boolean publicRead,
                                          String batchId,
                                          Long id,
                                          String fileName) {

        String path = fileService.buildKey(fileName, false);
        String bucketName = getBucketName(publicRead);

        AdminFilePO adminFilePO = AdminFilePO.builder()
                .id(id)
                .name(fileName)
                .size(0L)
                .bucketName(bucketName)
                .path(path)
                .fileType(FilenameUtils.getExtension(fileName))
                .fileUploadType(fileUploadProperties.getFileUploadType())
                .bizSource(bizSource)
                .publicRead(publicRead)
                .batchId(StringUtils.isBlank(batchId) ? IdUtil.generateStandardUuid() : batchId)
                .build();
        save(adminFilePO);

        return fileService.generatePreSignedUploadUrl(path, bucketName, Duration.ofMinutes(15));
    }

    @Override
    public void updateFileMetadata(AdminFileMetadataDTO fileMetadataDTO) {
        lambdaUpdate()
                .set(StringUtils.isNoneBlank(fileMetadataDTO.getName()), AdminFilePO::getName, fileMetadataDTO.getName())
                .set(Objects.nonNull(fileMetadataDTO.getSize()), AdminFilePO::getSize, fileMetadataDTO.getSize())
                .set(StringUtils.isNoneBlank(fileMetadataDTO.getName()), AdminFilePO::getFileType, FilenameUtils.getExtension(fileMetadataDTO.getName()))
                .eq(AdminFilePO::getId, fileMetadataDTO.getId())
                .update();
    }

    private String getBucketName(boolean publicRead) {
        return publicRead ? fileUploadProperties.getExtra().getPublicBucketName()
                : fileUploadProperties.getBucketName();
    }
}
