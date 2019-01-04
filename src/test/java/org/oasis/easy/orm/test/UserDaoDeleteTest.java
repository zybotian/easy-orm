package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.oasis.easy.orm.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public class UserDaoDeleteTest extends AbstractTestCase {

    @Autowired
    private UserDao userDao;

    public void testDelete() throws Exception {
        Assert.assertEquals(4, userDao.count());

        int deleted = userDao.deleteOne(1L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(3, userDao.count());

        deleted = userDao.deleteOne(2L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(2, userDao.count());

        deleted = userDao.deleteOne(3L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(1, userDao.count());

        deleted = userDao.deleteOne(4L);
        Assert.assertTrue(deleted > 0);
        Assert.assertEquals(0, userDao.count());

        deleted = userDao.deleteOne(1L);
        Assert.assertTrue(deleted == 0);
        Assert.assertEquals(0, userDao.count());
    }
}
