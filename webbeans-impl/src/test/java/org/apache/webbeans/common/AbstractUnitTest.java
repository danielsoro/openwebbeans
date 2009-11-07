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
package org.apache.webbeans.common;

import java.net.URL;
import java.util.Collection;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.webbeans.util.OpenWebBeansTestLifecycle;
import org.apache.webbeans.util.OpenWebBeansTestMetaDataDiscoveryService;


public abstract class AbstractUnitTest
{
    private OpenWebBeansTestLifecycle testLifecycle;

    protected AbstractUnitTest()
    {
        
    }
    
    protected void startContainer(Collection<Class<?>> beanClasses)
    {
        //Creates a new container
        testLifecycle = new OpenWebBeansTestLifecycle();
        
        //Deploy bean classes
        OpenWebBeansTestMetaDataDiscoveryService discoveyService = (OpenWebBeansTestMetaDataDiscoveryService)testLifecycle.getDiscoveryService();
        discoveyService.deployClasses(beanClasses);
        
        //Start application
        testLifecycle.applicationStarted(null);
    }
    
    protected void startContainer(Collection<Class<?>> beanClasses, Collection<URL> beanXmls)
    {
        //Creates a new container
        testLifecycle = new OpenWebBeansTestLifecycle();
        
        //Deploy bean classes
        OpenWebBeansTestMetaDataDiscoveryService discoveyService = (OpenWebBeansTestMetaDataDiscoveryService)testLifecycle.getDiscoveryService();
        discoveyService.deployClasses(beanClasses);
        discoveyService.deployXMLs(beanXmls);
        
        //Start application
        testLifecycle.applicationStarted(null);
        
    }
    
    
    protected void shutDownContainer()
    {
        //Shwtdown application
        if(this.testLifecycle != null)
        {
            this.testLifecycle.applicationEnded(null);
        }        
    }
    
    protected OpenWebBeansTestLifecycle getLifecycle()
    {
        return this.testLifecycle;
    }
    
    protected BeanManager getBeanManager()
    {
        return this.testLifecycle.getBeanManager();
    }
    
    protected URL getXMLUrl(String packageName, String fileName)
    {
        StringBuilder prefix = new StringBuilder(packageName.replace('.', '/'));
        prefix.append("/");
        prefix.append(fileName);
        prefix.append(".xml");
        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResource(prefix.toString());
    }
    
}
