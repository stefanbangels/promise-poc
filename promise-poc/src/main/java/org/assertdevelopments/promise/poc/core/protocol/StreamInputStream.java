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

import org.assertdevelopments.promise.poc.core.exceptions.RemoteStreamException;
import org.assertdevelopments.promise.poc.core.protocol.chunk.ChunkInputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StatusInputStream;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Stefan Bangels
 * @since 2015-07-09
 */
public final class StreamInputStream extends FilterInputStream {

    private final ChunkInputStream chunkInputStream;
    private final StatusInputStream statusInputStream;

    private boolean eof = false;

    public StreamInputStream(InputStream inputStream) {
        super(inputStream);
        this.chunkInputStream = new ChunkInputStream(inputStream, StreamConstants.MAX_CHUNK_SIZE);
        this.statusInputStream = new StatusInputStream(inputStream);
    }

    @Override
    public int read() throws IOException {
        int n = chunkInputStream.read();
        checkEOF(n);
        return n;
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public int read(byte[] bytes) throws IOException {
        int n = chunkInputStream.read(bytes);
        checkEOF(n);
        return n;
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public int read(byte[] bytes, int off, int len) throws IOException {
        int n = chunkInputStream.read(bytes, off, len);
        checkEOF(n);
        return n;
    }

    @Override
    public long skip(long length) throws IOException {
        long n = chunkInputStream.skip(length);
        checkEOF(n);
        return n;
    }

    private void checkEOF(long n) throws IOException {
        if (n == -1) {
            readRemaining();
        }
    }

    public void readRemaining() throws IOException {
        if (!eof) {
            eof = true;

            // skip remaining bytes
            chunkInputStream.skipRemaining();

            // read status
            StreamStatus status = statusInputStream.readStatus();

            // check status
            if (status.getStatusCode() == StreamStatus.STATUS_ERROR) {
                throw new RemoteStreamException("incomplete read from stream, because of a remote unexpected error " +
                        "(statusCode=" + status.getStatusCode() + ", message=" + status.getStatusMessage() + ")"
                );
            }
        }
    }

    @Override
    public int available() throws IOException {
        return chunkInputStream.available();
    }

    @Override
    public void mark(int readLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }

}
