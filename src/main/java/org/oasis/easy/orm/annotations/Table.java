package org.oasis.easy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author tianbo
 * @date 2019-01-07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 表名字
     */
    String value() default "";
}
