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
package org.apache.webbeans.test.unittests.inheritance;

import java.lang.annotation.Annotation;
import java.util.Set;

import junit.framework.Assert;

import org.apache.webbeans.common.TestContext;
import org.apache.webbeans.component.AbstractBean;
import org.apache.webbeans.config.inheritance.BeanInheritedMetaData;
import org.apache.webbeans.test.component.inheritance.InheritFromParentComponent;
import org.apache.webbeans.test.component.inheritance.types.InhDeployment1;
import org.apache.webbeans.test.component.inheritance.types.InhStereo1;
import org.apache.webbeans.test.component.inheritance.types.InhStereo2;
import org.junit.Before;
import org.junit.Test;

public class InheritanceTest extends TestContext
{
    
    public InheritanceTest()
    {
        super(InheritanceTest.class.getName());
    }

    @Before
    public void init()
    {
        initDefaultDeploymentTypes();
        initializeDeploymentType(InhDeployment1.class,2);
        initializeStereoType(InhStereo1.class);
        initializeStereoType(InhStereo2.class);
    }
    
    @Test
    public void testInheritedComponentMetaData()
    {
        AbstractBean<InheritFromParentComponent> component = defineManagedBean(InheritFromParentComponent.class);
        
        BeanInheritedMetaData<InheritFromParentComponent> data = new BeanInheritedMetaData<InheritFromParentComponent>(component);
        
        Set<Annotation> btypes = data.getInheritedQualifiers();
        Assert.assertEquals(2, btypes.size());
        
        Annotation annot = data.getInheritedDeploymentType();
        Assert.assertNotNull(annot);
        
        btypes = data.getInheritedInterceptorBindings();
        Assert.assertEquals(2, btypes.size());
        
        annot = data.getInheritedScopeType();
        Assert.assertNotNull(annot);
        
        btypes = data.getInheritedStereoTypes();
        Assert.assertEquals(2, btypes.size());
    }
}
