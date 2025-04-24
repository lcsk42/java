package com.lcsk42.frameworks.starter.designpattern.strategy;


import com.lcsk42.frameworks.starter.base.ApplicationContextHolder;
import com.lcsk42.frameworks.starter.base.init.ApplicationInitializingEvent;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Strategy Selector - Chooses and applies strategies based on a given strategy key.
 * Supports both direct matching and pattern-based matching.
 */
public class AbstractStrategyChoose implements ApplicationListener<ApplicationInitializingEvent> {

    // Map to store strategy name -> strategy instance mapping
    private final Map<String, AbstractExecuteStrategy> strategyMap = new HashMap<>();


    /**
     * Chooses a strategy based on the strategy key.
     * It supports direct matching or pattern matching based on the provided flag.
     *
     * @param strategyKey     The unique key identifying the strategy.
     * @param usePatternMatch If true, it uses pattern matching for strategy selection.
     * @return The chosen strategy.
     */
    public AbstractExecuteStrategy<?, ?> chooseStrategy(String strategyKey, Boolean usePatternMatch) {
        // If pattern matching is enabled, find strategy by matching pattern
        if (usePatternMatch != null && usePatternMatch) {
            return strategyMap.values().stream()
                    // Check if the strategy supports pattern matching
                    .filter(each -> StringUtils.hasText(each.getPatternMatchKey()))
                    // Match the strategy key with the pattern
                    .filter(each -> Pattern.compile(each.getPatternMatchKey()).matcher(strategyKey).matches())
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("Strategy Undefined"));
        }
        // Otherwise, find strategy by exact match
        return Optional.ofNullable(strategyMap.get(strategyKey))
                .orElseThrow(() -> new ServiceException(String.format("[%s] Strategy Undefined", strategyKey)));
    }


    /**
     * Chooses a strategy and then executes its `accept` method without returning a result.
     * This method is useful for strategies that do not require a return value.
     *
     * @param strategyKey The strategy key to identify the correct strategy.
     * @param request     The input data for the strategy.
     */
    public <T> void chooseAndAccept(String strategyKey, T request) {
        // Choose the strategy and execute the 'accept' method
        AbstractExecuteStrategy strategy = chooseStrategy(strategyKey, null);
        strategy.accept(request);
    }


    /**
     * Chooses a strategy and executes its `accept` method without returning a result.
     * Supports pattern matching if the `usePatternMatch` flag is set to true.
     *
     * @param strategyKey     The strategy key to identify the correct strategy.
     * @param request         The input data for the strategy.
     * @param usePatternMatch Flag indicating whether pattern matching should be used.
     */
    public <T> void chooseAndAccept(String strategyKey, T request, Boolean usePatternMatch) {
        // Choose the strategy and execute the 'accept' method with pattern matching option
        AbstractExecuteStrategy strategy = chooseStrategy(strategyKey, usePatternMatch);
        strategy.accept(request);
    }


    /**
     * Chooses a strategy and executes its `apply` method to return a result.
     * This method is useful for strategies that need to return a result after execution.
     *
     * @param strategyKey The strategy key to identify the correct strategy.
     * @param request     The input data for the strategy.
     * @param <T>         The type of the input.
     * @param <R>         The type of the result.
     * @return The result of the strategy's execution.
     */
    public <T, R> R chooseAndApply(String strategyKey, T request) {
        // Choose the strategy and execute the 'apply' method to get the result
        AbstractExecuteStrategy strategy = chooseStrategy(strategyKey, null);
        return (R) strategy.apply(request);
    }

    /**
     * This method is triggered when the application context is initialized.
     * It scans and registers all available strategies into the strategy map.
     *
     * @param event The application initialization event.
     */
    @Override
    public void onApplicationEvent(ApplicationInitializingEvent event) {
        // Get all the beans of type AbstractExecuteStrategy from the application context
        Map<String, AbstractExecuteStrategy> strategies = ApplicationContextHolder.getBeansOfType(AbstractExecuteStrategy.class);
        // Register each strategy by its unique strategy name
        strategies.forEach((beanName, strategy) -> {
            // Check if a strategy with the same name already exists
            AbstractExecuteStrategy<?, ?> existing = strategyMap.get(strategy.getStrategyName());
            if (existing != null) {
                throw new ServiceException(String.format("[%s] Duplicate Execution Strategy", strategy.getStrategyName()));
            }
            // Register the strategy
            strategyMap.put(strategy.getStrategyName(), strategy);
        });
    }
}