/*
 * Copyright 2020 Red Hat Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.registry.metrics;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.apicurio.registry.rest.RegistryExceptionMapper;

/**
 * @author eric.wittmann@gmail.com
 */
@ApplicationScoped
public class LivenessUtil {

    private static final Logger log = LoggerFactory.getLogger(PersistenceExceptionLivenessInterceptor.class);

    @Inject
    @ConfigProperty(name = "registry.liveness.errors.whitelist")
    List<String> whitelist;

    public boolean isIgnoreError(Throwable ex) {
        boolean ignored = this.isIgnored(ex) || this.isWhitelisted(ex);
        if (ignored) {
            log.debug("Ignored intercepted exception: " + ex.getClass().getName() + " :: " + ex.getMessage());
        }
        return ignored;
    }
    
    private boolean isIgnored(Throwable ex) {
        Set<Class<? extends Exception>> ignoredClasses = RegistryExceptionMapper.getIgnored();
        return ignoredClasses.contains(ex.getClass());
    }
    
    private boolean isWhitelisted(Throwable ex) {
        return this.whitelist != null && this.whitelist.contains(ex.getClass().getName());
    }

}