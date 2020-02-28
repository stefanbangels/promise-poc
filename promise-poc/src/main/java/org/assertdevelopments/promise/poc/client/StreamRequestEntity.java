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

package org.assertdevelopments.promise.poc.client;

import org.assertdevelopments.promise.poc.core.exceptions.LocalStreamException;
import org.assertdevelopments.promise.poc.core.protocol.StreamConstants;
import org.assertdevelopments.promise.poc.core.protocol.StreamOutputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Stefan Bangels
 * @since 2015-02-27
 */
final class StreamRequestEntity extends BasicHttpEntity {

    private final Logger logger = Logger.getLogger(getClass());

    private final StreamRequest request;

    StreamRequestEntity(StreamRequest request) {
        this.request = request;
        setContentType(StreamConstants.CONTENT_TYPE);
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        // write data
        StreamOutputStream chunkOutputStream = new StreamOutputStream(outputStream);
        try {
            request.writeEntity(chunkOutputStream);
            logger.info("sending stream status: success...");
            chunkOutputStream.writeStatus(StreamStatus.STATUS_OK, "OK");
        } catch (Throwable t) {
            logger.warn("sending stream status: error (code=" + StreamStatus.STATUS_ERROR + ", message=" + t.getMessage() + ")...");
            chunkOutputStream.writeStatus(StreamStatus.STATUS_ERROR, t.getMessage());
            throw new LocalStreamException("error while handling stream request", t);
        }
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return true;
    }

}
