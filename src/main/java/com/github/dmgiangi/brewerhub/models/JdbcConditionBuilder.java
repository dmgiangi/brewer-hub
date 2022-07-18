package com.github.dmgiangi.brewerhub.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JdbcConditionBuilder {
    List<String> conditionList = new ArrayList<>();
    JdbcBooleanOperator concatenation;

    public JdbcConditionBuilder(JdbcBooleanOperator concatenation) {
        this.concatenation = concatenation;
    }

    public JdbcConditionBuilder addCondition(String condition){
        if(condition != null && !"".equals(condition))
            conditionList.add(condition);
        return this;
    }

    public String getCondition(){
        StringBuilder stringBuilder = new StringBuilder();

        if(conditionList.size() != 0){
            for (String condition : conditionList) {
                stringBuilder
                        .append(concatenation.getValue())
                        .append(condition)
                        .append(" ");
            }
        }

        if (!stringBuilder.isEmpty()){
            return stringBuilder.toString()
                    .replaceFirst(concatenation.getValue(), " ");
        } else return "";
    }

    public JdbcConditionBuilder addCondition(String condition, Float floatValue) {
        if(floatValue != null)
            conditionList.add(condition.replaceAll("\\?", floatValue.toString()));
        return this;
    }
    public JdbcConditionBuilder addCondition(String condition, String stringValue) {
        if(stringValue != null && !"".equals(stringValue))
            conditionList.add(condition.replaceAll("\\?", stringValue));
        return this;
    }

    public JdbcConditionBuilder addIntCondition(String condition, String intValue) {
        if(intValue != null && !"".equals(intValue)){
            try {
                int value = Integer.parseInt(intValue);
                conditionList.add(condition.replaceAll("\\?", String.valueOf(value)));
            } catch (NumberFormatException e) {
                //TODO menage error
            }
        }

        return this;
    }

    public String getConditionWithParenthesis() {
        String condition = getCondition();
        if(!"".equals(condition))
            return "(" + condition + ")";
        return "";
    }

    public JdbcConditionBuilder addDateCondition(String condition, String date, String format) {
        if(date != null && !"".equals(date)){
            try{
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new SimpleDateFormat(format)
                                .parse(date));
                conditionList.add(condition.replaceAll("\\?", formattedDate));
            } catch (ParseException e) {
                //TODO MENAGE ERROR
            }
        }
        return this;
    }
}
