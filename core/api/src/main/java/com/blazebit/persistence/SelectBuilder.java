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

package com.blazebit.persistence;

import javax.persistence.Tuple;

/**
 * An interface for builders that support selecting.
 * This is related to the fact, that a query builder supports select clauses.
 *
 * @param <T> The result type
 * @param <X> The concrete builder type
 * @author Christian Beikov
 * @since 1.0
 */
public interface SelectBuilder<T, X extends SelectBuilder<T, X>> {
    
    /**
     * Like {@link Selectable#selectCase(java.lang.String)} but without an alias.
     * 
     * @return The case when builder
     */
    public CaseWhenBuilder<? extends SelectBuilder<Tuple, ?>> selectCase();
    
    /**
     * Starts a {@link CaseWhenBuilder} with the given alias as select alias.
     * 
     * @param alias The select alias for the case when expression
     * @return The case when builder
     */
    public CaseWhenBuilder<? extends SelectBuilder<Tuple, ?>> selectCase(String alias);

    /**
     * Like {@link Selectable#selectSimpleCase(java.lang.String, java.lang.String)} but without an alias.
     * 
     * @param expression The case operand
     * @return The simple case when builder
     */
    public SimpleCaseWhenBuilder<? extends SelectBuilder<Tuple, ?>> selectSimpleCase(String expression);
    
    /**
     * Starts a {@link SimpleCaseWhenBuilder} with the given alias as select alias.
     * The expression is the case operand which will be compared to the when expressions defined in the subsequent {@linkplain SimpleCaseWhenBuilder}.
     * 
     * @param expression The case operand
     * @param alias The select alias for the simple case when expression
     * @return The simple case when builder
     */
    public SimpleCaseWhenBuilder<? extends SelectBuilder<Tuple, ?>> selectSimpleCase(String expression, String alias);
    
    /**
     * Like {@link Selectable#selectSubquery(java.lang.String)} but without an alias.
     * 
     * @return The subquery initiator for building a subquery
     */
    public SubqueryInitiator<? extends SelectBuilder<Tuple, ?>> selectSubquery();
    
    /**
     * Starts a {@link SubqueryInitiator} for the select item with the given alias.
     * When the builder finishes, the select item is added to the parent container represented by the type {@linkplain X}.
     *
     * @param alias The select alias for the subquery
     * @return The subquery initiator for building a subquery
     */
    public SubqueryInitiator<? extends SelectBuilder<Tuple, ?>> selectSubquery(String alias);
    
    //TODO: javadoc
    public SubqueryInitiator<? extends SelectBuilder<Tuple, ?>> selectSubquery(String subqueryAlias, String expression,  String selectAlias);
    
    //TODO: javadoc
    public SubqueryInitiator<? extends SelectBuilder<Tuple, ?>> selectSubquery(String subqueryAlias, String expression);

    /**
     * Adds a select clause with the given expression to the query.
     *
     * @param expression The expression for the select clause
     * @return The query builder for chaining calls
     */
    public SelectBuilder<Tuple, ?> select(String expression);

    /**
     * Adds a select clause with the given expression and alias to the query.
     *
     * @param expression The expression for the select clause
     * @param alias      The alias for the expression
     * @return The query builder for chaining calls
     */
    public SelectBuilder<Tuple, ?> select(String expression, String alias);
}