/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Blazebit
 */

package com.blazebit.persistence.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A mapping to a JPQL expression which contains references to fields of the target entity/association.
 * The {@linkplain MappingIndex} annotation can be applied to abstract getter methods of an interface or abstract class. It may also be applied to constructor parameters.
 *
 * The use of the {@linkplain MappingIndex} annotation is optional when the {@link Mapping} annotation refers to an indexed collection and the Java type is of the same indexed collection type.
 *
 * <code>
 * Example 1:
 *
 * public List&lt;String&gt; getOrderedNames();
 *
 * Example 2:
 *
 * {@literal @}MappingIndex("INDEX(this)")
 * {@literal @}Mapping("UPPER(orderedNames)")
 * public List&lt;String&gt; getOrderedUpperName();
 *
 * </code>
 *
 * Example 1 shows a getter which implicitly uses the mapping index of the backing "orderedNames" collection.
 * Example 2 explicitly defines the mapping for the index based on the index of the "orderedNames" collection.
 *
 * @author Christian Beikov
 * @since 1.5.0
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingIndex {

    /**
     * The JPQL expression mapping the index value of the annotated getter or parameter should map to.
     *
     * @return The JPQL expression mapping
     */
    String value() default "";

    /**
     * The associations of the entity that should be fetched.
     * This is only valid if the mapping refers to an entity and is mapped as attribute with the original type.
     *
     * @return The associations of the entity that should be fetched
     */
    String[] fetches() default {};
}
