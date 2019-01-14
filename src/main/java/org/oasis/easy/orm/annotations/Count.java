package org.oasis.easy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author tianbo
 * @date 2019-01-07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Count {
    // model的字段名字,非列名字
    String value() default "id";

    boolean distinct() default true;
}
