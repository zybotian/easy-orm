package org.oasis.easy.orm.data.access;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public interface DataAccess {
    <T> List<T> select(String sql, Object[] args, RowMapper<T> rowMapper);

    int update(String sql, Object[] args, KeyHolder generatedKeyHolder);

    int[] batchUpdate(String sql, List<Object[]> batchArgs);
}
