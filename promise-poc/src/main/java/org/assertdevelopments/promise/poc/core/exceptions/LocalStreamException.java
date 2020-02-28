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
 * Local stream exception. An unchecked exception thrown when there's a local (the process that has made the stream
 * request) exception while generating the stream request.
 *
 * @author Stefan Bangels
 * @since 2015-07-13
 */
public final class LocalStreamException extends StreamException {

    public LocalStreamException() {
    }

    public LocalStreamException(String message) {
        super(message);
    }

    public LocalStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalStreamException(Throwable cause) {
        super(cause);
    }

}
