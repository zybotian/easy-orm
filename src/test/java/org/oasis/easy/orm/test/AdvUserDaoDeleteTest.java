package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.AdvUserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testDeleteById() throws Exception {
        User user = advUserDao.find(1L);
        Assert.assertTrue(user != null);
        int delete = advUserDao.deleteById(1L);
        Assert.assertTrue(delete > 0);
        User user1 = advUserDao.find(1L);
        Assert.assertTrue(user1 == null);
    }

    @Test
    public void testDeleteByGroupId() throws Exception {
        List<User> users = advUserDao.findList(0, 10);
        Assert.assertTrue(users.size() == 4);
        int delete = advUserDao.deleteByGroupId(100);
        Assert.assertTrue(delete > 0);
        List<User> users1 = advUserDao.findByGroups(Arrays.asList(100));
        Assert.assertTrue(users1.size() == 0);

        List<User> users2 = advUserDao.findByGroups(Arrays.asList(101));
        Assert.assertTrue(users2.size() == 2);
    }

    @Test
    public void testDeleteByName() throws Exception {
        List<User> users = advUserDao.findList(0, 10);
        Assert.assertTrue(users.size() == 4);
        int delete = advUserDao.deleteByName("test-name");
        Assert.assertTrue(delete > 0);
        List<User> users1 = advUserDao.findList(0, 10);
        Assert.assertTrue(users1.size() == 0);
    }

    @Test
    public void testDeleteByMarried() throws Exception {
        List<User> users = advUserDao.findList(0, 10);
        Assert.assertTrue(users.size() == 4);
        int delete = advUserDao.deleteByMarried(false);
        Assert.assertTrue(delete > 0);
        List<User> users1 = advUserDao.findList(0, 10);
        Assert.assertTrue(users1.size() == 2);
        List<User> users2 = advUserDao.findByMarried(true);
        Assert.assertTrue(users2.size() == 2);
    }

    @Test
    public void testDeleteByNameLike() throws Exception {
        List<User> users = advUserDao.findList(0, 10);
        Assert.assertTrue(users.size() == 4);
        int delete = advUserDao.deleteByNameLike("test%");
        Assert.assertTrue(delete > 0);
        List<User> users1 = advUserDao.findList(0, 10);
        Assert.assertTrue(users1.size() == 0);
    }

    @Test
    public void testDeleteByIdList() throws Exception {
        List<User> list = advUserDao.findList(0, 10);
        Assert.assertTrue(list.size() == 4);
        int deleted = advUserDao.deleteByIdList(Arrays.asList(1L, 2L, 3L));
        Assert.assertTrue(deleted > 0);
        List<User> list1 = advUserDao.findList(0, 10);
        Assert.assertTrue(list1.size() == 1);
        Assert.assertTrue(list1.get(0).getId() == 4);
    }

    @Test
    public void testDeleteByGroupIdList() throws Exception {
        int deleted = advUserDao.deleteByGroupIdList(Arrays.asList(1));
        Assert.assertTrue(deleted == 0);

        int deleted1 = advUserDao.deleteByGroupIdList(Arrays.asList(100));
        Assert.assertTrue(deleted1 > 0);
        List<User> users = advUserDao.findByGroups(Arrays.asList(100));
        Assert.assertTrue(users.size() == 0);
        List<User> users1 = advUserDao.findByGroups(Arrays.asList(101));
        Assert.assertTrue(users1.size() == 2);

        int deleted2 = advUserDao.deleteByGroupIdList(Arrays.asList(101));
        Assert.assertTrue(deleted2 > 0);
        List<User> users2 = advUserDao.findByGroups(Arrays.asList(100));
        Assert.assertTrue(users2.size() == 0);
        List<User> users3 = advUserDao.findByGroups(Arrays.asList(101));
        Assert.assertTrue(users3.size() == 0);
    }

    @Test
    public void testDeleteAdv() throws Exception {
        int delete = advUserDao.delete("test-name-1%", Arrays.asList(1, 2), 100, 120);
        Assert.assertTrue(delete == 0);
        Assert.assertTrue(advUserDao.findList(0, 10).size() == 4);

        // age:[10,18)
        int delete1 = advUserDao.delete("test-name%", Arrays.asList(100, 101, 201, 301, 404), 10, 18);
        Assert.assertTrue(delete1 == 0);
        Assert.assertTrue(advUserDao.findList(0, 10).size() == 4);

        // age:[18,18)
        int delete2 = advUserDao.delete("test-name%", Arrays.asList(100, 101, 201, 301, 404), 18, 18);
        Assert.assertTrue(delete2 == 0);
        Assert.assertTrue(advUserDao.findList(0, 10).size() == 4);

        // age:[18,19)
        int delete3 = advUserDao.delete("test-name%", Arrays.asList(100, 101, 201, 301, 404), 18, 19);
        Assert.assertTrue(delete3 == 1);
        Assert.assertTrue(advUserDao.findList(0, 10).size() == 3);

        int delete4 = advUserDao.delete("test-name%", Arrays.asList(100, 101, 201, 301, 404), 19, 20);
        Assert.assertTrue(delete4 == 1);
        Assert.assertTrue(advUserDao.findList(0, 10).size() == 2);

        int delete5 = advUserDao.delete("test-name%", Arrays.asList(100, 101, 201, 301, 404), 20, 22);
        Assert.assertTrue(delete5 == 2);
        Assert.assertTrue(advUserDao.findList(0, 10).size() == 0);
    }
}
