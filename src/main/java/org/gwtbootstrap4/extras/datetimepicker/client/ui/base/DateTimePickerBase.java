package org.gwtbootstrap4.extras.datetimepicker.client.ui.base;

/*
 * #%L
 * GwtBootstrap4
 * %%
 * Copyright (C) 2016 GwtBootstrap4
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap4.client.shared.event.HideEvent;
import org.gwtbootstrap4.client.shared.event.HideHandler;
import org.gwtbootstrap4.client.shared.event.ShowEvent;
import org.gwtbootstrap4.client.shared.event.ShowHandler;
import org.gwtbootstrap4.client.ui.TextBox;
import org.gwtbootstrap4.client.ui.base.HasId;
import org.gwtbootstrap4.client.ui.base.HasPlaceholder;
import org.gwtbootstrap4.client.ui.base.HasReadOnly;
import org.gwtbootstrap4.client.ui.base.HasResponsiveness;
import org.gwtbootstrap4.client.ui.base.ValueBoxBase;
import org.gwtbootstrap4.client.ui.base.helper.StyleHelper;
import org.gwtbootstrap4.client.ui.base.mixin.BlankValidatorMixin;
import org.gwtbootstrap4.client.ui.base.mixin.ErrorHandlerMixin;
import org.gwtbootstrap4.client.ui.constants.DeviceSize;
import org.gwtbootstrap4.client.ui.form.error.ErrorHandler;
import org.gwtbootstrap4.client.ui.form.error.ErrorHandlerType;
import org.gwtbootstrap4.client.ui.form.error.HasErrorHandler;
import org.gwtbootstrap4.client.ui.form.validator.HasBlankValidator;
import org.gwtbootstrap4.client.ui.form.validator.HasValidators;
import org.gwtbootstrap4.client.ui.form.validator.ValidationChangedEvent.ValidationChangedHandler;
import org.gwtbootstrap4.client.ui.form.validator.Validator;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.constants.*;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.ChangeDateEvent;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.ChangeDateHandler;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.ChangeMonthEvent;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.ChangeMonthHandler;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.ChangeYearEvent;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.ChangeYearHandler;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.OutOfRangeEvent;
import org.gwtbootstrap4.extras.datetimepicker.client.ui.base.events.OutOfRangeHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Joshua Godi
 * @author Steven Jardine
 */
