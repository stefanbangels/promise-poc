/*
 * Copyright 2015 Assert Developments
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.assertdevelopments.promise.poc.entities.objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Stefan Bangels
 * @since 2015-07-06
 */
public class GenericEntity implements Serializable {

    private final Map<String, Object> values = new HashMap<String, Object>();

    public final Set<String> getFields() {
        return values.keySet();
    }

    public final GenericEntity setNull(String field) {
        return doSetValue(field, null);
    }

    public final GenericEntity setString(String field, String value) {
        return doSetValue(field, value);
    }

    public final String getString(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof String)) {
            throw new IllegalStateException("value is not a string: " + value.getClass());
        }
        return (String) value;
    }

    public final GenericEntity setByte(String field, Byte value) {
        return doSetValue(field, value);
    }

    public final Byte getByte(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Byte)) {
            throw new IllegalStateException("value is not a byte: " + value.getClass());
        }
        return (Byte) value;
    }

    public final GenericEntity setShort(String field, Short value) {
        return doSetValue(field, value);
    }

    public final Short getShort(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Short)) {
            throw new IllegalStateException("value is not a short: " + value.getClass());
        }
        return (Short) value;
    }

    public final GenericEntity setInteger(String field, Integer value) {
        return doSetValue(field, value);
    }

    public final Integer getInteger(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Integer)) {
            throw new IllegalStateException("value is not an integer: " + value.getClass());
        }
        return (Integer) value;
    }

    public final GenericEntity setLong(String field, Long value) {
        return doSetValue(field, value);
    }

    public final Long getLong(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Long)) {
            throw new IllegalStateException("value is not a long: " + value.getClass());
        }
        return (Long) value;
    }

    public final GenericEntity setDouble(String field, Double value) {
        return doSetValue(field, value);
    }

    public final Double getDouble(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Double)) {
            throw new IllegalStateException("value is not a double: " + value.getClass());
        }
        return (Double) value;
    }

    public final GenericEntity setFloat(String field, Float value) {
        return doSetValue(field, value);
    }

    public final Float getFloat(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Float)) {
            throw new IllegalStateException("value is not a float: " + value.getClass());
        }
        return (Float) value;
    }

    public final GenericEntity setBoolean(String field, Boolean value) {
        return doSetValue(field, value);
    }

    public final Boolean getBoolean(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Boolean)) {
            throw new IllegalStateException("value is not a boolean: " + value.getClass());
        }
        return (Boolean) value;
    }

    public final GenericEntity setDate(String field, Date value) {
        return doSetValue(field, value);
    }

    public final Date getDate(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof Date)) {
            throw new IllegalStateException("value is not a date: " + value.getClass());
        }
        return (Date) value;
    }

    public final GenericEntity setBigDecimal(String field, BigDecimal value) {
        return doSetValue(field, value);
    }

    public final BigDecimal getBigDecimal(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof BigDecimal)) {
            throw new IllegalStateException("value is not a big decimal: " + value.getClass());
        }
        return (BigDecimal) value;
    }

    public final GenericEntity setEntity(String field, GenericEntity genericEntity) {
        return doSetValue(field, genericEntity);
    }

    public final boolean isEntity(String field) {
        Object value = doGetValue(field);
        return !(value != null && !(value instanceof GenericEntity));
    }

    public final GenericEntity getEntity(String field) {
        Object value = doGetValue(field);
        if (value != null && !(value instanceof GenericEntity)) {
            throw new IllegalStateException("value is not an entity: " + value.getClass());
        }
        return (GenericEntity) value;
    }

    public final GenericEntity setValue(String field, Object value) {
        if (value == null) {
            return setNull(field);
        } else if (value instanceof String) {
            return setString(field, (String) value);
        } else if (value instanceof Byte) {
            return setByte(field, (Byte) value);
        } else if (value instanceof Short) {
            return setShort(field, (Short) value);
        } else if (value instanceof Integer) {
            return setInteger(field, (Integer) value);
        } else if (value instanceof Long) {
            return setLong(field, (Long) value);
        } else if (value instanceof Double) {
            return setDouble(field, (Double) value);
        } else if (value instanceof Float) {
            return setFloat(field, (Float) value);
        } else if (value instanceof Boolean) {
            return setBoolean(field, (Boolean) value);
        } else if (value instanceof Date) {
            return setDate(field, (Date) value);
        } else if (value instanceof BigDecimal) {
            return setBigDecimal(field, (BigDecimal) value);
        } else if (value instanceof GenericEntity) {
            return setEntity(field, (GenericEntity) value);
        } else {
            throw new IllegalArgumentException("unsupported value type: " + value.getClass());
        }
    }

    public final Object getValue(String field) {
        return doGetValue(field);
    }

    private Object doGetValue(String field) {
        return values.get(field);
    }

    private GenericEntity doSetValue(String field, Object value) {
        values.put(field, value);
        return this;
    }

    @Override
    public final String toString() {
        return values.toString();
    }

}
