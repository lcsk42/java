package com.lcsk42.biz.admin.controller;

import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("admin-file")
@Tag(description = "Admin File Management", name = "AdminFileController")
public class AdminFileController {

    /**
     * Handles file upload requests.
     *
     * @param file the file to be uploaded
     */
    @PostMapping("/upload")
    @Operation(summary = "Upload a file", description = "Endpoint to upload files for admin purposes")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String targetDir = "/tmp/uploads";
        String fileName = ObjectUtils.defaultIfNull(file.getOriginalFilename(), "default-file-name.txt");
        File destFile = new File(targetDir, fileName);

        try {
            FileUtils.forceMkdirParent(destFile);
            FileUtils.writeByteArrayToFile(destFile, file.getBytes());
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            throw new ServiceException("File upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/download")
    @Operation(summary = "Download a file", description = "Endpoint to download files uploaded by admin")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        String targetDir = "/tmp/uploads";
        File file = new File(targetDir, fileName);

        if (!file.exists()) {
            throw new ServiceException("File not found: " + fileName);
        }

        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            FileUtils.copyFile(file, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new ServiceException("File download failed: " + e.getMessage());
        }
    }
}
