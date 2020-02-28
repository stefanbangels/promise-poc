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

package org.assertdevelopments.promise.poc.entities.inspector;

import org.assertdevelopments.promise.poc.entities.objects.GenericEntity;

import java.io.IOException;

/**
 * @author Stefan Bangels
 * @since 2015-07-06
 */
public final class GenericEntityInspector implements EntityInspector<GenericEntity> {

    public void inspect(GenericEntity genericEntity, EntityHandler handler) throws IOException {
        for (String field : genericEntity.getFields()) {
            if (genericEntity.isEntity(field)) {
                handler.handleStartEntityValue(field);
                inspect(genericEntity.getEntity(field), handler);
                handler.handleEndValue();
            } else {
                handler.handleValue(field, genericEntity.getValue(field));
            }
        }
    }

}
