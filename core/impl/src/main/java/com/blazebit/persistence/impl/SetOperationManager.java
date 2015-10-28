/*
 * Copyright 2015 Blazebit.
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
package com.blazebit.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import com.blazebit.persistence.spi.SetOperationType;

/**
 *
 * @author Christian Beikov
 * @author Moritz Becker
 * @since 1.1.0
 */
public class SetOperationManager {

	private AbstractCommonQueryBuilder<?, ?, ?, ?, ?> startQueryBuilder;
	private SetOperationType operator;
    private final boolean nested;
    private final List<AbstractCommonQueryBuilder<?, ?, ?, ?, ?>> setOperations;

    SetOperationManager(SetOperationType operator, boolean nested) {
        this.operator = operator;
        this.nested = nested;
        this.setOperations = new ArrayList<AbstractCommonQueryBuilder<?, ?, ?, ?, ?>>();
    }

    List<AbstractCommonQueryBuilder<?, ?, ?, ?, ?>> getSetOperations() {
    	return setOperations;
    }

    boolean hasSetOperations() {
        return setOperations.size() > 0;
    }
    
    boolean isNested() {
        return nested;
    }
    
    AbstractCommonQueryBuilder<?, ?, ?, ?, ?> getStartQueryBuilder() {
        return startQueryBuilder;
    }
    
    void setStartQueryBuilder(AbstractCommonQueryBuilder<?, ?, ?, ?, ?> startQueryBuilder) {
        this.startQueryBuilder = startQueryBuilder;
    }

    SetOperationType getOperator() {
        return operator;
    }
    
    void setOperator(SetOperationType operator) {
        this.operator = operator;
    }
    
    void addSetOperation(AbstractCommonQueryBuilder<?, ?, ?, ?, ?> queryBuilder) {
        queryBuilder.prepareAndCheck();
        setOperations.add(queryBuilder);
    }

}