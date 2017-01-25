/*
 * Copyright 2014 - 2017 Blazebit.
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

package com.blazebit.persistence.view.testsuite.collections.subview;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.testsuite.base.category.NoHibernate42;
import com.blazebit.persistence.testsuite.base.category.NoHibernate43;
import com.blazebit.persistence.testsuite.base.category.NoHibernate50;
import com.blazebit.persistence.testsuite.base.category.NoHibernate51;
import com.blazebit.persistence.testsuite.tx.TxVoidWork;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import com.blazebit.persistence.view.testsuite.AbstractEntityViewTest;
import com.blazebit.persistence.view.testsuite.collections.entity.simple.DocumentForEntityKeyMaps;
import com.blazebit.persistence.view.testsuite.collections.entity.simple.PersonForEntityKeyMaps;
import com.blazebit.persistence.view.testsuite.collections.subview.model.SubviewDocumentCollectionsView;
import com.blazebit.persistence.view.testsuite.collections.subview.model.SubviewDocumentForEntityKeyMapsView;
import com.blazebit.persistence.view.testsuite.collections.subview.model.SubviewPersonForEntityKeyMapsView;
import com.blazebit.persistence.view.testsuite.collections.subview.model.SubviewSimpleDocumentForEntityKeyMapsView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
public class EntityMapKeySubviewTest<T extends SubviewDocumentCollectionsView> extends AbstractEntityViewTest {
    private DocumentForEntityKeyMaps doc1;
    private DocumentForEntityKeyMaps doc2;


    @Override
    protected Class<?>[] getEntityClasses() {
        return new Class<?>[]{
                DocumentForEntityKeyMaps.class,
                PersonForEntityKeyMaps.class
        };
    }

    @Before
    public void setUp() {
        transactional(new TxVoidWork() {
            @Override
            public void work(EntityManager em) {
                doc1 = new DocumentForEntityKeyMaps("doc1");
                doc2 = new DocumentForEntityKeyMaps("doc2");

                PersonForEntityKeyMaps o1 = new PersonForEntityKeyMaps("pers1");
                PersonForEntityKeyMaps o2 = new PersonForEntityKeyMaps("pers2");
                PersonForEntityKeyMaps o3 = new PersonForEntityKeyMaps("pers3");
                PersonForEntityKeyMaps o4 = new PersonForEntityKeyMaps("pers4");

                em.persist(o1);
                em.persist(o2);
                em.persist(o3);
                em.persist(o4);

                doc1.getContactDocuments().put(o1, doc2);
                doc2.getContactDocuments().put(o2, doc1);

                em.persist(doc1);
                em.persist(doc2);
            }
        });

        doc1 = em.find(DocumentForEntityKeyMaps.class, doc1.getId());
        doc2 = em.find(DocumentForEntityKeyMaps.class, doc2.getId());
    }

    /**
     * Test for https://github.com/Blazebit/blaze-persistence/issues/329
     */
    @Test
    @Category({NoHibernate42.class, NoHibernate43.class, NoHibernate50.class, NoHibernate51.class})
    public void testCollections() {
        EntityViewConfiguration cfg = EntityViews.createDefaultConfiguration();
        cfg.addEntityView(SubviewDocumentForEntityKeyMapsView.class);
        cfg.addEntityView(SubviewSimpleDocumentForEntityKeyMapsView.class);
        cfg.addEntityView(SubviewPersonForEntityKeyMapsView.class);
        EntityViewManager evm = cfg.createEntityViewManager(cbf, em.getEntityManagerFactory());

        CriteriaBuilder<DocumentForEntityKeyMaps> criteria = cbf.create(em, DocumentForEntityKeyMaps.class, "d")
                .orderByAsc("id");
        CriteriaBuilder<SubviewDocumentForEntityKeyMapsView> cb = evm.applySetting(EntityViewSetting.create(SubviewDocumentForEntityKeyMapsView.class), criteria);
        List<SubviewDocumentForEntityKeyMapsView> results = cb.getResultList();

        assertEquals(2, results.size());
        // Doc1
        assertEquals(doc1.getName(), results.get(0).getName());
        assertContactDocumentsEquals(doc1.getContactDocuments(), results.get(0).getContactDocuments());

        // Doc2
        assertEquals(doc2.getName(), results.get(1).getName());
        assertContactDocumentsEquals(doc2.getContactDocuments(), results.get(1).getContactDocuments());
    }

    private static void assertContactDocumentsEquals(Map<PersonForEntityKeyMaps, DocumentForEntityKeyMaps> contactDocuments, Map<SubviewPersonForEntityKeyMapsView, SubviewSimpleDocumentForEntityKeyMapsView> contactDocumentSubviews) {
        if (contactDocuments == null) {
            assertNull(contactDocumentSubviews);
            return;
        }

        assertNotNull(contactDocumentSubviews);
        assertEquals(contactDocuments.size(), contactDocumentSubviews.size());
        for (Map.Entry<PersonForEntityKeyMaps, DocumentForEntityKeyMaps> contactDocumentEntry : contactDocuments.entrySet()) {
            boolean found = false;
            for (Map.Entry<SubviewPersonForEntityKeyMapsView, SubviewSimpleDocumentForEntityKeyMapsView> contactDocumentSubviewEntry : contactDocumentSubviews.entrySet()) {
                if (contactDocumentEntry.getKey().getName().equals(contactDocumentSubviewEntry.getKey().getName()) &&
                        contactDocumentEntry.getValue().getName().equals(contactDocumentSubviewEntry.getValue().getName())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Assert.fail("Could not find an entry (PersonForCollectionsView, SubviewDocumentCollectionsView) with names: (" + contactDocumentEntry.getKey().getName() + ", " + contactDocumentEntry.getValue().getName() + ")");
            }
        }
    }
}
