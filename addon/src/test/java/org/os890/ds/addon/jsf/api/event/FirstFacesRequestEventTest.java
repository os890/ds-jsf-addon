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

package org.os890.ds.addon.jsf.api.event;

import org.junit.jupiter.api.Test;
import org.os890.cdi.addon.dynamictestbean.EnableTestBeans;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link FirstFacesRequestEvent} verifying that the event
 * can be fired and observed within a CDI container.
 */
@EnableTestBeans
class FirstFacesRequestEventTest {

    @Inject
    private Event<FirstFacesRequestEvent> eventSource;

    private static int observedCount;

    /**
     * Resets state before each test.
     */
    @org.junit.jupiter.api.BeforeEach
    void resetState() {
        observedCount = 0;
    }

    void onEvent(@Observes FirstFacesRequestEvent event) {
        observedCount++;
    }

    @Test
    void eventCanBeInstantiated() {
        FirstFacesRequestEvent event = new FirstFacesRequestEvent();
        assertNotNull(event);
    }

    @Test
    void eventCanBeFiredAndObserved() {
        eventSource.fire(new FirstFacesRequestEvent());
        assertEquals(1, observedCount);
    }
}
