package org.gwtbootstrap4.extras.respond.client;

/*
 * #%L
 * GwtBootstrap4
 * %%
 * Copyright (C) 2013 - 2014 GwtBootstrap4
 * %%
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
 * #L%
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Joshua Godi
 */
public interface RespondClientBundle extends ClientBundle {
    static final RespondClientBundle INSTANCE = GWT.create(RespondClientBundle.class);

    @Source("resource/js/html5shiv-3.7.0.min.cache.js")
    TextResource html5Shiv();

    @Source("resource/js/respond-1.4.2.min.cache.js")
    TextResource respond();
}