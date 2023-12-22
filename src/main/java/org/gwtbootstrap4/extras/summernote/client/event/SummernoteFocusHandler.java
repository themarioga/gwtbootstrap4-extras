package org.gwtbootstrap4.extras.summernote.client.event;

/*
 * #%L
 * GwtBootstrap4
 * %%
 * Copyright (C) 2015 GwtBootstrap4
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

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link SummernoteInitEvent} events.
 *
 * @author Xiaodong Sun
 */
public interface SummernoteFocusHandler extends EventHandler {

    /**
     * Called when {@link SummernoteInitEvent} is fired.
     *
     * @param event the {@link SummernoteInitEvent} that was fired
     */
    void onSummernoteFocus(SummernoteFocusEvent event);
}