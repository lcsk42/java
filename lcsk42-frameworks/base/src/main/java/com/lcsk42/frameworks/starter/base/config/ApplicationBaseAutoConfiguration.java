package com.lcsk42.frameworks.starter.base.config;

import com.lcsk42.frameworks.starter.base.ApplicationContextHolder;
import com.lcsk42.frameworks.starter.base.init.ApplicationContentPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class ApplicationBaseAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContentPostProcessor applicationContentPostProcessor(ApplicationContext applicationContext) {
        return new ApplicationContentPostProcessor(applicationContext);
    }
}
