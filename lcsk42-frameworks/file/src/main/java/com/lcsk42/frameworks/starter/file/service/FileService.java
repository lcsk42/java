package com.lcsk42.frameworks.starter.file.service;

import com.lcsk42.frameworks.starter.base.constant.StringConstant;
import com.lcsk42.frameworks.starter.common.util.LocalDateTimeUtil;
import com.lcsk42.frameworks.starter.file.config.FileUploadProperties;
import com.lcsk42.frameworks.starter.file.enums.FileUploadType;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Defines operations for file management including upload, download, deletion, and renaming.
 * Supports both temporary and permanent file storage with date-based directory organization.
 */
public interface FileService {

    String TMP_DIR = "tmp/";

    /**
     * Configures and returns a FileService instance with the specified properties.
     *
     * @param properties Configuration properties for file operations
     * @return Configured FileService implementation
     */
    FileService of(FileUploadProperties properties);

    /**
     * Gets the storage type used by this file service.
     *
     * @return File storage type as defined in FileUploadType enum
     */
    FileUploadType getFileUploadType();

    /**
     * Retrieves the current file upload configuration.
     *
     * @return Current FileUploadProperties containing configuration details
     */
    FileUploadProperties getUploadProperties();

    /**
     * Generates a directory path based on the current date (yyyyMMdd format).
     *
     * @return Date-formatted directory path string
     */
    default String generateDateBasedDirectory() {
        return LocalDateTimeUtil.BASIC_ISO_DATE_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * Constructs a complete file path incorporating directory structure, timestamp, and filename.
     * Temporary files are prefixed with TMP_DIR: "[TMP_DIR]/yyyyMMdd/epochSecond-filename"
     * Permanent files use: "yyyyMMdd/epochSecond-filename"
     *
     * @param fileName  Original filename to include in the path
     * @param temporary True to mark as temporary file, false for permanent
     * @return Complete path string for file storage
     * @throws IllegalArgumentException if fileName is blank or null
     */
    default String buildKey(String fileName, boolean temporary) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("File name must not be blank or null");
        }

