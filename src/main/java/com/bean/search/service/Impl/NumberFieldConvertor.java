package com.bean.search.service.Impl;

import com.bean.search.entity.FieldMeta;
import com.bean.search.service.FieldConvertor;
import org.springframework.stereotype.Service;

/**
 * [数字 to 数字] 字段转换器
 * 与 {@link DefaultBeanReflector } 配合使用
 * @author hwj
 * @since v3.0.0
 */
@Service
public class NumberFieldConvertor implements FieldConvertor.BFieldConvertor {

    @Override
    public boolean supports(FieldMeta meta, Class<?> valueType, Class<?> targetType) {
        return Number.class.isAssignableFrom(valueType) && (
                targetType == int.class || targetType == Integer.class ||
                        targetType == long.class || targetType == Long.class ||
                        targetType == float.class || targetType == Float.class ||
                        targetType == double.class || targetType == Double.class ||
                        targetType == short.class || targetType == Short.class ||
                        targetType == byte.class || targetType == Byte.class
        );
    }

    @Override
    public Object convert(FieldMeta meta, Object value, Class<?> targetType) {
        Number number = (Number) value;
        if (targetType == int.class || targetType == Integer.class) {
            return number.intValue();
        }
        if (targetType == long.class || targetType == Long.class) {
            return number.longValue();
        }
        if (targetType == float.class || targetType == Float.class) {
            return number.floatValue();
        }
        if (targetType == double.class || targetType == Double.class) {
            return number.doubleValue();
        }
        if (targetType == short.class || targetType == Short.class) {
            return number.shortValue();
        }
        if (targetType == byte.class || targetType == Byte.class) {
            return number.byteValue();
        }
        throw new UnsupportedOperationException();
    }
}
