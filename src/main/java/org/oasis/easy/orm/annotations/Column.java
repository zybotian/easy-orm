package org.oasis.easy.orm.annotations;

import org.oasis.easy.orm.constant.SortType;

import java.lang.annotation.*;

/**
 * @author tianbo
 * @date 2019-01-07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 列名字
     */
    String value() default "";

    /**
     * 是否是主键
     */
    boolean pk() default false;

    /**
     * 排序类型
     */
    SortType sortType() default SortType.NONE;
}
