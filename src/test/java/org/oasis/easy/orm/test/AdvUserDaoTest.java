package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.AdvUserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class AdvUserDaoTest extends AbstractTestCase {

    @Autowired
    private AdvUserDao advUserDao;

    @Test
    public void testFind() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(1 == user.getId());
        Assert.assertTrue(18 == user.getAge());
        Assert.assertTrue(100 == user.getGroupId());
        Assert.assertTrue(100 == user.getSalary().intValue());
        Assert.assertEquals("test-name", user.getName());
        Assert.assertEquals("hubei", user.getAddress());
    }

    @Test
    public void testDelete() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(user != null);
        boolean delete = advUserDao.delete(1L);
        Assert.assertTrue(delete);
        User user1 = advUserDao.find(1L);
        Assert.assertTrue(user1 == null);
    }
}
