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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Stefan Bangels
 * @since 2015-02-03
 */
public class ChunkInputStreamTestCase extends TestCase {

    public static final int MAX_CHUNK_SIZE = 512;

    public void testRead() throws IOException {
        ChunkInputStream inputStream = new ChunkInputStream(new ByteArrayInputStream(createSample()), MAX_CHUNK_SIZE);
        assertEquals(0, inputStream.available());
        assertEquals(0x01, inputStream.read());
        assertEquals(0, inputStream.available());
        assertEquals(0x02, inputStream.read());
        assertEquals(1, inputStream.available());
        assertEquals(0x03, inputStream.read());
        assertEquals(0, inputStream.available());
        assertEquals(0x04, inputStream.read());
        assertEquals(2, inputStream.available());
        assertEquals(0x05, inputStream.read());
        assertEquals(1, inputStream.available());
        assertEquals(0x06, inputStream.read());
        assertEquals(0, inputStream.available());
        assertEquals(0x07, inputStream.read());
        assertEquals(3, inputStream.available());
        assertEquals(0x08, inputStream.read());
        assertEquals(2, inputStream.available());
        assertEquals(0x09, inputStream.read());
        assertEquals(1, inputStream.available());
        assertEquals(0xFF, inputStream.read());
        assertEquals(0, inputStream.available());
        assertEquals(-1, inputStream.read());
        assertEquals(0, inputStream.available());
        assertEquals(-1, inputStream.read());
        assertEquals(0, inputStream.available());
        inputStream.close();
    }

    public void testReadBytes() throws IOException {
        ChunkInputStream inputStream = new ChunkInputStream(new ByteArrayInputStream(createSample()), MAX_CHUNK_SIZE);
        byte[] buffer = new byte[4];
        assertEquals(0, inputStream.available());
        assertEquals(4, inputStream.read(buffer));
        assertTrue(Arrays.equals(buffer, new byte[]{0x01, 0x02, 0x03, 0x04}));
        buffer = new byte[3];
        assertEquals(2, inputStream.available());
        assertEquals(3, inputStream.read(buffer));
        assertTrue(Arrays.equals(buffer, new byte[]{0x05, 0x06, 0x07}));
        buffer = new byte[2];
        assertEquals(3, inputStream.available());
        assertEquals(2, inputStream.read(buffer));
        assertTrue(Arrays.equals(buffer, new byte[]{0x08, 0x09}));
        buffer = new byte[2];
        assertEquals(1, inputStream.available());
        assertEquals(1, inputStream.read(buffer));
        assertTrue(Arrays.equals(buffer, new byte[]{(byte) 0xFF, 0x00}));
        buffer = new byte[2];
        assertEquals(0, inputStream.available());
        assertEquals(-1, inputStream.read(buffer));
        assertTrue(Arrays.equals(buffer, new byte[]{0x00, 0x00}));
        buffer = new byte[2];
        assertEquals(0, inputStream.available());
        assertEquals(-1, inputStream.read(buffer));
        assertTrue(Arrays.equals(buffer, new byte[]{0x00, 0x00}));
    }

    private static byte[] createSample() {
        return new byte[]{
                0x00, 0x01, /* chunk-size 1 */
                0x01, /* chunk 1 */
                0x00, 0x02, /* chunk-size 2 */
                0x02, 0x03, /* chunk 2 */
                0x00, 0x03, /* chunk-size 3 */
                0x04, 0x05, 0x06, /* chunk 3 */
                0x00, 0x04, /* chunk-size 4 */
                0x07, 0x08, 0x09, (byte) 0xFF, /* chunk 4 */
                0x00, 0x00 /* chunk-eof */
        };
    }


}
