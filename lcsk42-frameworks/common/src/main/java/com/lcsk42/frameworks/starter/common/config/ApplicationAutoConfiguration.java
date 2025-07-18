package com.lcsk42.frameworks.starter.common.config;

import com.lcsk42.frameworks.starter.base.ApplicationContextHolder;
import com.lcsk42.frameworks.starter.base.init.ApplicationContentPostProcessor;
import com.lcsk42.frameworks.starter.common.threadpool.build.ThreadPoolBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.Executor;

/**
 * Spring Boot auto-configuration class for application-level beans.
 * Provides default implementations for core application components.
 */
public class ApplicationAutoConfiguration {
    /**
     * Creates an ApplicationContextHolder bean if none exists.
     *
     * @return new ApplicationContextHolder instance
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    /**
     * Creates an ApplicationContentPostProcessor bean if none exists.
     *
     * @param applicationContext the Spring application context
     * @return new ApplicationContentPostProcessor instance
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContentPostProcessor applicationContentPostProcessor(ApplicationContext applicationContext) {
        return new ApplicationContentPostProcessor(applicationContext);
    }

    /**
     * Creates the primary task executor bean.
     * Configures a default thread pool with:
     * - Thread name prefix "default-pool-"
     * - Non-daemon threads
     *
     * @return configured ThreadPoolExecutor instance
     */
    @Bean
    @Primary
    public Executor taskExecutor() {
        return ThreadPoolBuilder.builder()
                .threadFactory("default-pool-", false)
                .build();
    }
}
