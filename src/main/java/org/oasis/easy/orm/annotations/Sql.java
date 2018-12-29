package org.oasis.easy.orm.annotations;

import org.oasis.easy.orm.constant.SqlType;

import java.lang.annotation.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sql {
    String value() default "";

    SqlType type() default SqlType.AUTO_DETECT;
}
