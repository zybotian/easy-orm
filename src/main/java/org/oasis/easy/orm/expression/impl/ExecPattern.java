package org.oasis.easy.orm.expression.impl;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.expression.*;
import org.oasis.easy.orm.utils.CacheUtils;

import java.util.Map;

/**
 * @author tianbo
 * @date 2019-01-03
 */
public class ExecPattern implements IExecPattern {

    /**
     * 语句的缓存:key是sql语句value是编译好的unit列表
     */
    private static final Map<String, IExecPattern> cache = CacheUtils.getSynchronizedLRUCache(500);

    /**
     * 编译的语句
     */
    private final String pattern;

    /**
     * 输出的单元
     */
    private final IExecUnit unit;

    public ExecPattern(String pattern, IExecUnit unit) {
        this.pattern = pattern;
        this.unit = unit;
    }

    /**
     * 从语句编译ExqlPattern对象
     *
     * @param pattern - 待编译的语句
     * @return ExqlPattern 对象
     */
    public static IExecPattern compile(String pattern) {
        // 从缓存中获取编译好的语句
        IExecPattern compiledPattern = cache.get(pattern);
        if (compiledPattern == null) {
            // 重新编译语句
            ExecCompiler compiler = new ExecCompiler(pattern);
            compiledPattern = compiler.compile();
            // 语句的缓存
            cache.put(pattern, compiledPattern);
        }

        return compiledPattern;
    }

    @Override
    public void execute(IExecContext context, Map<String, ?> map) throws EasyOrmException {
        doExecute(context, new ExprResolver(map));
    }

    private void doExecute(IExecContext context, IExprResolver exprResolver) {
        unit.fill(context, exprResolver);
    }
}
