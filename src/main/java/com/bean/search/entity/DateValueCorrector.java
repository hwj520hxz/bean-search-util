package com.bean.search.entity;

import com.bean.search.entity.param.Operator;

import java.util.regex.Pattern;

/**
 * @author ：hwj
 * @version 版本号：V1.0
 * @Description ：日期参数值矫正器
 */
public class DateValueCorrector {
    // 预编译正则表达式
    static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}"); //4个数字-2个数字-2个数字
    static final Pattern DATE_HOUR_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}"); //4个数字-2个数字-2个数字 空格 2个数字
    static final Pattern DATE_MINUTE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}"); // 4个数字-2个数字-2个数字 空格 2个数字：2个数字

    public DateValueCorrector() {
    }

    /**
     * 日期参数值矫正处理
     * @param dateValues 参数值
     * @param operator 字段运算符
     * @return 矫正后的日期参数
     */
    public Object[] correct(Object[] dateValues, Operator operator) {
        int i;
        switch(operator) {
                // 小于
            case LessThan:
                // 大于等于 则拼接成（YYYY-MM-DD 00:00:00）
            case GreaterEqual:
                for(i = 0; i < dateValues.length; ++i) {
                    dateValues[i] = this.dateValue(dateValues[i], true);
                }

                return dateValues;
            case LessEqual:
                // 大于
            case GreaterThan:
                for(i = 0; i < dateValues.length; ++i) {
                    dateValues[i] = this.dateValue(dateValues[i], false);
                }

                return dateValues;
            case Between:
                if (dateValues.length > 0) {
                    dateValues[0] = this.dateValue(dateValues[0], true);
                }

                if (dateValues.length > 1) {
                    dateValues[1] = this.dateValue(dateValues[1], false);
                }
        }

        return dateValues;
    }

    /**
     *
     * @param value 日期值
     * @param roundDown 是否含等于
     * 大于等于某日则拼接成 YYYY-MM-DD 00:00:00  大于等于某时则拼接成 YYYY-MM-DD XX:00:00  大于等于某分则拼接成 YYYY-MM-DD XX:XX:00
     * 大于某日则拼接成 YYYY-MM-DD 23:59:59  大于某时则拼接成 YYYY-MM-DD XX:59:59  大于某分则拼接成 YYYY-MM-DD XX:XX:59
     **/
    protected Object dateValue(Object value, boolean roundDown) {
        if (value instanceof String) {
            String strValue = (String)value;
            if (DATE_PATTERN.matcher(strValue).matches()) { // 日期是否符合（YYYY-MM-DD）
                if (roundDown) {
                    return strValue + " 00:00:00";
                }
                return strValue + " 23:59:59";
            }

            if (DATE_HOUR_PATTERN.matcher(strValue).matches()) { // 日期是否符合（YYYY-MM-DD HH）
                if (roundDown) {
                    return strValue + ":00:00";
                }
                return strValue + ":59:59";
            }

            if (DATE_MINUTE_PATTERN.matcher(strValue).matches()) {
                if (roundDown) {
                    return strValue + ":00";
                }

                return strValue + ":59";
            }
        }

        return value;
    }


}
