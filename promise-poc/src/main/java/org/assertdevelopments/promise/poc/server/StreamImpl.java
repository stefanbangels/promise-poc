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

package org.assertdevelopments.promise.poc.server;

import org.assertdevelopments.promise.poc.core.exceptions.HttpStreamException;
import org.assertdevelopments.promise.poc.core.protocol.StreamInputStream;
import org.assertdevelopments.promise.poc.core.protocol.StreamOutputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementation for a full duplex stream (reading and writing at the same time).
 *
 * @author Stefan Bangels
 * @see Stream
 * @since 2015-07-10
 */
final class StreamImpl implements Stream {

    private final Logger logger = Logger.getLogger(getClass());

    private final String uri;
    private final String method;
    private final StreamInputStream streamInputStream;
    private final StreamOutputStream streamOutputStream;

    private boolean committed;

    /**
     * Default constructor that instantiates a full duplex stream for the provided uri and http method, and wraps
     * it around the provided input- and output stream.
     *
     * @param uri the uri
     * @param method the http Method
     * @param inputStream  the input stream
     * @param outputStream the output stream
     */
    StreamImpl(String uri, String method, InputStream inputStream, OutputStream outputStream) {
        this.uri = uri;
        this.method = method;
        this.streamInputStream = new StreamInputStream(inputStream);
        this.streamOutputStream = new StreamOutputStream(outputStream);
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public InputStream getInputStream() {
        return streamInputStream;
    }

    public OutputStream getOutputStream() {
        return streamOutputStream;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void sendSuccess() {
        if (isCommitted()) {
            throw new IllegalStateException("stream already committed");
        }
        logger.info("sending stream status: success...");
        sendStatus(StreamStatus.STATUS_OK, "OK");
    }

    public void sendError(int code, String message) {
        if (isCommitted()) {
            throw new IllegalStateException("stream already committed");
        }
        logger.warn("sending stream status: error (code=" + code + ", message=" + message + ")...");
        sendStatus(code, message);
    }

    /**
     * Finish the stream and write the stream status (code and message) to the stream output stream. After calling this
     * method, reading/writing from/to the stream will no longer be possible.
     *
     * @param code    the status code
     * @param message the status message
     */
    private void sendStatus(int code, String message) {
        try {
            // mark committed
            committed = true;

            // write eof and status
            streamOutputStream.writeStatus(code, message);
        } catch (IOException e) {
            throw new HttpStreamException("error while sending stream status", e);
        }
    }

}
