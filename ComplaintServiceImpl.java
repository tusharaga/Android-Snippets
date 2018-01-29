package com.fieldez.android.domain.service.impl;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.fieldez.android.SessionManager;
import com.fieldez.android.domain.handler.ComplaintDetailHandler;
import com.fieldez.android.domain.handler.OpenComplaintHandler;
import com.fieldez.android.domain.handler.SaveComplaintHandler;
import com.fieldez.android.domain.model.Complaint;
import com.fieldez.android.domain.model.ComplaintActivity;
import com.fieldez.android.domain.model.ComplaintAttachment;
import com.fieldez.android.domain.model.ComplaintLineItem;
import com.fieldez.android.domain.model.ComplaintSchedule;
import com.fieldez.android.domain.model.Customer;
import com.fieldez.android.domain.model.EventReminder;
import com.fieldez.android.domain.model.HandlerContext;
import com.fieldez.android.domain.model.User;
import com.fieldez.android.domain.repository.ComplaintActivityRepository;
import com.fieldez.android.domain.repository.ComplaintAttachmentRepository;
import com.fieldez.android.domain.repository.ComplaintLineItemRepository;
import com.fieldez.android.domain.repository.ComplaintRepository;
import com.fieldez.android.domain.repository.ComplaintScheduleRepository;
import com.fieldez.android.domain.service.ComplaintActivityService;
import com.fieldez.android.domain.service.ComplaintAttachmentService;
import com.fieldez.android.domain.service.ComplaintLineItemService;
import com.fieldez.android.domain.service.ComplaintScheduleService;
import com.fieldez.android.domain.service.ComplaintService;
import com.fieldez.android.domain.service.CustomerService;
import com.fieldez.android.domain.service.EventReminderService;
import com.fieldez.android.domain.service.UserService;
import com.fieldez.android.ui.Attachments;
import com.fieldez.android.util.CommonUtil;
import com.fieldez.android.util.Constants;
import com.fieldez.android.util.Constants.HttpType;
import com.fieldez.android.util.AttachmentDownloadUtil;
import com.fieldez.android.util.EventType;
import com.fieldez.android.util.HttpConnector;
import com.fieldez.android.util.Log;
import com.fieldez.android.util.StringUtil;
import com.fieldez.mobile.ApplicationContext;
import com.fieldez.mobile.R;

public class ComplaintServiceImpl implements ComplaintService {

	private static ComplaintServiceImpl cs;
	private static ComplaintRepository cr;
	private static ComplaintLineItemRepository clir;
	private static ComplaintLineItemService clis;
	private static ComplaintActivityService cas;
	private static ComplaintActivityRepository car;
	private static ComplaintAttachmentService cAtts;
	private static ComplaintAttachmentRepository cAttr;
	private static ComplaintScheduleRepository csr;
	private static ComplaintScheduleService css;
	private static UserService userService;
	private static EventReminderService reminderService;
	private static CustomerService customerService;
	private static ComplaintAttachmentService attachmentService;
	SessionManager session;
	private static final String TAG = ComplaintServiceImpl.class.getSimpleName();


	private static Context _context = ApplicationContext.getContext();
	private ComplaintServiceImpl() {
		cr = ComplaintRepository.getInstance();
		clir = ComplaintLineItemRepository.getInstance();
		session = new SessionManager(ApplicationContext.getContext());
		clis = ComplaintLineItemServiceImpl.getInstance();
		cas = ComplaintActivityServiceImpl.getInstance();
		car = ComplaintActivityRepository.getInstance();
		cAttr = ComplaintAttachmentRepository.getInstance();
		cAtts = ComplaintAttachmentServiceImpl.getInstance();
		csr = ComplaintScheduleRepository.getInstance();
		css = ComplaintScheduleServiceImpl.getInstance();
		userService = UserServiceImpl.getInstance();
		reminderService = EventReminderServiceImpl.getInstance();
		customerService = CustomerServiceImpl.getInstance();
		attachmentService = ComplaintAttachmentServiceImpl.getInstance();
	}

	public static ComplaintServiceImpl getInstance() {
		if (cs == null)
			cs = new ComplaintServiceImpl();
		return cs;

	}

	public void syncComplaint() {

	}