        return (temporary ? TMP_DIR : StringUtils.EMPTY)
                + generateDateBasedDirectory()
                + StringConstant.SLASH
                + Instant.now().getEpochSecond()
                + StringConstant.DASHED
                + fileName;
    }

    /**
     * Uploads a file from the specified input stream to the default bucket.
     *
     * @param inputStream Source data stream to upload
     * @param fileName    Name to assign to the uploaded file
     * @return Storage key/path of the uploaded file
     */
    default String uploadFile(InputStream inputStream, String fileName) {
        return uploadFile(inputStream, fileName, getUploadProperties().getBucketName());
    }

    /**
     * Uploads a file to a specific bucket.
     *
     * @param inputStream Source data stream to upload
     * @param fileName    Name to assign to the uploaded file
     * @param bucketName  Target storage bucket name
     * @return Storage key/path of the uploaded file
     */
    default String uploadFile(InputStream inputStream, String fileName, String bucketName) {
        return uploadFile(inputStream, fileName, bucketName, false);
    }

    /**
     * Uploads a temporary file to the default bucket.
     *
     * @param inputStream Source data stream to upload
     * @param fileName    Name to assign to the uploaded file
     * @return Storage key/path of the temporary file
     */
    default String uploadTemporaryFile(InputStream inputStream, String fileName) {
        return uploadTemporaryFile(inputStream, fileName, getUploadProperties().getBucketName());
    }

    /**
     * Uploads a temporary file to a specific bucket.
     *
     * @param inputStream Source data stream to upload
     * @param fileName    Name to assign to the uploaded file
     * @param bucketName  Target storage bucket name
     * @return Storage key/path of the temporary file
     */
    default String uploadTemporaryFile(InputStream inputStream, String fileName, String bucketName) {
        return uploadFile(inputStream, fileName, bucketName, true);
    }

    /**
     * Core file upload operation handling both temporary and permanent storage.
     *
     * @param inputStream Source data stream to upload
     * @param fileName    Name to assign to the uploaded file
     * @param bucketName  Target storage bucket name
     * @param temporary   True to store as temporary file, false for permanent
     * @return Storage key/path of the uploaded file
     */
    String uploadFile(InputStream inputStream, String fileName, String bucketName, boolean temporary);

    /**
     * Copies a file from its current location to a new path.
     *
     * @param oldKey     Current storage key/path of the file
     * @param fileName   New name to assign to the copied file
     * @param bucketName Bucket containing the source file
     * @return Storage key/path of the new file copy
     */
    default String copyFile(String oldKey, String fileName, String bucketName) {
        InputStream inputStream = downloadFile(oldKey, bucketName);
        return uploadFile(inputStream, fileName);
    }

    /**
     * Deletes a file from the default bucket.
     *
     * @param key Storage key/path of the file to delete
     */
    default void deleteFile(String key) {
        deleteFile(key, getUploadProperties().getBucketName());
    }

    /**
     * Deletes a file from a specific bucket.
     *
     * @param key        Storage key/path of the file to delete
     * @param bucketName Target bucket containing the file
     */
    void deleteFile(String key, String bucketName);

    /**
     * Deletes multiple files from the default bucket.
     *
     * @param keys Collection of storage keys/paths to delete
     */
    default void deleteFiles(List<String> keys) {
        deleteFiles(keys, getUploadProperties().getBucketName());
    }

    /**
     * Deletes multiple files from a specific bucket.
     *
     * @param keys       Collection of storage keys/paths to delete
     * @param bucketName Target bucket containing the files
     */
    default void deleteFiles(List<String> keys, String bucketName) {
        keys.forEach(key -> deleteFile(key, bucketName));
    }

    /**
     * Renames a file in the default bucket.
     *
     * @param oldKey      Current storage key/path of the file
     * @param newFileName New name to assign to the file
     */
    default void renameFile(String oldKey, String newFileName) {
        renameFile(oldKey, newFileName, getUploadProperties().getBucketName());
    }

    /**
     * Renames a file in a specific bucket.
     *
     * @param oldKey      Current storage key/path of the file
     * @param newFileName New name to assign to the file
     * @param bucketName  Target bucket containing the file
     */
    void renameFile(String oldKey, String newFileName, String bucketName);

    /**
     * Downloads a file from the default bucket.
     *
     * @param key Storage key/path of the file to download
     * @return InputStream containing the file data
     */
    default InputStream downloadFile(String key) {
        return downloadFile(key, getUploadProperties().getBucketName());
    }

    /**
     * Downloads a file from a specific bucket.
     *
     * @param key        Storage key/path of the file to download
     * @param bucketName Source bucket containing the file
     * @return InputStream containing the file data
     */
    InputStream downloadFile(String key, String bucketName);

    /**
     * Generates a time-limited pre-signed URL for secure file downloads.
     * <p>
     * The URL will expire after a default timeout period. Callers should implement
     * proper error handling for expired URLs.
     *
     * @param key               The file identifier to generate URL for
     * @param bucketName        The source bucket name
     * @param signatureDuration The duration before URL expiration
     * @return Pre-signed HTTPS URL for direct download access
     * @throws UnsupportedOperationException If not implemented by concrete class
     */
    default URL generatePreSignedDownloadUrl(String key, String bucketName, Duration signatureDuration) {
        throw new UnsupportedOperationException("Pre-signed URL generation not implemented");
    }

    /**
     * Generates a time-limited pre-signed URL for secure file uploads.
     * <p>
     * The generated URL allows direct upload without requiring additional permissions.
     *
     * @param key               The destination path where the file will be stored
     * @param bucketName        The target bucket name
     * @param signatureDuration The duration before URL expiration
     * @return Pre-signed HTTPS URL for direct upload
     * @throws UnsupportedOperationException If not implemented by concrete class
     */
    default URL generatePreSignedUploadUrl(String key, String bucketName, Duration signatureDuration) {
        throw new UnsupportedOperationException("Pre-signed URL generation not implemented");
    }
}
