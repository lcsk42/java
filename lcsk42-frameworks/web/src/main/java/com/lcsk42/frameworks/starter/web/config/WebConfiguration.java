package com.lcsk42.frameworks.starter.web.config;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class)
                && !clazz.getPackageName().startsWith("org.springdoc")
                && !clazz.getPackageName().startsWith("springfox.documentation"));
    }
}