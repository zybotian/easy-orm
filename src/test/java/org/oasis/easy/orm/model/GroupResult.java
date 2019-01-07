package org.oasis.easy.orm.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tianbo
 * @date 2019-01-07
 */
@Data
@Accessors(chain = true)
public class GroupResult {
    private int count;
    private String groupId;
}
