/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.newtests.interceptors.resolution;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.webbeans.component.InterceptorBean;
import org.apache.webbeans.component.creation.EjbInterceptorBeanBuilder;
import org.apache.webbeans.newtests.AbstractUnitTest;

import org.apache.webbeans.newtests.interceptors.ejb.EjbInterceptor;
import org.apache.webbeans.newtests.interceptors.ejb.ManagedBeanWithEjbInterceptor;
import org.apache.webbeans.newtests.interceptors.resolution.beans.UtilitySampleBean;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for building EJB-style interceptor beans
 */
public class EjbInterceptorBeanBuilderTest extends AbstractUnitTest
{

    @Test
    public void testEjbInterceptorBeanCreation()
    {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(UtilitySampleBean.class);
        beanClasses.add(EjbInterceptor.class);
        beanClasses.add(ManagedBeanWithEjbInterceptor.class);

        startContainer(beanClasses, null);

        AnnotatedType<EjbInterceptor> annotatedType = getBeanManager().createAnnotatedType(EjbInterceptor.class);

        EjbInterceptorBeanBuilder<EjbInterceptor> ibb
                = new EjbInterceptorBeanBuilder<EjbInterceptor>(getWebBeansContext(), annotatedType);
        ibb.defineEjbInterceptorRules();
        InterceptorBean<EjbInterceptor> bean = ibb.getBean();
        Assert.assertNotNull(bean);

        CreationalContext<EjbInterceptor> cc = getBeanManager().createCreationalContext(bean);

        EjbInterceptor ebi = bean.create(cc);
        Assert.assertNotNull(ebi);

        shutDownContainer();
    }

    @Test
    public void testInterceptorsManagerEjbBeanCreation()
    {
        Collection<Class<?>> beanClasses = new ArrayList<Class<?>>();
        beanClasses.add(UtilitySampleBean.class);
        beanClasses.add(EjbInterceptor.class);
        beanClasses.add(ManagedBeanWithEjbInterceptor.class);

        startContainer(beanClasses, null);

        Interceptor<EjbInterceptor> interceptor = getWebBeansContext().getInterceptorsManager().getEjbInterceptorForClass(EjbInterceptor.class);
        Assert.assertNotNull(interceptor);
        Assert.assertTrue(interceptor.intercepts(InterceptionType.AROUND_INVOKE));

        Interceptor<EjbInterceptor> interceptor2 = getWebBeansContext().getInterceptorsManager().getEjbInterceptorForClass(EjbInterceptor.class);
        Assert.assertNotNull(interceptor2);
        Assert.assertTrue(interceptor2.intercepts(InterceptionType.AROUND_INVOKE));

        Assert.assertTrue(interceptor == interceptor2); // should return previous instance

        shutDownContainer();
    }

}
