package org.oasis.easy.orm.expression.impl;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.oasis.easy.orm.expression.IExprResolver;
import org.oasis.easy.orm.utils.JexlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExprResolver implements IExprResolver {

    private JexlContext jexlContext;

    public ExprResolver(Map<String, ?> params) {
        Map<String, Object> map = new HashMap<>();
        if (MapUtils.isNotEmpty(params)) {
           map.putAll(params);
        }
        jexlContext = new MapContext(map);
    }

    @Override
    public Object evaluate(String expr) throws Exception {
        return JexlUtils.getJexl().createExpression(expr).evaluate(jexlContext);
    }

}
