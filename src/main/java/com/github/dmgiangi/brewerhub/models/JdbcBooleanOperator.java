package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.models.entity.WeightUnits;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum JdbcBooleanOperator {
    AND("AND "),
    OR("OR ");

    @Getter
    private final String value;

    public static JdbcBooleanOperator fromValue(String input) {
        for (JdbcBooleanOperator b : JdbcBooleanOperator.values()) {
            if (b.value.equalsIgnoreCase(input)) {
                return b;
            }
        }
        return null;
    }

}
