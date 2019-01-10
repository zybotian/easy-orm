package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.AdvUserDao;
import org.oasis.easy.orm.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class AdvUserDaoSelectTest extends AbstractTestCase {

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
    public void testFindById() throws Exception {
        User user = advUserDao.findById(1L);
        Assert.assertTrue(1 == user.getId());
        Assert.assertTrue(18 == user.getAge());
        Assert.assertTrue(100 == user.getGroupId());
        Assert.assertTrue(100 == user.getSalary().intValue());
        Assert.assertEquals("test-name", user.getName());
        Assert.assertEquals("hubei", user.getAddress());
    }

    @Test
    public void testFindByAgeRange() throws Exception {
        List<User> users = advUserDao.findByAgeRange(0, 30);
        Assert.assertTrue(users.size() == 4);
        Assert.assertTrue(1 == users.get(0).getId());
        Assert.assertTrue(2 == users.get(1).getId());
        Assert.assertTrue(3 == users.get(2).getId());
        Assert.assertTrue(4 == users.get(3).getId());
        Assert.assertTrue(18 == users.get(0).getAge());
        Assert.assertTrue(19 == users.get(1).getAge());
        Assert.assertTrue(20 == users.get(2).getAge());
        Assert.assertTrue(21 == users.get(3).getAge());
    }

    @Test
    public void testFindByName() throws Exception {
        List<User> users = advUserDao.findByName("test%");
        Assert.assertTrue(users.size() == 4);
        Assert.assertTrue(1 == users.get(0).getId());
        Assert.assertTrue(2 == users.get(1).getId());
        Assert.assertTrue(3 == users.get(2).getId());
        Assert.assertTrue(4 == users.get(3).getId());
        List<User> users2 = advUserDao.findByName("Test%");
        Assert.assertTrue(users2.size() == 0);
    }

    @Test
    public void testFindByIdList() throws Exception {
        List<User> users = advUserDao.findByIdList(Arrays.asList(1L, 2L, 3L, 4L));
        Assert.assertTrue(users.size() == 4);
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        Assert.assertTrue(1 == users.get(0).getId());
        Assert.assertTrue(2 == users.get(1).getId());
        Assert.assertTrue(3 == users.get(2).getId());
        Assert.assertTrue(4 == users.get(3).getId());
    }

    @Test
    public void testFindByGroups() throws Exception {
        List<User> users1 = advUserDao.findByGroups(Arrays.asList(1, 2, 3));
        Assert.assertTrue(users1.size() == 0);

        List<User> users2 = advUserDao.findByGroups(Arrays.asList(100, 101, 301, 404));
        Collections.sort(users2, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        Assert.assertTrue(users2.size() == 4);
        Assert.assertTrue(1 == users2.get(0).getId());
        Assert.assertTrue(2 == users2.get(1).getId());
        Assert.assertTrue(3 == users2.get(2).getId());
        Assert.assertTrue(4 == users2.get(3).getId());
    }

    @Test
    public void testFindList() throws Exception {
        List<User> list = advUserDao.findList(0, 10);
        Assert.assertTrue(list.size() == 4);

        List<User> list1 = advUserDao.findList(0, 1);
        Assert.assertTrue(list1.size() == 1);

        List<User> list2 = advUserDao.findList(0, 4);
        Assert.assertTrue(list2.size() == 4);

        List<User> list3 = advUserDao.findList(2, 1);
        Assert.assertTrue(list3.size() == 1);
        Assert.assertTrue(list3.get(0).getId() == 3);
    }

    @Test
    public void testFindByAgeRangeList() throws Exception {
        // (18,21)
        List<User> users = advUserDao.findByAgeRange(18, 21, 0, 10);
        Assert.assertTrue(users.size() == 2);
        Assert.assertTrue(users.get(0).getAge() == 19);
        Assert.assertTrue(users.get(1).getAge() == 20);

        List<User> users2 = advUserDao.findByAgeRange(19, 25, 0, 10);
        Assert.assertTrue(users2.size() == 2);
        Assert.assertTrue(users2.get(0).getAge() == 20);
        Assert.assertTrue(users2.get(1).getAge() == 21);

        List<User> users3 = advUserDao.findByAgeRange(10, 25, 0, 10);
        Assert.assertTrue(users3.size() == 4);
        Assert.assertTrue(users3.get(0).getAge() == 18);
        Assert.assertTrue(users3.get(1).getAge() == 19);
        Assert.assertTrue(users3.get(2).getAge() == 20);
        Assert.assertTrue(users3.get(3).getAge() == 21);

        List<User> users4 = advUserDao.findByAgeRange(10, 25, 0, 2);
        Assert.assertTrue(users4.size() == 2);
        Assert.assertTrue(users4.get(0).getAge() == 18);
        Assert.assertTrue(users4.get(1).getAge() == 19);

        List<User> users5 = advUserDao.findByAgeRange(10, 25, 2, 2);
        Assert.assertTrue(users5.size() == 2);
        Assert.assertTrue(users5.get(0).getAge() == 20);
        Assert.assertTrue(users5.get(1).getAge() == 21);
    }

    @Test
    public void testFindList2() throws Exception {
        List<User> list = advUserDao.findList(null, null, null, null, null, null, null, 0,
                10);
        Assert.assertTrue(list.size() == 4);

        List<User> list2 = advUserDao.findList(0, null, null, null, null, null, null, 0,
                10);
        Assert.assertTrue(list2.size() == 4);

        List<User> list3 = advUserDao.findList(0, 30, null, null, null, null, null, 0,
                10);
        Assert.assertTrue(list3.size() == 4);

        List<User> list4 = advUserDao.findList(0, 30, "%test%", null, null, null, null, 0,
                10);
        Assert.assertTrue(list4.size() == 4);

        List<User> list5 = advUserDao.findList(0, 30, "%test%", null, Arrays.asList(100, 101), null, null, 0,
                10);
        Assert.assertTrue(list5.size() == 4);

        List<User> list6 = advUserDao.findList(0, 30, "%test%", null, Arrays.asList(100, 101), 0.0, 1000.0, 0,
                10);
        Assert.assertTrue(list6.size() == 4);

        List<User> list7 = advUserDao.findList(0, 30, "%test%", "%h%", Arrays.asList(100, 101), 0.0, 1000.0, 0,
                10);
        Assert.assertTrue(list7.size() == 3);

        List<User> list8 = advUserDao.findList(0, 30, "%test%", "hu%", Arrays.asList(100, 101), 0.0, 1000.0, 0,
                10);
        Assert.assertTrue(list8.size() == 2);

        List<User> list9 = advUserDao.findList(0, 30, "test%", "hu%", Arrays.asList(100, 101), 0.0, 1000.0, 0,
                10);
        Assert.assertTrue(list9.size() == 2);
    }

    @Test
    public void testFindByMarried() throws Exception {
        List<User> users = advUserDao.findByMarried(true);
        sort(users);
        Assert.assertTrue(users.size() == 2);
        Assert.assertTrue(users.get(0).getId() == 2);
        Assert.assertTrue(users.get(1).getId() == 4);

        List<User> users1 = advUserDao.findByMarried(false);
        sort(users1);
        Assert.assertTrue(users1.size() == 2);
        Assert.assertTrue(users1.get(0).getId() == 1);
        Assert.assertTrue(users1.get(1).getId() == 3);
    }
}
