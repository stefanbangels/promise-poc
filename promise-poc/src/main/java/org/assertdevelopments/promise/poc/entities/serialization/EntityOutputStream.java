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

package org.assertdevelopments.promise.poc.entities.serialization;

import org.assertdevelopments.promise.poc.entities.inspector.EntityHandler;
import org.assertdevelopments.promise.poc.entities.inspector.EntityInspector;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Stefan Bangels
 * @since 2015-02-27
 */
public class EntityOutputStream<T> extends FilterOutputStream {

    private final DataOutputStream outputStream;
    private final EntityInspector<T> entityInspector;

    public EntityOutputStream(OutputStream outputStream, EntityInspector<T> entityInspector) {
        super(outputStream);
        this.outputStream = new DataOutputStream(outputStream);
        this.entityInspector = entityInspector;
    }

    public final void writeEntity(T entity) throws IOException {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }

        // inspect entity field values
        entityInspector.inspect(entity, new EntityHandler() {
            public void handleValue(String field, Object value) throws IOException {
                if (value == null) {
                    outputStream.writeByte(ValueTypes.NULL);
                    outputStream.writeUTF(field);
                } else if (value instanceof String) {
                    outputStream.writeByte(ValueTypes.STRING);
                    outputStream.writeUTF(field);
                    outputStream.writeUTF((String) value);
                } else if (value instanceof Byte) {
                    outputStream.writeByte(ValueTypes.BYTE);
                    outputStream.writeUTF(field);
                    outputStream.writeByte((Byte) value);
                } else if (value instanceof Short) {
                    outputStream.writeByte(ValueTypes.SHORT);
                    outputStream.writeUTF(field);
                    outputStream.writeShort((Short) value);
                } else if (value instanceof Integer) {
                    outputStream.writeByte(ValueTypes.INTEGER);
                    outputStream.writeUTF(field);
                    outputStream.writeInt((Integer) value);
                } else if (value instanceof Long) {
                    outputStream.writeByte(ValueTypes.LONG);
                    outputStream.writeUTF(field);
                    outputStream.writeLong((Long) value);
                } else if (value instanceof Float) {
                    outputStream.writeByte(ValueTypes.FLOAT);
                    outputStream.writeUTF(field);
                    outputStream.writeFloat((Float) value);
                } else if (value instanceof Double) {
                    outputStream.writeByte(ValueTypes.DOUBLE);
                    outputStream.writeUTF(field);
                    outputStream.writeDouble((Double) value);
                } else if (value instanceof Boolean) {
                    outputStream.writeByte(ValueTypes.BOOLEAN);
                    outputStream.writeUTF(field);
                    outputStream.writeBoolean((Boolean) value);
                } else if (value instanceof Date) {
                    outputStream.writeByte(ValueTypes.DATE);
                    outputStream.writeUTF(field);
                    outputStream.writeLong(((Date) value).getTime());
                } else if (value instanceof BigDecimal) {
                    outputStream.writeByte(ValueTypes.BIG_DECIMAL);
                    outputStream.writeUTF(field);
                    outputStream.writeUTF(String.valueOf(value));
                } else {
                    throw new IOException("field " + field + " has an unknown value type: " + value.getClass());
                }
            }

            public void handleStartEntityValue(String field) throws IOException {
                outputStream.writeByte(ValueTypes.ENTITY);
                outputStream.writeUTF(field);
            }

            public void handleEndValue() throws IOException {
                outputStream.writeByte(ValueTypes.ETX);
            }
        });

        // close entity
        outputStream.writeByte(ValueTypes.ETX);
    }

}
