package com.fieldez.android.domain.validator;

import com.fieldez.android.domain.model.EntityLabel;

public class ListFieldValidator implements FieldValidator{
	
	private EntityLabel el;
	
	public ListFieldValidator(EntityLabel el) {
		this.el = el;
	}	
	public String validate(String data) {
	
		if (el.isMandatory() && (data == null || data.trim().length() == 0)) {
			return el.getDisplayLabel() + " is mandatory";
		}
		else{
			return null;
		}
	}
}
