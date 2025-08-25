package com.lcsk42.frameworks.starter.file.config;

import com.lcsk42.frameworks.starter.file.enums.FileUploadType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

/**
 * Configuration properties for file upload functionality.
 * These properties can be configured in application.yml/application.properties
 * with the prefix 'framework.file.upload'.
 */
@Data
@ConfigurationProperties(FileUploadProperties.PREFIX)
public class FileUploadProperties {
    /**
     * The configuration prefix for file upload properties.
     * Example usage in properties file:
     * framework.file.upload.fileUploadType=LOCAL
     */
    public static final String PREFIX = "framework.file.upload";

    /**
     * Type of storage to be used for file uploads.
     * Supported values are LOCAL, S3, etc.
     */
    private FileUploadType fileUploadType;

    /**
     * Endpoint URL for cloud storage service (e.g., S3 endpoint URL).
     * Required when using cloud storage services.
     */
    private String endpoint;

    /**
     * Default bucket/container name for storing files.
     * Required when using cloud storage services.
     */
    private String bucketName;

    /**
     * Region identifier for cloud storage service.
     * Required for some cloud storage providers.
     */
    private Region region;

    /**
     * Access key ID for authenticating with cloud storage service.
     * Required when using cloud storage services.
     */
    private String accessKeyId;

    /**
     * Secret access key for authenticating with cloud storage service.
     * Required when using cloud storage services.
     */
    private String accessKeySecret;

    /**
     * Additional/extended configuration options.
     */
    private Extra extra;

    /**
     * Nested class containing extended configuration properties.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Extra {
        /**
         * Bucket/container name for storing public files.
         * Used when maintaining separate buckets for private and public files.
         */
        private String publicBucketName;
    }
}