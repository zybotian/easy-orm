package org.oasis.easy.orm.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tianbo
 * @date 2019-01-04
 */
@Data
@Accessors(chain = true)
public class UserQuery {
    private String name;
    private Integer minAge;
    private Integer maxAge;
    private List<Integer> groups;
    private Boolean married;
    private String address;
    private Integer offset;
    private Integer pageSize;
}
