package com.lcsk42.frameworks.starter.web.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the class or method to indicate that the return value
 * does not need to be converted by the global result processor.
 * <p>
 * This annotation is used to exempt certain classes or methods
 * from the global result processing, ensuring that their return
 * values are not modified or wrapped by any default processors.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompatibleOutput {
}