package org.oasis.easy.orm.expression.impl;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.jexl2.*;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.expression.IExprResolver;
import org.oasis.easy.orm.utils.CacheUtils;
import org.oasis.easy.orm.utils.JexlUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExprResolver implements IExprResolver {

    private static final String MAP_KEY = "_map_vars_";

    private static final String COLON = ":";

    private static final int VAR_NAME_LENGTH = 32;

    private static final Pattern PREFIX_PATTERN = Pattern.compile("(\\:)([a-zA-Z0-9_]+)(\\.[a-zA-Z0-9_]+)*");

    /**
     * 表达式缓存
     */
    private static final Map<String, Expression> cache = CacheUtils.getSynchronizedLRUCache();

    private Map<String, Object> mapVars;

    private JexlContext jexlContext;

    public ExprResolver(Map<String, ?> params) {
        Map<String, Object> map = new HashMap<>();
        if (MapUtils.isNotEmpty(params)) {
            map.put(MAP_KEY, params);
            mapVars = new HashMap<>();
            mapVars.putAll(params);
        }
        jexlContext = new MapContext(map);
    }

    @Override
    public Object evaluate(String expr) throws EasyOrmException {
        if (cache.containsKey(expr)) {
            return cache.get(expr).evaluate(jexlContext);
        }

        // string builder默认容量是16
        StringBuilder builder = new StringBuilder(VAR_NAME_LENGTH);
        int index = 0;
        // 匹配正则表达式, 并替换内容
        Matcher matcher = PREFIX_PATTERN.matcher(expr);
        while (matcher.find()) {
            builder.append(expr.substring(index, matcher.start()));

            String prefix = matcher.group(1);
            String name = matcher.group(2);
            if (StringUtils.equals(COLON, prefix)) {
                name = COLON + name;
                if (MapUtils.isEmpty(mapVars) || !mapVars.containsKey(name)) {
                    throw new EasyOrmException(ErrorCode.INVALID_PARAM, name + " not defined in dao method");
                }
                // _map_vars_[':entity'].id
                builder.append(MAP_KEY);
                builder.append("['");
                builder.append(name);
                builder.append("']");
            }
            index = matcher.end(2);
        }

        builder.append(expr.substring(index));

        String convertedExpr = builder.toString();
        Expression expression = JexlUtils.getJexl().createExpression(convertedExpr);
        cache.put(expr, expression);
        return expression.evaluate(jexlContext);
    }

}
