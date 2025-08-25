package com.lcsk42.biz.admin.controller;

import com.lcsk42.frameworks.starter.common.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Tag(description = "Common Operations", name = "CommonController")
public class CommonController {

    @GetMapping("/snowflake-id")
    @Operation(summary = "Generate a unique Snowflake ID",
            description = "Endpoint to generate a unique identifier using the Snowflake algorithm")
    public Long generateSnowflakeId() {
        return IdUtil.getSnowflakeNextId();
    }
}
