package com.lcsk42.frameworks.starter.file.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileUploadType {
    S3("aws_s3"),
    OSS("aliyun_oss"),
    FTP("ftp"),
    LOCAL("local"),
    ;

    @EnumValue
    @JsonValue
    private final String type;
}