	public void getOpenCallsFromServer() {
		Log.d(TAG, " ComplaintServiceImp : getOpenCallsFromServer");
		cr.setSyncing();
		HandlerContext ctx = null;
		Context c = ApplicationContext.getContext();
		if (session != null && session.isLoggedIn()) {
			String userExternalId = session.getUserDetails().get("id");
			String userPassword = session.getUserDetails().get("password");
			User u = new User(userExternalId, userPassword);
			Complaint com = new Complaint(u);
			com.setSubordinateExternalId(userExternalId);
			ctx = new HandlerContext(new OpenComplaintHandler(), com, Constants.URL_TO_GET_OPEN_CALLS,HttpType.POST);
			HttpConnector con = new HttpConnector();
			con.postData(ctx);

			List<User> subordinates = userService.getSubordinates();
			for (User user : subordinates) {
				Complaint complaint = new Complaint(u);
				complaint.setSubordinateExternalId(user.getExternalId());
				complaint.setKeyValuePairForSubOrdinates();
				HandlerContext context = new HandlerContext(
						new OpenComplaintHandler(), complaint, Constants.URL_TO_GET_OPEN_CALLS,HttpType.POST);
				HttpConnector conn = new HttpConnector();
				conn.postData(context);

			}
		}
	}


	public void getComplaintFromServer(String externalId, int Lid,
			boolean updateDirty, EventType eventType) {
		HandlerContext ctx = null;

		if (session.isLoggedIn()) {
			String userExternalId = session.getUserDetails().get("id");
			String userPassword = session.getUserDetails().get("password");
			User u = new User(userExternalId, userPassword);
			Complaint com = new Complaint(u);
			com.setExternalId(externalId);
			if (Lid != 0) {
				com.setTmpSyncId(Lid);
				com.setId(Lid);
			}
			com.setKeyValueForComplaintDetails();
			ctx = new HandlerContext(new ComplaintDetailHandler(), com, Constants.URL_TO_GET_CALL_DETAILS,HttpType.POST, eventType);
			HttpConnector con = new HttpConnector();
			con.postData(ctx);
		}
	}

	public void getComplaintFromServer(Complaint c, EventType eventType) {
		HandlerContext ctx = null;
		String userExternalId = session.getUserDetails().get("id");
		String userPassword = session.getUserDetails().get("password");
		User u = new User(userExternalId, userPassword);
		c.setUser(u);
		c.setLoginKeyValue(u);
		c.setKeyValueForComplaintDetails();
		ctx = new HandlerContext(new ComplaintDetailHandler(), c, Constants.URL_TO_GET_CALL_DETAILS,HttpType.POST, eventType);
		HttpConnector con = new HttpConnector();
		con.postData(ctx);
	}

	public int SaveComplaint(Complaint complaint) {
		int id = cr.persist(complaint);
		for (ComplaintLineItem cli : complaint.getLineItems()) {
			cli.setComplaintId(complaint.getServerId());
			clir.persist(cli);
		}
		return id;
	}

	public void save(Complaint c) {

	}

	public int update(Complaint complaint) {
		return cr.update(complaint);

	}

