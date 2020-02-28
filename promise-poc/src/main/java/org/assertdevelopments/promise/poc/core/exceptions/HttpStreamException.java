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

package org.assertdevelopments.promise.poc.core.exceptions;

/**
 * HTTP stream exception. An unchecked exception thrown when there is an I/O- or protocol error while opening, closing,
 * reading or writing the stream.
 *
 * @author Stefan Bangels
 * @since 2015-07-13
 */
public final class HttpStreamException extends StreamException {

    public HttpStreamException() {
    }

    public HttpStreamException(String message) {
        super(message);
    }

    public HttpStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStreamException(Throwable cause) {
        super(cause);
    }

}
