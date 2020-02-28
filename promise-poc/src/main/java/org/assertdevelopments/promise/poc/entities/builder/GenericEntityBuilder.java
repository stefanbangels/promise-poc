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

package org.assertdevelopments.promise.poc.entities.builder;

import org.assertdevelopments.promise.poc.entities.objects.GenericEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Stefan Bangels
 * @since 2015-07-06
 */
final class GenericEntityBuilder implements EntityBuilder<GenericEntity> {

    private final List<GenericEntity> stack = new LinkedList<GenericEntity>();

    public GenericEntityBuilder() {
        stack.add(new GenericEntity());
    }

    public EntityBuilder<GenericEntity> setValue(String field, Object value) {
        getCurrentStackItem().setValue(field, value);
        return this;
    }

    public EntityBuilder<GenericEntity> startEntityValue(String field) {
        GenericEntity genericEntity = new GenericEntity();
        getCurrentStackItem().setEntity(field, genericEntity);
        stack.add(genericEntity);
        return this;
    }

    public EntityBuilder<GenericEntity> endValue() {
        stack.remove(stack.size() - 1);
        return this;
    }

    private GenericEntity getCurrentStackItem() {
        return stack.get(stack.size() - 1);
    }

    public GenericEntity getEntity() {
        return stack.get(0);
    }

}
