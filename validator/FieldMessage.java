package com.fieldez.android.domain.validator;

public class FieldMessage {
	private String label;
	private String message;
	
	public FieldMessage(String label, String message) {
		this.label = label;
		this.message = message;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}
