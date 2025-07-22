package com.lcsk42.biz.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoDTO {
    private Long id;

    private String username;

    private Boolean enabled;

    private List<String> roles;
}
