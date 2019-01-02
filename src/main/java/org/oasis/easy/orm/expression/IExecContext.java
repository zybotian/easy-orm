package org.oasis.easy.orm.expression;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public interface IExecContext {
    /**
     * 填充文本
     */
    void fillText(String var);

    /**
     * 填充变量
     */
    void fillValue(Object obj);

    /**
     * 获取参数
     */
    Object[] getParams();

    /**
     * 获取内容
     */
    String getContent();
}
