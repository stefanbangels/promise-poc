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
import org.assertdevelopments.promise.poc.samples.common.User;
import org.assertdevelopments.promise.poc.server.AbstractStreamServlet;
import org.assertdevelopments.promise.poc.server.Stream;

import javax.servlet.annotation.WebServlet;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Stefan Bangels
 * @since 2014-10-23
 */
@WebServlet(name = "ObjectsStreamServlet", urlPatterns = "/ws/objects")
public final class ObjectsStreamServlet extends AbstractStreamServlet {

    private final Logger logger = Logger.getLogger(getClass());

    public void handleStreamRequest(Stream stream) throws Throwable {
        // read request
        ObjectInputStream in = new ObjectInputStream(stream.getInputStream());
        while (true) {
            Object genericEntity = in.readObject();
            if (genericEntity == null) {
                break;
            }
            logger.debug("IN: " + genericEntity);
        }

        // write response
        ObjectOutputStream out = new ObjectOutputStream(stream.getOutputStream());
        for (long n = 0; n < 500000; n++) {
            out.writeObject(new User(n, "server-user-" + n));
        }
        out.writeObject(null);
        out.flush();
    }

}
