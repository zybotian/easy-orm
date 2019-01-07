package org.oasis.easy.orm.test;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.dao.UserDao;
import org.oasis.easy.orm.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author tianbo
 * @date 2018-01-04
 */
public class UserDaoSelectTest extends AbstractTestCase {

    @Autowired
    private UserDao userDao;

    @Test
    public void testQuery() throws Exception {
        internalTest_FindByPrimaryKey();
        internalTest_FindList();
        internalTest_FindArray();
        internalTest_FindCollection();
        internalTest_FindSet();
        internalTest_FindListSingle();
        internalTest_FindSetSingle();
        internalTest_FindCollectionSingle();
        internalTest_FindArraySingle();
        internalTest_FindListAdv();
        internalTest_FindMap();
        internalTest_countGroup();
    }

    private void internalTest_FindByPrimaryKey() throws Exception {
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

    private void internalTest_FindList() {
        Assert.assertEquals(4, userDao.count());

        List<User> users = userDao.findList(0, 100);
        Assert.assertEquals(4, users.size());

        Assert.assertEquals(18, users.get(0).getAge().intValue());
        Assert.assertEquals(100, users.get(0).getGroupId().intValue());
        Assert.assertEquals("hubei", users.get(0).getAddress());
        Assert.assertEquals(100, users.get(0).getSalary().intValue());

        Assert.assertEquals(19, users.get(1).getAge().intValue());
        Assert.assertEquals(101, users.get(1).getGroupId().intValue());
        Assert.assertEquals("hunan", users.get(1).getAddress());
        Assert.assertEquals(200, users.get(1).getSalary().intValue());

        Assert.assertEquals(20, users.get(2).getAge().intValue());
        Assert.assertEquals(100, users.get(2).getGroupId().intValue());
        Assert.assertEquals("shanghai", users.get(2).getAddress());
        Assert.assertEquals(300, users.get(2).getSalary().intValue());

        Assert.assertEquals(21, users.get(3).getAge().intValue());
        Assert.assertEquals(101, users.get(3).getGroupId().intValue());
        Assert.assertEquals("jiangsu", users.get(3).getAddress());
        Assert.assertEquals(400, users.get(3).getSalary().intValue());
    }

    private void internalTest_FindCollection() {
        Assert.assertEquals(4, userDao.count());

        Collection<User> userCollection = userDao.findCollection(0, 100);
        Assert.assertEquals(4, userCollection.size());

        User[] users = new User[4];
        userCollection.toArray(users);

        Arrays.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });

        Assert.assertEquals(18, users[0].getAge().intValue());
        Assert.assertEquals(100, users[0].getGroupId().intValue());
        Assert.assertEquals("hubei", users[0].getAddress());
        Assert.assertEquals(100, users[0].getSalary().intValue());

        Assert.assertEquals(19, users[1].getAge().intValue());
        Assert.assertEquals(101, users[1].getGroupId().intValue());
        Assert.assertEquals("hunan", users[1].getAddress());
        Assert.assertEquals(200, users[1].getSalary().intValue());

        Assert.assertEquals(20, users[2].getAge().intValue());
        Assert.assertEquals(100, users[2].getGroupId().intValue());
        Assert.assertEquals("shanghai", users[2].getAddress());
        Assert.assertEquals(300, users[2].getSalary().intValue());

