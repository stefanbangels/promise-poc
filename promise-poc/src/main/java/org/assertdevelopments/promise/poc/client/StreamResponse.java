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

import org.assertdevelopments.promise.poc.core.exceptions.HttpStreamException;
import org.assertdevelopments.promise.poc.core.protocol.StreamInputStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Stefan Bangels
 * @since 2015-02-27
 */
public final class StreamResponse {

    private final Logger logger = Logger.getLogger(getClass());

    private final StreamInputStream streamInputStream;

    private final CloseableHttpResponse response;

    StreamResponse(CloseableHttpResponse response) throws IOException {
        this.response = response;
        this.streamInputStream = new StreamInputStream(response.getEntity().getContent());
    }

    public InputStream getInputStream() {
        return streamInputStream;
    }

    public void close() {
        logger.debug("closing stream...");
        try {
            // reads remaining bytes, reads and processes the request status
            streamInputStream.readRemaining();

            // close response
            logger.debug("closing response...");
            response.close();

            logger.info("closed stream.");
        } catch (IOException e) {
            throw new HttpStreamException("error while closing stream", e);
        }
    }

}
