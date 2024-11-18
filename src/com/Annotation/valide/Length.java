package com.Annotation.valide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // This annotation can be used on classes
@Retention(RetentionPolicy.RUNTIME)
public @interface Length {
    public int max() default 0;
}
