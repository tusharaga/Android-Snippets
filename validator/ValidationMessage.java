package com.fieldez.android.domain.validator;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationMessage {
	private int lineNumber;
	private Collection<FieldMessage> fieldMessages;
	
	public ValidationMessage() {		
	}
	public ValidationMessage(int lineNumber, Collection<FieldMessage> fieldMessages) {
		this.lineNumber = lineNumber;
		this.fieldMessages = fieldMessages;
	}
	public ValidationMessage(Collection<FieldMessage> fieldMessages) {
		this.fieldMessages = fieldMessages;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Collection<FieldMessage> getFieldMessages() {
		return fieldMessages;
	}
	public void setFieldMessages(Collection<FieldMessage> fieldMessages) {
		this.fieldMessages = fieldMessages;
	}	
	public void addFieldMessage(FieldMessage fieldMessage) {
		fieldMessages = fieldMessages == null ? 
				new ArrayList<FieldMessage>() : fieldMessages;
		fieldMessages.add(fieldMessage);		
	}
	public void addFieldMessages(Collection<FieldMessage> fieldMessages) {
		if (this.fieldMessages == null) {
			this.fieldMessages = fieldMessages;
		} else {
			this.fieldMessages.addAll(fieldMessages);
		}
	}
}
