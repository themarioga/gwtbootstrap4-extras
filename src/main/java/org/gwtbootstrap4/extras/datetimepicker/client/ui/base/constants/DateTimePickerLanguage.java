package org.gwtbootstrap4.extras.datetimepicker.client.ui.base.constants;

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

import com.google.gwt.resources.client.TextResource;

/**
 * @author Joshua Godi
 */
public enum DateTimePickerLanguage {
    AR("ar"),
    AZ("az"),
    BG("bg"),
    BN("bn"),
    CA("ca"),
    CS("cs"),
    DA("da"),
    DE("de"),
    EE("ee"),
    EL("el"),
    ES("es"),
    FI("fi"),
    FR("fr"),
    HE("he"),
    HR("hr"),
    HU("hu"),
    ID("id"),
    IS("is"),
    IT("it"),
    JA("ja"),
    KA("ka"),
    KO("ko"),
    LT("lt"),
    LV("lv"),
    MS("ms"),
    NB("nb"),
    NL("nl"),
    NO("no"),
    PL("pl"),
    PT_BR("pt-BR"),
    PT("pt"),
    RO("ro"),
    RS_LATIN("rs-latin"),
    RS("rs"),
    RU("ru"),
    SK("sk"),
    SL("sl"),
    SV("sv"),
    SW("sw"),
    TH("th"),
    TR("tr"),
    UA("ua"),
    UK("uk"),
    ZH_CN("zh-CN"),
    ZH_TW("zh-TW"),
    EN("en"); // Base language, don't need another file

    private final String code;

    private DateTimePickerLanguage(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
