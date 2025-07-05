package com.lcsk42.frameworks.starter.designpattern.strategy;

/**
 * Strategy pattern interface (optimized in a functional style)
 *
 * @param <T> The input type (request parameter)
 * @param <R> The output type (response result)
 */
public interface AbstractExecuteStrategy<T, R> {

    /**
     * Get the unique identifier for the strategy (e.g., strategy name).
     *
     * @return The strategy's name or unique identifier.
     */
    String getStrategyName();

    /**
     * Get the pattern matching key (optional, used for dynamic routing).
     *
     * @return The matching pattern, returns null by default to indicate it is not enabled.
     */
    default String getPatternMatchKey() {
        return null;
    }

    /**
     * Execute the strategy without returning a value (similar to Consumer.accept).
     *
     * @param input The input parameter.
     * @throws UnsupportedOperationException if not implemented by the strategy.
     */
    default void accept(T input) {
        throw new UnsupportedOperationException("accept() must be implemented");
    }

    /**
     * Execute the strategy and return a result (similar to Function.apply).
     *
     * @param input The input parameter.
     * @return The result of executing the strategy.
     * @throws UnsupportedOperationException if not implemented by the strategy.
     */
    default R apply(T input) {
        throw new UnsupportedOperationException("apply() must be implemented");
    }

    /**
     * Check if the strategy supports the given identifier.
     *
     * @param strategyKey The strategy identifier.
     * @return true if the strategy supports the given key, otherwise false.
     */
    default boolean supports(String strategyKey) {
        return getStrategyName().equals(strategyKey);
    }
}

