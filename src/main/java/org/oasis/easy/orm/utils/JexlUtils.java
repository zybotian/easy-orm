package org.oasis.easy.orm.utils;

import org.apache.commons.jexl2.JexlEngine;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class JexlUtils {

    public static JexlEngine getJexl() {
        return JexlHolder.jexlEngine;
    }

    private static class JexlHolder {
        private static JexlEngine jexlEngine = new JexlEngine();

        static {
            jexlEngine.setDebug(false);
        }
    }
}
