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

package org.assertdevelopments.promise.poc.core.protocol.status;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream for reading the stream status from an input stream.
 *
 * @author Stefan Bangels
 * @see StreamStatus
 * @see StatusOutputStream
 * @since 2015-02-25
 */
public final class StatusInputStream extends DataInputStream {

    public StatusInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public StreamStatus readStatus() throws IOException {
        StreamStatus status = new StreamStatus();
        status.setStatusCode(readInt());
        status.setStatusMessage(readUTF());
        return status;
    }

}
