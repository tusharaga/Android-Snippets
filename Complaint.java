package com.fieldez.android.domain.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fieldez.android.util.DateUtil;
import com.fieldez.android.util.StringUtil;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Complaint extends BaseModel implements Serializable {
	@DatabaseField
	private String customerName;
	@DatabaseField
	private String customerAddress;
	@DatabaseField
	private String customerAddress1;
	@DatabaseField
	private String customerAddress2;
	@DatabaseField
	private String customerContactNo;
	@DatabaseField
	private String customerContact;
	@DatabaseField
	private String modelName;
	@DatabaseField
	private String problemDescription;
	@DatabaseField
	private String status;
	@DatabaseField
	private String productName;
	@DatabaseField
	private String productSerialNo;
	@DatabaseField
	private String productWarrantyType;
	@DatabaseField(unique = true)
	private String externalId;
	@DatabaseField
	private String specialInstructions;
	@DatabaseField
	private String remarks;
	@DatabaseField
	private String attribute1;
	@DatabaseField
	private String attribute2;
	@DatabaseField
	private String attribute3;
	@DatabaseField
	private String attribute4;
	@DatabaseField
	private String attribute5;
	@DatabaseField
	private String attribute6;
	@DatabaseField
	private String attribute7;
	@DatabaseField
	private String attribute8;
	@DatabaseField
	private String attribute9;
	@DatabaseField
	private String attribute10;
	@DatabaseField
	private String attribute11;
	@DatabaseField
	private String attribute12;
	@DatabaseField
	private String attribute13;
	@DatabaseField
	private String attribute14;
	@DatabaseField
	private String attribute15;
	@DatabaseField
	private String attribute16;
	@DatabaseField
	private String attribute17;
	@DatabaseField
	private String attribute18;
	@DatabaseField
	private String attribute19;
	@DatabaseField
	private String attribute20;
	@DatabaseField
	private String attribute21;
	@DatabaseField
	private String attribute22;
	@DatabaseField
	private String attribute23;
	@DatabaseField
	private String attribute24;
	@DatabaseField
	private String attribute25;
	@DatabaseField
	private String attribute26;
	@DatabaseField
	private String attribute27;
	@DatabaseField
	private String attribute28;
	@DatabaseField
	private String attribute29;
	@DatabaseField
	private String attribute30;
	@DatabaseField
	private int workflowId;
	@DatabaseField
	private String complaintStatus;
	@DatabaseField
	private int complaintStatusId;
	@DatabaseField(dataType = DataType.DATE_STRING)
	private Date appointmentTime;
	@DatabaseField
	private boolean deleted;
	@DatabaseField
	private int scheduleId;
	@DatabaseField
	private String workflowName;
	@DatabaseField
	private boolean closed;

	@DatabaseField
	private boolean synching;

	@DatabaseField private int worklocationId;

	@DatabaseField private int parentId;
	@DatabaseField private String parentExternalId;


	@DatabaseField
	private String customerMiddleName;

	@DatabaseField
	private String customerLastName;

	@DatabaseField
	private Date clientCreateTime;

	@DatabaseField
	private double latitude;

	@DatabaseField
	private double longitude;

	@DatabaseField
	private String createdBy;

	@DatabaseField
	private boolean actionable;

	@DatabaseField
	private int customerId;
	private Customer customer;

	private Collection<ComplaintLineItem> lineItems;
	private Collection<ComplaintAttachment> complaintAttachment;
	private Collection<ComplaintActivity> activities;

	private ComplaintWorkflow complaintWorkflow;

	private boolean activityAdded;
	private User assignee;
	private ComplaintSchedule schedule;
	private boolean hasAttachments;
	private String subordinateExternalId;
	private Map<String, String> keyVal;
	
	private int tmpSyncId;

	private ComplaintActivity currentActivity;

	public Complaint(ComplaintSchedule cs, String externalId) {
		this.schedule = cs;
		this.externalId = externalId;
	}

	public Complaint(ComplaintSchedule cs, String externalId, int complaintId) {
		this.schedule = cs;
		this.externalId = externalId;
		setId(complaintId);
	}


	public void setLoginKeyValue(User u) {
		keyVal = new HashMap<String, String>();
		keyVal.put("assignee.externalId", u.getExternalId());
		keyVal.put("assignee.password", u.getPassword());
	}

	public void setKeyValueForComplaintDetails() {
		if(!StringUtil.isEmpty((externalId)))
			keyVal.put("externalId", externalId);			
		 else
			 keyVal.put("id", tmpSyncId +"");
	}

	public void setKeyValuePairForSubOrdinates() {
		keyVal = new HashMap<String, String>();
		keyVal.put("actor.externalId", getUserId());
		keyVal.put("actor.password", getUserPassword());
		keyVal.put("assignee.externalId", this.getSubordinateExternalId());
	}


	public void setKeyValueForUpdate() {
		keyVal.put("user.externalId", getUserId());
		keyVal.put("user.password", getUserPassword());
	}

	public void setCustomerServerId(int customerId) {
		if(customerId > 0) {
			keyVal.put("customer.id", Integer.toString(customerId));
		}
	}
	
	public void setkeyValForSave() {
		keyVal = new HashMap<String, String>();

		keyVal.put("appLoginId", getUserId());
		keyVal.put("appPassword", getUserPassword());

		
		if (customerName != null && !customerName.equals(""))
			keyVal.put("customer.name", customerName);

		if(customerMiddleName != null && !customerMiddleName.equals("")) {
			keyVal.put("customer.middleName", customerMiddleName);
		}

		if(customerLastName != null && !customerLastName.equals("")) {
			keyVal.put("customer.lastName", customerLastName);
		}
		if (customerAddress1 != null && customerAddress1 != "")
			keyVal.put("customer.address1", customerAddress1);

		if (problemDescription != null && !problemDescription.equals(""))
			keyVal.put("problemDescription", problemDescription);

		//	ComplaintSchedule cs = createSchedule();
		keyVal.put("schedule[0].assignee.externalId", getSchedule().getAssigneeExternalId());

		if(getSchedule().getStartTime() != null) {
			keyVal.put("schedule[0].startTime", DateUtil.formatDateTimeToSend(getSchedule().getStartTime()));
		}
		if(getSchedule().getEndTime() != null) {
			keyVal.put("schedule[0].endTime", DateUtil
					.formatDateTimeToSend(getSchedule().getEndTime()));
		}

		keyVal.put("externalId", externalId);
		// this.externalId=externalId.concat("-").concat(c.getUser().getExternalId());
		if (workflowName != null && !workflowName.equals("")) {
			keyVal.put("workflow.name", workflowName);
		}

		if (customerAddress2 != null && !customerAddress2.equals("")) {
			keyVal.put("customer.address2", customerAddress2);
		}

		if (customerContactNo != null && !customerContactNo.equals("")) {
			keyVal.put("customerContactNo", customerContactNo);
		}
		if (customerContact != null && !customerContact.equals("")) {
			keyVal.put("customerContact", customerContact);
		}

		if (modelName != null && !modelName.equals("")) {
			keyVal.put("modelName", modelName);
		}

		if (productName != null && !productName.equals("")) {
			keyVal.put("productName", productName);
		}

		if (productSerialNo != null && !productSerialNo.equals("")) {
			keyVal.put("productSerialNo", productSerialNo);
		}

		if (productWarrantyType != null && !productWarrantyType.equals("")) {
			keyVal.put("productWarrantyType", productWarrantyType);
		}

		if (remarks != null && remarks != "") {
			keyVal.put("remarks", remarks);
		}

		if (specialInstructions != null && !specialInstructions.equals("")) {
			keyVal.put("specialInstructions", specialInstructions);
		}

		if (productSerialNo != null && !productSerialNo.equals("")) {
			keyVal.put("productSerialNo", productSerialNo);
		}

		if (attribute1 != null && !attribute1.equals("")) {
			keyVal.put("attribute1", attribute1);
		}

		if (attribute2 != null && !attribute2.equals("")) {
			keyVal.put("attribute2", attribute2);
		}

		if (attribute3 != null && !attribute3.equals("")) {
			keyVal.put("attribute3", attribute3);
		}

		if (attribute4 != null && !attribute4.equals("")) {
			keyVal.put("attribute4", attribute4);
		}

		if (attribute5 != null && !attribute5.equals("")) {
			keyVal.put("attribute5", attribute5);
		}

		if (attribute6 != null && !attribute6.equals("")) {
			keyVal.put("attribute6", attribute6);
		}

		if (attribute7 != null && !attribute7.equals("")) {
			keyVal.put("attribute7", attribute7);
		}

		if (attribute8 != null && !attribute8.equals("")) {
			keyVal.put("attribute8", attribute8);
		}

		if (attribute9 != null && !attribute9.equals("")) {
			keyVal.put("attribute9", attribute9);
		}

		if (attribute10 != null && !attribute10.equals("")) {
			keyVal.put("attribute10", attribute10);
		}

		if (attribute11 != null && !attribute11.equals("")) {
			keyVal.put("attribute11", attribute11);
		}

		if (attribute12 != null &&! attribute12.equals("")) {
			keyVal.put("attribute12", attribute12);
		}

		if (attribute13 != null && !attribute13.equals("")) {
			keyVal.put("attribute13", attribute13);
		}

		if (attribute14 != null && !attribute14.equals("")) {
			keyVal.put("attribute14", attribute14);
		}

		if (attribute15 != null && !attribute15.equals("")) {
			keyVal.put("attribute15", attribute15);
		}

		if (attribute16 != null && !attribute16.equals("")) {
			keyVal.put("attribute16", attribute16);
		}

		if (attribute17 != null && !attribute17.equals("")) {
			keyVal.put("attribute17", attribute17);
		}

		if (attribute18 != null && !attribute18.equals("")) {
			keyVal.put("attribute18", attribute18);
		}

		if (attribute19 != null && !attribute19.equals("")) {
			keyVal.put("attribute19", attribute19);
		}

		if (attribute20 != null && !attribute20.equals("")) {
			keyVal.put("attribute20", attribute20);
		}
		if (attribute21 != null && !attribute21.equals("")) {
			keyVal.put("attribute21", attribute21);
		}
		if (attribute22 != null && !attribute22.equals("")) {
			keyVal.put("attribute22", attribute22);
		}
		if (attribute23 != null && !attribute23.equals("")) {
			keyVal.put("attribute23", attribute23);
		}
		if (attribute24 != null && !attribute24.equals("")) {
			keyVal.put("attribute24", attribute24);
		}
		if (attribute25 != null && !attribute25.equals("")) {
			keyVal.put("attribute25", attribute25);
		}
		if (attribute26 != null && !attribute26.equals("")) {
			keyVal.put("attribute26", attribute26);
		}
		if (attribute27 != null && !attribute27.equals("")) {
			keyVal.put("attribute27", attribute27);
		}
		if (attribute28 != null && !attribute28.equals("")) {
			keyVal.put("attribute28", attribute28);
		}
		if (attribute29 != null && !attribute29.equals("")) {
			keyVal.put("attribute29", attribute29);
		}
		if (attribute30 != null && !attribute30.equals("")) {
			keyVal.put("attribute30", attribute30);
		}
		if(worklocationId != 0) {
			keyVal.put("workLocation.id", Integer.toString(worklocationId));
		} 
		if(parentId > 0) {
			keyVal.put("parent.id", Integer.toString(parentId));
		}
		if(parentExternalId != null && !parentExternalId.equals("")) {
			keyVal.put("parent.externalId", parentExternalId);
		}
		/*if(status != null && !status.equals("")) {
			keyVal.put("complaintStatus.status", status);
		}*/
		if(clientCreateTime != null) {
			keyVal.put("clientCreateTime", DateUtil
					.formatDateTimeToSend(clientCreateTime));
		}
		if(longitude != 0.0) {
			keyVal.put("longitude", Double.toString(longitude));			
		}
		if(latitude != 0.0) {
			keyVal.put("latitude", Double.toString(latitude));			
		}
		
		if(customer != null) {
			if(customer.getCustomerCity() != null) {
				keyVal.put("customer.city", customer.getCustomerCity());
			}
			
			if(customer.getCustomerState() != null) {
				keyVal.put("customer.state", customer.getCustomerState());
			}
			
			if(customer.getCustomerCountry() != null) {
				keyVal.put("customer.country", customer.getCustomerCountry());
			}
			
			if(customer.getCustomerPinCode() != null) {
				keyVal.put("customer.pincode", customer.getCustomerPinCode());
			}
			
			if(customer.getCustomerContactNo() != null) {
				keyVal.put("customer.contactNo", customer.getCustomerContactNo());
			}
			
			if(customer.getCustomerMobileNo() != null) {
				keyVal.put("customer.mobileNo", customer.getCustomerMobileNo());
			}
			
			if(customer.getCustomerExternalId() != null) {
				keyVal.put("customer.customerExternalId", customer.getCustomerExternalId());
			}
			
			if(customer.getCustomerEmail() != null) {
				keyVal.put("customer.emailId", customer.getCustomerEmail());
			}
			
			if(customer.getCustomerGender() != null) {
				keyVal.put("customer.gender", customer.getCustomerGender());
			}

			if(customer.getCustomerOrganization() != null) {
				keyVal.put("customer.organizationName", customer.getCustomerOrganization());
			}
			
			if(customer.getCustomerDOB() != null) {
				keyVal.put("customer.dateOfBirth", customer.getCustomerDOB());
			}
			
			if(customer.getCustomerAttribute1() != null) {
				keyVal.put("customer.attribute1", customer.getCustomerAttribute1());
			}
			
			if(customer.getCustomerAttribute2() != null) {
				keyVal.put("customer.attribute2", customer.getCustomerAttribute2());
			}
			
			if(customer.getCustomerAttribute3() != null) {
				keyVal.put("customer.attribute3", customer.getCustomerAttribute3());
			}
			
			if(customer.getCustomerAttribute4() != null) {
				keyVal.put("customer.attribute4", customer.getCustomerAttribute4());
			}
			
			if(customer.getCustomerAttribute5() != null) {
				keyVal.put("customer.attribute5", customer.getCustomerAttribute5());
			}
			
			if(customer.getCustomerAttribute6() != null) {
				keyVal.put("customer.attribute6", customer.getCustomerAttribute6());
			}
			
			if(customer.getCustomerAttribute7() != null) {
				keyVal.put("customer.attribute7", customer.getCustomerAttribute7());
			}
			
			if(customer.getCustomerAttribute8() != null) {
				keyVal.put("customer.attribute8", customer.getCustomerAttribute8());
			}
			
			if(customer.getCustomerAttribute9() != null) {
				keyVal.put("customer.attribute9", customer.getCustomerAttribute9());
			}
			
			if(customer.getCustomerAttribute10() != null) {
				keyVal.put("customer.attribute10", customer.getCustomerAttribute10());
			}
			
			if(customer.getCustomerAttribute11() != null) {
				keyVal.put("customer.attribute11", customer.getCustomerAttribute11());
			}
			
			if(customer.getCustomerAttribute12() != null) {
				keyVal.put("customer.attribute12", customer.getCustomerAttribute12());
			}
			
			if(customer.getCustomerAttribute13() != null) {
				keyVal.put("customer.attribute13", customer.getCustomerAttribute13());
			}
			
			if(customer.getCustomerAttribute14() != null) {
				keyVal.put("customer.attribute14", customer.getCustomerAttribute14());
			}
			
			if(customer.getCustomerAttribute15() != null) {
				keyVal.put("customer.attribute15", customer.getCustomerAttribute15());
			}
			
			if(customer.getCustomerAttribute16() != null) {
				keyVal.put("customer.attribute16", customer.getCustomerAttribute16());
			}
			
			if(customer.getCustomerAttribute17() != null) {
				keyVal.put("customer.attribute17", customer.getCustomerAttribute17());
			}
			
			if(customer.getCustomerAttribute18() != null) {
				keyVal.put("customer.attribute18", customer.getCustomerAttribute18());
			}
			
			if(customer.getCustomerAttribute19() != null) {
				keyVal.put("customer.attribute19", customer.getCustomerAttribute19());
			}
			
			if(customer.getCustomerAttribute20() != null) {
				keyVal.put("customer.attribute20", customer.getCustomerAttribute20());
			}
			
			if(customer.getCustomerType() != null) {
				keyVal.put("customer.customerType", customer.getCustomerType());
			}
			
			if(customer.getCustomerSubOrg() != null) {
				keyVal.put("customer.subOrg", customer.getCustomerSubOrg());
			}
		}
		
	}

	public ComplaintSchedule createSchedule(Date startDate, Date endDate, String assignee) {
		ComplaintSchedule cs = new ComplaintSchedule();
		if(startDate == null) {
			startDate  = new Date();
		}
		if(endDate == null) {
			endDate = new Date(new Date().getTime() + 1800000);
		} 
		if(assignee == null || assignee.equals("")) {
			cs.setAssigneeExternalId(getUserId());
		} else {
			cs.setAssigneeExternalId(assignee);
		}
		cs.setStartTime(startDate);
		cs.setEndTime(endDate);
		setSchedule(cs);
		return cs;
	}

	@Override
	public Map<String, String> toKeyValuePair() {
		return keyVal;
	}

	public Complaint() {
		// TODO Auto-generated constructor stub
	}


	public Complaint(int id, String externalId) {
		setId(id);
		this.externalId = externalId;
	}


	public Complaint(User user) {
		super(user);
		setLoginKeyValue(user);
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerAddress1() {
		return customerAddress1;
	}

	public void setCustomerAddress1(String customerAddress1) {
		this.customerAddress1 = customerAddress1;
	}

	public String getCustomerAddress2() {
		return customerAddress2;
	}

	public void setCustomerAddress2(String customerAddress2) {
		this.customerAddress2 = customerAddress2;
	}

	public String getCustomerContactNo() {
		return customerContactNo;
	}

	public void setCustomerContactNo(String customerContactNo) {
		this.customerContactNo = customerContactNo;
	}

	public String getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSerialNo() {
		return productSerialNo;
	}

	public void setProductSerialNo(String productSerialNo) {
		this.productSerialNo = productSerialNo;
	}

	public String getProductWarrantyType() {
		return productWarrantyType;
	}

	public void setProductWarrantyType(String productWarrantyType) {
		this.productWarrantyType = productWarrantyType;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}

	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}

	public String getAttribute8() {
		return attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}

	public String getAttribute9() {
		return attribute9;
	}

	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}

	public String getAttribute10() {
		return attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

	public String getAttribute11() {
		return attribute11;
	}

	public void setAttribute11(String attribute11) {
		this.attribute11 = attribute11;
	}

	public String getAttribute12() {
		return attribute12;
	}

	public void setAttribute12(String attribute12) {
		this.attribute12 = attribute12;
	}


	public String getSubordinateExternalId() {
		return subordinateExternalId;
	}

	public void setSubordinateExternalId(String subordinateExternalId) {
		this.subordinateExternalId = subordinateExternalId;
	}

	public String getAttribute13() {
		return attribute13;
	}

	public void setAttribute13(String attribute13) {
		this.attribute13 = attribute13;
	}

	public String getAttribute14() {
		return attribute14;
	}

	public void setAttribute14(String attribute14) {
		this.attribute14 = attribute14;
	}

	public String getAttribute15() {
		return attribute15;
	}

	public void setAttribute15(String attribute15) {
		this.attribute15 = attribute15;
	}

	public String getAttribute16() {
		return attribute16;
	}

	public void setAttribute16(String attribute16) {
		this.attribute16 = attribute16;
	}

	public String getAttribute17() {
		return attribute17;
	}

	public void setAttribute17(String attribute17) {
		this.attribute17 = attribute17;
	}

	public String getAttribute18() {
		return attribute18;
	}

	public void setAttribute18(String attribute18) {
		this.attribute18 = attribute18;
	}

	public String getAttribute19() {
		return attribute19;
	}

	public void setAttribute19(String attribute19) {
		this.attribute19 = attribute19;
	}

	public String getAttribute20() {
		return attribute20;
	}

	public void setAttribute20(String attribute20) {
		this.attribute20 = attribute20;
	}


	public String getAttribute21() {
		return attribute21;
	}

	public void setAttribute21(String attribute21) {
		this.attribute21 = attribute21;
	}

	public String getAttribute22() {
		return attribute22;
	}

	public void setAttribute22(String attribute22) {
		this.attribute22 = attribute22;
	}

	public String getAttribute23() {
		return attribute23;
	}

	public void setAttribute23(String attribute23) {
		this.attribute23 = attribute23;
	}

	public String getAttribute24() {
		return attribute24;
	}

	public void setAttribute24(String attribute24) {
		this.attribute24 = attribute24;
	}

	public String getAttribute25() {
		return attribute25;
	}

	public void setAttribute25(String attribute25) {
		this.attribute25 = attribute25;
	}

	public String getAttribute26() {
		return attribute26;
	}

	public void setAttribute26(String attribute26) {
		this.attribute26 = attribute26;
	}

	public String getAttribute27() {
		return attribute27;
	}

	public void setAttribute27(String attribute27) {
		this.attribute27 = attribute27;
	}

	public String getAttribute28() {
		return attribute28;
	}

	public void setAttribute28(String attribute28) {
		this.attribute28 = attribute28;
	}

	public String getAttribute29() {
		return attribute29;
	}

	public void setAttribute29(String attribute29) {
		this.attribute29 = attribute29;
	}

	public String getAttribute30() {
		return attribute30;
	}

	public void setAttribute30(String attribute30) {
		this.attribute30 = attribute30;
	}

	public Date getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public boolean isActivityAdded() {
		return activityAdded;
	}

	public void setActivityAdded(boolean activityAdded) {
		this.activityAdded = activityAdded;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isHasAttachments() {
		return hasAttachments;
	}

	public void setHasAttachments(boolean hasAttachments) {
		this.hasAttachments = hasAttachments;
	}

	public Collection<ComplaintLineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Collection<ComplaintLineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public String getComplaintStatus() {
		return complaintStatus;
	}

	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public int getComplaintStatusId() {
		return complaintStatusId;
	}

	public void setComplaintStatusId(int complaintStatusId) {
		this.complaintStatusId = complaintStatusId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Collection<ComplaintActivity> getActivities() {
		return activities;
	}

	public void setActivities(Collection<ComplaintActivity> activities) {
		this.activities = activities;
	}

	public ComplaintWorkflow getComplaintWorkflow() {
		return complaintWorkflow;
	}

	public void setComplaintWorkflow(ComplaintWorkflow complaintWorkflow) {
		this.complaintWorkflow = complaintWorkflow;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public Map<String, String> getKeyVal() {
		return keyVal;
	}

	public void setKeyVal(Map<String, String> keyVal) {
		this.keyVal = keyVal;
	}

	public ComplaintSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(ComplaintSchedule schedule) {
		this.schedule = schedule;
	}

	public Collection<ComplaintAttachment> getComplaintAttachment() {
		return complaintAttachment;
	}

	public void setComplaintAttachment(
			Collection<ComplaintAttachment> complaintAttachment) {
		this.complaintAttachment = complaintAttachment;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isSynching() {
		return synching;
	}

	public void setSynching(boolean synching) {
		this.synching = synching;
	}


	public int getWorklocationId() {
		return worklocationId;
	}

	public void setWorklocationId(int worklocationId) {
		this.worklocationId = worklocationId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getParentExternalId() {
		return parentExternalId;
	}

	public void setParentExternalId(String parentExternalId) {
		this.parentExternalId = parentExternalId;
	}


	public ComplaintActivity getCurrentActivity() {
		return currentActivity;
	}


	public void setCurrentActivity(ComplaintActivity currentActivity) {
		this.currentActivity = currentActivity;
	}



	public String getCustomerMiddleName() {
		return customerMiddleName;
	}


	public void setCustomerMiddleName(String customerMiddleName) {
		this.customerMiddleName = customerMiddleName;
	}


	public String getCustomerLastName() {
		return customerLastName;
	}


	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}


	public Date getClientCreateTime() {
		return clientCreateTime;
	}


	public void setClientCreateTime(Date clientCreateTime) {
		this.clientCreateTime = clientCreateTime;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public boolean isActionable() {
		return actionable;
	}

	public void setActionable(boolean actionable) {
		this.actionable = actionable;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getTmpSyncId() {
		return tmpSyncId;
	}

	public void setTmpSyncId(int tmpSyncId) {
		this.tmpSyncId = tmpSyncId;
	}

	public ComplaintLineItem getComplaintLineItemByServerId(int serverId) {
		for (ComplaintLineItem cli : this.getLineItems()) {
			if (cli.getServerId() == serverId) {
				return cli;
			}
		}
		return new ComplaintLineItem();
	}

	public void setPage(int pageindex) {
		keyVal.put("page", Integer.toString(pageindex));

	}

	@Override
	public String toString() {
		return "Complaint [customerName=" + customerName + ", customerAddress="
				+ customerAddress + ", customerAddress1=" + customerAddress1
				+ ", customerAddress2=" + customerAddress2
				+ ", customerContactNo=" + customerContactNo
				+ ", customerContact=" + customerContact + ", modelName="
				+ modelName + ", problemDescription=" + problemDescription
				+ ", status=" + status + ", productName=" + productName
				+ ", productSerialNo=" + productSerialNo
				+ ", productWarrantyType=" + productWarrantyType
				+ ", externalId=" + externalId + ", specialInstructions="
				+ specialInstructions + ", remarks=" + remarks
				+ ", attribute1=" + attribute1 + ", attribute2=" + attribute2
				+ ", attribute3=" + attribute3 + ", attribute4=" + attribute4
				+ ", attribute5=" + attribute5 + ", attribute6=" + attribute6
				+ ", attribute7=" + attribute7 + ", attribute8=" + attribute8
				+ ", attribute9=" + attribute9 + ", attribute10=" + attribute10
				+ ", attribute11=" + attribute11 + ", attribute12="
				+ attribute12 + ", attribute13=" + attribute13
				+ ", attribute14=" + attribute14 + ", attribute15="
				+ attribute15 + ", attribute16=" + attribute16
				+ ", attribute17=" + attribute17 + ", attribute18="
				+ attribute18 + ", attribute19=" + attribute19
				+ ", attribute20=" + attribute20 + ", attribute21="
				+ attribute21 + ", attribute22=" + attribute22
				+ ", attribute23=" + attribute23 + ", attribute24="
				+ attribute24 + ", attribute25=" + attribute25
				+ ", attribute26=" + attribute26 + ", attribute27="
				+ attribute27 + ", attribute28=" + attribute28
				+ ", attribute29=" + attribute29 + ", attribute30="
				+ attribute30 + ", workflowId=" + workflowId
				+ ", complaintStatus=" + complaintStatus
				+ ", complaintStatusId=" + complaintStatusId
				+ ", appointmentTime=" + appointmentTime + ", deleted="
				+ deleted + ", scheduleId=" + scheduleId + ", workflowName="
				+ workflowName + ", closed=" + closed + ", synching="
				+ synching + ", worklocationId=" + worklocationId
				+ ", parentId=" + parentId + ", parentExternalId="
				+ parentExternalId + ", customerMiddleName="
				+ customerMiddleName + ", customerLastName=" + customerLastName
				+ ", clientCreateTime=" + clientCreateTime + ", latitude="
				+ latitude + ", longitude=" + longitude + ", createdBy="
				+ createdBy + ", actionable=" + actionable + ", customerId="
				+ customerId + ", customer=" + customer + ", lineItems="
				+ lineItems + ", complaintAttachment=" + complaintAttachment
				+ ", activities=" + activities + ", complaintWorkflow="
				+ complaintWorkflow + ", activityAdded=" + activityAdded
				+ ", assignee=" + assignee + ", schedule=" + schedule
				+ ", hasAttachments=" + hasAttachments
				+ ", subordinateExternalId=" + subordinateExternalId
				+ ", tmpSyncId=" + tmpSyncId + ", currentActivity="
				+ currentActivity + "]";
	}

	
}
