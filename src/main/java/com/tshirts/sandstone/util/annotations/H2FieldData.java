package com.tshirts.sandstone.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface H2FieldData {
    boolean primaryKey() default false;
    boolean notNull() default false;
    boolean autoIncrement() default false;
}
