package com.lcsk42.frameworks.starter.designpattern.chain;

import org.springframework.core.Ordered;

/**
 * Abstract Responsibility Chain Handler Interface
 *
 * <p>
 * This interface defines the contract for a handler in the chain of responsibility pattern.
 * The handler is responsible for processing requests and passing them along the chain if needed.
 * Each handler in the chain can either process the request or forward it to the next handler.
 * </p>
 *
 * @param <T> The type of request parameter that the handler will process.
 */
public interface AbstractChainHandler<T> extends Ordered {

    /**
     * Execute the chain of responsibility logic
     *
     * <p>
     * This method is used to handle the request. Each handler in the chain will
     * either process the request or pass it to the next handler in the chain.
     * </p>
     *
     * @param input The input for the responsibility chain execution, typically containing the data to be processed.
     */
    void accept(T input);

    /**
     * @return The identifier for the chain of responsibility component,
     * typically used to distinguish between different handlers in the chain.
     */
    String getHandlerName();
}
