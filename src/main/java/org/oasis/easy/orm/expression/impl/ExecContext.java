package org.oasis.easy.orm.expression.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.oasis.easy.orm.expression.IExecContext;
import org.oasis.easy.orm.utils.ExecUtils;

import java.util.*;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExecContext implements IExecContext {

    /**
     * NULL常量
     */
    private static final String NULL = "NULL";

    /**
     * sql语句中变量取值的占位符
     */
    private static final char QUESTION = '?';

    /**
     * sql语句的逗号分隔符
     */
    private static final char COMMA = ',';

    /**
     * 参数列表
     */
    private final List<Object> params;

    /**
     * 输出缓冲区
     */
    private final StringBuilder sbCache;

    public ExecContext(int capacity) {
        sbCache = new StringBuilder(capacity);
        params = new ArrayList<>();
    }

    @Override
    public void fillText(String text) {
        sbCache.append(text);
    }

    @Override
    public void fillValue(Object obj) {
        if (obj instanceof Collection) {
            fillCollection((Collection) obj);
        } else if (obj != null && obj.getClass().isArray() && obj.getClass() != byte[].class) {
            fillCollection(ExecUtils.asCollection(obj));
        } else {
            // Map暂不考虑
            params.add(obj);
            sbCache.append(QUESTION);
        }
    }

    private void fillCollection(Collection coll) {
        if (CollectionUtils.isEmpty(coll)) {
            sbCache.append(NULL);
            return;
        }

        int count = 0;
        for (Object obj : coll) {
            if (obj == null) {
                continue;
            }
            params.add(obj);
            if (count > 0) {
                sbCache.append(COMMA);
            }
            sbCache.append(QUESTION);
            count++;
        }
    }

    @Override
    public Object[] getParams() {
        return params.toArray();
    }

    @Override
    public String getContent() {
        return sbCache.toString();
    }
}
