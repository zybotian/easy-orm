package org.oasis.easy.orm.mapper.sql;

import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.constant.SortType;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tianbo
 * @date 2019-01-14
 */
@Data
@Accessors(chain = true)
public class Order {
    private List<OrderField> orderFieldList = new LinkedList<>();

    public Order orderBy(String field, SortType direction) {
        if (direction != null && direction != SortType.NONE && StringUtils.isNotEmpty(field)) {
            this.orderFieldList.add(new OrderField(field, direction));
            return this;
        }
        throw new EasyOrmException(ErrorCode.INVALID_PARAM, "order by field is invalid, field:" + field + ", direction:" + direction);
    }

    public Order asc(String field) {
        return orderBy(field, SortType.ASC);
    }

    public Order desc(String field) {
        return orderBy(field, SortType.DESC);
    }

    public Order reset() {
        this.orderFieldList = new LinkedList<>();
        return this;
    }

    @Data
    @AllArgsConstructor
    public static class OrderField {
        // field是model中的字段名字, 不是列的名字
        private String field;
        private SortType direction;
    }
}
