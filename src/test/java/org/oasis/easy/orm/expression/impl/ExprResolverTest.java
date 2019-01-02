package org.oasis.easy.orm.expression.impl;

import org.junit.Assert;
import org.junit.Test;

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
        map.put("id", 1L);
        map.put("name", "张三");
        map.put("age", 18);
        map.put("major", "计算机");
        map.put("$province", "湖北");
        map.put("$city", "武汉市");
        map.put("$area", "武昌区");

        ExprResolver exprResolver = new ExprResolver(map);

        Assert.assertEquals(1L, exprResolver.evaluate("id"));
        Assert.assertEquals("张三", exprResolver.evaluate("name"));
        Assert.assertEquals(18, exprResolver.evaluate("age"));
        Assert.assertEquals("计算机", exprResolver.evaluate("major"));
        Assert.assertEquals("湖北", exprResolver.evaluate("$province"));
        Assert.assertEquals("武汉市", exprResolver.evaluate("$city"));
        Assert.assertEquals("武昌区", exprResolver.evaluate("$area"));
    }
}
