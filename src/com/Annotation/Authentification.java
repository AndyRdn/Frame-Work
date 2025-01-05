package com.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
@Target(ElementType.METHOD) // This annotation can be used on classes
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentification {
    public String[] roles() default {};
}
