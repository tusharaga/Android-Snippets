package com.fieldez.android.domain.validator;

import com.fieldez.android.domain.model.EntityLabel;

public class NumericFieldValidator implements FieldValidator {
	private EntityLabel el;
	
	public NumericFieldValidator(EntityLabel el) {
		this.el = el;
	}

	public String validate(String data) {
		String message = null;
		long val = -1;
		boolean hasData = false;
		try {
			if (data != null && data.trim().length() != 0) {
				hasData = true;
				val = Long.parseLong(data);
			}
		} catch (Exception e) {
			message = el.getDisplayLabel()+" should be numeric";
		}
			int maxVal = -1;
			boolean hasMax = el.getMaxValues() != null ? true : false;
			try {				
				maxVal = Integer.parseInt(el.getMaxValues());
			} catch (Exception e) {				
			}
			int minVal = -1;
			boolean hasMin = el.getMinValues() != null ? true : false;
			try {
				minVal = Integer.parseInt(el.getMinValues());
			} catch (Exception e) {				
			}
			
			if (hasData && hasMax && hasMin && minVal == maxVal && minVal != val) {
				message = el.getDisplayLabel()+" should be "+minVal;
			} else if (hasMax && hasMin && hasData &&
					(val < minVal || val > maxVal)) {			
				message = el.getDisplayLabel()+" should be between "+
					minVal+" and "+maxVal;
			} else if (hasData && hasMin && val < minVal) {
				message = el.getDisplayLabel()+" should be more than "+minVal;
			} else if (hasData && hasMax && val > maxVal) {
				message = el.getDisplayLabel()+" should be less than "+maxVal;
			}
		return message;
	}
}
