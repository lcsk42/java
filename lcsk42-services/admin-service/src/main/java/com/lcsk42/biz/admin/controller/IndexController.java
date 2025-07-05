package com.lcsk42.biz.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "ok";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