	public void saveOrUpdate(Complaint c) {
		Log.d(TAG, " ComplaintServiceImp : saveOrUpdate");
		Date syncDate = new Date();
		Complaint dbComplaint = cr.getSingleEntityByProperty("serverId", c.getServerId());
		if(dbComplaint != null) {
			c.setId(dbComplaint.getId());
			c.setDirty(false);
			cr.update(c);
		} else {
			c.setDirty(false);
			cr.persist(c);
		}
		Log.d(TAG, c.getId()+ " External ID : " + c.getExternalId() + " complaintId : " + c.getId() );
		if (c != null && c.getLineItems() != null
				&& c.getLineItems().size() > 0) {
			if(c.getId() > 0) {
				for (ComplaintLineItem cli : c.getLineItems()) {
					ComplaintLineItem item = clir.getlineItembyValues(cli.getServerId(), cli.getExternalId(), c.getId());

					//ComplaintLineItem item = (clir.getSingleEntityByProperty("serverId", cli.getServerId()));
					cli.setComplaintId(c.getId());
					cli.setSyncDateTime(syncDate);
					if (item != null) {
						if (!item.isDirty()) {
							cli.setId(item.getId());
							cli.setDirty(false);
							clir.update(cli);
						}
					}  else {
						cli.setDirty(false);
						clir.persist(cli);
					}
				}
			} else {
				Log.e(TAG, "COMPLAINT ID IS ZERO");
			}
		}
		if (c != null && c.getActivities() != null
				&& c.getActivities().size() > 0) {
			if(c.getId() > 0) {	
				for (ComplaintActivity ca : c.getActivities()) {
					ComplaintActivity activity = car.getSingleEntityByProperty("serverId", ca.getServerId());
					ca.setComplaintId(c.getId());
					ca.setSyncDateTime(syncDate);
					if (activity != null) {
						ca.setId(activity.getId());
						ca.setDirty(false);
						car.update(ca);
					} else {
						ca.setDirty(false);
						car.persist(ca);
					}
				}
			}
		}
		if (c != null && c.getComplaintAttachment() != null
				&& c.getComplaintAttachment().size() > 0) {
			if(c.getId() > 0) {
				for (ComplaintAttachment complaintAttachment : c
						.getComplaintAttachment()) {
					ComplaintAttachment attachment = (cAttr.getSingleEntityByProperty("serverId", complaintAttachment.getServerId()));
					complaintAttachment.setComplaintId(c.getId());
					complaintAttachment.setSyncDateTime(syncDate);
					if(complaintAttachment.getActivityIdServer() > 0) {
						ComplaintActivity ca = car.getSingleEntityByProperty("serverId", complaintAttachment.getActivityIdServer());
						if(ca != null)
							complaintAttachment.setActivityId(ca.getId());
					}
					if (attachment != null) {
						complaintAttachment.setId(attachment.getId());
						complaintAttachment.setPath(attachment.getPath());
						complaintAttachment.setCreatedDate(attachment.getCreatedDate());
						complaintAttachment.setAttachDate(attachment.getAttachDate());
					}
					int id = cAttr.saveOrUpdate(complaintAttachment);
					if(attachment == null || StringUtil.isEmpty(attachment.getPath())) {
						AttachmentDownloadUtil attachmentDownloadUtil = new AttachmentDownloadUtil(complaintAttachment.getServerId(), complaintAttachment.getName(),id);
						attachmentDownloadUtil.downloadFile();
					}
				}
			}
		}
		if (c != null && c.getSchedule() != null) {
			if(c.getId() > 0) {	
				ComplaintSchedule cs = c.getSchedule();
				ComplaintSchedule schedule = csr.getSingleEntityByProperty(
						"complaintId", c.getId());
				if(schedule == null || (schedule != null && !schedule.isDirty())) {
					cs.setComplaintId(c.getId());
					cs.setSyncDateTime(syncDate);
					if (schedule != null) {
						cs.setId(schedule.getId());
					}
					csr.saveOrUpdate(cs);
					if(cs.getStartTime() != null && cs.getEndTime() != null) {
						int reminderMins =  _context.getResources().getInteger(R.integer.defaultAppointmentReminderMins);//30; // default is 30 mins
						EventReminder eventReminder = null;
						if(!reminderService.isReminderExists(c.getExternalId()) 
								&& cs.getAssigneeExternalId().equalsIgnoreCase(userService.getLoggedInUser().getExternalId())) {
							eventReminder = new EventReminder(cs.getId(), cs.getStartTime(),reminderMins ,c.getExternalId(), c.getCustomerName());
							reminderService.save(eventReminder);
						}
					}
				}
			}
		} 


		if(c != null) {
			if(c.getCustomer() != null) {
				Customer customer = c.getCustomer();
				customer.setComplaintId(c.getId());
				customer.setWorkflowId(c.getWorkflowId());
				int id = customerService.saveCustomerDetails(customer);
				Log.e(TAG + "===============================", "Got row id " + id);
				// update complaint with customer fk (customer row id)
				if(id > 0) {
					c.setCustomerId(id);
					cr.update(c);
				}
			}
		}
	}

	public Complaint getComplaintDetailsByExternalId(String externalId) {
		Complaint c = cr.getSingleEntityByProperty("externalId", externalId);
		List<ComplaintLineItem> lineItems = clir.getEntityByProperty(
				"complaintId", c.getId());
		c.setLineItems(lineItems);
		List<ComplaintActivity> comAct = car.getEntityByProperty("complaintId",
				c.getId());
		c.setActivities(comAct);
		return c;

	}



	public Complaint getComplaintDetailsById(int id) {
		Complaint c = cr.getById(id);
		if(c != null) {
			List<ComplaintLineItem> lineItems = clir.getEntityByProperty("complaintId", c.getId());
			if(lineItems != null) {
				c.setLineItems(lineItems);			
			}
			List<ComplaintActivity> comAct = car.getEntityByProperty("complaintId",
					c.getId());
			if(comAct != null) {
				c.setActivities(comAct);			
			}
		}
		return c;

	}

