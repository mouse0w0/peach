package com.github.mouse0w0.peach.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
    String value() default "";

    boolean required() default false;

    Class<? extends Converter> converter() default Converter.class;
}
