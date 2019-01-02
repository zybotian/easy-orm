package org.oasis.easy.orm.expression.impl;

import org.junit.Assert;
import org.junit.Test;
import org.oasis.easy.orm.expression.IExecContext;

import java.util.Arrays;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExecContextTest {

    @Test
    public void testFill() throws Exception {
        IExecContext context = new ExecContext(512);

        context.fillText("WHERE uid = ");
        context.fillValue(102);
        context.fillText(" AND sid IN (");
        context.fillValue(new int[]{12, 24, 32, 33});
        context.fillText(") AND (create_time >= ");
        context.fillValue(1545137144000L);
        context.fillText(" OR create_time < ");
        context.fillValue(1576673144000L);
        context.fillText(")");
        context.fillText(" AND status IN (");
        context.fillValue(Arrays.asList(1, 2));
        context.fillText(")");
        context.fillText(" AND flag IN (");
        context.fillValue(null);
        context.fillText(")");

        Assert.assertEquals("WHERE uid = ? AND sid IN (?,?,?,?) AND (create_time >= ? OR create_time < ?) AND status" +
                " IN (?,?) AND flag IN (?)", context.getContent());
        Object[] expected = {102, 12, 24, 32, 33, 1545137144000L, 1576673144000L, 1, 2, null};
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], context.getParams()[i]);
        }
    }
}
