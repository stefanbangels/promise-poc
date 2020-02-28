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
import org.assertdevelopments.promise.poc.entities.objects.GenericEntity;
import org.assertdevelopments.promise.poc.entities.serialization.GenericEntityInputStream;
import org.assertdevelopments.promise.poc.entities.serialization.GenericEntityOutputStream;
import org.assertdevelopments.promise.poc.server.AbstractStreamServlet;
import org.assertdevelopments.promise.poc.server.Stream;

import javax.servlet.annotation.WebServlet;

/**
 * @author Stefan Bangels
 * @since 19/02/16
 */
@WebServlet(name = "EntityStreamServlet", urlPatterns = "/ws/entity")
public final class EntityStreamServlet extends AbstractStreamServlet {

    private final Logger logger = Logger.getLogger(getClass());

    public void handleStreamRequest(Stream stream) throws Throwable {
        // read request
        GenericEntityInputStream in = new GenericEntityInputStream(stream.getInputStream());
        while (true) {
            GenericEntity entity = in.readEntity();
            if (entity == null) {
                break;
            }
            logger.debug("IN: " + entity);
        }

        // write response
        GenericEntityOutputStream out = new GenericEntityOutputStream(stream.getOutputStream());
        for (long n = 0; n < 500000; n++) {
            out.writeEntity(new GenericEntity()
                    .setLong("id", n)
                    .setString("name", "server-user-" + n)
            );
        }
        out.flush();
    }

}
