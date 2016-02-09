/*
 * Copyright 2014 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.persistence.view.impl.objectbuilder.transformer;

import java.util.HashSet;
import java.util.Set;

import com.blazebit.persistence.view.impl.collection.RecordingSet;

/**
 *
 * @author Christian Beikov
 * @since 1.0
 */
public class UpdatableSetTupleListTransformer extends AbstractNonIndexedTupleListTransformer<RecordingSet<Set<Object>, Object>> {

    public UpdatableSetTupleListTransformer(int[] parentIdPositions, int startIndex) {
        super(parentIdPositions, startIndex);
    }
    
    @Override
    protected Object createCollection() {
        return new RecordingSet<Set<Object>, Object>(new HashSet<Object>());
    }

    @Override
    protected void addToCollection(RecordingSet<Set<Object>, Object> set, Object value) {
        set.getDelegate().add(value);
    }

}
