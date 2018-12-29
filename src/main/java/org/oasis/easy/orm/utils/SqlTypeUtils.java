package org.oasis.easy.orm.utils;

import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author tianbo
 * @date 2018-12-21
 */
public class SqlTypeUtils {

    private static final String[] QUERY_PATTERNS = {
            "^\\s*SELECT.*",
            "^\\s*GET.*",
            "^\\s*FIND.*",
            "^\\s*QUERY.*",
            "^\\s*COUNT.*",
            "^\\s*READ.*",
            "^\\s*SHOW.*",
            "^\\s*DESC.*",
            "^\\s*DESCRIBE.*"};
    private static final List<Pattern> SELECT_PATTERNS = new ArrayList<>(QUERY_PATTERNS.length);

    static {
        for (String pattern : QUERY_PATTERNS) {
            SELECT_PATTERNS.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
        }
    }


    public static boolean matchQuery(String... args) {
        if (ObjectUtils.isEmpty(args)) {
            throw new EasyOrmException(ErrorCode.INVALID_PARAM, "invalid matching select parameters");
        }
        for (Pattern pattern : SELECT_PATTERNS) {
            for (String arg : args) {
                if (pattern.matcher(arg).find()) {
                    return true;
                }
            }
        }

        return false;
    }
}
