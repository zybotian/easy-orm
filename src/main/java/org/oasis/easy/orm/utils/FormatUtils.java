package org.oasis.easy.orm.utils;

import com.google.common.base.CaseFormat;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public class FormatUtils {

    /**
     * 将以下划线分割的字符串转换为小驼峰命名
     *
     * @param source 输入示例 my_first_loan
     * @return 输出示例 myFirstLoan
     */
    public static String lowerUnderScoreToLowerCamel(String source) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, source);
    }

    /**
     * 将以下划线分割的字符串转换为大驼峰命名
     *
     * @param source 输入示例 my_first_loan
     * @return 输出示例 MyFirstLoan
     */
    public static String lowerUnderscoreToUpperCamel(String source) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, source);
    }

    /**
     * 将小驼峰命名的字符串转成以下划线分割的字符串
     *
     * @param source 输入示例 myFirstLoan
     * @return 输出示例 my_first_loan
     */
    public static String lowerCamelToLowerUnderScore(String source) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, source);
    }

    /**
     * 将大驼峰命名的字符串转成以下划线分割的字符串
     *
     * @param source 输入示例 MyFirstLoan
     * @return 输出示例 my_first_loan
     */
    public static String upperCamelToLowerUnderScore(String source) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, source);
    }

}
