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

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Input Stream for reading a stream body (with an unknown size, in chunks) from an input stream.
 *
 * @author Stefan Bangels
 * @see ChunkOutputStream
 * @since 2015-02-03
 */
public final class ChunkInputStream extends FilterInputStream {

    private byte[] chunk;
    private int chunkSize = 0;
    private int chunkOffset = 0;
    private boolean eof = false;

    public ChunkInputStream(InputStream inputStream, int maxBufferSize) {
        super(inputStream);
        chunk = new byte[maxBufferSize];
    }

    @Override
    public int read() throws IOException {
        if (!checkReadNextChunk()) {
            return -1;
        }
        return chunk[chunkOffset++] & 0xff;
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (!checkReadNextChunk()) {
            return -1; // eof
        }
        if (length <= 0) {
            return 0;
        }
        int c = 0;
        while (length > 0) {
            int size = Math.min(length, chunkSize - chunkOffset);
            System.arraycopy(chunk, chunkOffset, bytes, offset, size);
            offset += size;
            chunkOffset += size;
            length -= size;
            c += size;
            if (length > 0) {
                if (!readNextChunk()) {
                    break; // eof
                }
            }
        }
        return c;
    }

    public void skipRemaining() throws IOException {
        while (true) {
            if (skip(Integer.MAX_VALUE) == -1) {
                break;
            }
        }
    }

    @Override
    public long skip(long length) throws IOException {
        if (!checkReadNextChunk()) {
            return -1; // eof
        }
        if (length <= 0) {
            return 0;
        }
        long c = 0;
        while (length > 0) {
            long size = Math.min(length, chunkSize - chunkOffset);
            chunkOffset += size;
            length -= size;
            c += size;
            if (length > 0) {
                if (!readNextChunk()) {
                    break; // eof
                }
            }
        }
        return c;
    }

    private boolean checkReadNextChunk() throws IOException {
        return chunkOffset < chunkSize || readNextChunk();
    }

    private boolean readNextChunk() throws IOException {
        if (eof) {
            return false;
        }
        int size = readShort();
        if (size > chunk.length) {
            throw new IOException("chunk size exceeds maximum buffer size: size = " + size + ", maximum = " + chunk.length);
        } else if (size != 0) {
            readFully(chunk, 0, size);
            this.chunkOffset = 0;
            this.chunkSize = size;
            return true;
        } else {
            eof = true;
            return false;
        }
    }

    private int readShort() throws IOException {
        int ch1 = super.read();
        int ch2 = super.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 8) + ch2);
    }

    private void readFully(byte[] bytes, int offset, int length) throws IOException {
        while (length > 0) {
            int n = super.read(bytes, offset, length);
            if (n < 0) {
                throw new EOFException();
            }
            offset += n;
            length -= n;
        }
    }

    @Override
    public int available() throws IOException {
        return !eof ? chunkSize - chunkOffset : 0;
    }

    @Override
    public synchronized void mark(int readLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

}
