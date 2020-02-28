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

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Stefan Bangels
 * @since 2015-06-01
 */
public class StatusInputStreamTestCase extends TestCase {

    public void testRead() throws IOException {
        StatusInputStream inputStream = new StatusInputStream(new ByteArrayInputStream(createSample()));

        // read ok
        StreamStatus status = inputStream.readStatus();
        assertEquals(StreamStatus.STATUS_OK, status.getStatusCode());
        assertEquals("OK", status.getStatusMessage());

        // read error
        status = inputStream.readStatus();
        assertEquals(StreamStatus.STATUS_ERROR, status.getStatusCode());
        assertEquals("ERROR", status.getStatusMessage());

        inputStream.close();
    }

    private static byte[] createSample() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream writer = new DataOutputStream(out);
        writer.writeInt(StreamStatus.STATUS_OK);
        writer.writeUTF("OK");
        writer.writeInt(StreamStatus.STATUS_ERROR);
        writer.writeUTF("ERROR");
        return out.toByteArray();
    }

}
