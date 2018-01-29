package com.fieldez.android.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldez.android.domain.dto.CustomItemDTO;
import com.fieldez.android.domain.model.ComplaintWorkflow;
import com.fieldez.android.domain.model.Customer;
import com.fieldez.android.domain.model.DependentList;
import com.fieldez.android.domain.model.EntityLabel;
import com.fieldez.android.domain.model.Product;
import com.fieldez.android.domain.model.User;
import com.fieldez.android.domain.model.WorkLocation;
import com.fieldez.android.domain.repository.EntityLabelRepository;
import com.fieldez.android.domain.repository.ProductRepository;
import com.fieldez.android.domain.repository.WorkLocationRepository;
import com.fieldez.android.domain.service.CustomerService;
import com.fieldez.android.domain.service.DependentListService;
import com.fieldez.android.domain.service.IntentIntegrator;
import com.fieldez.android.domain.service.UserService;
import com.fieldez.android.domain.service.impl.CustomerServiceImpl;
import com.fieldez.android.domain.service.impl.DependentListServiceImpl;
import com.fieldez.android.domain.service.impl.UserServiceImpl;
import com.fieldez.android.epabx.CustomerCallFactory;
import com.fieldez.android.epabx.ExotelEpabxConnector;
import com.fieldez.android.epabx.GenericCallConnector;
import com.fieldez.android.ui.AbstractActivity;
import com.fieldez.android.ui.CustomDateTimeDialogWrapper;
import com.fieldez.android.ui.DelayAutoCompleteTextView;
import com.fieldez.android.ui.PlaceAutocompleteAdapter;
import com.fieldez.android.ui.components.tablegrid.TableEditText;
import com.fieldez.android.ui.components.tablegrid.TableTextView;
import com.fieldez.mobile.ApplicationContext;
import com.fieldez.mobile.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class LabelUtil extends Activity implements MultiselectOnSelectListner, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
	private static Context _context = ApplicationContext.getContext();
	public static final String COMPLAINT_CALL_NUMBER = "Complaint.callNo";
	public static final String COMPLAINT_ENTITY_NAME = "Complaint.entityname";
	public static final String COMPLAINT_WORKFLOW = "Complaint.workflow";
	public static final String COMPLAINT_STATUS = "Complaint.status";
	public static final String COMPLAINT_CUST_NAME = "Complaint.customerName";
	public static final String COMPLAINT_CUST_ADD1 = "Complaint.customerAddress1";
	public static final String COMPLAINT_CUST_ADD2 = "Complaint.customerAddress2";
	public static final String COMPLAINT_CUST_CONT_NO = "Complaint.customerContactNo";
	public static final String COMPLAINT_CUST_CONT = "Complaint.customerContact";
	public static final String COMPLAINT_PROBLEM = "Complaint.problemDescription";
	public static final String COMPLAINT_PROD_NAME = "Complaint.productName";
	public static final String COMPLAINT_MODEL_NAME = "Complaint.modelName";
	public static final String COMPLAINT_PROD_SER_NO = "Complaint.productSerialNo";
	public static final String COMPLAINT_PROD_WARRANTY = "Complaint.productWarrantyType";
	public static final String COMPLAINT_SPL_INST = "Complaint.specialInstructions";
	public static final String COMPLAINT_REMARKS = "Complaint.remarks";
	public static final String COMPLAINTACTIVITY_REMARKS = "ComplaintActivity.remarks";
	public static final String COMPLAINT_ENGINEER = "Complaint.engineer";
	public static final String COMPLAINT_START_TIME = "Complaint.startTime";
	public static final String COMPLAINT_CALL_DURATION = "Complaint.callDuration";
	public static final String COMPLAINT_CALL = "Complaint.call";
	public static final String COMPLAINTACTIVITY_ACTION = "ComplaintActivity.action";
	public static final String COMPLAINTACTIVITY_PURPOSE = "ComplaintActivity.purpose";
	public static final String COMPLAINTACTIVITY_ACTION_ON = "ComplaintActivity.actionOn";
	public static final String COMPLAINTACTIVITY_CUSTOMER_SIGN = "ComplaintActivity.customerSign";
	public static final String COMPLAINT_ATTRIBUTE1 = "Complaint.attribute1";
	public static final String COMPLAINT_ATTRIBUTE2 = "Complaint.attribute2";
	public static final String COMPLAINT_ATTRIBUTE3 = "Complaint.attribute3";
	public static final String COMPLAINT_ATTRIBUTE4 = "Complaint.attribute4";
	public static final String COMPLAINT_ATTRIBUTE5 = "Complaint.attribute5";
	public static final String COMPLAINT_ATTRIBUTE6 = "Complaint.attribute6";
	public static final String COMPLAINT_ATTRIBUTE7 = "Complaint.attribute7";
	public static final String COMPLAINT_ATTRIBUTE8 = "Complaint.attribute8";
	public static final String COMPLAINT_ATTRIBUTE9 = "Complaint.attribute9";
	public static final String COMPLAINT_ATTRIBUTE10 = "Complaint.attribute10";
	public static final String COMPLAINT_ATTRIBUTE11 = "Complaint.attribute11";
	public static final String COMPLAINT_ATTRIBUTE12 = "Complaint.attribute12";
	public static final String COMPLAINT_ATTRIBUTE13 = "Complaint.attribute13";
	public static final String COMPLAINT_ATTRIBUTE14 = "Complaint.attribute14";
	public static final String COMPLAINT_ATTRIBUTE15 = "Complaint.attribute15";
	public static final String COMPLAINT_ATTRIBUTE16 = "Complaint.attribute16";
	public static final String COMPLAINT_ATTRIBUTE17 = "Complaint.attribute17";
	public static final String COMPLAINT_ATTRIBUTE18 = "Complaint.attribute18";
	public static final String COMPLAINT_ATTRIBUTE19 = "Complaint.attribute19";
	public static final String COMPLAINT_ATTRIBUTE20 = "Complaint.attribute20";
	public static final String COMPLAINT_ATTRIBUTE21 = "Complaint.attribute21";
	public static final String COMPLAINT_ATTRIBUTE22 = "Complaint.attribute22";
	public static final String COMPLAINT_ATTRIBUTE23 = "Complaint.attribute23";
	public static final String COMPLAINT_ATTRIBUTE24 = "Complaint.attribute24";
	public static final String COMPLAINT_ATTRIBUTE25 = "Complaint.attribute25";
	public static final String COMPLAINT_ATTRIBUTE26 = "Complaint.attribute26";
	public static final String COMPLAINT_ATTRIBUTE27 = "Complaint.attribute27";
	public static final String COMPLAINT_ATTRIBUTE28 = "Complaint.attribute28";
	public static final String COMPLAINT_ATTRIBUTE29 = "Complaint.attribute29";
	public static final String COMPLAINT_ATTRIBUTE30 = "Complaint.attribute30";
	public static final String COMPLAINT_WORKLOCATION = "Complaint.workLocation";

	public static final String COMPLAINT_CUST_MIDDLE_NAME = "Complaint.customerMiddleName";
	public static final String COMPLAINT_CUST_LAST_NAME = "Complaint.customerLastName";

	public static final String ACTIVITY_ATTRIBUTE1 = "Activity.attribute1";
	public static final String ACTIVITY_ATTRIBUTE2 = "Activity.attribute2";
	public static final String ACTIVITY_ATTRIBUTE3 = "Activity.attribute3";
	public static final String ACTIVITY_ATTRIBUTE4 = "Activity.attribute4";
	public static final String ACTIVITY_ATTRIBUTE5 = "Activity.attribute5";
	public static final String ACTIVITY_ATTRIBUTE6 = "Activity.attribute6";
	public static final String ACTIVITY_ATTRIBUTE7 = "Activity.attribute7";
	public static final String ACTIVITY_ATTRIBUTE8 = "Activity.attribute8";
	public static final String ACTIVITY_ATTRIBUTE9 = "Activity.attribute9";
	public static final String ACTIVITY_ATTRIBUTE10 = "Activity.attribute10";
	public static final String ACTIVITY_ATTRIBUTE11 = "Activity.attribute11";
	public static final String ACTIVITY_ATTRIBUTE12 = "Activity.attribute12";
	public static final String ACTIVITY_ATTRIBUTE13 = "Activity.attribute13";
	public static final String ACTIVITY_ATTRIBUTE14 = "Activity.attribute14";
	public static final String ACTIVITY_ATTRIBUTE15 = "Activity.attribute15";
	public static final String ACTIVITY_ATTRIBUTE16 = "Activity.attribute16";
	public static final String ACTIVITY_ATTRIBUTE17 = "Activity.attribute17";
	public static final String ACTIVITY_ATTRIBUTE18 = "Activity.attribute18";
	public static final String ACTIVITY_ATTRIBUTE19 = "Activity.attribute19";
	public static final String ACTIVITY_ATTRIBUTE20 = "Activity.attribute20";
	public static final String ACTIVITY_ATTRIBUTE21 = "Activity.attribute21";
	public static final String ACTIVITY_ATTRIBUTE22 = "Activity.attribute22";
	public static final String ACTIVITY_ATTRIBUTE23 = "Activity.attribute23";
	public static final String ACTIVITY_ATTRIBUTE24 = "Activity.attribute24";
	public static final String ACTIVITY_ATTRIBUTE25 = "Activity.attribute25";
	public static final String ACTIVITY_ATTRIBUTE26 = "Activity.attribute26";
	public static final String ACTIVITY_ATTRIBUTE27 = "Activity.attribute27";
	public static final String ACTIVITY_ATTRIBUTE28 = "Activity.attribute28";
	public static final String ACTIVITY_ATTRIBUTE29 = "Activity.attribute29";
	public static final String ACTIVITY_ATTRIBUTE30 = "Activity.attribute30";
	public static final String ACTIVITY_ATTRIBUTE31 = "Activity.attribute31";
	public static final String ACTIVITY_ATTRIBUTE32 = "Activity.attribute32";
	public static final String ACTIVITY_ATTRIBUTE33 = "Activity.attribute33";
	public static final String ACTIVITY_ATTRIBUTE34 = "Activity.attribute34";
	public static final String ACTIVITY_ATTRIBUTE35 = "Activity.attribute35";
	public static final String ACTIVITY_ATTRIBUTE36 = "Activity.attribute36";
	public static final String ACTIVITY_ATTRIBUTE37 = "Activity.attribute37";
	public static final String ACTIVITY_ATTRIBUTE38 = "Activity.attribute38";
	public static final String ACTIVITY_ATTRIBUTE39 = "Activity.attribute39";
	public static final String ACTIVITY_ATTRIBUTE40 = "Activity.attribute40";
	public static final String ACTIVITY_ATTRIBUTE41 = "Activity.attribute41";
	public static final String ACTIVITY_ATTRIBUTE42 = "Activity.attribute42";
	public static final String ACTIVITY_ATTRIBUTE43 = "Activity.attribute43";
	public static final String ACTIVITY_ATTRIBUTE44 = "Activity.attribute44";
	public static final String ACTIVITY_ATTRIBUTE45 = "Activity.attribute45";
	public static final String ACTIVITY_ATTRIBUTE46 = "Activity.attribute46";
	public static final String ACTIVITY_ATTRIBUTE47 = "Activity.attribute47";
	public static final String ACTIVITY_ATTRIBUTE48 = "Activity.attribute48";
	public static final String ACTIVITY_ATTRIBUTE49 = "Activity.attribute49";
	public static final String ACTIVITY_ATTRIBUTE50 = "Activity.attribute50";

	public static final String ACTIVITY_REMARKS = "Activity.remarks";

	public static final String COMPLAINT_LINE_ITEM_EXTERNALID = "ComplaintLineItem.externalId";
	public static final String COMPLAINT_LINE_ITEM_TYPE = "ComplaintLineItem.type";
	public static final String COMPLAINT_LINE_ITEM_QUANTITY = "ComplaintLineItem.quantity";
	public static final String COMPLAINT_LINE_ITEM_UNIT = "ComplaintLineItem.unit";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE1 = "ComplaintLineItem.attribute1";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE2 = "ComplaintLineItem.attribute2";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE3 = "ComplaintLineItem.attribute3";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE4 = "ComplaintLineItem.attribute4";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE5 = "ComplaintLineItem.attribute5";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE6 = "ComplaintLineItem.attribute6";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE7 = "ComplaintLineItem.attribute7";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE8 = "ComplaintLineItem.attribute8";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE9 = "ComplaintLineItem.attribute9";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE10 = "ComplaintLineItem.attribute10";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE11 = "ComplaintLineItem.attribute11";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE12 = "ComplaintLineItem.attribute12";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE13 = "ComplaintLineItem.attribute13";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE14 = "ComplaintLineItem.attribute14";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE15 = "ComplaintLineItem.attribute15";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE16 = "ComplaintLineItem.attribute16";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE17 = "ComplaintLineItem.attribute17";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE18 = "ComplaintLineItem.attribute18";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE19 = "ComplaintLineItem.attribute19";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE20 = "ComplaintLineItem.attribute20";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE21 = "ComplaintLineItem.attribute21";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE22 = "ComplaintLineItem.attribute22";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE23 = "ComplaintLineItem.attribute23";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE24 = "ComplaintLineItem.attribute24";
	public static final String COMPLAINT_LINE_ITEM_ATTRIBUTE25 = "ComplaintLineItem.attribute25";

	public static final String PRODUCT_NAME = "Name";
	public static final String PRODUCT_CATEGORY = "Category";
	public static final String PRODUCT_DESCRIPTION = "Description";
	public static final String PRODUCT_UNIT_OF_MEASURE = "Unit Of Measure";
	public static final String PRODUCT_ITEM_NO = "Item No";
	public static final String PRODUCT_MANUFACTURER = "Manufacturer";
	public static final String PRODUCT_CODE = "Code";
	public static final String PRODUCT_PRICE = "Price";
	public static final String PRODUCT_QUANTITY = "Quantity";
	public static final String PRODUCT_TOTAL_PRICE = "Total Price";
	public static final String PRODUCT_ID = "product_id";

	// customer entities 

	public static final String CUSTOMER_COUNTRY = "Customer.country";
	public static final String CUSTOMER_MOBILE_NO = "Customer.mobileNo";
	public static final String CUSTOMER_ORGANIZATION_NAME = "Customer.organizationName";
	public static final String CUSTOMER_EMAIL_ID = "Customer.emailId";
	public static final String CUSTOMER_CITY = "Customer.city";
	public static final String CUSTOMER_DOB = "Customer.dateOfBirth";
	public static final String CUSTOMER_STATE = "Customer.state";
	public static final String CUSTOMER_EXTERNAL_ID = "Customer.customerExternalId";
	public static final String CUSTOMER_GENDER = "Customer.gender";
	public static final String CUSTOMER_CONTACT_NO = "Customer.contactNo";
	public static final String CUSTOMER_PINCODE = "Customer.pincode";
	public static final String CUSTOMER_ATTRIBUTE1 = "Customer.attribute1";
	public static final String CUSTOMER_ATTRIBUTE2 = "Customer.attribute2";
	public static final String CUSTOMER_ATTRIBUTE3 = "Customer.attribute3";
	public static final String CUSTOMER_ATTRIBUTE4 = "Customer.attribute4";
	public static final String CUSTOMER_ATTRIBUTE5 = "Customer.attribute5";
	public static final String CUSTOMER_ATTRIBUTE6 = "Customer.attribute6";
	public static final String CUSTOMER_ATTRIBUTE7 = "Customer.attribute7";
	public static final String CUSTOMER_ATTRIBUTE8 = "Customer.attribute8";
	public static final String CUSTOMER_ATTRIBUTE9 = "Customer.attribute9";
	public static final String CUSTOMER_ATTRIBUTE10 = "Customer.attribute10";
	public static final String CUSTOMER_ATTRIBUTE11 = "Customer.attribute11";
	public static final String CUSTOMER_ATTRIBUTE12 = "Customer.attribute12";
	public static final String CUSTOMER_ATTRIBUTE13 = "Customer.attribute13";
	public static final String CUSTOMER_ATTRIBUTE14 = "Customer.attribute14";
	public static final String CUSTOMER_ATTRIBUTE15 = "Customer.attribute15";
	public static final String CUSTOMER_ATTRIBUTE16 = "Customer.attribute16";
	public static final String CUSTOMER_ATTRIBUTE17 = "Customer.attribute17";
	public static final String CUSTOMER_ATTRIBUTE18 = "Customer.attribute18";
	public static final String CUSTOMER_ATTRIBUTE19 = "Customer.attribute19";
	public static final String CUSTOMER_ATTRIBUTE20 = "Customer.attribute20";
	public static final String CUSTOMER_SUB_ORG = "Customer.subOrg";
	public static final String CUSTOMER_TYPE = "Customer.type";


	private static ComplaintWorkflow workflow;
	private static String workflowEntityName;

	// For date calendar selection
	static final int DEFAULTDATESELECTOR_ID = 0;
	static final int ALTERNATIVEDATESELECTOR_ID = 1;
	static final int CUSTOMDATESELECTOR_ID = 2;
	static final int MONTHYEARDATESELECTOR_ID = 3;
	static final int TIMESELECTOR_ID = 4;
	static final int DEFAULTDATESELECTOR_WITHLIMIT_ID = 6;
	static final int TIMESELECTOR_WITHLIMIT_ID = 7;
	static final int DATETIMESELECTOR_ANY = 5;
	static final int DATETIMESELECTOR_CURRENT_AND_FUTURE = 8;
	static final int DATETIMESELECTOR_CURRENT = 9;
	static final int DATETIMESELECTOR_CURRENT_AND_PAST = 10;
	static final int DATETIMESELECTOR_PAST = 11;
	static final int DATETIMESELECTOR_FUTURE = 12;
	static final int DATESELECTOR_ANY = 13;
	static final int DATESELECTOR_CURRENT_AND_FUTURE = 14;
	static final int DATESELECTOR_CURRENT = 15;
	static final int DATESELECTOR_CURRENT_AND_PAST = 16;
	static final int DATESELECTOR_PAST = 17;
	static final int DATESELECTOR_FUTURE = 18;
	static final int MULTISELECTION = 19;
	private final static int CAMERA_REQUEST = 20;
	public static int customerServerId = 0;
	private final static String TAG = LabelUtil.class.getSimpleName();
	public static CustomerService customerService =  CustomerServiceImpl.getInstance();
	private static Map<String, View> productCategoryMap = new HashMap<String, View>();
	private static Map<EntityLabel, View> customerDetailsMap = new HashMap<EntityLabel, View>();
	private static WorkLocationRepository workLocationRepository = WorkLocationRepository.getInstance();
	private static ProductRepository productRepository = ProductRepository.getInstance();
	private static EntityLabelRepository entityLabelRepository = EntityLabelRepository.getInstance();
	private static DependentListService dependentListService = DependentListServiceImpl.getInstance();

	public static Map<Integer, View> spinnerMap = new HashMap<Integer, View>();
	public static Map<Integer, String> childMap = new HashMap<Integer, String>();
	private static Map<Integer, View> dependentDisplayMap = new HashMap<Integer, View>();
	private static UserService userService =  UserServiceImpl.getInstance();
	public static int customerTmpRowId;
	public static int customerTmpflagId;

	private GoogleApiClient mGoogleApiClient;
	private PlaceAutocompleteAdapter placeAutocompleteAdapter;
	private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
			new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

	public static ComplaintWorkflow getWorkflow() {
		return workflow;
	}

	public static void setWorkflow(ComplaintWorkflow workflow) {
		LabelUtil.workflow = workflow;
	}

	public static String getWorkflowEntityName() {
		return workflowEntityName;
	}

	public static void setWorkflowEntityName(String workflowEntityName) {
		LabelUtil.workflowEntityName = workflowEntityName;
	}

	public static CustomItemDTO getCustomItem(String fieldName, Context context, boolean search) {

		EntityLabel el = new EntityLabel();

		EntityLabel ell = workflow.getMapEls() != null ? workflow.getMapEls()
				.get(fieldName) : null;
				if(ell != null) {
					el.copy(ell);
					if (el != null) {
						if(search) {
							el.setMandatory(false);
						}
						if(el.getDataSource() != null && el.getDataSource().equalsIgnoreCase("system")) {
							return null;
						}
						return new CustomItemDTO(el, getView(el, context, false));
					}
				}
				return null;
	}

	public static CustomItemDTO getCustomItem(String fieldName,
			Context context, String value) {
		CustomItemDTO dto = getCustomItem(fieldName, context, false);
		if(dto != null) {
			// commented for it makes the value = "" in edit line items 

			/*if(dto.getEntityLabel() != null && dto.getEntityLabel().getMappedName() != null 
					&& dto.getEntityLabel().getMappedName().equalsIgnoreCase(PRODUCT_NAME)) {
				value = "";
			}*/
			setValue(dto, value);
		}
		return dto; 
	}

	private static void setValue(CustomItemDTO dto, String value) {
		boolean isCustomerAttr = false;
		View view = dto.getView();
		EntityLabel el = dto.getEntityLabel();
		if((el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_MIDDLE_NAME)
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_LAST_NAME) 
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_MIDDLE_NAME)
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_ADD1)
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_ADD2)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE1)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE2)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE3)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE4)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE5)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE6)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE7)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE8)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE9)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE10)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE11)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE12)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE13)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE14)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE15)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE16)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE17)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE18)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE19)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE20)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_CITY)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_STATE)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_COUNTRY)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_PINCODE)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_DOB)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_CONTACT_NO)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_MOBILE_NO)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_EMAIL_ID)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_EXTERNAL_ID)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ORGANIZATION_NAME)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_GENDER)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_TYPE)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_SUB_ORG)) && (customerTmpflagId > 0)
				) {
			isCustomerAttr = true;
		}
		if (view instanceof LinearLayout) {
			LinearLayout layout = (LinearLayout) view;
			if (layout.getChildCount() >= 2) {
				View v = layout.getChildAt(1);
				if (v != null) {
					if (v instanceof EditText) {
						((EditText) v).setText(value);
						if(isCustomerAttr)
							v.setEnabled(false);
					} else if (v instanceof Spinner) {
						if(dto.getEntityLabel().getListValues() != null) {
							int i = getValuePosition(dto.getEntityLabel()
									.getListValues(), value);
							((Spinner) v).setSelection(i);
							if(isCustomerAttr)
								v.setEnabled(false);
						} else {
							childMap.put(dto.getEntityLabel().getServerId(), value);
						}
					} else if (v instanceof LinearLayout) {
						View v2 = ((LinearLayout) v).getChildAt(0);
						if (v2 instanceof EditText) {
							if(value != null && (value.contains("-") || value.contains("/") || value.contains(":")) 
									&& (dto.getEntityLabel().getDataType() != null && dto.getEntityLabel().getDataType().equalsIgnoreCase("DateTime") 
									|| dto.getEntityLabel().getDataType().equalsIgnoreCase("Date"))) {
								((EditText) v2).setText(DateUtil.formatDateToDisplay(value));	
								if(isCustomerAttr)
									v.setEnabled(false);
							} else {
								((EditText) v2).setText(value);
								if(isCustomerAttr)
									v.setEnabled(false);
							}
						}
					}
				}
			} else {
				if (view instanceof EditText) {
					((EditText) view).setText(value);
					if(isCustomerAttr)
						view.setEnabled(false);
				} else if (view instanceof Spinner) {
					int i = getValuePosition(dto.getEntityLabel()
							.getListValues(), value);
					((Spinner) view).setSelection(i);
					if(isCustomerAttr)
						view.setEnabled(false);
				}
			}
		}
	}

	protected static int getValuePosition(String listValues,
			String valueToCheck) {
		String[] vals = new String[] {};
		if(listValues != null) {
			vals = listValues.split(",");
		}
		int valuePosition = 0;
		// Function to return the position of a specified string in the array
		List<String> valueList = new ArrayList<String>(
				Arrays.asList(vals));
		valuePosition = valueList.indexOf(valueToCheck);
		if (valuePosition == -1) {
			return 0;
		} else {
			return valuePosition;
		}
	}

	public static CustomItemDTO getCustomItem(String statusName,
			String fieldName, Context context) {

		EntityLabel el = null;
		if (workflow.getAttributeMap() != null
				&& workflow.getAttributeMap().get(statusName) != null) {
			el = workflow.getAttributeMap().get(statusName).get(fieldName);
		}
		if (el != null)
			return new CustomItemDTO(el, getView(el, context, false));
		return null;
	}


	public static CustomItemDTO getCustomItemFromAction(String action,
			String fieldName, Context context, String value) {

		EntityLabel el = null;
		CustomItemDTO dto = null;
		if (workflow.getAttributeActionMap() != null
				&& workflow.getAttributeActionMap().get(action) != null) {
			el = workflow.getAttributeActionMap().get(action).get(fieldName);
		}

		if (el != null)
			dto = new CustomItemDTO(el, getView(el, context, false));
		if(dto != null && value != null) {
			setValue(dto, value);
		}
		return dto;
	}


	public static CustomItemDTO getCustomItemFromAction(String action,
			String fieldName, Context context) {

		EntityLabel el = null;
		if (workflow.getAttributeActionMap() != null
				&& workflow.getAttributeActionMap().get(action) != null) {
			el = workflow.getAttributeActionMap().get(action).get(fieldName);
		}
		if (el != null)
			return new CustomItemDTO(el, getView(el, context, false));
		return null;
	}

	public static String getLabelFromAction(String action,
			String fieldName) {

		EntityLabel el = null;
		if (workflow.getAttributeActionMap() != null
				&& workflow.getAttributeActionMap().get(action) != null) {
			el = workflow.getAttributeActionMap().get(action).get(fieldName);
		}
		if (el != null)
			return el.getDisplayLabel();
		return null;
	}


	public static CustomItemDTO getCustomItemForRemarks(String action,
			Context context, String value) {
		EntityLabel el = null;
		CustomItemDTO dto = null;
		if (workflow.getRemarksLabelMap() != null
				&& workflow.getRemarksLabelMap().get(action) != null) {
			el = workflow.getRemarksLabelMap().get(action);
		}
		if (el != null)
			if(el.getDisplayLabel() == null || el.getDisplayLabel().equals("")) {
				el.setDisplayLabel(_context.getString(R.string.customRemarksLabel));
			}
		dto = new CustomItemDTO(el, getView(el, context, false));
		if(dto != null && value != null) {
			setValue(dto, value);
		}
		return dto;
	}

	public static View getView(EntityLabel el, final Context context, boolean search) {
		if (el != null) {
			LinearLayout linearLayout = CommonUtil.getLinearLayout(context);
			TextView label = null;
			LinearLayout lnrLt = null;
			if(el.getDisplayLabel().contains("^")) {
				String displayLabel = el.getDisplayLabel();
				el.setDisplayLabel(displayLabel.substring(0, displayLabel.indexOf('^')));
				lnrLt = setCustomSepartorLayout(context, el);
				linearLayout.addView(lnrLt);

				el.setDisplayLabel(displayLabel.substring(displayLabel.indexOf('^') + 1, displayLabel.length()).trim());
				label =(setTextFieldValue(context, el));
				linearLayout.addView(label);
			} else {
				label =(setTextFieldValue(context, el));
				linearLayout.addView(label);
			}
			if(el.isDependentDisplay()) {
				linearLayout.setVisibility(View.GONE);
				dependentDisplayMap.put(el.getServerId(), linearLayout);
			}
			if (el.getDataType().equalsIgnoreCase("List") && (el.getDataSource() == null || el.getDataSource().equalsIgnoreCase("NONE"))) {
				View tmpView = getListLayout(el, context, search);
				linearLayout.addView(tmpView);
				checkAddCustomerAttr(el,tmpView);
				return linearLayout;
			} else if(el.getDataType().equalsIgnoreCase("MultiSelect") && (el.getDataSource() == null || el.getDataSource().equalsIgnoreCase("NONE"))) {
				View tmpView = getMultiSelectLayout(el, context);
				linearLayout.addView(tmpView);
				//checkAddCustomerAttr(el,tmpView);
				return linearLayout;
			} else if (el.getDataType().equalsIgnoreCase("DateTime")) {
				linearLayout.addView(getDateTimeLayout(el, context));
				return linearLayout;
			} else if (el.getDataType().equalsIgnoreCase("Date")) {
				linearLayout.addView(getDateLayout(el, context));
				return linearLayout;
			} else {
				if( el.getDataSource() != null && el.getDataSource().equalsIgnoreCase("Barcode")) {
					linearLayout.addView(getBarcodeLayout(el, context));
					return linearLayout;
				}
				if( el.getBusinessDataType() != null && el.getBusinessDataType().equalsIgnoreCase("Barcode")) {
					linearLayout.addView(getBarcodeLayout(el, context));
					return linearLayout;
				}
				/*if( el.getBusinessDataType() != null && el.getBusinessDataType().equalsIgnoreCase("Address")) {
					linearLayout.addView(getAddressLayout(el, context));s
					return linearLayout;
				}*/
				if(el.getDataSource() != null && el.getDataSource().equalsIgnoreCase("worklocation")) {
					List<WorkLocation> allWrkLocs = workLocationRepository.getAll();
					if(allWrkLocs != null && allWrkLocs.size() > 0) {
						StringBuilder wrklocs = new StringBuilder();
						for (WorkLocation wl : allWrkLocs) {
							wrklocs.append(wl.getId());
							wrklocs.append(",");
						}
						wrklocs.substring(0, wrklocs.length() -1);
						el.setListValues(wrklocs.toString());
						linearLayout.addView(getWorkLocationLayout(el,context));
						return linearLayout;
					} else {
						if(label != null) {
							linearLayout.removeView(label);
						}
						return linearLayout;
					}
				} 
				if(el.getDataSource() != null && el.getDataSource().equalsIgnoreCase("connectedUsers")) { 
					List<User> users = userService.getAllusers();
					if(users != null && users.size() > 0) {
						StringBuilder ConnUsers = new StringBuilder();;
						for (User user : users) {
							ConnUsers.append(user.getUserName().trim() + "(" + user.getExternalId().trim() + ")");
							ConnUsers.append(",");
						}
						ConnUsers.substring(0, ConnUsers.length() -1);
						el.setListValues(ConnUsers.toString());
						linearLayout.addView(getConnectedUsersLayout(el,context));
						return linearLayout;
					} else {
						if(label != null) {
							linearLayout.removeView(label);
						}
						return linearLayout;
					}
				} 

				if(el.getMappedName() != null && !el.getMappedName().equalsIgnoreCase("") && !el.getMappedName().equalsIgnoreCase("name")) {
					EditText editText = new EditText(context);
					// editText.setId(el.getId());
					editText.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
					setTextViewDataType(el, editText);
					linearLayout.addView(editText);
					productCategoryMap.put(el.getMappedName(), editText);
					return linearLayout;
				}
				if(el.getMappedName() != null && el.getMappedName().equalsIgnoreCase(PRODUCT_NAME)) {
					linearLayout.addView(getAutoCompleteLayout(el,context));
					return linearLayout;
				}

				EditText editText = new EditText(context);
				// editText.setId(el.getId());
				editText.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				if (el.getDataType().equalsIgnoreCase("numeric") || el.getDataType().equalsIgnoreCase("NumericString") || el.getDataType().equalsIgnoreCase("alphanumeric")) {
					setTextViewDataType(el, editText);
				} /*else if (el.getDataType().equalsIgnoreCase("NumericString")) {
					setTextViewDataType(el, editText);
				} else if (el.getDataType().equalsIgnoreCase("alphanumeric")) {
					setTextViewDataType(el, editText);
				} */
				//editText.setHint(el.getDisplayLabel());

				if(el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_NAME)) {
					DelayAutoCompleteTextView acTextView = new DelayAutoCompleteTextView(context);
					acTextView.setTextColor(Color.BLACK);
					acTextView.setThreshold(3);
					acTextView.setImeOptions(EditorInfo.IME_ACTION_SEARCH | EditorInfo.IME_FLAG_NO_EXTRACT_UI);

					////////////

					ProgressBar pb = new ProgressBar(_context, null, android.R.attr.progressBarStyleSmall);
					pb.setVisibility(View.GONE);
					pb.setIndeterminate(true);


					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					pb.setLayoutParams(params);

					///////////

					//acTextView.setLoadingIndicator(pb);
					CustomerAutoTextAdapter adapter = new CustomerAutoTextAdapter(_context);
					acTextView.setAdapter(adapter);
					acTextView.setOnItemClickListener(adapter);
					linearLayout.addView(acTextView);
					return linearLayout;
				}
				linearLayout.addView(editText);
				checkAddCustomerAttr(el, editText);
				return linearLayout;
			}
		} else {
			return null;
		}
	}

	private static View getListLayout(EntityLabel el, Context context, boolean search) {

		Spinner spinner = new Spinner(context);
		List<EntityLabel> els = new ArrayList<EntityLabel>(0);
		String listValues = el.getListValues();
		if(listValues != null && !listValues.equals("")) {
			if(search) {
				String s = ",";
				listValues  = s+el.getListValues();
			}
			String[] vals = listValues.split(",");
			if(vals != null && vals.length > 0) {
				for (String value : vals) {
					els.add(new EntityLabel(el.getServerId(), value));
				}
			}
		}
		ArrayAdapter<EntityLabel> listVals = new ArrayAdapter<EntityLabel>(
				context, android.R.layout.simple_spinner_item, els);

		listVals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(listVals);
		spinnerMap.put(el.getServerId(), spinner);
		if(el.isHasDependency()) {
			spinner.setOnItemSelectedListener(new LabelUtil().new DropDownChangeListner());
		}
		return spinner;
	}

	private static View getAutoCompleteLayout(EntityLabel el, Context context) {
		AutoCompleteTextView completeTextView = new AutoCompleteTextView(context);
		completeTextView.setThreshold(1);
		completeTextView.setTextColor(Color.BLACK);
		ItemAutoTextAdapter adapter = new LabelUtil().new ItemAutoTextAdapter(_context, null);
		completeTextView.setAdapter(adapter);
		completeTextView.setOnItemClickListener(adapter);
		completeTextView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		//completeTextView.requestFocus();
		return completeTextView;
	}

	public static String getLabelName(EntityLabel el) {
		String label = new String(el.getDisplayLabel());
		return el.isMandatory() ? label.concat("*") : label;
	}

	public static String getLabelName(String fieldName) {
		if(workflow != null) {
			EntityLabel el = workflow.getMapEls() != null ? workflow.getMapEls()
					.get(fieldName) : null;
					if (el != null)
						return el.getDisplayLabel();
					return null;
		}
		return null;
	}

	public static void setTextViewDataType(EntityLabel el, EditText editText) {
		String dataType = "default";
		editText.setHint(el.getToolTip() != null ? el.getToolTip() : "");
		if (el != null) {
			dataType = el.getDataType();
		}
		if (dataType.equalsIgnoreCase("numeric")) {
			editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			if (el.getMaxValues() != null)
				editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						el.getMaxValues().trim().length()) });
		} else if (dataType.equalsIgnoreCase("NumericString")) {
			editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			if (el.getMaxValues() != null)
				editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						Integer.valueOf(el.getMaxValues())) });
		} else {
			editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			editText.setHorizontallyScrolling(false);
			editText.setMaxLines(8);
			if (el.getMaxValues() != null)
				editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						Integer.valueOf(el.getMaxValues())) });

			if (el.getAttributeName().equalsIgnoreCase("Activity.remarks")) {
				editText.setText(el.getListValues());
			}
		}
	}

	public static List<String> getnextActionFromStatus(String status) {
		return workflow.getNextStatusMap() != null ? workflow
				.getNextStatusMap().get(status) : null;
	}

	public static List<String> getnextActionFromAction(String action) {
		return workflow.getNextActionMap() != null ? workflow
				.getNextActionMap().get(action) : null;
	}


	public static boolean hasActivityAttributes(String action) {
		if (workflow.getAttributeActionMap() != null) {
			if (workflow.getAttributeActionMap().get(action) != null && workflow.getAttributeActionMap().get(action).size() > 0)
				return true;
			return false;
		}
		return false;
	}

	private static TextView setTextFieldValue(Context context,
			EntityLabel entityLabel) {
		TextView textView = new TextView(context);
		textView.setTypeface(null, Typeface.BOLD);
		String displayLabel = entityLabel.getDisplayLabel();
		/*if(entityLabel.getMappedName() != null && !entityLabel.getMappedName().equals("")) {
			displayLabel = entityLabel.getMappedName();
		}*/
		textView.setClickable(false);
		textView.setGravity(Gravity.LEFT);
		textView.setPadding(5, 3, 0, 3);
		textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		if (entityLabel.isMandatory()) {
			/*textView.setTextColor(Color.parseColor(context
					.getString(R.color.text_color)));
			 */
			String text = "<font color=#282828>"+displayLabel+"</font> <font color=#ff0000>*</font>";
			textView.setText(Html.fromHtml(text));

		} else {
			textView.setTextColor(Color.parseColor(context
					.getString(R.color.text_color)));
			textView.setText(displayLabel);
		}
		return textView;
	}

	private static LinearLayout setCustomSepartorLayout(Context context, EntityLabel entityLabel) {
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new  LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setOrientation(LinearLayout.HORIZONTAL);

		TextView textView = new TextView(context);
		textView.setTypeface(null, Typeface.BOLD);
		textView.setPadding(5, 3, 0, 3);
		String displayLabel = entityLabel.getDisplayLabel();
		textView.setGravity(Gravity.LEFT);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		if(displayLabel != null)
			textView.setText(displayLabel.trim());

		TextView seperator = new TextView(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 1, 1f);
		lp.gravity = Gravity.CENTER_VERTICAL;
		seperator.setBackgroundColor(Color.BLACK);
		lp.setMargins(0, 5, 0, 5);
		seperator.setLayoutParams(lp);
		layout.addView(textView);
		layout.addView(seperator);

		return layout;
	}
	private static View getDateTimeLayout(EntityLabel el, final Context context) {
		final int dateSelectionType;
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		EditText editText = new EditText(context);
		// editText.setId(el.getId());
		editText.setEnabled(false);
		editText.setFocusable(false);
		LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		editText.setPadding(0, 0, 15, 0);
		// editparams.setMargins(0, 0, 30, 0);
		editText.setLayoutParams(editparams);
		final ImageView imageView = new ImageView(context);
		imageView.setClickable(true);
		imageView.setTag(R.id.SELECTOR_OUTPUT_FIELD, editText);
		if (el.getMinValues() != null) {
			if (el.getMinValues().equalsIgnoreCase("CURRENT")) {
				dateSelectionType = DATETIMESELECTOR_CURRENT;
			} else if (el.getMinValues().equalsIgnoreCase("PAST")) {
				dateSelectionType = DATETIMESELECTOR_PAST;
			} else if (el.getMinValues().equalsIgnoreCase("FUTURE")) {
				dateSelectionType = DATETIMESELECTOR_FUTURE;
			} else if (el.getMinValues().equalsIgnoreCase("CURRENT_AND_PAST")) {
				dateSelectionType = DATETIMESELECTOR_CURRENT_AND_PAST;
			} else if (el.getMinValues().equalsIgnoreCase("CURRENT_AND_FUTURE")) {
				dateSelectionType = DATETIMESELECTOR_CURRENT_AND_FUTURE;
			} else if (el.getMinValues().equalsIgnoreCase("ANY")) {
				dateSelectionType = DATETIMESELECTOR_ANY;
			} else {
				dateSelectionType = DATETIMESELECTOR_ANY;
			}
		} else {
			dateSelectionType = DATETIMESELECTOR_ANY;
		}
		// imageView.setTag (R.id.TAG_DATETIME_SELECTION_TYPE,
		// dateSelectionType);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_RIGHT);
		// imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.calendar_ico);
		imageView.setVisibility(View.VISIBLE);
		imageView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				AbstractActivity.setOutputField((EditText) imageView
						.getTag(R.id.SELECTOR_OUTPUT_FIELD));
				AbstractActivity.setDateSelectionType(dateSelectionType);
				AbstractActivity a = (AbstractActivity) context;
				a.showDialog(new Random().nextInt());
			}
		});
		layout2.addView(editText);
		layout2.addView(imageView, imageParams);
		return layout2;
	}

	private static View getWorkLocationLayout(EntityLabel el, final Context context) {
		Spinner s = new Spinner(context);
		ArrayAdapter<WorkLocation> wlList = new ArrayAdapter<WorkLocation>(context, android.R.layout.simple_spinner_item,workLocationRepository.getAll());
		wlList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(wlList);
		return s;
	}

	private static View getConnectedUsersLayout(EntityLabel el, final Context context) {	
		if(el.getDataType().equalsIgnoreCase("MultiSelect")) {
			return (getMultiSelectLayout(el, context));
		} else if(el.getDataType().equalsIgnoreCase("List")) {
			return getListLayout(el, context, false);
		}
		TextView errorTv = new TextView(context);
		errorTv.setTextColor(Color.RED);
		errorTv.setText("Attribute Not configured corretly");
		return errorTv;
		/*Spinner s = new Spinner(context);
		ArrayAdapter<User> users = new ArrayAdapter<User>(context, android.R.layout.simple_spinner_item, userService.getAllusers());
		users.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(users);
		return s;*/
	}

	private static View getBarcodeLayout(EntityLabel el, final Context context) {
		EditText editText = new EditText(context);
		if(el.getMappedName() != null && el.getMappedName().equalsIgnoreCase(PRODUCT_NAME)) {
			editText = (EditText) getAutoCompleteLayout(el, context);
		}
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));


		editText.setHint(el.getToolTip() != null ? el.getToolTip() : "");
		// editText.setId(el.getId());
		//editText.setEnabled(false);
		//editText.setFocusable(false);
		LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		editText.setPadding(5, 0, 15, 0);
		// editparams.setMargins(0, 0, 30, 0);
		editText.setLayoutParams(editparams);
		final ImageView imageView = new ImageView(context);
		imageView.setClickable(true);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_RIGHT);
		// imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.barcode_on);
		imageView.setVisibility(View.VISIBLE);
		imageView.setTag(R.id.SELECTOR_OUTPUT_FIELD, editText);
		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AbstractActivity.setOutputField((EditText) imageView.getTag(R.id.SELECTOR_OUTPUT_FIELD));
				IntentIntegrator integrator = new IntentIntegrator((AbstractActivity)context);
				integrator.initiateScan();
			}
		});
		layout2.addView(editText);
		layout2.addView(imageView, imageParams);
		return layout2;
	}


	private View getAddressLayout(EntityLabel el, final Context context) {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
		.addApi(Places.GEO_DATA_API)
		.addOnConnectionFailedListener(this)
		.build();
		mGoogleApiClient.connect();

		AutoCompleteTextView completeTextView = new AutoCompleteTextView(context);
		completeTextView.setThreshold(1);
		completeTextView.setTextColor(Color.BLACK);
		placeAutocompleteAdapter = new PlaceAutocompleteAdapter(context, android.R.layout.simple_list_item_1, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
		completeTextView.setAdapter(placeAutocompleteAdapter);
		completeTextView.setOnItemClickListener(mAutocompleteClickListener);
		completeTextView.setHint(el.getToolTip() != null ? el.getToolTip() : "");
		completeTextView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		//completeTextView.requestFocus();
		return completeTextView;

	}

	private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			/*
            Retrieve the place ID of the selected item from the Adapter.
            The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
            read the place ID.
			 */
			final PlaceAutocompleteAdapter.PlaceAutocomplete item = placeAutocompleteAdapter.getItem(position);
			final String placeId = String.valueOf(item.placeId);
			Log.i(TAG, "Autocomplete item selected: " + item.description);

			/*
            Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
			 */
			PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
					.getPlaceById(mGoogleApiClient, placeId);
			//placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

			Toast.makeText(ApplicationContext.getContext(), "Clicked: " + item.description,
					Toast.LENGTH_SHORT).show();
			Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);

		}
	};

	private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
		@Override
		public void onResult(PlaceBuffer places) {
			if (!places.getStatus().isSuccess()) {
				// Request did not complete successfully
				Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
				places.release();
				return;
			}
			// Get the Place object from the buffer.
			final Place place = places.get(0);		
			// ============================================================================================== 
			
			// Format details of the place for display and show it in a TextView.
			/*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
					place.getId(), place.getAddress(), place.getPhoneNumber(),
					place.getWebsiteUri()));

			// Display the third party attributions if set.
			final CharSequence thirdPartyAttribution = places.getAttributions();
			if (thirdPartyAttribution == null) {
				mPlaceDetailsAttribution.setVisibility(View.GONE);
			} else {
				mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
				mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
			}*/
			Log.i(TAG, "Place details received: " + place.getName());
			places.release();
		}
	};


	private static View getDateLayout(EntityLabel el, final Context context) {
		final int dateSelectionType;
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		EditText editText = new EditText(context);
		// editText.setId(el.getId());
		editText.setEnabled(false);
		editText.setFocusable(false);
		LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		editText.setPadding(0, 0, 15, 0);
		// editparams.setMargins(0, 0, 30, 0);
		editText.setLayoutParams(editparams);
		final ImageView imageView = new ImageView(context);
		imageView.setClickable(true);
		imageView.setTag(R.id.SELECTOR_OUTPUT_FIELD, editText);
		if (el.getMinValues() != null) {
			if (el.getMinValues().equalsIgnoreCase("CURRENT")) {
				dateSelectionType = DATESELECTOR_CURRENT;
			} else if (el.getMinValues().equalsIgnoreCase("PAST")) {
				dateSelectionType = DATESELECTOR_PAST;
			} else if (el.getMinValues().equalsIgnoreCase("FUTURE")) {
				dateSelectionType = DATESELECTOR_FUTURE;
			} else if (el.getMinValues().equalsIgnoreCase("CURRENT_AND_PAST")) {
				dateSelectionType = DATESELECTOR_CURRENT_AND_PAST;
			} else if (el.getMinValues().equalsIgnoreCase("CURRENT_AND_FUTURE")) {
				dateSelectionType = DATESELECTOR_CURRENT_AND_FUTURE;
			} else if (el.getMinValues().equalsIgnoreCase("ANY")) {
				dateSelectionType = DATESELECTOR_ANY;
			} else {
				dateSelectionType = DATESELECTOR_ANY;
			}
		} else {
			dateSelectionType = DATESELECTOR_ANY;
		}
		// imageView.setTag (R.id.TAG_DATE_SELECTION_TYPE, dateSelectionType);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_RIGHT);
		// imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.calendar_ico);
		imageView.setVisibility(View.VISIBLE);
		imageView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				AbstractActivity.setOutputField((EditText) imageView.getTag(R.id.SELECTOR_OUTPUT_FIELD));
				AbstractActivity.setDateSelectionType(dateSelectionType);
				AbstractActivity a = (AbstractActivity) context;

				a.showDialog(new Random().nextInt());
			}
		});
		layout2.addView(editText);
		layout2.addView(imageView, imageParams);
		return layout2;
	}

	private static View getMultiSelectLayout(final EntityLabel el, final Context context) {
		final int dateSelectionType;
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		EditText editText = new EditText(context);
		// editText.setId(el.getId());
		editText.setEnabled(false);
		editText.setFocusable(false);
		LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		editText.setPadding(0, 0, 15, 0);
		// editparams.setMargins(0, 0, 30, 0);
		editText.setLayoutParams(editparams);
		final ImageView imageView = new ImageView(context);
		imageView.setClickable(true);
		imageView.setTag(R.id.SELECTOR_OUTPUT_FIELD, editText);
		dateSelectionType = MULTISELECTION;
		// imageView.setTag (R.id.TAG_DATETIME_SELECTION_TYPE,
		// dateSelectionType);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_RIGHT);
		// imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.multiselect_ico);
		imageView.setVisibility(View.VISIBLE);
		imageView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				AbstractActivity.setOutputField((EditText) imageView
						.getTag(R.id.SELECTOR_OUTPUT_FIELD));

				String listValues = el.getListValues();
				String[] vals = null;
				if(listValues != null)
					vals = listValues.split(",");					
				else
					vals = new String[]{};

				AbstractActivity.setListValues(vals);
				if(el.isHasDependency())
					AbstractActivity.setMultiSelectListner(new LabelUtil(), el);
				AbstractActivity.setDateSelectionType(dateSelectionType);
				AbstractActivity a = (AbstractActivity) context;
				a.showDialog(new Random().nextInt());
			}
		});
		layout2.addView(editText);
		layout2.addView(imageView, imageParams);
		return layout2;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static View getCustomDateTimeLayout(EntityLabel el, final Context context,EditText editText,final CustomDateTimeDialogWrapper wrapper) {
		final int dateSelectionType;
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		editText = new EditText(context);
		// editText.setId(el.getId());
		editText.setEnabled(false);
		editText.setFocusable(false);
		LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		editText.setPadding(0, 0, 15, 0);
		// editparams.setMargins(0, 0, 30, 0);
		editText.setLayoutParams(editparams);
		final ImageView imageView = new ImageView(context);
		imageView.setClickable(true);
		imageView.setTag(R.id.SELECTOR_OUTPUT_FIELD, editText);
		if (el.getMinValues() != null) {
			if (el.getMinValues().equalsIgnoreCase("CURRENT")) {
				dateSelectionType = DATETIMESELECTOR_CURRENT;
			} else if (el.getMinValues().equalsIgnoreCase("PAST")) {
				dateSelectionType = DATETIMESELECTOR_PAST;
			} else if (el.getMinValues().equalsIgnoreCase("FUTURE")) {
				dateSelectionType = DATETIMESELECTOR_FUTURE;
			} else if (el.getMinValues().equalsIgnoreCase("CURRENT_AND_PAST")) {
				dateSelectionType = DATETIMESELECTOR_CURRENT_AND_PAST;
			} else if (el.getMinValues().equalsIgnoreCase("CURRENT_AND_FUTURE")) {
				dateSelectionType = DATETIMESELECTOR_CURRENT_AND_FUTURE;
			} else if (el.getMinValues().equalsIgnoreCase("ANY")) {
				dateSelectionType = DATETIMESELECTOR_ANY;
			} else {
				dateSelectionType = DATETIMESELECTOR_ANY;
			}
		} else {
			dateSelectionType = DATETIMESELECTOR_ANY;
		}
		// imageView.setTag (R.id.TAG_DATETIME_SELECTION_TYPE,
		// dateSelectionType);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_RIGHT);
		// imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.calendar_ico);
		imageView.setVisibility(View.VISIBLE);
		imageView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				wrapper.setOutputField((EditText) imageView
						.getTag(R.id.SELECTOR_OUTPUT_FIELD));
				wrapper.setDateSelectionType(dateSelectionType);
				wrapper.showDateTimeDialog();
			}
		});
		layout2.addView(editText);
		layout2.addView(imageView, imageParams);
		return layout2;
	}

	public class ItemAutoTextAdapter extends CursorAdapter implements android.widget.AdapterView.OnItemClickListener {
		public ItemAutoTextAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			final String productPricePerUnit = cursor.getString(cursor.getColumnIndexOrThrow("price"));
			String productUnitOfMeasure = cursor.getString(cursor.getColumnIndexOrThrow(("unitOfMeasure")));
			String productId = cursor.getString(cursor.getColumnIndexOrThrow("itemId"));
			String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow("description"));
			String itemCategory = cursor.getString(cursor.getColumnIndexOrThrow("category"));
			String itemManufacturer = cursor.getString(cursor.getColumnIndexOrThrow("manufacturer"));

			EditText proDesc = (EditText)productCategoryMap.get(PRODUCT_DESCRIPTION);
			if(proDesc != null) 
				proDesc.setText(itemDescription);

			EditText proManufacturer = (EditText)productCategoryMap.get(PRODUCT_MANUFACTURER);
			if(proManufacturer != null)
				proManufacturer.setText(itemManufacturer);

			EditText prounitOfMeasure = (EditText)productCategoryMap.get(PRODUCT_UNIT_OF_MEASURE);
			if(prounitOfMeasure != null)
				prounitOfMeasure.setText(productUnitOfMeasure);

			EditText proItemNo = (EditText)productCategoryMap.get(PRODUCT_ITEM_NO);
			if(proItemNo != null)
				proItemNo.setText(productId);

			EditText proPrice = (EditText)productCategoryMap.get(PRODUCT_PRICE);
			if(proPrice != null)
				proPrice.setText(productPricePerUnit);

			final EditText proTotalPrice = (EditText)productCategoryMap.get(PRODUCT_TOTAL_PRICE);

			EditText proQuantity = (EditText)productCategoryMap.get(PRODUCT_QUANTITY);
			if(proQuantity != null)
				proQuantity.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// FIXME
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {}

					@Override
					public void afterTextChanged(Editable s) {
						if(s.length() > 0) {
							try {
								double totalPrice = (Double) (Long.parseLong(s.toString()) * (Double.parseDouble(productPricePerUnit)));
								if(proTotalPrice != null) {
									proTotalPrice.setText(String.valueOf((totalPrice)));
								} else 
									Toast.makeText(_context, "Total Price : " + totalPrice  , Toast.LENGTH_LONG).show();
							} catch(Exception e) {
								Toast.makeText(_context, _context.getString(R.string.productValuesNotValidMsg) , Toast.LENGTH_LONG).show();
							}
						} else {
							if(proTotalPrice != null) {
								proTotalPrice.setText("");
							}
						}
					}
				});


		}
		@Override
		public String convertToString(Cursor cursor) {
			final int columnIndex = cursor.getColumnIndexOrThrow("name");
			final String str = cursor.getString(columnIndex);
			return str;
		}
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
			final String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
			final String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
			((TextView) view.findViewById(R.id.ccLarge)).setText(name);
			((TextView) view.findViewById(R.id.ccMed)).setText("(" + category + ") " + description);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final View view = inflater.inflate(R.layout.custcontentview, parent, false);
			return view;
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}
			Cursor c = productRepository.getCursorForMatchingProducts(constraint.toString());
			return c;			
		}
	}

	private class DropDownChangeListner implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			Object obj = parent.getItemAtPosition(position);
			if(obj instanceof EntityLabel) {
				invokeChildrens((EntityLabel)obj, view.getContext());
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// Do nothing
		}
	}



	private void invokeChildrens(EntityLabel el, Context context) {
		if(el != null) {
			Set<Entry<Integer, View>> invisibleLinearLayouts = dependentDisplayMap.entrySet();
			for (Iterator<Entry<Integer, View>> iterator = invisibleLinearLayouts.iterator(); iterator
					.hasNext();) {
				Entry<Integer, View> entry = (Entry<Integer, View>) iterator.next();
				entry.getValue().setVisibility(View.GONE);
				if(entry.getValue() instanceof LinearLayout) {
					LinearLayout ll = (LinearLayout) entry.getValue();
					View childView = null;
					if(ll.getChildAt(0) instanceof LinearLayout) {
						childView = ll.getChildAt(2);
					} else {
						childView = ll.getChildAt(1); 
					}
					if(childView != null) {
						if(childView instanceof Spinner) {
							ArrayAdapter myAdap = (ArrayAdapter) ((Spinner)childView).getAdapter();
							int spinnerPosition = myAdap.getPosition("");
							if(spinnerPosition > -1) 
								((Spinner) childView).setSelection(spinnerPosition);
						} else if(childView instanceof EditText) {
							((EditText)childView).setText("");
						}
					}
				}
			}

			List<DependentList> dependentLists = dependentListService.getAllByParentIdAndName(el.getServerId(), el.getListTmpValue());
			List<EntityLabel> els = new ArrayList<EntityLabel>(0);
			if(dependentLists != null) {
				for (DependentList dl : dependentLists) {
					if(dl != null) {
						String dependentSpinnerValue = dl.getChildName();
						int childId = dl.getChildId();
						if(dependentDisplayMap.containsKey(childId)) {
							dependentDisplayMap.get(childId).setVisibility(View.VISIBLE);
						}
						if(dependentSpinnerValue != null) {
							String[] vals = dependentSpinnerValue.split(",");
							if(spinnerMap.containsKey(dl.getChildId())) {
								//if(((Spinner)spinnerMap.get(dl.getChildId())).getAdapter().getCount() <= 0) {
								els.clear();
								if(vals != null && vals.length > 0) {
									for (String value : vals) {
										els.add(new EntityLabel(dl.getChildId(), value));
									}
								}
								//}
							}
						}
					}

					if(spinnerMap != null && spinnerMap.size() > 0) {
						if(dl == null) {
							els.clear();
							dl = dependentListService.getByParentId(el.getServerId());
						}

						ArrayAdapter<EntityLabel> listVals = new ArrayAdapter<EntityLabel>(
								context, android.R.layout.simple_spinner_item, els);

						listVals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

						if(dl != null) {
							Spinner spinner = (Spinner) spinnerMap.get(dl.getChildId());
							if(spinner != null) {
								// for housing , dependent display issue to be addressed.
								//if(((Spinner)spinnerMap.get(dl.getChildId())).getAdapter().getCount() <= 0) {
									spinner.setAdapter(listVals);
								//}
								if(childMap.containsKey(dl.getChildId())) {
									String value = childMap.get(dl.getChildId());

									if(value != null && dl.getChildName() != null) {
										int i = getValuePosition(dl.getChildName(), value);
										spinner.setSelection(i);
									}
								}

								if(el.isHasDependency()) {
									spinner.setOnItemSelectedListener(new LabelUtil().new DropDownChangeListner());
								}
							}
						}
					}
				}
			}

		}
	}


	private static void invokeChildrens(EntityLabel entLbl, List<String> list) {
		if(entLbl != null) {
			Set<Entry<Integer, View>> invisibleLinearLayouts = dependentDisplayMap.entrySet();
			for (Iterator<Entry<Integer, View>> iterator = invisibleLinearLayouts.iterator(); iterator
					.hasNext();) {
				Entry<Integer, View> entry = (Entry<Integer, View>) iterator.next();
				entry.getValue().setVisibility(View.GONE);
				if(entry.getValue() instanceof LinearLayout) {
					LinearLayout ll = (LinearLayout) entry.getValue();
					View childView = ll.getChildAt(1);
					if(childView instanceof Spinner) {
						ArrayAdapter myAdap = (ArrayAdapter) ((Spinner)childView).getAdapter();
						int spinnerPosition = myAdap.getPosition("");
						if(spinnerPosition > -1)
							((Spinner) childView).setSelection(spinnerPosition);
					} else if(childView instanceof EditText) {
						((EditText)childView).setText("");
					}
				}
			}

			for (String selected : list) {
				EntityLabel el = new EntityLabel(entLbl.getServerId(), selected);

				List<DependentList> dependentLists = dependentListService.getAllByParentIdAndName(el.getServerId(), el.getListTmpValue());

				if(dependentLists != null) {
					for (DependentList dl : dependentLists) {
						List<EntityLabel> els = new ArrayList<EntityLabel>(0);
						if(dl != null) {
							String dependentSpinnerValue = dl.getChildName();


							int childId = dl.getChildId();
							if(dependentDisplayMap.containsKey(childId)) {
								dependentDisplayMap.get(childId).setVisibility(View.VISIBLE);
							}

							if(dependentSpinnerValue != null) {
								String[] vals = dependentSpinnerValue.split(",");
								if(spinnerMap.containsKey(dl.getChildId())) {
									if(((Spinner)spinnerMap.get(dl.getChildId())).getAdapter().getCount() <= 0) {
										if(vals != null && vals.length > 0) {
											for (String value : vals) {
												els.add(new EntityLabel(dl.getChildId(), value));
											}
										}
									}
								}
							}
						}

						if(spinnerMap != null && spinnerMap.size() > 0) {
							if(dl == null) {
								els.clear();
								dl = dependentListService.getByParentId(el.getServerId());
							}

							ArrayAdapter<EntityLabel> listVals = new ArrayAdapter<EntityLabel>(
									ApplicationContext.getContext(), android.R.layout.simple_spinner_item, els);

							listVals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

							if(dl != null) {
								Spinner spinner = (Spinner) spinnerMap.get(dl.getChildId());
								if(spinner != null) {
									if(((Spinner)spinnerMap.get(dl.getChildId())).getAdapter().getCount() <= 0) {
										spinner.setAdapter(listVals);
									}
									if(childMap.containsKey(dl.getChildId())) {
										String value = childMap.get(dl.getChildId());

										if(value != null && dl.getChildName() != null) {
											int i = getValuePosition(dl.getChildName(), value);
											spinner.setSelection(i);
										}
									}

									if(el.isHasDependency()) {
										spinner.setOnItemSelectedListener(new LabelUtil().new DropDownChangeListner());
									}
								}
							}
						}
					}
				}

			}
		}
	}

	// Method invoked when selections are made in multiselect  
	@Override
	public void onSelect(String selections, EntityLabel el) {
		Log.d(TAG, "MULTISELECT : ON SELECT CALLED " + selections);

		List<String> listOfSelectedItems = Arrays.asList(selections.split(","));
		/*for (String selected : listOfSelectedItems) {
			invokeChildrens(new EntityLabel(el.getId(), selected));
		}*/
		invokeChildrens(el, listOfSelectedItems);
	}



	public static int isMultiSelectOrSpinner(String fieldName) {
		if(workflow != null) {
			EntityLabel el = workflow.getMapEls() != null ? workflow.getMapEls()
					.get(fieldName) : null;
					if (el != null) {
						if(el.getDataType().equalsIgnoreCase("MultiSelect"))
							return el.getServerId();
						else
							return 0;
					}
					return 0;
		}
		return 0;
	}

	public static boolean isDependentDisplay(String fieldName) {
		if(workflow != null) {
			EntityLabel el = workflow.getMapEls() != null ? workflow.getMapEls()
					.get(fieldName) : null;
					if (el != null) {
						return el.isDependentDisplay();
					}
					return false;
		}
		return false;
	}

	public static int getDependentDisplayEntityLabelId(String fieldName) {
		if(workflow != null) {
			EntityLabel el = workflow.getMapEls() != null ? workflow.getMapEls()
					.get(fieldName) : null;
					if (el != null) {
						return el.getServerId();
					}
					return 0;
		}
		return 0;
	}


	public static CustomItemDTO getLabel(String fieldName, String value, Context context) {
		if(workflow != null) {
			EntityLabel el = workflow.getMapEls() != null ? workflow.getMapEls()
					.get(fieldName) : null;
					if (el != null)
						return new CustomItemDTO(el, getView(getLabelName(fieldName),value, el, context));
					return null;
		}
		return null;
	}

	public static View getView(String labelName, String value, EntityLabel entityLabel, Context ctx) {

		if(labelName != null && labelName.contains("^")) {
			LinearLayout vertLyt = new LinearLayout(_context);
			vertLyt.setOrientation(LinearLayout.VERTICAL);

			LinearLayout horLll = new LinearLayout(_context);
			horLll.setOrientation(LinearLayout.HORIZONTAL);
			TextView tv1 = new TextView(_context);
			tv1.setText(labelName.substring(0, labelName.indexOf('^')).trim());
			tv1.setTypeface(null, Typeface.BOLD_ITALIC);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//	layoutParams.gravity = Gravity.LEFT;
			tv1.setLayoutParams(layoutParams);
			tv1.setTextColor(Color.GRAY);

			TextView tv2 = new TextView(_context);
			//tv3.setEllipsize(TruncateAt.MARQUEE);
			//			tv2.setText(value);
			tv2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			android.widget.LinearLayout.LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 1);
			layoutParams2.gravity = Gravity.CENTER_VERTICAL;
			//layoutParams3.gravity = Gravity.LEFT;
			tv2.setBackgroundColor(Color.parseColor(_context.getString(R.color.seperator)));
			tv2.setLayoutParams(layoutParams2);

			horLll.addView(tv1);
			horLll.addView(tv2);

			vertLyt.addView(horLll);

			horLll = new LinearLayout(_context);
			horLll.setOrientation(LinearLayout.HORIZONTAL);
			tv1 = new TextView(_context);
			tv1.setText(labelName.substring(labelName.indexOf('^') + 1, labelName.length()).trim());
			tv1.setTypeface(null, Typeface.BOLD);
			layoutParams = new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 2);
			//	layoutParams.gravity = Gravity.LEFT;
			tv1.setLayoutParams(layoutParams);

			tv2 = new TextView(_context);
			//tv3.setEllipsize(TruncateAt.MARQUEE);
			tv2.setText(value);
			tv2.setGravity(Gravity.LEFT);
			layoutParams2 = new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 2);
			//layoutParams3.gravity = Gravity.LEFT;
			tv2.setLayoutParams(layoutParams2);

			horLll.addView(tv1);
			horLll.addView(tv2);

			vertLyt.addView(horLll);

			return vertLyt;
		} else {
			return getDetailsView(labelName, value, entityLabel, ctx);
		}
	}


	private static View getDetailsView(String labelName, final String value, EntityLabel entityLabel, final Context ctx) {
		LinearLayout horLll = new LinearLayout(_context);
		horLll.setOrientation(LinearLayout.HORIZONTAL);
		TextView tv1 = new TextView(_context);
		tv1.setText(labelName);
		tv1.setTypeface(null, Typeface.BOLD);
		LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 2);
		//	layoutParams.gravity = Gravity.LEFT;
		tv1.setLayoutParams(layoutParams);
		tv1.setTextColor(Color.BLACK);
		TextView tv2 = new TextView(_context);
		tv2.setText(":");

		LayoutParams layoutParams2 = new android.widget.LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		//	layoutParams2.gravity = Gravity.CENTER;
		tv2.setLayoutParams(layoutParams2);

		TextView tv3 = new TextView(_context);
		//tv3.setEllipsize(TruncateAt.MARQUEE);
		tv3.setText(value);
		tv3.setGravity(Gravity.LEFT);
		LayoutParams layoutParams3 = new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 2);
		//layoutParams3.gravity = Gravity.LEFT;
		tv3.setLayoutParams(layoutParams3);
		tv3.setTextColor(Color.BLACK);
		horLll.addView(tv1);
		//	horLll.addView(tv2);
		horLll.addView(tv3);



		if(entityLabel != null && entityLabel.getBusinessDataType() != null && entityLabel.getBusinessDataType().equalsIgnoreCase("phoneNumber")) {
			horLll.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					final Dialog dialog = new Dialog(ctx);
					dialog.setContentView(R.layout.calldialog);
					dialog.setTitle("Call Customer");
					/*((TextView) dialog.findViewById(R.id.calltext))
					.setText(complaint.getCustomerName());*/
					TextView phone = (TextView) dialog
							.findViewById(R.id.phonetext);
					phone.setText(value);
					phone.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							CustomerCallFactory callConnector = new GenericCallConnector();
							callConnector.setupCall(value, ctx);
						}
					});

					// Added for Exotel Epabx

					TextView epabx = (TextView) dialog
							.findViewById(R.id.epabxtext);
					if(_context.getResources().getBoolean(R.bool.enableEpabx)) {
						epabx.setText(value);
						epabx.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								dialog.dismiss();
								if(CommonUtil.getMobileNumber() != null && !CommonUtil.getMobileNumber().equals("")) {
									Toast.makeText(ctx, "Initiated call through EPABX...Kindly wait", Toast.LENGTH_SHORT).show();
									CustomerCallFactory callConnector = new ExotelEpabxConnector();
									callConnector.setupCall(value, ctx);
								} else {
									// if the system does not retun the number then ask the user to enter it
									AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

									alert.setTitle(_context.getString(R.string.userMobileNumberDialogTitle));
									// Set an EditText view to get user input 
									final EditText input = new EditText(ctx);
									input.setInputType(InputType.TYPE_CLASS_NUMBER);
									alert.setView(input);
									alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											String value = input.getText().toString();
											Toast.makeText(ctx, "Initiated call through EPABX...Kindly wait", Toast.LENGTH_SHORT).show();
											CustomerCallFactory callConnector = new ExotelEpabxConnector();
											callConnector.setupCall(value, ctx);
										}
									});

									alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											// Canceled.
										}
									});

									alert.show();
								}
							}
						});
					} else {
						epabx.setVisibility(View.GONE);
					}
					Button dialogButton = (Button) dialog
							.findViewById(R.id.dialogButtonCancel);
					dialogButton
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.show();
				}
			});
		}

		return horLll;
	}


	public static View getProductView(String productLabel, String value) {
		Map<String,EntityLabel> els = workflow.getMapEls() != null ? workflow.getMapEls() : null;
		View view = null;
		if(els != null) {
			Set<Entry<String, EntityLabel>> se = els.entrySet();
			for (Iterator<Entry<String, EntityLabel>> iterator = se.iterator(); iterator.hasNext();) {
				Entry<String,EntityLabel> entry = (Entry<String,EntityLabel>) iterator.next();
				String lbl = entry.getValue().getMappedName();
				String labelName = entry.getValue().getDisplayLabel();
				if(lbl != null && lbl.equalsIgnoreCase(productLabel)) {
					view = getDetailsView(labelName, value, null, null);
				}
			}
		}
		return view;
	}

	public static String getProductAttributeName(String productLabel) {
		Map<String,EntityLabel> els = workflow.getMapEls() != null ? workflow.getMapEls() : null;
		if(els != null) {
			Set<Entry<String, EntityLabel>> se = els.entrySet();
			for (Iterator<Entry<String, EntityLabel>> iterator = se.iterator(); iterator.hasNext();) {
				Entry<String,EntityLabel> entry = (Entry<String,EntityLabel>) iterator.next();
				String labelName = entry.getValue().getMappedName();
				if(labelName != null && labelName.equalsIgnoreCase(productLabel)) {
					return entry.getValue().getAttributeName();
				}
			}
		}
		return null;
	}


	public static View getProductTableView(EntityLabel el, Context context, String fieldName) {
		if(el != null) {
			String mappedName = el.getMappedName();
			if(mappedName != null && !mappedName.equals("")) {
				if(mappedName.equalsIgnoreCase(PRODUCT_NAME) || mappedName.equalsIgnoreCase(PRODUCT_CODE) || mappedName.equalsIgnoreCase(PRODUCT_PRICE) 
						|| mappedName.equalsIgnoreCase(PRODUCT_CATEGORY) || mappedName.equalsIgnoreCase(PRODUCT_MANUFACTURER)
						|| mappedName.equalsIgnoreCase(PRODUCT_UNIT_OF_MEASURE) || mappedName.equalsIgnoreCase(PRODUCT_DESCRIPTION) 
						|| mappedName.equalsIgnoreCase(PRODUCT_ITEM_NO)) {
					TableTextView textView = new TableTextView(context);
					textView.title = el.getDisplayLabel();
					textView.fieldName = el.getAttributeName();
					textView.setTextViewAttributes();
					return textView;
				}
				else {
					TableEditText editText = new TableEditText(context);
					editText.title = el.getDisplayLabel();
					editText.fieldName = el.getAttributeName();
					editText.isClickable = true;
					editText.setTextViewAttributes();
					setTextViewDataType(el, editText);
					return editText;
				}
			} 
			else {
				TableEditText editText = new TableEditText(context);
				editText.title = el.getDisplayLabel();
				editText.fieldName = el.getAttributeName();
				editText.isClickable = true;
				editText.setTextViewAttributes();
				setTextViewDataType(el, editText);
				return editText;
			}
		} else if(fieldName.equalsIgnoreCase(PRODUCT_ID)) {
			TableTextView productId = new TableTextView(context);
			productId.fieldName = PRODUCT_ID;
			productId.isVisible = false;
			productId.isKey = true;
			productId.setTextViewAttributes();
			return productId;
		}
		return null;
	}


	public static CustomItemDTO getproductCustomItem(String fieldName,
			Context context, Product product) {

		CustomItemDTO dto = null;
		if(fieldName.equalsIgnoreCase(PRODUCT_ID)) {
			dto = new CustomItemDTO(new EntityLabel(PRODUCT_ID, 1), getProductTableView(null, context, fieldName));
			if(dto != null) {
				setProductValue(dto, product, fieldName);
			}
		} else {
			EntityLabel el = workflow.getMapEls() != null ? workflow.getMapEls()
					.get(fieldName) : null;
					if (el != null) {
						dto =  new CustomItemDTO(el, getProductTableView(el, context, fieldName));
						if(dto != null) {
							setProductValue(dto, product, fieldName);
						}
					}
		}
		return dto; 
	}

	private static void setProductValue(CustomItemDTO dto, Product product, String fieldName) {
		if(dto.getEntityLabel() != null) {
			String mappedName = dto.getEntityLabel().getMappedName();
			if(mappedName != null && !mappedName.equals("")) {
				if(mappedName.equalsIgnoreCase(PRODUCT_NAME)) {
					dto.setProductValue(product.getName());
				} else if(mappedName.equalsIgnoreCase(PRODUCT_ITEM_NO)) {
					dto.setProductValue(product.getItemId());
				} else if(mappedName.equalsIgnoreCase(PRODUCT_CATEGORY)) {
					dto.setProductValue(product.getCategory());
				} else if(mappedName.equalsIgnoreCase(PRODUCT_MANUFACTURER)) {
					dto.setProductValue(product.getManufacturer());
				} else if(mappedName.equalsIgnoreCase(PRODUCT_UNIT_OF_MEASURE)) {
					dto.setProductValue(product.getUnitOfMeasure());
				} else if(mappedName.equalsIgnoreCase(PRODUCT_DESCRIPTION)) {
					dto.setProductValue(product.getDescription());
				} else if(mappedName.equalsIgnoreCase(PRODUCT_PRICE)) {
					dto.setProductValue(product.getPrice());
				} 
				else {
					dto.setProductValue("");
				}
			} else {
				dto.setProductValue("");
			}
		} else if(fieldName.equalsIgnoreCase(PRODUCT_ID)) {
			dto.setProductValue(product.getId() + "");
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class CustomerAutoTextAdapter extends BaseAdapter implements Filterable ,android.widget.AdapterView.OnItemClickListener {
		private static final int MAX_RESULTS = 10;
		private Context mContext;
		private static String contraint = "";
		private List<Customer> resultList = new ArrayList<Customer>();
		private int pageIdx = 0;
		public CustomerAutoTextAdapter(Context context) {
			mContext = context;
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			//if(position < resultList.size() -1) {
			//Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			Customer customer = getItem(position);
			/*final String customerFName = cursor.getString(cursor.getColumnIndexOrThrow("customerName"));
				String customerMName = cursor.getString(cursor.getColumnIndexOrThrow(("customerMiddleName")));
				String customerLName = cursor.getString(cursor.getColumnIndexOrThrow("customerLastName"));
				String customerAdd1 = cursor.getString(cursor.getColumnIndexOrThrow("customerAddress1"));
				String customerAdd2 = cursor.getString(cursor.getColumnIndexOrThrow("customerAddress2"));
				String customerContactNo = cursor.getString(cursor.getColumnIndexOrThrow("customerContactNo"));
				String customerEmail = cursor.getString(cursor.getColumnIndexOrThrow("customerEmail"));
				String customerGender = cursor.getString(cursor.getColumnIndexOrThrow("customerGender"));
				String customerCity = cursor.getString(cursor.getColumnIndexOrThrow("customerCity"));
				String customerState = cursor.getString(cursor.getColumnIndexOrThrow("customerState"));
				String customerCountry = cursor.getString(cursor.getColumnIndexOrThrow("customerCountry"));
				String customerDOB = cursor.getString(cursor.getColumnIndexOrThrow("customerDOB"));
				String customerExternalId = cursor.getString(cursor.getColumnIndexOrThrow("customerExternalId"));
				String customerOrganization = cursor.getString(cursor.getColumnIndexOrThrow("customerOrganization"));
				String customerMobileNo = cursor.getString(cursor.getColumnIndexOrThrow("customerMobileNo"));
				String customerPinCode = cursor.getString(cursor.getColumnIndexOrThrow("customerPinCode"));

				String customerAttribute1 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute1"));
				String customerAttribute2 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute2"));
				String customerAttribute3 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute3"));
				String customerAttribute4 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute4"));
				String customerAttribute5 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute5"));
				String customerAttribute6 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute6"));
				String customerAttribute7 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute7"));
				String customerAttribute8 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute8"));
				String customerAttribute9 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute9"));
				String customerAttribute10 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute10"));
				String customerAttribute11 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute11"));
				String customerAttribute12 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute12"));
				String customerAttribute13 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute13"));
				String customerAttribute14 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute14"));
				String customerAttribute15 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute15"));
				String customerAttribute16 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute16"));
				String customerAttribute17 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute17"));
				String customerAttribute18 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute18"));
				String customerAttribute19 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute19"));
				String customerAttribute20 = cursor.getString(cursor.getColumnIndexOrThrow("customerAttribute20"));

				String customerType = cursor.getString(cursor.getColumnIndexOrThrow("customerType"));
				String customerSubOrg = cursor.getString(cursor.getColumnIndexOrThrow("customerSubOrg"));

				customerId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
				customerRowId = cursor.getInt(cursor.getColumnIndexOrThrow("rowid"));
			 */			
			customerServerId = customer.get_id();
			customerTmpRowId = customer.getRowId();
			customerTmpflagId = customer.get_id();

			for (EntityLabel el : customerDetailsMap.keySet()) {
				View v = customerDetailsMap.get(el);
				if(el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_MIDDLE_NAME)) 
					setCustomerValue(el,v, customer.getCustomerMiddleName());
				if(el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_LAST_NAME)) 
					setCustomerValue(el,v, customer.getCustomerLastName());
				if(el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_ADD1)) 
					setCustomerValue(el,v, customer.getCustomerAddress1());
				if(el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_ADD2)) 
					setCustomerValue(el,v, customer.getCustomerAddress2());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_CONTACT_NO)) 
					setCustomerValue(el,v, customer.getCustomerContactNo());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_EMAIL_ID)) 
					setCustomerValue(el,v, customer.getCustomerEmail());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_GENDER)) 
					setCustomerValue(el,v, customer.getCustomerGender());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_MOBILE_NO)) 
					setCustomerValue(el,v, customer.getCustomerMobileNo());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ORGANIZATION_NAME)) 
					setCustomerValue(el,v, customer.getCustomerOrganization());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_DOB)) 
					setCustomerValue(el,v, customer.getCustomerDOB());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_CITY)) 
					setCustomerValue(el,v, customer.getCustomerCity());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_STATE)) 
					setCustomerValue(el,v, customer.getCustomerState());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_COUNTRY)) 
					setCustomerValue(el,v, customer.getCustomerCountry());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_PINCODE)) 
					setCustomerValue(el,v, customer.getCustomerPinCode());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_EXTERNAL_ID)) 
					setCustomerValue(el,v, customer.getCustomerExternalId());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE1)) 
					setCustomerValue(el,v, customer.getCustomerAttribute1());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE2)) 
					setCustomerValue(el,v, customer.getCustomerAttribute2());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE3)) 
					setCustomerValue(el,v, customer.getCustomerAttribute3());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE4)) 
					setCustomerValue(el,v, customer.getCustomerAttribute4());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE5)) 
					setCustomerValue(el,v, customer.getCustomerAttribute5());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE6)) 
					setCustomerValue(el,v, customer.getCustomerAttribute6());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE7)) 
					setCustomerValue(el,v, customer.getCustomerAttribute7());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE8)) 
					setCustomerValue(el,v, customer.getCustomerAttribute8());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE9)) 
					setCustomerValue(el,v, customer.getCustomerAttribute9());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE10)) 
					setCustomerValue(el,v, customer.getCustomerAttribute10());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE11)) 
					setCustomerValue(el,v, customer.getCustomerAttribute11());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE12)) 
					setCustomerValue(el,v, customer.getCustomerAttribute12());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE13)) 
					setCustomerValue(el,v, customer.getCustomerAttribute13());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE14)) 
					setCustomerValue(el,v, customer.getCustomerAttribute14());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE15)) 
					setCustomerValue(el,v, customer.getCustomerAttribute15());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE16)) 
					setCustomerValue(el,v, customer.getCustomerAttribute16());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE17)) 
					setCustomerValue(el,v, customer.getCustomerAttribute17());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE18)) 
					setCustomerValue(el,v, customer.getCustomerAttribute18());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE19)) 
					setCustomerValue(el,v, customer.getCustomerAttribute19());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE20)) 
					setCustomerValue(el,v, customer.getCustomerAttribute20());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_TYPE)) 
					setCustomerValue(el,v, customer.getCustomerType());
				if(el.getAttributeName().equalsIgnoreCase(CUSTOMER_SUB_ORG)) 
					setCustomerValue(el,v, customer.getCustomerSubOrg());
			}


		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public Customer getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			/*if(position >= resultList.size() -1) {
				convertView = inflater.inflate(R.layout.custcontentview1, parent, false);
			} else {*/
			convertView = inflater.inflate(R.layout.custcontentview, parent, false);

			final String fname = getItem(position).getCustomerName();
			final String mName = getItem(position).getCustomerMiddleName();
			final String lName = getItem(position).getCustomerLastName();
			final String address1 = getItem(position).getCustomerAddress1();
			final String customerOrg = getItem(position).getCustomerOrganization();
			String fullName = fname;
			if(mName != null && !mName.equals("")) {
				fullName = fullName.concat(" " + mName);
			} if(lName != null && !lName.equals("")) {
				fullName = fullName.concat(" " + lName);
			}
			((TextView) convertView.findViewById(R.id.ccLarge)).setText(fullName);
			((TextView) convertView.findViewById(R.id.ccCenter)).setVisibility(View.VISIBLE);
			((TextView)convertView.findViewById(R.id.ccCenter)).setText(customerOrg);
			((TextView) convertView.findViewById(R.id.ccMed)).setText("(" + address1 != null ? address1 : ""+ ") " );
			//}	


			/*if(position==getCount()-1) {
					List<Customer> serverCust = customerService.findServerCustomers(contraint,0 );
					if(serverCust != null && serverCust.size() > 0) {
						resultList.clear();
						resultList.addAll(serverCust);
						notifyDataSetChanged();
					}

				}*/
			return convertView;
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {

					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						CustomerAutoTextAdapter.contraint = constraint.toString(); 
						List<Customer> customers = customerService.findServerCustomers(contraint,0);
						if(customers == null || !(customers.size() > 0)) {
							customers = customerService.findCustomers(constraint.toString());
						}
						// Assign the data to the FilterResults
						filterResults.values = customers;
						filterResults.count = customers.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						resultList = (List<Customer>) results.values;
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}};
				return filter;
		}
	} 


	private static void checkAddCustomerAttr(EntityLabel el, View tmpView) {
		if(el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_MIDDLE_NAME)
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_LAST_NAME) 
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_MIDDLE_NAME)
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_ADD1)
				|| el.getAttributeName().equalsIgnoreCase(COMPLAINT_CUST_ADD2)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE1)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE2)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE3)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE4)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE5)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE6)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE7)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE8)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE9)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE10)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE11)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE12)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE13)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE14)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE15)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE16)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE17)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE18)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE19)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ATTRIBUTE20)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_CITY)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_STATE)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_COUNTRY)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_PINCODE)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_DOB)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_CONTACT_NO)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_MOBILE_NO)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_EMAIL_ID)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_EXTERNAL_ID)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_ORGANIZATION_NAME)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_GENDER)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_TYPE)
				|| el.getAttributeName().equalsIgnoreCase(CUSTOMER_SUB_ORG)
				) {
			customerDetailsMap.put(el, tmpView);
		}

	}

	public static void setCustomerValue(EntityLabel el, View v, String value) {
		if(el.getDataType() != null && el.getDataType().equalsIgnoreCase("List")) {
			if(v instanceof Spinner) { 
				int i = getValuePosition(el.getListValues(), value);
				((Spinner) v).setSelection(i);
			}
		} else {
			if(v instanceof EditText) {
				((EditText) v).setText(value);
			}
		}
		v.setEnabled(false);
	}

	private static View getCameraLayout(EntityLabel el, final Context context) {
		EditText editText = new EditText(context);
		if(el.getMappedName() != null && el.getMappedName().equalsIgnoreCase(PRODUCT_NAME)) {
			editText = (EditText) getAutoCompleteLayout(el, context);
		}
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		editText.setHint(el.getToolTip() != null ? el.getToolTip() : "");
		// editText.setId(el.getId());
		//editText.setEnabled(false);
		//editText.setFocusable(false);
		LinearLayout.LayoutParams editparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		editText.setPadding(5, 0, 15, 0);
		// editparams.setMargins(0, 0, 30, 0);
		editText.setLayoutParams(editparams);
		final ImageView imageView = new ImageView(context);
		imageView.setClickable(true);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_RIGHT);
		// imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.barcode_on);
		imageView.setVisibility(View.VISIBLE);
		imageView.setTag(R.id.SELECTOR_OUTPUT_FIELD, editText);
		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				EditText outputField = (EditText) imageView.getTag(R.id.SELECTOR_OUTPUT_FIELD);
				AbstractActivity.setOutputField(outputField);
				File ATTACH_FILE_PATH = new File(Environment.getExternalStorageDirectory().toString() + _context.getString(R.string.attachmentsFilePath));
				if (!ATTACH_FILE_PATH.exists()) {
					ATTACH_FILE_PATH.mkdirs();
				}
				String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
				outputField.setText(fileName);
				File file = new File(ATTACH_FILE_PATH,fileName); 
				Uri outputFileUri = Uri.fromFile(file); 
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri); 
				((AbstractActivity)context).startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
		layout2.addView(editText);
		layout2.addView(imageView, imageParams);
		return layout2;
	}

	private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
			CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
		Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
				websiteUri));
		return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
				websiteUri));

	}

	/**
	 * Called when the Activity could not connect to Google Play services and the auto manager
	 * could resolve the error automatically.
	 * In this case the API is not available and notify the user.
	 *
	 * @param connectionResult can be inspected to determine the cause of the failure
	 */

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
				+ connectionResult.getErrorCode());
		// TODO(Developer): Check error code and notify the user of error state and resolution.
		Toast.makeText(this,
				"Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}
}
