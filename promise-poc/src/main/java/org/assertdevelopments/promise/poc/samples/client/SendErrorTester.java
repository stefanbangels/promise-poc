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

package org.assertdevelopments.promise.poc.samples.client;

import org.apache.log4j.Logger;
import org.assertdevelopments.promise.poc.client.StreamClient;
import org.assertdevelopments.promise.poc.client.StreamRequest;
import org.assertdevelopments.promise.poc.client.StreamResponse;

import java.io.*;

/**
 * @author Stefan Bangels
 * @since 2015-02-03
 */
public final class SendErrorTester {

    private static final String URI = "/ws/send-error";

    private static Logger logger = Logger.getLogger(SendErrorTester.class);

    public static void main(String[] args) throws IOException {
        StreamClient client = new StreamClient();
        try {
            long timer = System.currentTimeMillis();

            // create request
            StreamRequest request = new StreamRequest() {
                public void writeEntity(OutputStream outputStream) throws IOException {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    for (int n = 0; n < 100; n++) {
                        writer.write("OUT: " + n + "\n");
                    }
                    writer.flush();
                }
            };

            // execute request
            StreamResponse response = new StreamClient().sendRequest(Constants.BASE_URL + URI, request);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getInputStream()));
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    logger.info("IN: " + line);
                }
            } finally {
                response.close();
            }

            logger.info((System.currentTimeMillis() - timer) + "ms");
        } finally {
            client.close();
        }
    }

}
