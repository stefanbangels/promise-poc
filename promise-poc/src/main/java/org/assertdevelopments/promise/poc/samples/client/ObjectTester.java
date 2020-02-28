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
import org.assertdevelopments.promise.poc.samples.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author Stefan Bangels
 * @since 2015-08-12
 */
public final class ObjectTester {

    private static final String URI = "/ws/objects";

    private static Logger logger = Logger.getLogger(ObjectTester.class);

    public static void main(String[] args) throws Exception {
        StreamClient client = new StreamClient();
        try {
            long timer = System.currentTimeMillis();

            // create request
            StreamRequest request = new StreamRequest() {
                public void writeEntity(OutputStream outputStream) throws IOException {
                    ObjectOutputStream out = new ObjectOutputStream(outputStream);
                    for (long n = 0; n < 1000; n++) {
                        out.writeObject(new User(n, "client-user-" + n));
                    }
                    out.writeObject(null);
                    out.flush();
                }
            };

            // execute request
            int n = 0;
            StreamResponse response = new StreamClient().sendRequest(Constants.BASE_URL + URI, request);
            try {
                ObjectInputStream in = new ObjectInputStream(response.getInputStream());
                while (true) {
                    User user = (User) in.readObject();
                    logger.info("IN: " + user);
                    if (user == null) {
                        break;
                    }
                    n++;
                }
            } finally {
                response.close();
            }

            logger.info((System.currentTimeMillis() - timer) + "ms (" + n + " items)");
        } finally {
            client.close();
        }
    }

}
