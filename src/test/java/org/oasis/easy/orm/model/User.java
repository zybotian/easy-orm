package org.oasis.easy.orm.model;

import org.oasis.easy.orm.annotations.Column;
import org.oasis.easy.orm.annotations.Table;
import org.oasis.easy.orm.constant.SortType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tianbo
 * @date 2018-12-29
 * @notes POJO定义应当按照约定, 使用小驼峰的方式进行命名
 */
@Data
@Accessors(chain = true)
@Table
public class User {
    @Column(pk = true, sortType = SortType.ASC)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column(value = "group_id")
    private Integer groupId;

    @Column
    private String address;

    @Column
    private Boolean married;

    @Column
    private Double salary;

    @Column
    private Long createTime;

    @Column
    private Long updateTime;
}
