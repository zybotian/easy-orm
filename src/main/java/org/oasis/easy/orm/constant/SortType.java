package org.oasis.easy.orm.constant;

/**
 * @author tianbo
 * @date 2019-01-07
 */
public enum SortType {
    NONE(""),
    ASC("ASC"),
    DESC("DESC"),;

    private String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
