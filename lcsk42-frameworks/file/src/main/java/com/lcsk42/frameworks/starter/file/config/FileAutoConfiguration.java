package com.lcsk42.frameworks.starter.file.config;

import com.lcsk42.frameworks.starter.file.enums.FileUploadType;
import com.lcsk42.frameworks.starter.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

@AllArgsConstructor
@EnableConfigurationProperties({FileUploadProperties.class})
public class FileAutoConfiguration {

    private final FileUploadProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public FileService fileService() {
        FileUploadType fileUploadType = properties.getFileUploadType();

        Objects.requireNonNull(fileUploadType, "File service type cannot be null");

        try {
            return ServiceLoader.load(FileService.class).stream()
                    .map(ServiceLoader.Provider::get)
                    .filter(service -> fileUploadType.equals(service.getFileUploadType()))
                    .findFirst()
                    .map(service -> service.of(properties))
                    .orElseThrow(() ->
                            new IllegalArgumentException("No FileService implementation available for type: " +
                                    fileUploadType));
        } catch (ServiceConfigurationError e) {
            throw new IllegalStateException("Failed to load FileService implementations", e);
        }
    }
}