	public void saveToServer(Complaint complaint, String mode) {
		CommonUtil.writeLogToFile("complaintserviceimpl saving dirtyComplaint : " + complaint);
		ComplaintSchedule schedule = null;
		Customer c = complaint.getCustomer();
		int customerRowID = 0;
		if(c != null) {
			if(c.get_id() <= 0) {
				c.setDirty(true);
			}
			int tmpId = customerService.saveCustomerDetails(c);
			if(c.get_id() > 0) {
				customerRowID = customerService.getRowIdByServerId(c.get_id());
			} else {
				customerRowID = tmpId;
			}
			Log.i("Customer id", ""+customerRowID);
			if(customerRowID > 0) {
				complaint.setCustomerId(customerRowID);
			}
		}/* else if(complaint.getCustomerId() > 0) { // check if this customer exists 
			customerRowID  = customerService.getRowIdByServerId(complaint.getCustomerId());
			if(customerRowID > 0) {
				complaint.setCustomerId(customerRowID);
			}
		}*/
		
		if(mode != null && mode.equalsIgnoreCase("EDIT")){
			schedule = css.getByComplaintId(complaint.getId());
		}
		ComplaintSchedule cs = complaint.getSchedule();
		int id = cr.saveOrUpdate(complaint);
		cs.setComplaintId(id);
		cs.setDirty(true);
		//css.showConflictionschedules(cs);
		if(schedule != null) {
			cs.setId(schedule.getId());
		}
		csr.saveOrUpdate(cs);

		if(Attachments.attachments.size() > 0) {
			for(ComplaintAttachment ca: Attachments.attachments) {
				ca.setComplaintId(id);
				attachmentService.save(ca);
			}
			Attachments.attachments.clear();
		}
		
		// get customer Serverid and set it to complaint
		int customerRowId = complaint.getCustomerId();
		if(customerRowId > 0) {	
			Customer customer = customerService.getByRowId(customerRowId);
			complaint.setCustomer(customer);
			complaint.setkeyValForSave();
			if(customer != null && customer.getId() > 0) 
				complaint.setCustomerServerId(customer.getId());
		}
		String url;
		if(mode != null && mode.equalsIgnoreCase("EDIT")){
			url = Constants.URL_TO_UPDATE_CALL;
			complaint.setKeyValueForUpdate();
		} else {
			url = Constants.URL_TO_POST_NEW_CALL;
		}

		if(complaint.getCurrentActivity() != null) {
			ComplaintActivity activity = complaint.getCurrentActivity();
			activity.setComplaintId(id);
			cas.saveLocally(activity);
		}
		Log.e("==========================", "Save to server Checking if posting : " +complaint.getExternalId() + "Posting :" + complaint.isPosting() + "");		
		if(CommonUtil.checkInternetConnection()) {
			if(c != null) {
				if(c.get_id() <= 0) {
					customerService.setPosting(customerRowID, true);
				}
			}
			Log.e("==========================", "Posting for complaint after checking  : " +complaint.getExternalId() + "is , Posting :" + complaint.isPosting() + "");
			//			complaint.setPosting(true);
			//			cr.update(complaint);
			CommonUtil.writeLogToFile("complaintserviceimpl calling http : " + complaint);
			HandlerContext ctx = new HandlerContext(new SaveComplaintHandler(),
					complaint, url,HttpType.POST);
			HttpConnector con = new HttpConnector();
			con.postData(ctx);
		}
	}


	public void deleteComplaint(int complaintId) {

		List<ComplaintSchedule> schedules = csr.getEntityByProperty("complaintId", complaintId);
		List<ComplaintAttachment> attachments = cAttr.getEntityByProperty("complaintId", complaintId);
		List<ComplaintActivity> activities = car.getEntityByProperty("complaintId", complaintId);
		List<ComplaintLineItem> lineItems = clir.getEntityByProperty("complaintId", complaintId);


		csr.delete(schedules);
		cAttr.delete(attachments);
		car.delete(activities);
		clir.delete(lineItems);
		cr.deleteById(complaintId);


	}


	@Override
	public void deleteById(int id) {
		cr.deleteById(id);
	}


	private boolean checkIfPosting(Complaint complaint) {
		Complaint com = cr.getById(complaint.getId());
		return com.isPosting();
	}

	@Override
	public void getClosedCallsFromServer(int pageindex) {
		Log.d(TAG, " ComplaintServiceImp : getClosedCallsFromServer");
		//cr.setSyncing();
		HandlerContext ctx = null;
		if (session != null && session.isLoggedIn()) {
			String userExternalId = session.getUserDetails().get("id");
			String userPassword = session.getUserDetails().get("password");
			User u = new User(userExternalId, userPassword);
			Complaint com = new Complaint(u);
			com.setPage(pageindex);
			com.setSubordinateExternalId(userExternalId);
			ctx = new HandlerContext(new OpenComplaintHandler(), com, Constants.URL_TO_GET_CLOSED_CALLS,HttpType.POST);
			HttpConnector con = new HttpConnector();
			con.postData(ctx);

			List<User> subordinates = userService.getSubordinates();
			for (User user : subordinates) {
				Complaint complaint = new Complaint(u);
				complaint.setPage(pageindex);
				complaint.setSubordinateExternalId(user.getExternalId());
				complaint.setKeyValuePairForSubOrdinates();
				HandlerContext context = new HandlerContext(
						new OpenComplaintHandler(), complaint, Constants.URL_TO_GET_CLOSED_CALLS,HttpType.POST);
				HttpConnector conn = new HttpConnector();
				conn.postData(context);

			}
		}
	}

}
