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
package org.apache.webbeans.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.util.TypeLiteral;

import org.apache.webbeans.container.BeanManagerImpl;
import org.apache.webbeans.logger.WebBeansLogger;
import org.apache.webbeans.util.AnnotationUtil;
import org.apache.webbeans.util.ClassUtil;

/**
 * Event implementation.
 * 
 * @version $Rev$ $Date$
 *
 * @param <T> event type
 * @see Event
 */
public class EventImpl<T> implements Event<T>, Serializable
{
    private static final long serialVersionUID = -9035218380365451350L;
    
    private final WebBeansLogger logger = WebBeansLogger.getLogger(EventImpl.class);

    /**Event binding types*/
    private Annotation[] injectedBindings;

    /**Event types*/
    private Type eventType;

    /**Bean manager*/
    private transient BeanManagerImpl manager = null;

    /**
     * Creates a new event.
     * 
     * @param injectedBindings event bindings
     * @param eventType event type
     */
    public EventImpl(Annotation[] injectedBindings, Type eventType)
    {
        this.injectedBindings = injectedBindings;
        this.eventType = eventType;
        this.manager = BeanManagerImpl.getManager();
    }

    /**
     * Fires event with given event object.
     */
    public void fire(T event)
    {
        this.manager.fireEvent(event, this.injectedBindings);
    }

    /**
     * Returns total binding annotations.
     * 
     * @param annotations new annotations
     * @return total binding annotations
     */
    private Annotation[] getEventBindings(Annotation... annotations)
    {
        AnnotationUtil.checkQualifierConditions(annotations);
        
        Set<Annotation> eventBindings = new HashSet<Annotation>();
        
        for(Annotation ann : this.injectedBindings)
        {
            eventBindings.add(ann);
        }
        
        Annotation[] anns = null;

        for (Annotation binding : annotations)
        {
            eventBindings.add(binding);
        }

        anns = new Annotation[eventBindings.size()];
        anns = eventBindings.toArray(anns);

        return anns;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event<T> select(Annotation... bindings)
    {
        Event<T> sub = new EventImpl<T>(getEventBindings(bindings),this.eventType);
        
        return sub;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <U extends T> Event<U> select(Class<U> subtype, Annotation... bindings)
    {
        if(ClassUtil.isDefinitionConstainsTypeVariables(subtype))
        {
            throw new IllegalArgumentException("Class : " + subtype + " cannot contain type variable");
        }
        
        Type sub = subtype;
        
        if(sub == null)
        {
            sub = this.eventType;
        }
        
        Event<U> subEvent = new EventImpl<U>(getEventBindings(bindings),sub);
        
        return subEvent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <U extends T> Event<U> select(TypeLiteral<U> subtype, Annotation... bindings)
    {
        return select(subtype.getRawType(), bindings);
    }
    
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        try
        {
            final ObjectInputStream inputStream = in;
            
            AccessController.doPrivileged(
                new PrivilegedExceptionAction()
                {
                    public Object run() throws Exception
                    {
                        inputStream.defaultReadObject();
                        return null; 
                    }
                }
            );
        }
        catch (PrivilegedActionException pae)
        {
            Throwable cause = ((PrivilegedActionException)pae).getCause();

            if (cause instanceof IOException)
            {
                throw (IOException)cause;
            }
            else if (cause instanceof ClassNotFoundException)
            {
                throw (ClassNotFoundException)cause;
            }
            else if (cause instanceof RuntimeException)
            {
                throw (RuntimeException)cause;
            }
            else
            {
                if (logger.wblWillLogDebug())
                {
                    logger.trace("Unexpected exception via PAE [{0}]", cause);
                }
                
            }
        }
        
        this.manager = BeanManagerImpl.getManager();
    }
}