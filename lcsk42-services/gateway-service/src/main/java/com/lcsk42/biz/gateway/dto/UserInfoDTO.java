package com.lcsk42.biz.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Information Data Transfer Object")
public class UserInfoDTO {

    @Schema(description = "User ID")
    private String userId;

    @Schema(description = "Username of the user")
    private String username;
}
