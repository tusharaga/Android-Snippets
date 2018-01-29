package com.fieldez.android.domain.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fieldez.android.domain.model.EntityLabel;

public class StringFieldValidator implements FieldValidator {
	private EntityLabel el;
	public StringFieldValidator(EntityLabel el) {
		this.el = el;
	}
	public String validate(String data) {
		if (el.isMandatory() && (data == null || data.trim().length() == 0)) {
			return el.getDisplayLabel() + " is mandatory";
		}
		String message = null;
		String dataType = el.getDataType();
		if (dataType != null && dataType.equals("NumericString") && 
				data != null && data.trim().length() > 0) {
			if (!data.trim().matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
				return el.getDisplayLabel() + " can only take numeric data";
			}
		}
		int maxLength = getLength(el.getMaxValues());
		int minLength = getLength(el.getMinValues());
		if (maxLength > -1 && data != null && data.trim().length() != 0 && data.length() > maxLength) {
			message =  el.getDisplayLabel() + " should be less than "+maxLength
					+" characters in length"; 
		}

		if (minLength > -1 && data != null && data.trim().length() != 0 && data.length() < minLength) {
			message =  el.getDisplayLabel() + " should be more than "+minLength
					+" characters in length"; 
		}

		if(el.getDataSource() != null && el.getDataSource().equalsIgnoreCase("EMAIL")) {
			if(!validateEmail(data)) {
				message = el.getDisplayLabel() + " is not a valid email";
			}
		}

		if(el.getBusinessDataType() != null && el.getBusinessDataType().equalsIgnoreCase("EMAIL")) {
			if(!validateEmail(data)) {
				message = el.getDisplayLabel() + " is not a valid email";
			}
		}

		return message;
	}

	private static int getLength(String value) {
		int length = -1;
		try {
			length = Integer.parseInt(value);
		} catch (Exception e) {

		}
		return length;
	}


	private boolean validateEmail(String eMail) {
		String regExpn =
				"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
						+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
						+"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
						+"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = eMail;
		if(!eMail.equals("")) {
			Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);
			if(!matcher.matches()) {
				return false;
			}
		}
		return true;
	}
}
