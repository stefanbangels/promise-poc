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

package org.assertdevelopments.promise.poc.entities.stream;

import org.assertdevelopments.promise.poc.entities.objects.GenericEntity;
import org.assertdevelopments.promise.poc.entities.serialization.GenericEntityInputStream;
import org.assertdevelopments.promise.poc.entities.serialization.GenericEntityOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Stefan Bangels
 * @since 2015-08-14
 */
public final class EntityStream {

    private final GenericEntityInputStream inputStream;
    private final GenericEntityOutputStream outputStream;

    public EntityStream(
            InputStream inputStream, OutputStream outputStream) {
        this.inputStream = new GenericEntityInputStream(inputStream);
        this.outputStream = new GenericEntityOutputStream(outputStream);
    }

    public GenericEntity newEntity() {
        return new GenericEntity();
    }

    public GenericEntity readEntity() throws IOException {
        return inputStream.readEntity();
    }

    public boolean skipEntity() throws IOException {
        return inputStream.skipEntity();
    }

    public void writeEntity(GenericEntity entity) throws IOException {
        outputStream.writeEntity(entity);
    }

}
