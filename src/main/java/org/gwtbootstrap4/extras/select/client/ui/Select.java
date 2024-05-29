package org.gwtbootstrap4.extras.select.client.ui;

/*
 * #%L
 * GwtBootstrap4
 * %%
 * Copyright (C) 2013 - 2016 GwtBootstrap4
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

import static org.gwtbootstrap4.extras.select.client.ui.SelectOptions.SHOW_TICK;

import java.util.Map.Entry;

import com.google.gwt.dom.client.OptionElement;

/**
 * Standard select box.
 *
 * @author Xiaodong Sun
 */
public class Select extends SelectBase<String> {

    public Select() {
    }

    @Override
    public final boolean isMultiple() {
        return false;
    }

    @Override
    protected void setSelectedValue(String value) {
        if (isAttached()) {
            super.setValue(getElement(), value);
        } else {
            for (Entry<OptionElement, Option> entry : itemMap.entrySet()) {
                Option opt = entry.getValue();
                opt.setSelected(opt.getValue().equals(value));
            }
        }
    }

    @Override
    public String getValue() {
        if (isAttached()) {
            return super.getValue(getElement());
        }
        for (Entry<OptionElement, Option> entry : itemMap.entrySet()) {
            Option opt = entry.getValue();
            if (opt.isSelected())
                return opt.getValue();
        }
        return null;
    }

    /**
     * Returns the selected item or <code>null</code> if no item is selected.
     *
     * @return the selected items list
     */
    public Option getSelectedItem() {
        for (Entry<OptionElement, Option> entry : itemMap.entrySet()) {
            Option opt = entry.getValue();
            if (opt.isSelected())
                return opt;
        }
        return null;
    }

}
