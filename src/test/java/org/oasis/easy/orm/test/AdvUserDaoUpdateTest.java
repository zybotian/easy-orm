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
public class AdvUserDaoUpdateTest extends AbstractTestCase {

    @Autowired
    private AdvUserDao advUserDao;

    @Test
    public void testUpdate() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(user.getAge() == 18);
        Assert.assertTrue(user.getGroupId() == 100);
        Assert.assertTrue(100 == user.getSalary().intValue());
        Assert.assertEquals("hubei", user.getAddress());

        user.setAge(81);
        user.setGroupId(250);
        user.setAddress("IBM");
        user.setSalary(12000.0);
        boolean update = advUserDao.update(user);
        Assert.assertTrue(update);

        User user1 = advUserDao.find(1L);
        Assert.assertTrue(user1.getAge() == 81);
        Assert.assertTrue(user1.getGroupId() == 250);
        Assert.assertTrue(12000 == user.getSalary().intValue());
        Assert.assertEquals("IBM", user1.getAddress());
    }
}
