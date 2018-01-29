package com.fieldez.android.domain.service;

import com.fieldez.android.domain.model.Complaint;
import com.fieldez.android.util.EventType;

public interface ComplaintService {
	void getComplaintFromServer(String id,int Lid, boolean updateDirty,EventType eventType);
	public void getOpenCallsFromServer();
	public void getClosedCallsFromServer(int index);
	public void save(Complaint complaint);
	public int update(Complaint complaint);
	public void saveOrUpdate(Complaint c);
	public Complaint getComplaintDetailsByExternalId(String externalId);
	public void saveToServer(Complaint complaint, String mode);
	public void getComplaintFromServer(Complaint c, EventType eventType);
	public void deleteById(int id);
	public void deleteComplaint(int complaintId);
	public Complaint getComplaintDetailsById(int id);
	
	
}
