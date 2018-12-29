package org.oasis.easy.orm.model;

import lombok.Data;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String address;
    private Boolean married;
    private Double salary;
}
