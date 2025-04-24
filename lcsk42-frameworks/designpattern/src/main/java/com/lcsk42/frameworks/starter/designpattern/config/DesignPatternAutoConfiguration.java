package com.lcsk42.frameworks.starter.designpattern.config;


import com.lcsk42.frameworks.starter.base.config.ApplicationBaseAutoConfiguration;
import com.lcsk42.frameworks.starter.designpattern.chain.AbstractChainContext;
import com.lcsk42.frameworks.starter.designpattern.strategy.AbstractStrategyChoose;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Design Pattern Auto Configuration
 */
@ImportAutoConfiguration(ApplicationBaseAutoConfiguration.class)
public class DesignPatternAutoConfiguration {

    /**
     * Strategy Mode Selector
     */
    @Bean
    public AbstractStrategyChoose abstractStrategyChoose() {
        return new AbstractStrategyChoose();
    }

    /**
     * Chain of Responsibility Context
     */
    @Bean
    public AbstractChainContext abstractChainContext() {
        return new AbstractChainContext();
    }
}
