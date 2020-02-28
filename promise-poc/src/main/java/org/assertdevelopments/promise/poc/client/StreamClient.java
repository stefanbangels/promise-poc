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
import org.assertdevelopments.promise.poc.core.protocol.StreamConstants;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author Stefan Bangels
 * @since 2014-10-27
 */
public final class StreamClient {

    public static final String USER_AGENT = "Promise Stream Client/1.0";

    private final Logger logger = Logger.getLogger(getClass());

    private final CloseableHttpClient httpClient;

    private static StreamClient instance;

    public static synchronized StreamClient getInstance() {
        if (instance == null) {
            instance = new StreamClient();
        }
        return instance;
    }

    public StreamClient() {
        this.httpClient = HttpClients.createDefault();
    }

    public StreamResponse sendRequest(String uri) {
        return sendRequest(uri, null);
    }

    public StreamResponse sendRequest(String uri, StreamRequest request) {
        logger.info("opening stream for " + uri + "...");
        try {
            // create method
            HttpPost httpMethod = new HttpPost(uri);
            httpMethod.setHeader("User-Agent", USER_AGENT);
            httpMethod.setHeader("Accept", StreamConstants.CONTENT_TYPE);
            httpMethod.setHeader("Cache-Control", "no-cache");
            httpMethod.setHeader("Pragma", "no-cache");
            httpMethod.setHeader("Expires", "0");
            if (request != null) {
                httpMethod.setEntity(new StreamRequestEntity(request));
            }

            logger.info("streaming request...");
            CloseableHttpResponse response = httpClient.execute(httpMethod);

            // get status code
            int status = response.getStatusLine().getStatusCode();
            logger.debug("HTTP status code is " + status + ".");

            // check status code
            if (status != HttpStatus.SC_ACCEPTED) {
                throw new HttpStreamException("error while executing stream request, unexpected HTTP status code: " + status);
            }

            logger.info("streaming response...");
            return new StreamResponse(response);
        } catch (Throwable t) {
            throw new HttpStreamException("error while executing request for stream " + uri, t);
        }
    }

    public final void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new HttpStreamException("error while closing http client", e);
        }
    }

}
