package com.lcsk42.biz.admin.controller;

import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "IndexController")
public class IndexController {

    @GetMapping("/")
    @Operation(summary = "Index Endpoint")
    public String index() {
        return "ok";
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping Endpoint")
    public String ping() {
        return "pong";
    }

    @GetMapping("/userId")
    @Operation(summary = "Get Current User ID")
    public Long getCurrentUserId() {
        // This method should return the ID of the currently authenticated user.
        // The actual implementation will depend on your security context and user management.
        return 1L; // Placeholder for demonstration purposes
    }

    @GetMapping("/void")
    @Operation(summary = "Void Endpoint")
    public void nonReturn() {

    }

    @GetMapping("/exception")
    @Operation(summary = "Exception Endpoint")
    public void exception() {
        throw new ServiceException("An error occurred in the IndexController");
    }
}
