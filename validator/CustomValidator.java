package com.fieldez.android.domain.validator;

import android.content.Context;

import com.fieldez.android.domain.model.BaseModel;

public abstract class CustomValidator {
	public abstract String validate(BaseModel baseModel, String mode, Context context);
	
	public boolean checkLineItemDependency(BaseModel model) {
		return false;
	}
}
