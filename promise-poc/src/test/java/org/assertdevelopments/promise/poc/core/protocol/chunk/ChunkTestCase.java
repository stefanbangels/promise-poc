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

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author Stefan Bangels
 * @since 2015-06-26
 */
public class ChunkTestCase extends TestCase {

    public static final int MAX_CHUNK_SIZE = 512;

    public void testLifeCycle() throws IOException {
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        pipedOutputStream.connect(pipedInputStream);

        ChunkInputStream inputStream = new ChunkInputStream(pipedInputStream, MAX_CHUNK_SIZE);
        ChunkOutputStream outputStream = new ChunkOutputStream(pipedOutputStream, MAX_CHUNK_SIZE);
        for (int n = 0; n < 10000; n++) {
            outputStream.write(n);
            outputStream.flush();
            assertEquals((byte) n, (byte) inputStream.read());
        }
        outputStream.writeEOF();
        outputStream.flush();
        outputStream.close();
        assertEquals(-1, inputStream.read());
    }

}
