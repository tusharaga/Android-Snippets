package com.fieldez.android.domain.validator;

import com.fieldez.android.domain.model.EntityLabel;

public class FlexFieldValidator {
	private final EntityLabel el;
	
	public FlexFieldValidator(EntityLabel entityLabel) {
		this.el = entityLabel;
	}
	public FieldMessage validate(String value) {
		if (el == null) {
			return null;
		}
		if (el.isMandatory() && (value == null || value.trim().length() == 0)) {
			return new FieldMessage(el.getDisplayLabel(),
					el.getDisplayLabel()+" is mandatory");
		}
		FieldValidator fv = null;
		String dataType = el.getDataType();
		if (dataType == null) {
			return null;
		}
		if (dataType.equalsIgnoreCase("Alphanumeric") || dataType.equalsIgnoreCase("NumericString")) {
			fv = new StringFieldValidator(el);
		}  else if (dataType.equalsIgnoreCase("Date") || dataType.equalsIgnoreCase("DateTime")) {
			fv = new DateFieldValidator(el);
		} else if (dataType.equalsIgnoreCase("Numeric")) {
			fv = new NumericFieldValidator(el);
		} 
		else if (dataType.equalsIgnoreCase("List")) {
			fv = new ListFieldValidator(el);
		}
		else {
			
		}
		String message = null;
		if (fv != null) {
			message = fv.validate(value);
		}
		return message == null ? null : 
			new FieldMessage(el.getDisplayLabel(), message);
	}
}
