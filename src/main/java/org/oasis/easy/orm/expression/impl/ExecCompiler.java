package org.oasis.easy.orm.expression.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.expression.IExecUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tianbo
 * @date 2019-01-02
 */
public class ExecCompiler {

    private static final char BRACE_LEFT = '(';

    private static final char BRACE_RIGHT = ')';

    private static final char BLOCK_LEFT = '{';

    private static final char BLOCK_RIGHT = '}';

    private static final String SHARP = "#";

    private static final String KEYWORD_IF = "if";

    private static final String SHARP_ELSE = "#else";

    /**
     * sql语句中关键字的正则表达式
     */
    private static final Pattern PATTERN_KEYWORD = Pattern.compile("([\\:][a-zA-Z0-9_\\.]+)|#(#|if)?");

    /**
     * 空的输出单元
     */
    private static final EmptyUnit EMPTY_UNIT = new EmptyUnit();

    /**
     * 待编译的语句
     */
    private final String pattern;

    /**
     * 待编译的语句的长度
     */
    private final int length;

    /**
     * 正则表达式匹配的当前位置
     */
    private int position = 0;

    public ExecCompiler(String pattern) {
        this.pattern = pattern;
        this.length = pattern.length();
    }

    public IExecUnit compile() {
        if (StringUtils.isEmpty(pattern)) {
            throw new EasyOrmException(ErrorCode.MISSING_PARAM, "pattern is missing");
        }
        return compileUnit();
    }

    /**
     * 将sql语句转成各种类型的小输出单元
     *
     * @see BunchUnit
     * @see ChoiceUnit
     * @see EmptyUnit
     * @see ExprUnit
     * @see JoinUnit
     * @see TextUnit
     */
    private IExecUnit compileUnit() {
        Matcher matcher = PATTERN_KEYWORD.matcher(pattern);
        List<IExecUnit> units = new ArrayList<>();
        int fromIndex = 0;
        while ((position < length) && matcher.find(position)) {
            position = matcher.end();
            String expr = matcher.group(1);
            if (expr != null) {
                if (matcher.start() > fromIndex) {
                    // 创建文本输出单元
                    units.add(new TextUnit(pattern.substring(fromIndex, matcher.start())));
                }

                // 创建:expr形式的表达式输出单元
                units.add(new ExprUnit(expr));
                fromIndex = position;
                continue;
            }

            // 检查#后面的关键字
            String keyword = matcher.group(2);
            // 处理 ##(:expr) 形式的子句
            if (keyword.equals(SHARP)) {
                // 获取括号内的内容
                expr = findBrace(BRACE_LEFT, BRACE_RIGHT);
                if (expr != null) {
                    if (matcher.start() > fromIndex) {
                        // 创建文本子句
                        units.add(new TextUnit(pattern.substring(fromIndex, matcher.start())));
                    }

                    // 创建 ##(:expr) 形式的表达式
                    units.add(new JoinUnit(expr));
                    fromIndex = position;
                }
            }
            // 处理  #if(:expr) {...} #else {...} 形式的子句
            else if (keyword.equals(KEYWORD_IF)) {
                // 获取括号内的内容
                expr = findBrace(BRACE_LEFT, BRACE_RIGHT);
                if (expr != null) {
                    // 编译  {...} 单元
                    IExecUnit unitIf = compileBlock();
                    if (unitIf != null) {
                        // 创建文本子句
                        if (matcher.start() > fromIndex) {
                            units.add(new TextUnit(pattern.substring(fromIndex, matcher.start())));
                        }

                        IExecUnit unitElse = null;
                        // 匹配  #else {...} 子句
                        if (match(SHARP_ELSE, position)) {
                            // 编译  {...} 单元
                            unitElse = compileBlock();
                        }

                        // 创建  #if(:expr) {...} #else {...} 形式的子句
                        units.add(new ChoiceUnit(expr, unitIf, unitElse));
                        fromIndex = position;
                    }
                }
            }
        }

        if (fromIndex < length) {
            // 匹配了正则表达式之后的剩余内容
            units.add(new TextUnit(pattern.substring(fromIndex)));
        }

        if (CollectionUtils.isEmpty(units)) {
            return EMPTY_UNIT;
        }
        if (units.size() == 1) {
            return units.get(0);
        }
        return new BunchUnit(units);
    }

    /**
     * 从指定起始位置查找左括号'('或'['或'{'
     *
     * @param chLeft    匹配的左括号
     * @param fromIndex 查找的起始位置
     * @return 左括号的下标(空格跳过), 未找到返回-1
     */
    private int findLeftBrace(char chLeft, int fromIndex) {
        for (int i = fromIndex; i < length; i++) {
            char ch = pattern.charAt(i);
            if (ch == chLeft) {
                return i;
            } else if (!Character.isWhitespace(ch)) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * 查找匹配的右括号:'{}', '[]', '()'等
     *
     * @param chLeft    匹配的左括号
     * @param chRight   匹配的右括号
     * @param fromIndex 查找的起始位置
     * @return 右括号的下标, 未找到返回-1
     */
    private int findRightBrace(char chLeft, char chRight, int fromIndex) {
        int level = 0;
        for (int index = fromIndex; index < length; index++) {
            char ch = pattern.charAt(index);
            if (ch == chLeft) {
                // 找到了左括号
                level++;
            } else if (ch == chRight) {
                // 找到了右括号
                if (level == 0) {
                    return index;
                }
                // 找到了右括号,但与左括号数量未能匹配
                level--;
            }
        }
        return -1;
    }

    /**
     * 从当前位置查找匹配的一对括号,返回括号内的内容
     * 如果有匹配的括号, 返回后的当前位置指向匹配的右括号后一个字符。
     *
     * @param chLeft  匹配的左括号
     * @param chRight 匹配的右括号
     * @return 返回括号内的内容, 没有括号返回null
     */
    private String findBrace(char chLeft, char chRight) {
        int left = findLeftBrace(chLeft, position);
        if (left >= position) {
            int start = left + 1;
            int end = findRightBrace(chLeft, chRight, start);
            if (end >= start) {
                // 当前位置修改为匹配的右括号后一个字符的下标
                position = end + 1;
                // 返回匹配的括号内的内容
                return pattern.substring(start, end);
            }
        }
        return null;
    }

    /**
     * 从当前位置查找匹配的{},并将{}内的内容编译成一个语句单元列表
     *
     * 如果有匹配的{},当前位置指向匹配的右括号后一个字符
     *
     * @return 语句单元列表, 没有{}返回 null
     */
    private IExecUnit compileBlock() {
        String group = findBrace(BLOCK_LEFT, BLOCK_RIGHT);
        if (group != null) {
            ExecCompiler compiler = new ExecCompiler(group);
            return compiler.compileUnit();
        }

        return null;
    }

    /**
     * 匹配指定的关键字
     *
     * 如果匹配成功, 当前位置修改为关键字最后一个字符之后
     *
     * @param keyword   匹配的关键字
     * @param fromIndex 查找的起始位置
     * @return true/false
     */
    private boolean match(String keyword, int fromIndex) {
        int match = 0;
        for (int i = fromIndex; i < length; i++) {
            char ch = pattern.charAt(i);
            if (!Character.isWhitespace(ch)) {
                match = i;
                break;
            }
        }

        for (int i = 0; i < keyword.length(); i++) {
            char ch = pattern.charAt(match);
            if (ch != keyword.charAt(i)) {
                return false;
            }
            match++;
        }

        position = match;
        return true;
    }

}
