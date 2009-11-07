/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.webbeans.test.unittests.pfield;

import javax.enterprise.inject.spi.Bean;

import org.apache.webbeans.common.TestContext;
import org.apache.webbeans.context.ContextFactory;
import org.apache.webbeans.test.component.CheckWithCheckPayment;
import org.apache.webbeans.test.component.CheckWithMoneyPayment;
import org.apache.webbeans.test.component.PaymentProcessorComponent;
import org.apache.webbeans.test.component.pfield.ProducerFieldDefinitionComponent;
import org.apache.webbeans.test.component.pfield.ProducerFieldInjectedComponent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProducerFieldComponentTest extends TestContext
{
    public ProducerFieldComponentTest()
    {
        super(ProducerFieldComponentTest.class.getName());
    }
    
    @Before
    public void init()
    {
        initDefaultDeploymentTypes();
    }
    
    
    @Test
    public void testInjectedProducerField()
    {
        ContextFactory.initRequestContext(null);
        
        defineManagedBean(CheckWithCheckPayment.class);
        defineManagedBean(CheckWithMoneyPayment.class);
        
        Bean<PaymentProcessorComponent> pc = defineManagedBean(PaymentProcessorComponent.class);
        Object obj = getManager().getInstance(pc);
        
        Assert.assertTrue(obj instanceof PaymentProcessorComponent);
        
        Bean<ProducerFieldDefinitionComponent> beanDefine = defineManagedBean(ProducerFieldDefinitionComponent.class);
        Bean<ProducerFieldInjectedComponent> beanInjected = defineManagedBean(ProducerFieldInjectedComponent.class);
        
        ProducerFieldDefinitionComponent defineComponentInstance = getManager().getInstance(beanDefine);
        
        Assert.assertNotNull(defineComponentInstance);
        Assert.assertTrue(defineComponentInstance.isExist());
        
        ProducerFieldInjectedComponent injectedComponentInstance = getManager().getInstance(beanInjected);
        
        Assert.assertNotNull(injectedComponentInstance);
        
        Assert.assertNotNull(injectedComponentInstance.getPaymentProcessorName());
    }

}
