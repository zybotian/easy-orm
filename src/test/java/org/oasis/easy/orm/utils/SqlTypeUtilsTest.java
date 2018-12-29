package org.oasis.easy.orm.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tianbo
 * @date 2018-12-21
 */
public class SqlTypeUtilsTest {

    @Test
    public void testMatchSelect() throws Exception {
        // 匹配的情况
        Assert.assertTrue(SqlTypeUtils.matchQuery(" select * from user where id=1", "findById"));
        Assert.assertTrue(SqlTypeUtils.matchQuery(" select id,name,type from user where id=1", "findOne"));
        Assert.assertTrue(SqlTypeUtils.matchQuery(" select id,name,type from user where age=10", "listByAge"));
        Assert.assertTrue(SqlTypeUtils.matchQuery(" count(*) from user where type=1", "countByType"));
        Assert.assertTrue(SqlTypeUtils.matchQuery("select * from user where age=10 and type=2", "findByAgeAndType"));
        Assert.assertTrue(SqlTypeUtils.matchQuery(" select distinct(count(id)) from user where age=10",
                "countByAge"));
        Assert.assertTrue(SqlTypeUtils.matchQuery(" select * from user where id=1 for update", "lockById"));

        // 不匹配的情况
        Assert.assertFalse(SqlTypeUtils.matchQuery(" update user set age=23 where id=1", "updateUserAge"));
        Assert.assertFalse(SqlTypeUtils.matchQuery(" insert into user values('1','2','3')", "insertOne"));
        Assert.assertFalse(SqlTypeUtils.matchQuery(" delete from user where id=1", "deleteById"));
    }

}
