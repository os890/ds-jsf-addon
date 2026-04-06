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

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import java.util.logging.Logger;

/**
 * CDI portable extension that conditionally vetoes the
 * {@link FirstFacesRequestListener} based on DeltaSpike
 * class-deactivation or project-stage configuration.
 *
 * <p>The listener is vetoed when either:</p>
 * <ul>
 *   <li>It has been deactivated via a
 *       {@link org.apache.deltaspike.core.spi.activation.ClassDeactivator}.</li>
 *   <li>The configured project stage (property
 *       {@code first-faces-request_project-stage}) does not match the
 *       current DeltaSpike project stage.</li>
 * </ul>
 */
public class VetoExtension implements Extension {

    private static final Logger LOG = Logger.getLogger(VetoExtension.class.getName());

    /**
     * Observes annotated type processing and vetoes
     * {@link FirstFacesRequestListener} when conditions are met.
     *
     * @param pat the process annotated type event
     */
    protected void init(@Observes ProcessAnnotatedType<FirstFacesRequestListener> pat) {
        //to support multiple project-stages in parallel, it would be needed to support separators
        // -> split the value and check the individual parts
        String configuredProjectStageName =
                ConfigResolver.getPropertyValue("first-faces-request_project-stage");
        ProjectStage configuredProjectStage = configuredProjectStageName != null
                ? ProjectStage.valueOf(configuredProjectStageName) : null;

        boolean listenerDeactivated =
                !ClassDeactivationUtils.isActivated(FirstFacesRequestListener.class);

        if (listenerDeactivated
                || (configuredProjectStage != null
                    && configuredProjectStage
                        != ProjectStageProducer.getInstance().getProjectStage())) {
            pat.veto();
            LOG.info(FirstFacesRequestListener.class + " deactivated");
        }
    }
}
