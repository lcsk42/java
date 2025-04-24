package com.lcsk42.frameworks.starter.designpattern.chain;

import com.lcsk42.frameworks.starter.base.ApplicationContextHolder;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract Chain Context that manages and executes a chain of responsibility.
 *
 * <p>
 * This class is responsible for managing the components of the chain of responsibility
 * and executing them based on the provided "mark". It also initializes and sorts the
 * chain handlers at application startup.
 * </p>
 *
 * @param <T> The type of request parameter that the chain handlers will process.
 */
public final class AbstractChainContext<T> implements CommandLineRunner {

    // Container to hold the list of chain handlers for each chain identifier (mark)
    private final Map<String, List<AbstractChainHandler>> abstractChainHandlerContainer = new HashMap<>();

    /**
     * Executes the chain of responsibility components for a given mark.
     *
     * <p>
     * This method will find the corresponding handlers for the given "mark" and
     * execute them in the order defined by the {@link Ordered} interface.
     * If no handlers are found for the provided mark, a {@link ServiceException} is thrown.
     * </p>
     *
     * @param handlerName The identifier of the chain of responsibility component.
     * @param input       The parameters to be passed through the chain of responsibility.
     * @throws ServiceException If no handlers are found for the given mark.
     */
    public void accept(String handlerName, T input) {
        List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(handlerName);
        if (CollectionUtils.isEmpty(abstractChainHandlers)) {
            throw new ServiceException(String.format("[%s] Chain of Responsibility ID is undefined.", handlerName));
        }
        // Execute each accept in the chain
        abstractChainHandlers.forEach(each -> each.accept(input));
    }

    /**
     * Initializes the chain of responsibility handlers at application startup.
     *
     * <p>
     * This method runs after the application context is initialized and sorts the handlers
     * according to their {@link Ordered} value, ensuring that the handlers are executed
     * in the correct order.
     * </p>
     *
     * @param args Command-line arguments passed to the application.
     * @throws Exception If an error occurs during initialization.
     */
    @Override
    public void run(String... args) throws Exception {
        // Get all beans of type AbstractChainHandler from the application context
        Map<String, AbstractChainHandler> chainFilterMap = ApplicationContextHolder
                .getBeansOfType(AbstractChainHandler.class);

        chainFilterMap.forEach((beanName, bean) -> {
            // Get or initialize the list of handlers for the given handler name
            List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(bean.getHandlerName());
            if (CollectionUtils.isEmpty(abstractChainHandlers)) {
                abstractChainHandlers = new ArrayList<>();
            }
            abstractChainHandlers.add(bean);

            // Sort the handlers based on the order value (Ordered interface)
            List<AbstractChainHandler> actualAbstractChainHandlers = abstractChainHandlers.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder))
                    .collect(Collectors.toList());

            // Update the container with the sorted handlers
            abstractChainHandlerContainer.put(bean.getHandlerName(), actualAbstractChainHandlers);
        });
    }
}
