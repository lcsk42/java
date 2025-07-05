package com.lcsk42.frameworks.starter.base;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * ApplicationContextHolder
 * <p>
 * A utility class to hold and access the Spring {@link ApplicationContext} globally.
 * This is useful for accessing Spring-managed beans outside managed components.
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    // Static reference to the Spring ApplicationContext
    private static ApplicationContext context;

    /**
     * Get a Spring-managed bean by its class type.
     *
     * @param clazz The class of the bean.
     * @param <T>   The type of the bean.
     * @return The bean instance.
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * Get a Spring-managed bean by its name.
     *
     * @param name The name of the bean.
     * @return The bean instance.
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * Get a Spring-managed bean by its name and type.
     *
     * @param name  The name of the bean.
     * @param clazz The expected class of the bean.
     * @param <T>   The type of the bean.
     * @return The bean instance.
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    /**
     * Get all beans of the given type from the Spring context.
     *
     * @param clazz The class of beans to retrieve.
     * @param <T>   The type of the beans.
     * @return A map of bean names to bean instances.
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    /**
     * Find a specific annotation on a bean by name.
     *
     * @param beanName       The name of the bean.
     * @param annotationType The type of annotation to look for.
     * @param <A>            The annotation type.
     * @return The annotation instance if present, otherwise null.
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return context.findAnnotationOnBean(beanName, annotationType);
    }

    /**
     * Get the current Spring {@link ApplicationContext} instance.
     *
     * @return The application context.
     */
    public static ApplicationContext getInstance() {
        return context;
    }

    /**
     * Set the Spring {@link ApplicationContext}.
     * This method is automatically called by Spring during context initialization.
     *
     * @param applicationContext The application context to set.
     * @throws BeansException If setting the context fails.
     */
    @Override
    @SuppressWarnings("squid:S2696")
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.context = applicationContext;
    }
}
