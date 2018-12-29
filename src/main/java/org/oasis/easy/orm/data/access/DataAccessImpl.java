package org.oasis.easy.orm.data.access;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class DataAccessImpl implements DataAccess {

    private JdbcTemplate jdbcTemplate;

    @Override
    public <T> List<T> select(String sql, Object[] args, RowMapper<T> rowMapper) {
        PreparedStatementCreator psc = getPreparedStatementCreator(sql, args, false);
        return jdbcTemplate.query(psc, new RowMapperResultSetExtractor<>(rowMapper));
    }

    @Override
    public int update(String sql, Object[] args, KeyHolder generatedKeyHolder) {
        boolean returnKeys = generatedKeyHolder != null;
        PreparedStatementCreator psc = getPreparedStatementCreator(sql, args, returnKeys);
        if (returnKeys) {
            return jdbcTemplate.update(psc, generatedKeyHolder);
        } else {
            return jdbcTemplate.update(psc);
        }
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private PreparedStatementCreator getPreparedStatementCreator(final String sql, final Object[] args, final boolean returnKeys) {
        return new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps;
                if (returnKeys) {
                    ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                } else {
                    ps = con.prepareStatement(sql);
                }

                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, args[i]);
                    }
                }
                return ps;
            }
        };
    }
}
