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

import org.assertdevelopments.promise.poc.entities.builder.EntityBuilder;
import org.assertdevelopments.promise.poc.entities.builder.EntityBuilderFactory;
import org.assertdevelopments.promise.poc.entities.builder.SkipEntityBuilderFactory;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Stefan Bangels
 * @since 2015-02-27
 */
public class EntityInputStream<T> extends FilterInputStream {

    private static final SkipEntityBuilderFactory SKIP_ENTITY_BUILDER_FACTORY = new SkipEntityBuilderFactory();

    private final EntityBuilderFactory<T> entityBuilderFactory;

    private final DataInputStream inputStream;

    public EntityInputStream(InputStream inputStream, EntityBuilderFactory<T> entityBuilderFactory) {
        super(inputStream);
        this.inputStream = new DataInputStream(inputStream);
        this.entityBuilderFactory = entityBuilderFactory;
    }

    public final T readEntity() throws IOException {
        return readEntity(entityBuilderFactory);
    }

    public final boolean skipEntity() throws IOException {
        return readEntity(SKIP_ENTITY_BUILDER_FACTORY) != null;
    }

    private <E> E readEntity(EntityBuilderFactory<E> entityBuilderFactory) throws IOException {
        int type = inputStream.read();
        if (type == -1) {
            // eof, no more entities to read
            return null;
        }

        EntityBuilder<E> builder = entityBuilderFactory.createEntityBuilder();
        if (type == ValueTypes.ETX) {
            // entity has no field values
            return builder.getEntity();
        } else {
            // read field values
            int depth = 0;
            while (true) {
                String field = inputStream.readUTF();
                if (type == ValueTypes.NULL) {
                    builder.setValue(field, null);
                } else if (type == ValueTypes.STRING) {
                    builder.setValue(field, inputStream.readUTF());
                } else if (type == ValueTypes.BYTE) {
                    builder.setValue(field, inputStream.readByte());
                } else if (type == ValueTypes.SHORT) {
                    builder.setValue(field, inputStream.readShort());
                } else if (type == ValueTypes.INTEGER) {
                    builder.setValue(field, inputStream.readInt());
                } else if (type == ValueTypes.LONG) {
                    builder.setValue(field, inputStream.readLong());
                } else if (type == ValueTypes.FLOAT) {
                    builder.setValue(field, inputStream.readFloat());
                } else if (type == ValueTypes.DOUBLE) {
                    builder.setValue(field, inputStream.readDouble());
                } else if (type == ValueTypes.BOOLEAN) {
                    builder.setValue(field, inputStream.readBoolean());
                } else if (type == ValueTypes.DATE) {
                    builder.setValue(field, new Date(inputStream.readLong()));
                } else if (type == ValueTypes.BIG_DECIMAL) {
                    builder.setValue(field, new BigDecimal(inputStream.readUTF()));
                } else if (type == ValueTypes.ENTITY) {
                    builder.startEntityValue(field);
                    depth++;
                } else {
                    throw new IOException("unknown field type, field: " + field + ", type: " + type);
                }

                // read next byte and check ETX
                while (true) {
                    type = inputStream.readUnsignedByte();
                    if (type != ValueTypes.ETX) {
                        // received new field value
                        break;
                    } else if (depth > 0) {
                        // close inner entity
                        depth--;
                        builder.endValue();
                    } else {
                        // close root entity
                        return builder.getEntity();
                    }
                }
            }
        }
    }

}
