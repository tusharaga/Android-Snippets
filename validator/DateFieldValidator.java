package com.fieldez.android.domain.validator;

import java.util.Date;

import android.text.format.DateUtils;
import android.widget.DatePicker;

import com.fieldez.android.domain.model.EntityLabel;
import com.fieldez.android.util.DateUtil;

public class DateFieldValidator implements FieldValidator {	
	private EntityLabel el;
	public DateFieldValidator(EntityLabel el) {
		this.el = el;
	}

	public String validate(String data) {
		if (el.isMandatory() && (data == null || data.trim().length() == 0)) {
			return el.getDisplayLabel() + " is mandatory";
		}
		
		if (data == null || data.trim().length() == 0) {
			return null;
		}
		Date date = null;
		try {
			if(el.getDataType().equalsIgnoreCase("Date")) {
				date = DateUtil.stringToDateSendFormat(data);
			} else if(el.getDataType().equalsIgnoreCase("DateTime")) {
				date = DateUtil.stringToDateTimeSendFormat(data);
			}
			
		} catch (Exception e) {
			
		}
		
		String msg = null;
		if(el.getMinValues() != null) {
			if (el.getMinValues().equals("CURRENT") && 
					!DateUtil.isSameDay(date)) {
				msg = el.getDisplayLabel()+" should be the current date"; 
			} else if (el.getMinValues().equals("PAST") 
					&& !DateUtil.isPastDay(date)) {
				msg = el.getDisplayLabel()+" should be a past date"; 
			} else if (el.getMinValues().equals("FUTURE") 
					&& !DateUtil.isFutureDay(date)) {
				msg = el.getDisplayLabel()+" should be a future date"; 
			} else if (el.getMinValues().equals("CURRENT_AND_PAST") 
					&& !DateUtil.isPastOrCurrentDay(date)) {
				msg = el.getDisplayLabel()+" should be a either past/current date"; 
			} else if (el.getMinValues().equals("CURRENT_AND_FUTURE")
					&& !DateUtil.isFutureOrCurrentDay(date)) {
				msg = el.getDisplayLabel()+" should be a either future/current date";
			}
		}
		return msg;
	}
}
