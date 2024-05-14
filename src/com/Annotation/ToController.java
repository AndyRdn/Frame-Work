package com.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // This annotation can be used on classes
@Retention(RetentionPolicy.RUNTIME) // This annotation is retained at runtime
public @interface ToController {

}