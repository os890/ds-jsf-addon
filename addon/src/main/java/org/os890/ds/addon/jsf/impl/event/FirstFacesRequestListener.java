/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.ds.addon.jsf.impl.event;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.lifecycle.Initialized;
import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassUtils;
import org.os890.ds.addon.jsf.api.event.FirstFacesRequestEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.logging.Logger;

//allows to use e.g. a customizable lazy startup-event
//or a customizable and >portable< startup-event as an alternative to @Singleton + @Startup + @PostConstruct
@ApplicationScoped
public class FirstFacesRequestListener implements Deactivatable {
    private final static Logger LOG = Logger.getLogger(FirstFacesRequestListener.class.getName());

    @Inject
    private BeanManager beanManager;

    private boolean eventFired = false;

    protected void onBeforeFacesRequest(@Observes @Initialized FacesContext facesContext) {
        if (this.eventFired) {
            return;
        }
        fireFirstFacesRequestEvent();
    }

    private synchronized void fireFirstFacesRequestEvent() {
        if (this.eventFired) {
            return;
        }

        String configuredEventClassName = ConfigResolver.getProjectStageAwarePropertyValue(
            "first-faces-request_event-class", FirstFacesRequestEvent.class.getName());

        Object eventInstance = ClassUtils.tryToInstantiateClassForName(configuredEventClassName);

        this.beanManager.fireEvent(eventInstance);
        LOG.info(configuredEventClassName + " fired as first-faces-request event");
        this.eventFired = true;
    }
}
