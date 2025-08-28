package com.lcsk42.biz.admin.service;

import com.lcsk42.biz.admin.domain.dto.AdminFileMetadataDTO;
import com.lcsk42.biz.admin.domain.po.AdminFilePO;
import com.lcsk42.biz.admin.domain.vo.AdminFileVO;
import com.lcsk42.biz.admin.common.enums.BizSourceEnum;
import com.lcsk42.frameworks.starter.mybatis.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

public interface AdminFileService extends IService<AdminFilePO> {

    AdminFileVO upload(MultipartFile file,
                       BizSourceEnum bizSource,
                       Boolean publicRead,
                       String batchId,
                       Long id,
                       String fileName);

    AdminFileVO uploadTempFile(MultipartFile file, String batchId);

    List<AdminFileVO> listByBatchId(String batchId);

    AdminFileVO getById(Long id);

    void deleteById(Long id);

    void deleteByBatchId(String batchId);

    void download(Long id, HttpServletResponse response);

    URL generatePreSignedDownloadUrl(Long id);

    URL generatePreSignedUploadUrl(BizSourceEnum bizSource, Boolean publicRead, String batchId, Long id, String fileName);

    void updateFileMetadata(AdminFileMetadataDTO fileMetadataDTO);
}
