/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.webbeans.test.unittests.dependent;

import java.util.List;

import junit.framework.Assert;

import org.apache.webbeans.common.TestContext;
import org.apache.webbeans.component.AbstractBean;
import org.apache.webbeans.context.ContextFactory;
import org.apache.webbeans.test.component.dependent.DependentComponent;
import org.apache.webbeans.test.component.dependent.DependentOwnerComponent;
import org.apache.webbeans.test.component.dependent.circular.DependentA;
import org.apache.webbeans.test.component.dependent.circular.DependentB;
import org.junit.Before;
import org.junit.Test;

public class DependentComponentTest extends TestContext
{
    public DependentComponentTest()
    {
        super(DependentComponentTest.class.getName());
    }

    @Before
    public void init()
    {
        super.init();
    }

    @Test
    public void testDependent()
    {
        clear();
        defineManagedBean(DependentComponent.class);
        defineManagedBean(DependentOwnerComponent.class);

        ContextFactory.initRequestContext(null);

        List<AbstractBean<?>> comps = getComponents();

        Assert.assertEquals(2, comps.size());

        DependentOwnerComponent comp = (DependentOwnerComponent) getManager().getInstance(comps.get(1));

        DependentComponent dc = comp.getDependent();

        Assert.assertNotNull(dc);

        ContextFactory.destroyRequestContext(null);
    }

    @Test
    public void testDependentCircular()
    {
        clear();
        
        ContextFactory.initRequestContext(null);

        AbstractBean<DependentA> componentA = defineManagedBean(DependentA.class);
        AbstractBean<DependentB> componentB = defineManagedBean(DependentB.class);
        
        Assert.assertNotNull(componentB);
        
        DependentA dependentA = getManager().getInstance(componentA);
        Assert.assertNotNull(dependentA);
        Assert.assertNotNull(dependentA.getDependentB());

    }

}