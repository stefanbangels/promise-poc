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

package org.assertdevelopments.promise.poc.core.protocol.chunk;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Stefan Bangels
 * @since 2015-02-03
 */
public class ChunkOutputStreamTestCase extends TestCase {

    public void testWrite() throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ChunkOutputStream out = new ChunkOutputStream(result, 4);
        out.write(1);
        out.write(2);
        out.write(3);
        out.write(4);
        out.write(5);
        out.write(6);
        out.write(7);
        out.write(8);
        out.write(9);
        out.write(10);
        out.flush();
        out.write(11);
        out.writeEOF();

        try {
            out.write(1);
            fail();
        } catch (EOFException e) {
            // writing bytes past EOF causes an EOFException
        }

        try {
            out.writeEOF();
            fail();
        } catch (EOFException e) {
            // writing EOF past EOF causes an EOFException
        }

        out.close();

        assertTrue(Arrays.equals(result.toByteArray(), new byte[]{
                0x00, 0x04, /* chunk-size 1 */
                0x01, 0x02, 0x03, 0x04, /* chunk 1 */
                0x00, 0x04, /* chunk-size 2 */
                0x05, 0x06, 0x07, 0x08, /* chunk 2 */
                0x00, 0x02, /* chunk-size 3 */
                0x09, 0x0A, /* chunk 3 */
                0x00, 0x01, /* chunk-size 4 */
                0x0B, /* chunk 4 */
                0x00, 0x00 /* chunk-eof */
        }));
    }

}