        Assert.assertEquals(21, users[3].getAge().intValue());
        Assert.assertEquals(101, users[3].getGroupId().intValue());
        Assert.assertEquals("jiangsu", users[3].getAddress());
        Assert.assertEquals(400, users[3].getSalary().intValue());
    }

    private void internalTest_FindSet() {
        Assert.assertEquals(4, userDao.count());

        Set<User> userSet = userDao.findSet(0, 100);
        Assert.assertEquals(4, userSet.size());

        User[] users = new User[4];
        userSet.toArray(users);

        Arrays.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });

        Assert.assertEquals(18, users[0].getAge().intValue());
        Assert.assertEquals(100, users[0].getGroupId().intValue());
        Assert.assertEquals("hubei", users[0].getAddress());
        Assert.assertEquals(100, users[0].getSalary().intValue());

        Assert.assertEquals(19, users[1].getAge().intValue());
        Assert.assertEquals(101, users[1].getGroupId().intValue());
        Assert.assertEquals("hunan", users[1].getAddress());
        Assert.assertEquals(200, users[1].getSalary().intValue());

        Assert.assertEquals(20, users[2].getAge().intValue());
        Assert.assertEquals(100, users[2].getGroupId().intValue());
        Assert.assertEquals("shanghai", users[2].getAddress());
        Assert.assertEquals(300, users[2].getSalary().intValue());

        Assert.assertEquals(21, users[3].getAge().intValue());
        Assert.assertEquals(101, users[3].getGroupId().intValue());
        Assert.assertEquals("jiangsu", users[3].getAddress());
        Assert.assertEquals(400, users[3].getSalary().intValue());
    }

    private void internalTest_FindArray() throws Exception {
        User[] users = userDao.findArray(0, 10);
        Assert.assertEquals(4, users.length);

        Assert.assertEquals(18, users[0].getAge().intValue());
        Assert.assertEquals(100, users[0].getGroupId().intValue());
        Assert.assertEquals("hubei", users[0].getAddress());
        Assert.assertEquals(100, users[0].getSalary().intValue());

        Assert.assertEquals(19, users[1].getAge().intValue());
        Assert.assertEquals(101, users[1].getGroupId().intValue());
        Assert.assertEquals("hunan", users[1].getAddress());
        Assert.assertEquals(200, users[1].getSalary().intValue());

        Assert.assertEquals(20, users[2].getAge().intValue());
        Assert.assertEquals(100, users[2].getGroupId().intValue());
        Assert.assertEquals("shanghai", users[2].getAddress());
        Assert.assertEquals(300, users[2].getSalary().intValue());

        Assert.assertEquals(21, users[3].getAge().intValue());
        Assert.assertEquals(101, users[3].getGroupId().intValue());
        Assert.assertEquals("jiangsu", users[3].getAddress());
        Assert.assertEquals(400, users[3].getSalary().intValue());
    }

    private void internalTest_FindListSingle() {
        List<String> list = userDao.findListSingle("address", 0, 10);
        Assert.assertTrue(4 == list.size());

        Assert.assertEquals("hubei", list.get(0));
        Assert.assertEquals("hunan", list.get(1));
        Assert.assertEquals("shanghai", list.get(2));
        Assert.assertEquals("jiangsu", list.get(3));
    }

    private void internalTest_FindSetSingle() {
        Set<String> sets = userDao.findSetSingle("address", 0, 10);
        Assert.assertTrue(4 == sets.size());

        Assert.assertTrue(sets.contains("hubei"));
        Assert.assertTrue(sets.contains("hunan"));
        Assert.assertTrue(sets.contains("shanghai"));
        Assert.assertTrue(sets.contains("jiangsu"));

        Set<String> name = userDao.findSetSingle("name", 0, 10);
        Assert.assertTrue(1 == name.size());
        Assert.assertTrue(name.contains("test-name"));
    }

    private void internalTest_FindCollectionSingle() {
        Collection<Integer> results = userDao.findCollectionSingle("age", 0, 10);
        Assert.assertTrue(4 == results.size());
        Integer[] integers = new Integer[4];
        results.toArray(integers);
        Assert.assertTrue(18 == integers[0]);
        Assert.assertTrue(19 == integers[1]);
        Assert.assertTrue(20 == integers[2]);
        Assert.assertTrue(21 == integers[3]);
    }

    private void internalTest_FindArraySingle() {
        Boolean[] results = userDao.findArraySingle("married", 0, 10);
        Assert.assertTrue(4 == results.length);
        Assert.assertEquals(false, results[0]);
        Assert.assertEquals(true, results[1]);
        Assert.assertEquals(false, results[2]);
        Assert.assertEquals(true, results[3]);
    }

    private void internalTest_FindListAdv() {
        // 除了分页参数外,全部都是null
        UserQuery userQuery = new UserQuery()
                .setMarried(null)
                .setOffset(0)
                .setPageSize(10);

        Assert.assertTrue(4 == userDao.findListAdv(userQuery).size());

        userQuery.setMarried(true);
        Assert.assertTrue(2 == userDao.findListAdv(userQuery).size());

        userQuery.setMarried(false);
        Assert.assertTrue(2 == userDao.findListAdv(userQuery).size());

        userQuery.setMarried(null);
        userQuery.setGroups(Arrays.asList(100, 101));
        Assert.assertTrue(4 == userDao.findListAdv(userQuery).size());

        userQuery.setGroups(Arrays.asList(100));
        Assert.assertTrue(2 == userDao.findListAdv(userQuery).size());

        userQuery.setGroups(null);
        userQuery.setName("test%");
        Assert.assertTrue(4 == userDao.findListAdv(userQuery).size());
        userQuery.setOrderBy("age");
        userQuery.setOrderType("desc");
        List<User> users = userDao.findListAdv(userQuery);
        Assert.assertTrue(users.get(0).getAge() == 21);
        Assert.assertTrue(users.get(1).getAge() == 20);
        Assert.assertTrue(users.get(2).getAge() == 19);
        Assert.assertTrue(users.get(3).getAge() == 18);

        userQuery.setOrderType(null);
        userQuery.setOrderBy(null);

        userQuery.setOrderBy(null);
        userQuery.setOrderType(null);
        userQuery.setName(null);
        userQuery.setMinAge(18);
        userQuery.setMaxAge(20);
        Assert.assertTrue(3 == userDao.findListAdv(userQuery).size());

        userQuery.setMinAge(null);
        userQuery.setMaxAge(null);
        userQuery.setAddress("hu");
        // hubei hunan
        Assert.assertTrue(2 == userDao.findListAdv(userQuery).size());

        // hubei hunan shanghai
        userQuery.setAddress("h");
        Assert.assertTrue(3 == userDao.findListAdv(userQuery).size());

        userQuery.setMinAge(18);
        userQuery.setMaxAge(21);
        userQuery.setName("test-name");
        userQuery.setGroups(Arrays.asList(100, 101));
        Assert.assertTrue(3 == userDao.findListAdv(userQuery).size());

        userQuery.setMarried(true);
        Assert.assertTrue(1 == userDao.findListAdv(userQuery).size());

        userQuery.setMarried(false);
        Assert.assertTrue(2 == userDao.findListAdv(userQuery).size());
    }

    private void internalTest_FindMap() {
        List<Map<String, Object>> mapResult = userDao.findMap(0, 10);
        for (Map<String, Object> map : mapResult) {
            if ((Long) map.get("ID") == 1L) {
                Assert.assertTrue((Integer) map.get("AGE") == 18);
                Assert.assertTrue((Integer) map.get("GROUP_ID") == 100);
                Assert.assertTrue("hubei".equals(map.get("ADDRESS").toString()));
            } else if ((Long) map.get("ID") == 2L) {
                Assert.assertTrue((Integer) map.get("AGE") == 19);
                Assert.assertTrue((Integer) map.get("GROUP_ID") == 101);
                Assert.assertTrue("hunan".equals(map.get("ADDRESS").toString()));
            } else if ((Long) map.get("ID") == 3L) {
                Assert.assertTrue((Integer) map.get("AGE") == 20);
                Assert.assertTrue((Integer) map.get("GROUP_ID") == 100);
                Assert.assertTrue("shanghai".equals(map.get("ADDRESS").toString()));
            } else if ((Long) map.get("ID") == 4L) {
                Assert.assertTrue((Integer) map.get("AGE") == 21);
                Assert.assertTrue((Integer) map.get("GROUP_ID") == 101);
                Assert.assertTrue("jiangsu".equals(map.get("ADDRESS").toString()));
            } else {
                Assert.fail();
            }
        }
    }

    private void internalTest_countGroup() {
        List<GroupResult> groupResults = userDao.countGroup();
        Assert.assertEquals("100", groupResults.get(0).getGroupId());
        Assert.assertEquals("101", groupResults.get(1).getGroupId());
        Assert.assertEquals(2, groupResults.get(0).getCount());
        Assert.assertEquals(2, groupResults.get(1).getCount());
    }
}
