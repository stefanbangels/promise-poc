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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Stefan Bangels
 * @since 2015-06-01
 */
public class StatusOutputStreamTestCase extends TestCase {

    public void testWrite() throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        StatusOutputStream out = new StatusOutputStream(result);
        out.writeStatus(new StreamStatus(StreamStatus.STATUS_OK, "OK"));
        out.writeStatus(new StreamStatus(StreamStatus.STATUS_ERROR, "ERROR"));
        out.close();

        assertTrue(Arrays.equals(result.toByteArray(), createSample()));
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
