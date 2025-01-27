/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Blazebit
 */

package com.blazebit.persistence.integration.view.cdi;

import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityViewExtensionTest {

    @Inject
    private EntityViewConfiguration config;

    @Test
    public void testInjection() throws Exception {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();
        container.getContextControl().startContexts();
        BeanProvider.injectFields(this);

        assertNotNull(config);
        assertFalse(config.getEntityViews().isEmpty());
        assertTrue(config.getEntityViews().contains(TestView.class));

        container.shutdown();
    }
}