public class DateTimePickerBase extends Widget implements HasEnabled, HasReadOnly, HasId, HasResponsiveness, HasVisibility,
        HasPlaceholder, HasAutoClose, HasDaysOfWeekDisabled, HasEndDate, HasFormat, HasMinuteStep, HasShowTodayButton,
        HasShowClearButton, HasStartDate, HasDateTimePickerHandlers, HasLanguage, HasName, HasValue<Date>, HasPosition,
        HasViewMode, LeafValueEditor<Date>, HasEditorErrors<Date>, HasErrorHandler, HasValidators<Date>,
        HasBlankValidator<Date> {

    static class DatePickerValidatorMixin extends BlankValidatorMixin<DateTimePickerBase, Date> {
        private boolean showing = false;

        public void setShowing(boolean showing) {
            this.showing = showing;
        }

        public DatePickerValidatorMixin(DateTimePickerBase inputWidget, ErrorHandler errorHandler) {
            super(inputWidget, errorHandler);
        }

        @Override
        protected com.google.web.bindery.event.shared.HandlerRegistration setupBlurValidation() {
            return getInputWidget().addDomHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent event) {
                    getInputWidget().validate(!showing && getValidateOnBlur());
                }
            }, BlurEvent.getType());
        }
    }

    // Check http://www.gwtproject.org/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html
    // for more information on syntax
    private static final Map<Character, Character> DATE_TIME_FORMAT_MAP = new HashMap<Character, Character>();
    static {
        DATE_TIME_FORMAT_MAP.put('h', 'H'); // 12/24 hours
        DATE_TIME_FORMAT_MAP.put('H', 'h'); // 12/24 hours
        DATE_TIME_FORMAT_MAP.put('m', 'M'); // months
        DATE_TIME_FORMAT_MAP.put('i', 'm'); // minutes
        DATE_TIME_FORMAT_MAP.put('p', 'a'); // meridian
        DATE_TIME_FORMAT_MAP.put('P', 'a'); // meridian
    }

    private final TextBox textBox;
    private DateTimeFormat dateTimeFormat;
    private final DateTimeFormat startEndDateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
    private LeafValueEditor<Date> editor;
    private final ErrorHandlerMixin<Date> errorHandlerMixin = new ErrorHandlerMixin<Date>(this);
    private final DatePickerValidatorMixin validatorMixin = new DatePickerValidatorMixin(this,
                                                                                         errorHandlerMixin.getErrorHandler());
    /**
     * DEFAULT values
     */
    private String format = "mm/dd/yyyy hh:ii";
    private DateTimePickerDayOfWeek[] daysOfWeekDisabled = {};
    private boolean keepOpen = false;
    private boolean showTodayButton = false;
    private boolean showClearButton = false;
    private int minuteStep = 5;
    private DateTimePickerViewMode viewMode = DateTimePickerViewMode.DAY;
    private DateTimePickerLanguage language = DateTimePickerLanguage.EN;
    private DateTimePickerHorizontalPosition horizontalPosition = DateTimePickerHorizontalPosition.RIGHT;
    private DateTimePickerVerticalPosition verticalPosition = DateTimePickerVerticalPosition.BOTTOM;
    private String minDate = null;
    private String maxDate = null;

    public DateTimePickerBase() {
        textBox = new TextBox();
        setElement((Element) textBox.getElement());
        setFormat(format);
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public void setAlignment(final ValueBoxBase.TextAlignment align) {
        textBox.setAlignment(align);
    }

    /** {@inheritDoc} */
    @Override
    public void setPlaceholder(final String placeHolder) {
        textBox.setPlaceholder(placeHolder);
    }

    /** {@inheritDoc} */
    @Override
    public String getPlaceholder() {
        return textBox.getPlaceholder();
    }

    public void setReadOnly(final boolean readOnly) {
        textBox.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return textBox.isReadOnly();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    /** {@inheritDoc} */
    @Override
    public void setEnabled(final boolean enabled) {
        textBox.setEnabled(enabled);
    }

    /** {@inheritDoc} */
    @Override
    public void setId(final String id) {
        textBox.setId(id);
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return textBox.getId();
    }

    /** {@inheritDoc} */
    @Override
    public void setName(final String name) {
        textBox.setName(name);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return textBox.getName();
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        StyleHelper.setVisibleOn(this, deviceSize);
    }

    /** {@inheritDoc} */
    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        StyleHelper.setHiddenOn(this, deviceSize);
    }

    /** {@inheritDoc} */
    @Override
    public void setLanguage(final DateTimePickerLanguage language) {
        this.language = language;
    }

    /** {@inheritDoc} */
    @Override
    public DateTimePickerLanguage getLanguage() {
        return language;
    }

    /** {@inheritDoc} */
    @Override
    public void setHorizontalPosition(final DateTimePickerHorizontalPosition horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    /** {@inheritDoc} */
    @Override
    public DateTimePickerHorizontalPosition getHorizontalPosition() {
        return horizontalPosition;
    }

    /** {@inheritDoc} */
    @Override
    public void setVerticalPosition(final DateTimePickerVerticalPosition verticalPosition) {
        this.verticalPosition = verticalPosition;
    }

    /** {@inheritDoc} */
    @Override
    public DateTimePickerVerticalPosition getVerticalPosition() {
        return verticalPosition;
    }

    @Override
    public void setViewMode(DateTimePickerViewMode dateTimePickerViewMode) {
        this.viewMode = dateTimePickerViewMode;
    }


    /**
     * Call this whenever changing any settings: minView, startView, format, etc. If you are changing
     * format and date value, the updates must take in such order:
     * <p/>
     * 1. DateTimePicker.reload()
     * 2. DateTimePicker.setValue(newDate); // Date newDate.
     * <p/>
     * Otherwise date value is not updated.
     */
    public void reload() {
        configure();
    }

    public void show() {
        show(getElement());
    }

    public void hide() {
        hide(getElement());
    }

    /** {@inheritDoc} */
    @Override
    public void setKeepOpen(final boolean keepOpen) {
        this.keepOpen = keepOpen;
    }

    /** {@inheritDoc} */
    @Override
    public void onShow(final Event e) {
        validatorMixin.setShowing(true);
        fireEvent(new ShowEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addShowHandler(final ShowHandler showHandler) {
        return addHandler(showHandler, ShowEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onHide(final Event e) {
        validatorMixin.setShowing(false);
        validate(getValidateOnBlur());
        fireEvent(new HideEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addHideHandler(final HideHandler hideHandler) {
        return addHandler(hideHandler, HideEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeDate(final Event e) {
        fireEvent(new ChangeDateEvent(e));
        ValueChangeEvent.fire(DateTimePickerBase.this, getValue());
        hide();
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeDateHandler(final ChangeDateHandler changeDateHandler) {
        return addHandler(changeDateHandler, ChangeDateEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeYear(final Event e) {
        fireEvent(new ChangeYearEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeYearHandler(final ChangeYearHandler changeYearHandler) {
        return addHandler(changeYearHandler, ChangeYearEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeMonth(final Event e) {
        fireEvent(new ChangeMonthEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeMonthHandler(final ChangeMonthHandler changeMonthHandler) {
        return addHandler(changeMonthHandler, ChangeMonthEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onOutOfRange(final Event e) {
        fireEvent(new OutOfRangeEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addOutOfRangeHandler(final OutOfRangeHandler outOfRangeHandler) {
        return addHandler(outOfRangeHandler, OutOfRangeEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void setDaysOfWeekDisabled(final DateTimePickerDayOfWeek... daysOfWeekDisabled) {
        this.daysOfWeekDisabled = daysOfWeekDisabled;
    }

    /** {@inheritDoc} */
    @Override
    public void setStartDate(final Date startDate) {
        // Has to be in the format YYYY-MM-DD
        setStartDate(startEndDateFormat.format(startDate));
    }

    /** {@inheritDoc} */
    @Override
    public void setStartDate(final String startDate) {
        this.minDate = startDate;
    }

    /** {@inheritDoc} */
    @Override
    public void clearStartDate() {
        this.minDate = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setEndDate(final Date endDate) {
        this.maxDate = startEndDateFormat.format(endDate);
    }

    /** {@inheritDoc} */
    @Override
    public void setEndDate(final String endDate) {
        this.maxDate = endDate;
    }

    /** {@inheritDoc} */
    @Override
    public void clearEndDate() {
        this.maxDate = null;
    }

    /** {@inheritDoc} */
    @Override
    public void setMinuteStep(final int minuteStep) {
        this.minuteStep = minuteStep;
    }

    /** {@inheritDoc} */
    @Override
    public void setShowTodayButton(final boolean showTodayButton) {
        this.showTodayButton = showTodayButton;
    }

    /** {@inheritDoc} */
    @Override
    public void setShowClearButton(final boolean showClearButton) {
        this.showClearButton = showClearButton;
    }

    /**
     * Convert GWT date time format to bootstrap date time format
     *
     * @param format date time format using GWT notation
     * @return date time format using bootstrap notation
     */
    private static String toBootstrapDateFormat(final String format) {
        String bootstrap_format = format;

        // Replace long day name "EEEE" with "DD"
        bootstrap_format = bootstrap_format.replace("EEEE", "DD");
        // Replace short day name "EE" with "DD"
        bootstrap_format = bootstrap_format.replaceAll("E{1,3}", "D");
        // Replace minutes "m" with "i"
        bootstrap_format = bootstrap_format.replaceAll("m", "i");
        // Replace "H" with "h" and vice versa
        bootstrap_format = bootstrap_format.replaceAll("H", "Q");
        bootstrap_format = bootstrap_format.replaceAll("h", "H");
        bootstrap_format = bootstrap_format.replaceAll("Q", "h");
        // Replace "a" with "P" for AM/PM markers
        bootstrap_format = bootstrap_format.replaceAll("a", "P");

        // If there are at least 3 Ms there is month name in wording
        if (bootstrap_format.contains("MMM")) {
            // Replace long date month "MMMM" with "MM"
            bootstrap_format = bootstrap_format.replace("MMMM", "MM");
            // Replace month name "MMM" with "M"
            bootstrap_format = bootstrap_format.replace("MMM", "M");
        }
        else {
            // Replace month number with leading 0 "MM" with "mm"
            bootstrap_format = bootstrap_format.replace("MM", "mm");
            // Replace month number "M" with "m"
            bootstrap_format = bootstrap_format.replace("M", "m");
        }
        if (!bootstrap_format.contains("yy")) {
            // Replace full year format "y" with "yyyy"
            bootstrap_format = bootstrap_format.replace("y", "yyyy");
        }

        return bootstrap_format;
    }

    /**
     * Sets format of the date using GWT notation
     *
     * @param format date time format in GWT notation
     */
    public void setGWTFormat(final String format) {
        this.format = toBootstrapDateFormat(format);

        // Get the old value
        final Date oldValue = getValue();

        // Make the new DateTimeFormat
        this.dateTimeFormat = DateTimeFormat.getFormat(format);

        if (oldValue != null) {
            setValue(oldValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setFormat(final String format) {
        this.format = format;

        // Get the old value
        final Date oldValue = getValue();

        // Make the new DateTimeFormat
        setDateTimeFormat(format);

        if (oldValue != null) {
            setValue(oldValue);
        }
    }

    private void setDateTimeFormat(final String format) {
        final StringBuilder fb = new StringBuilder(format);
        for (int i = 0; i < fb.length(); i++) {
            if (DATE_TIME_FORMAT_MAP.containsKey(fb.charAt(i))) {
                fb.setCharAt(i, DATE_TIME_FORMAT_MAP.get(fb.charAt(i)));
            }
        }

        this.dateTimeFormat = DateTimeFormat.getFormat(fb.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Date getValue() {
        try {
            return dateTimeFormat != null && textBox.getValue() != null ? dateTimeFormat.parse(textBox.getValue()) : null;
        } catch (final Exception e) {
            return null;
        }
    }

    public String getBaseValue() {
        return textBox.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> dateValueChangeHandler) {
        textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                ValueChangeEvent.fire(DateTimePickerBase.this, getValue());
            }
        });
        return addHandler(dateValueChangeHandler, ValueChangeEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Date value) {
        setValue(value, false);
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Date value, final boolean fireEvents) {
        errorHandlerMixin.clearErrors();
        textBox.setValue(value != null ? dateTimeFormat.format(value) : null);

        configure();

        if (fireEvents) {
            ValueChangeEvent.fire(DateTimePickerBase.this, value);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        super.onLoad();
        configure();
    }

    /** {@inheritDoc} */
    @Override
    protected void onUnload() {
        super.onUnload();
        if (isInstantiated(getElement())) {
            remove(getElement());
        }
    }

    protected void configure() {
        getElement().setAttribute("data-date-format", format);

        // If configuring not for the first time, datetimepicker must be removed first.
        if (isInstantiated(getElement())) {
            this.remove(getElement());
        }

        configure(getElement(), format, language.getCode(), toDaysOfWeekDisabledString(daysOfWeekDisabled), keepOpen,
                viewMode.getValue(),
                minDate != null ? minDate.toString() : "false", maxDate != null ? maxDate.toString() : "false",
                showTodayButton, showClearButton, minuteStep,
                horizontalPosition.getPosition(), verticalPosition.getPosition());
    }

    protected void execute(final String cmd) {
        execute(getElement(), cmd);
    }

    private native void execute(Element e, String cmd) /*-{
        $wnd.jQuery(e).datetimepicker(cmd);
    }-*/;

    private native void remove(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('destroy');
    }-*/;

    private native void clear(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('clear');
    }-*/;

    private native void toggle(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('toggle');
    }-*/;

    private native void show(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('show');
    }-*/;

    private native void hide(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('hide');
    }-*/;

    private native void disable(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('disable');
    }-*/;

    private native void enable(Element e) /*-{
        $wnd.jQuery(e).datetimepicker('enable');
    }-*/;

    protected native void configure(Element e, String format, String locale,
                                    String daysOfWeekDisabled, boolean keepOpen, String viewMode,
                                    String minDate, String maxDate, boolean showTodayButton, boolean showClearButton,
                                    int stepping, String horizontalPosition, String verticalPosition) /*-{
        var that = this;
        $wnd.jQuery(e).datetimepicker({
            format: format,
            locale: locale,
            daysOfWeekDisabled: daysOfWeekDisabled.split(","),
            keepOpen: keepOpen,
            viewMode: viewMode,
            minDate: (minDate === "false") ? false : minDate,
            maxDate: (maxDate === "false") ? false : maxDate,
            showTodayButton: showTodayButton,
            showClear: showClearButton,
            stepping: stepping,
            widgetPositioning: {
                horizontal: horizontalPosition,
                vertical: verticalPosition
            }
        })
            .on('show', function (e) {
                that.@org.gwtbootstrap4.extras.datetimepicker.client.ui.base.DateTimePickerBase::onShow(Lcom/google/gwt/user/client/Event;)(e);
            })
            .on("hide", function (e) {
                that.@org.gwtbootstrap4.extras.datetimepicker.client.ui.base.DateTimePickerBase::onHide(Lcom/google/gwt/user/client/Event;)(e);
            })
            .on("changeDate", function (e) {
                that.@org.gwtbootstrap4.extras.datetimepicker.client.ui.base.DateTimePickerBase::onChangeDate(Lcom/google/gwt/user/client/Event;)(e);
            })
            .on("changeYear", function (e) {
                that.@org.gwtbootstrap4.extras.datetimepicker.client.ui.base.DateTimePickerBase::onChangeYear(Lcom/google/gwt/user/client/Event;)(e);
            })
            .on("changeMonth", function (e) {
                that.@org.gwtbootstrap4.extras.datetimepicker.client.ui.base.DateTimePickerBase::onChangeMonth(Lcom/google/gwt/user/client/Event;)(e);
            })
            .on("outOfRange", function (e) {
                that.@org.gwtbootstrap4.extras.datetimepicker.client.ui.base.DateTimePickerBase::onOutOfRange(Lcom/google/gwt/user/client/Event;)(e);
            });
    }-*/;

    private native boolean isInstantiated(Element e) /*-{
        if ($wnd.jQuery(e).data("DateTimePicker")) return true;
        else return false;
    }-*/;

    protected String toDaysOfWeekDisabledString(final DateTimePickerDayOfWeek... dateTimePickerDayOfWeeks) {
        this.daysOfWeekDisabled = dateTimePickerDayOfWeeks;

        final StringBuilder builder = new StringBuilder();

        if (dateTimePickerDayOfWeeks != null) {
            int i = 0;
            for (final DateTimePickerDayOfWeek dayOfWeek : dateTimePickerDayOfWeeks) {
                builder.append(dayOfWeek.getValue());

                i++;
                if (i < dateTimePickerDayOfWeeks.length) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    @Override
    public com.google.web.bindery.event.shared.HandlerRegistration addValidationChangedHandler(ValidationChangedHandler handler) {
        return validatorMixin.addValidationChangedHandler(handler);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getAllowBlank() {
        return validatorMixin.getAllowBlank();
    }

    /** {@inheritDoc} */
    @Override
    public void setAllowBlank(boolean allowBlank) {
        validatorMixin.setAllowBlank(allowBlank);
    }

    /** {@inheritDoc} */
    @Override
    public void addValidator(Validator<Date> validator) {
        validatorMixin.addValidator(validator);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getValidateOnBlur() {
        return validatorMixin.getValidateOnBlur();
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeValidator(Validator<Date> validator) {
        return validatorMixin.removeValidator(validator);
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        validatorMixin.reset();
    }

    /** {@inheritDoc} */
    @Override
    public void setValidateOnBlur(boolean validateOnBlur) {
        validatorMixin.setValidateOnBlur(validateOnBlur);
    }

    /** {@inheritDoc} */
    @Override
    public void setValidators(@SuppressWarnings("unchecked") Validator<Date>... validators) {
        validatorMixin.setValidators(validators);
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate() {
        return validatorMixin.validate();
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate(boolean show) {
        return validatorMixin.validate(show);
    }

    /** {@inheritDoc} */
    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandlerMixin.getErrorHandler();
    }

    /** {@inheritDoc} */
    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        errorHandlerMixin.setErrorHandler(errorHandler);
        validatorMixin.setErrorHandler(errorHandler);
    }

    /** {@inheritDoc} */
    @Override
    public ErrorHandlerType getErrorHandlerType() {
        return errorHandlerMixin.getErrorHandlerType();
    }

    /** {@inheritDoc} */
    @Override
    public void setErrorHandlerType(ErrorHandlerType errorHandlerType) {
        errorHandlerMixin.setErrorHandlerType(errorHandlerType);
    }

    /** {@inheritDoc} */
    @Override
    public void showErrors(List<EditorError> errors) {
        errorHandlerMixin.showErrors(errors);
    }

}
