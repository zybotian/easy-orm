package org.oasis.easy.orm.dao;

import org.oasis.easy.orm.annotations.Dao;
import org.oasis.easy.orm.model.User;

/**
 * @author tianbo
 * @date 2019-01-07
 */
@Dao
public interface AdvUserDao extends BasicDao<User, Long> {
}
