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
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Output Stream for writing a stream body (with an unknown size, in chunks) to an output stream.
 *
 * @author Stefan Bangels
 * @see ChunkInputStream
 * @since 2015-02-03
 */
public final class ChunkOutputStream extends FilterOutputStream {

    private final byte[] chunk;
    private int chunkOffset;
    private boolean eof = false;

    public ChunkOutputStream(OutputStream outputStream, int bufferSize) {
        super(outputStream);
        this.chunk = new byte[bufferSize];
    }

    @Override
    public void write(int b) throws IOException {
        if (eof) {
            throw new EOFException();
        }
        if (chunkOffset >= chunk.length) {
            flush();
        }
        chunk[chunkOffset++] = (byte) b;
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public void write(byte[] bytes) throws IOException {
        this.write(bytes, 0, bytes.length);
    }

    @Override
    @SuppressWarnings({"NullableProblems"})
    public void write(byte[] bytes, int offset, int length) throws IOException {
        if (eof) {
            throw new EOFException();
        }
        while (length > 0) {
            if (chunkOffset >= chunk.length) {
                flush();
            }
            int size = Math.min(length, chunk.length - chunkOffset);
            System.arraycopy(bytes, offset, chunk, chunkOffset, size);
            offset += size;
            chunkOffset += size;
            length -= size;
        }
    }

    public void writeEOF() throws IOException {
        if (eof) {
            throw new EOFException();
        }
        flush();
        writeShort(0);
        eof = true;
        out.flush();
    }

    @Override
    public void flush() throws IOException {
        if (!eof && chunkOffset > 0) {
            writeShort(chunkOffset);
            out.write(chunk, 0, chunkOffset);
            chunkOffset = 0;
        }
        out.flush();
    }

    private void writeShort(int v) throws IOException {
        out.write((v >>> 8) & 0xFF);
        out.write(v & 0xFF);
    }

}
