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
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;
import org.apache.deltaspike.core.util.ProjectStageProducer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.util.logging.Logger;

public class VetoExtension implements Extension {
    private static final Logger LOG = Logger.getLogger(VetoExtension.class.getName());

    protected void init(@Observes ProcessAnnotatedType pat) {
        //to support some old ee6 servers which have issues with ProcessAnnotatedType<FirstFacesRequestListener>
        if (!pat.getAnnotatedType().getJavaClass().equals(FirstFacesRequestListener.class)) {
            return;
        }

        //to support multiple project-stages in parallel, it would be needed to support separators -> split the value and check the individual parts
        String configuredProjectStageName = ConfigResolver.getPropertyValue("first-faces-request_project-stage");
        ProjectStage configuredProjectStage = configuredProjectStageName != null ? ProjectStage.valueOf(configuredProjectStageName) : null;

        boolean listenerDeactivated = !ClassDeactivationUtils.isActivated(FirstFacesRequestListener.class);

        if (listenerDeactivated || (configuredProjectStage != null && configuredProjectStage != ProjectStageProducer.getInstance().getProjectStage())) {
            pat.veto();
            LOG.info(FirstFacesRequestListener.class + " deactivated");
        }
    }
}
