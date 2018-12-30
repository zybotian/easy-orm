package org.oasis.easy.orm.test;

import org.junit.Test;
import org.oasis.easy.orm.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends AbstractTestCase {

    @Autowired
    private UserDao userDao;

    @Test
    public void testQuery() throws Exception {
//        User user = userDao.findByPrimaryKey(1L);
//        Assert.assertNotNull(user);
    }
}
