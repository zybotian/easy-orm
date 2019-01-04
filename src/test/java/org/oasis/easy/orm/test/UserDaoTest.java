package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.UserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public class UserDaoTest extends AbstractTestCase {

    @Autowired
    private UserDao userDao;

    @Test
    public void testQuery() throws Exception {
        User user = userDao.findByPrimaryKey(1L);
        Assert.assertEquals(1, user.getId().longValue());
        Assert.assertEquals(18, user.getAge().intValue());
        Assert.assertEquals(100, user.getGroupId().intValue());
        Assert.assertEquals("hubei", user.getAddress());
        Assert.assertEquals(false, user.getMarried());
        Assert.assertEquals(100, user.getSalary().intValue());


        User user2 = userDao.findByPrimaryKey(2L);
        Assert.assertEquals(2, user2.getId().longValue());
        Assert.assertEquals(19, user2.getAge().intValue());
        Assert.assertEquals(101, user2.getGroupId().intValue());
        Assert.assertEquals("hunan", user2.getAddress());
        Assert.assertEquals(true, user2.getMarried());
        Assert.assertEquals(200, user2.getSalary().intValue());

        User user3 = userDao.findByPrimaryKey(3L);
        Assert.assertEquals(3, user3.getId().longValue());
        Assert.assertEquals(20, user3.getAge().intValue());
        Assert.assertEquals(100, user3.getGroupId().intValue());
        Assert.assertEquals("shanghai", user3.getAddress());
        Assert.assertEquals(false, user3.getMarried());
        Assert.assertEquals(300, user3.getSalary().intValue());

        User user4 = userDao.findByPrimaryKey(4L);
        Assert.assertEquals(4, user4.getId().longValue());
        Assert.assertEquals(21, user4.getAge().intValue());
        Assert.assertEquals(101, user4.getGroupId().intValue());
        Assert.assertEquals("jiangsu", user4.getAddress());
        Assert.assertEquals(true, user4.getMarried());
        Assert.assertEquals(400, user4.getSalary().intValue());
    }
}
