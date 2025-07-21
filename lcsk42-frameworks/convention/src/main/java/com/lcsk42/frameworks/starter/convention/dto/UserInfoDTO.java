package com.lcsk42.frameworks.starter.convention.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Schema(description = "User Information Data Transfer Object")
public class UserInfoDTO {

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Username of the user")
    private String username;

    @Schema(description = "Token of the user")
    private String token;
}
