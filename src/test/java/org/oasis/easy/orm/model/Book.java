package org.oasis.easy.orm.model;

import lombok.Data;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Data
public class Book {
    private Long id;
    private String name;
    private Integer price;
    private Integer type;
    private Integer count;
    private Boolean available;
}
