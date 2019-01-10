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
public class AdvUserDaoDeleteTest extends AbstractTestCase {

    @Autowired
    private AdvUserDao advUserDao;

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
