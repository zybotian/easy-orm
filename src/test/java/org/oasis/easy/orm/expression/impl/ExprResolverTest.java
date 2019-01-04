package org.oasis.easy.orm.expression.impl;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExprResolverTest {

    @Test
    public void testExecuteExpr() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put(":id", 1L);
        map.put(":name", "张三");
        map.put(":age", 18);
        map.put(":major", "计算机");
        map.put(":province", "湖北");
        map.put(":city", "武汉市");
        map.put(":area", "武昌区");
        User user = new User()
                .setId(1L)
                .setName("张三丰")
                .setAge(134)
                .setGroupId(1)
                .setMarried(false)
                .setSalary(10.0)
                .setAddress("武当山");
        User user2 = new User()
                .setId(2L)
                .setName("张无忌")
                .setAge(21)
                .setGroupId(1)
                .setMarried(true)
                .setSalary(100.0)
                .setAddress("光明顶");
        map.put(":1", user);
        map.put(":user", user2);

        ExprResolver exprResolver = new ExprResolver(map);

        Assert.assertEquals(1L, exprResolver.evaluate(":id"));
        Assert.assertEquals("张三", exprResolver.evaluate(":name"));
        Assert.assertEquals(18, exprResolver.evaluate(":age"));
        Assert.assertEquals("计算机", exprResolver.evaluate(":major"));
        Assert.assertEquals("湖北", exprResolver.evaluate(":province"));
        Assert.assertEquals("武汉市", exprResolver.evaluate(":city"));
        Assert.assertEquals("武昌区", exprResolver.evaluate(":area"));

        Assert.assertEquals(1L, exprResolver.evaluate(":1.id"));
        Assert.assertEquals("张三丰", exprResolver.evaluate(":1.name"));
        Assert.assertEquals(134, exprResolver.evaluate(":1.age"));
        Assert.assertEquals(1, exprResolver.evaluate(":1.groupId"));
        Assert.assertEquals(false, exprResolver.evaluate(":1.married"));
        Assert.assertEquals(10, ((Double) (exprResolver.evaluate(":1.salary"))).intValue());
        Assert.assertEquals("武当山", exprResolver.evaluate(":1.address"));

        Assert.assertEquals(2L, exprResolver.evaluate(":user.id"));
        Assert.assertEquals("张无忌", exprResolver.evaluate(":user.name"));
        Assert.assertEquals(21, exprResolver.evaluate(":user.age"));
        Assert.assertEquals(1, exprResolver.evaluate(":user.groupId"));
        Assert.assertEquals(true, exprResolver.evaluate(":user.married"));
        Assert.assertEquals(100, ((Double) (exprResolver.evaluate(":user.salary"))).intValue());
        Assert.assertEquals("光明顶", exprResolver.evaluate(":user.address"));
    }
}
