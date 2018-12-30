package org.oasis.easy.orm.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tianbo
 * @date 2018-12-29
 */
@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    private Integer age;
    private Integer groupId;
    private String address;
    private Boolean married;
    private Double salary;
    private Long createTime;
    private Long updateTime;
}
