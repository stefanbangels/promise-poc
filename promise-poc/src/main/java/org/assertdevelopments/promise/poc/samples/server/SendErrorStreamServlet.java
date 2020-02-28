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

package org.assertdevelopments.promise.poc.samples.server;

import org.apache.log4j.Logger;
import org.assertdevelopments.promise.poc.server.AbstractStreamServlet;
import org.assertdevelopments.promise.poc.server.Stream;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author Stefan Bangels
 * @since 2014-10-23
 */
@WebServlet(name = "SendErrorStreamServlet", urlPatterns = "/ws/send-error")
public final class SendErrorStreamServlet extends AbstractStreamServlet {

    private final Logger logger = Logger.getLogger(getClass());

    public void handleStreamRequest(Stream stream) throws Throwable {
        // read request
        BufferedReader in = new BufferedReader(new InputStreamReader(stream.getInputStream()));
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            logger.debug("IN: " + line);
        }

        // write response
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream.getOutputStream()));
        for (int n = 0; n < 500000; n++) {
            writer.write("test-" + n + "\n");
            if (n == 100000) {
                stream.sendError(500, "server side error!");
                break;
            }
        }
        writer.flush();
    }

}
