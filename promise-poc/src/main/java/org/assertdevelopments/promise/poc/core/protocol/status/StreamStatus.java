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

/**
 * The stream status (status code and status message).
 *
 * @author Stefan Bangels
 * @since 2015-02-26
 */
public final class StreamStatus {

    public static final int STATUS_OK = 200;
    public static final int STATUS_ERROR = 500;

    private int statusCode;
    private String statusMessage;

    public StreamStatus() {
    }

    public StreamStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return "StreamStatus{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }

}
