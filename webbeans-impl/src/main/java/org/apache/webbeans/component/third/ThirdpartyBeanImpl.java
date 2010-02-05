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
package org.apache.webbeans.component.third;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;

import org.apache.webbeans.component.AbstractBean;
import org.apache.webbeans.component.WebBeansType;

public class ThirdpartyBeanImpl<T> extends AbstractBean<T>
{
    private Bean<T> bean = null;
    
    
    public ThirdpartyBeanImpl(Bean<T> bean)
    {
        super(WebBeansType.THIRDPARTY);
        
        this.bean = bean;
        
    }
    
    @Override
    public Set<Annotation> getQualifiers()
    {
        
        return bean.getQualifiers();
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints()
    {
        
        return bean.getInjectionPoints();
    }

    @Override
    public String getName()
    {
        
        return bean.getName();
    }

    @Override
    public Class<? extends Annotation> getScope()
    {
        
        return bean.getScope();
    }

    @Override
    public Set<Type> getTypes()
    {
        
        return bean.getTypes();
    }

    @Override
    public boolean isNullable()
    {
        
        return bean.isNullable();
    }

    public T create(CreationalContext<T> context)
    {
        
        return bean.create(context);
    }

    public void destroy(T instance, CreationalContext<T> context)
    {
        bean.destroy(instance,context);
        
    }


    /* (non-Javadoc)
     * @see org.apache.webbeans.component.AbstractBean#getId()
     */
    @Override
    public String getId()
    {
        if(this.bean instanceof PassivationCapable)
        {
            PassivationCapable pc = (PassivationCapable)this.bean;
            return pc.getId();
        }
        
        return null;
    }
    
    

    @Override
    public boolean isPassivationCapable()
    {
        return this.bean instanceof PassivationCapable ? true : false;
    }

    @Override
    protected T createInstance(CreationalContext<T> creationalContext)
    {
        throw new UnsupportedOperationException();
    }



    @Override
    protected void destroyInstance(T instance,CreationalContext<T> creationalContext)
    {
        throw new UnsupportedOperationException();
        
    }

}
