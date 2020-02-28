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

package org.assertdevelopments.promise.poc.entities.serialization;

import org.assertdevelopments.promise.poc.entities.inspector.GenericEntityInspector;
import org.assertdevelopments.promise.poc.entities.objects.GenericEntity;

import java.io.OutputStream;

/**
 * @author Stefan Bangels
 * @since 2015-07-06
 */
public final class GenericEntityOutputStream extends EntityOutputStream<GenericEntity> {

    public GenericEntityOutputStream(OutputStream outputStream) {
        super(outputStream, new GenericEntityInspector());
    }

}
