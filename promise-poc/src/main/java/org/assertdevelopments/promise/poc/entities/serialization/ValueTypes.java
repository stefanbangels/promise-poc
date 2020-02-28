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

/**
 * @author Stefan Bangels
 * @since 2015-02-27
 */
interface ValueTypes {

    int ETX = 0;

    int NULL = 1;
    int STRING = 2;
    int BYTE = 3;
    int SHORT = 4;
    int INTEGER = 5;
    int LONG = 6;
    int DOUBLE = 7;
    int FLOAT = 8;
    int BOOLEAN = 9;
    int DATE = 10;
    int BIG_DECIMAL = 11;

    int ENTITY = 32;

}