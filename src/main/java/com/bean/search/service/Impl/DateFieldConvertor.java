package com.bean.search.service.Impl;

import com.bean.search.entity.FieldMeta;
import com.bean.search.service.FieldConvertor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.util.Date;

/**
 * 日期字段转换器
 *
 * @author hwj
 * @since v3.1.0
 */
@Service
public class DateFieldConvertor implements FieldConvertor.BFieldConvertor {
    /**
     * 时区
     */
    private ZoneId zoneId = ZoneId.systemDefault();

    @Override
    public boolean supports(FieldMeta meta, Class<?> valueType, Class<?> targetType) {
        return (
                valueType == Date.class ||
                        valueType == java.sql.Date.class ||
                        valueType == Timestamp.class ||
                        valueType == LocalDateTime.class ||
                        valueType == LocalDate.class)
                && (
                targetType == Date.class ||
                        targetType == java.sql.Date.class ||
                        targetType == Timestamp.class ||
                        targetType == LocalDateTime.class ||
                        targetType == LocalDate.class
        );
    }

    @Override
    public Object convert(FieldMeta meta, Object value, Class<?> targetType) {
        Class<?> valueType = value.getClass();
        if (Date.class.isAssignableFrom(valueType)) {
            Date date = (Date) value;
            if (targetType == java.sql.Date.class) {
                return new java.sql.Date(date.getTime());
            }
            if (targetType == Timestamp.class) {
                return new Timestamp(date.getTime());
            }
            if (targetType == LocalDateTime.class) {
                return LocalDateTime.ofInstant(date.toInstant(), zoneId);
            }
            if (targetType == LocalDate.class) {
                return LocalDateTime.ofInstant(date.toInstant(), zoneId);
            }
        }
        LocalDateTime dateTime;
        if (valueType == LocalDateTime.class) {
            dateTime = (LocalDateTime) value;
        } else {
            dateTime = LocalDateTime.of((LocalDate) value, LocalTime.of(0, 0));
        }
        if (targetType == LocalDateTime.class) {
            return dateTime;
        }
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        if (targetType == Date.class) {
            return new Date(instant.toEpochMilli());
        }
        if (targetType == java.sql.Date.class) {
            return new java.sql.Date(instant.toEpochMilli());
        }
        if (targetType == Timestamp.class) {
            return new Timestamp(instant.toEpochMilli());
        }
        if (targetType == LocalDate.class) {
            return LocalDateTime.ofInstant(instant, zoneId);
        }
        throw new UnsupportedOperationException();
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

}
