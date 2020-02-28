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

package org.assertdevelopments.promise.poc.core.protocol;

import org.assertdevelopments.promise.poc.core.protocol.chunk.ChunkOutputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StatusOutputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Stefan Bangels
 * @since 2015-07-09
 */
public final class StreamOutputStream extends OutputStream {

    private final ChunkOutputStream chunkOutputStream;
    private final StatusOutputStream statusOutputStream;

    public StreamOutputStream(OutputStream outputStream) {
        this.chunkOutputStream = new ChunkOutputStream(outputStream, StreamConstants.MAX_CHUNK_SIZE);
        this.statusOutputStream = new StatusOutputStream(outputStream);
    }

    @Override
    public void write(int n) throws IOException {
        chunkOutputStream.write(n);
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public void write(byte[] bytes) throws IOException {
        chunkOutputStream.write(bytes);
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public void write(byte[] bytes, int off, int len) throws IOException {
        chunkOutputStream.write(bytes, off, len);
    }

    public final void writeStatus(int code, String message) throws IOException {
        writeStatus(new StreamStatus(code, message));
    }

    private void writeStatus(StreamStatus status) throws IOException {
        // write eof
        chunkOutputStream.writeEOF();

        // write status
        statusOutputStream.writeStatus(status);
        statusOutputStream.flush();
    }

    @Override
    public void flush() throws IOException {
        chunkOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }

}
