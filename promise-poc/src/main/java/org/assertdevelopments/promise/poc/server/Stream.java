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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface for representing bi-directional streams.
 *
 * @author Stefan Bangels
 * @since 2015-02-27
 */
public interface Stream {

    /**
     * Get the request uri.
     *
     * @return the request uri
     */
    String getUri();

    /**
     * Get the request method.
     *
     * @return the request method
     */
    String getMethod();

    /**
     * Get the input stream, for reading bytes from the bi-directional stream. The returned input stream will be
     * managed by the implementation of the stream and by the owning process (that created the stream implementation),
     * so there is no need to close the input stream yourself (it has no effect whatsoever). The type of input stream
     * returned by this method, is abstract and implementation specific.
     *
     * @return the input stream
     */
    InputStream getInputStream();

    /**
     * Get the output stream, for writing bytes to the bi-directional stream. The returned output stream will be
     * managed by the implementation of the stream and by the owning process (that created the stream implementation),
     * so there is no need to close the input stream yourself (it has no effect whatsoever). The type of output stream
     * returned by this method, is abstract and implementation specific.
     *
     * @return the output stream
     */
    OutputStream getOutputStream();

    /**
     * Returns true if the stream is committed, false if not.
     *
     * @see #sendSuccess()
     * @see #sendError(int, String)
     * @return true if committed, false if not
     */
    boolean isCommitted();

    /**
     * Finish the stream and report to the requester that stream was successfully handled, meaning: the request was
     * successfully read from the stream, processed without any problems, and the response was successfully written to
     * the stream. After calling this method, reading/writing from/to the stream will no longer be possible.
     *
     * @throws IllegalStateException if the response was already committed
     * @see #sendError(int, String)
     * @see #isCommitted()
     */
    void sendSuccess() throws IllegalStateException;

    /**
     * Finish the stream and report to the requester that there were errors while handling the stream (errors during
     * reading from stream, processing the stream or writing to the stream).
     *
     * @param code    the error code
     * @param message the error message
     * @throws IllegalStateException if the response was already committed
     * @see #sendSuccess()
     * @see #isCommitted()
     */
    void sendError(int code, String message) throws IllegalStateException;

}
