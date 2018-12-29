package org.oasis.easy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlParam {
    String value();
}