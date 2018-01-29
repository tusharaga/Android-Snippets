package com.fieldez.android.domain.repository;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fieldez.android.domain.dto.CallSearchDTO;
import com.fieldez.android.domain.dto.ComplaintDTO;
import com.fieldez.android.domain.model.Complaint;
import com.fieldez.android.domain.model.ComplaintWorkflow;
import com.fieldez.android.domain.model.CustomerWorkflow;
import com.fieldez.android.domain.model.User;
import com.fieldez.android.domain.service.UserService;
import com.fieldez.android.domain.service.impl.UserServiceImpl;
import com.fieldez.android.util.DatabaseHelper;
import com.fieldez.android.util.DateUtil;
import com.fieldez.android.util.Log;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

public class ComplaintRepository extends BaseRepository<Complaint> {

	private static final String TAG = ComplaintRepository.class.getSimpleName();
	private static ComplaintRepository complaintRepository;
	private static ComplaintScheduleRepository complaintScheduleRepository;
	private static UserService userService;

	public static ComplaintRepository getInstance() {
		if (complaintRepository == null)
			complaintRepository = new ComplaintRepository();
		return complaintRepository;
	}

	private ComplaintRepository() {
		super(Complaint.class);
		complaintScheduleRepository = ComplaintScheduleRepository.getInstance();
		userService = UserServiceImpl.getInstance();
	}

	public Complaint getByExternalId(String id) {
		return getSingleEntityByProperty("externalId", id);
	}


	public List<ComplaintDTO> searchCall(CallSearchDTO searchDTO, boolean isFromDayView, boolean isScheduleSearch) {

		try {
			GenericRawResults<ComplaintDTO> rawResults = getDao(type).queryRaw(
					getSelectSQLQuery(searchDTO, isFromDayView, isScheduleSearch), new CallListRowMapper());
			return rawResults.getResults();
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
		return null;
	}

	private String getSelectSQLQuery(CallSearchDTO dto, boolean isFromDayView, boolean isScheduleSearch) {
		StringBuilder sbr = null;
		if(dto != null && dto.getWorkflows() != null && dto.getWorkflows().size() > 0) {
			sbr = new StringBuilder("");
			for (ComplaintWorkflow wf : dto.getWorkflows()) {
				sbr.append(wf.getId());
				sbr.append(",");
			}
			if(sbr.toString().contains(",")) {
				sbr = sbr.replace(sbr.length() -1, sbr.length(), "");

			}
		}
		StringBuilder sb = new StringBuilder( 
				"select c.externalId, cust.customerName,c.status, cs.startTime, cs.endTime, c.customerId ," +
						"cs.assigneeExternalId , c.id, c.deleted, c.closed, cust.customerContact, c.workflowId, " +
						"cw.name, c.serverId, cust.customerContactNo, c.productName, c.productWarrantyType, c.specialInstructions, " +
						"cust.customerMiddleName, cust.customerLastName, cust.customerAddress1, " +
						"(select MAX (activityTime) from complaintactivity where complaintId=c.id) max_activity_time ,c.dirty , cust.customerOrganization " +
						"from complaint c ,complaintschedule cs , complaintworkflow cw , " + DatabaseHelper.CUSTOMER_451_VIRTUAL_TABLE +  " cust " +  
				" where c.id=cs.complaintId AND c.workflowId=cw.id AND c.customerId = cust.rowId ");

		if (dto.getWorkflows() != null && sbr != null) {
			sb.append(" AND c.workflowId IN (").append(sbr).append(")");
		}
		if(dto.getWorkflow() != null) {
			sb.append(" AND c.workflowId=").append(dto.getWorkflow().getId());
		}

		if (dto.getStatus() != null && !dto.getStatus().equals("")) {
			sb.append(" AND c.status=").append("'").append(dto.getStatus())
			.append("'");
		}

		if (dto.getAssignee() != null && !dto.getAssignee().equals("") && !dto.getAssignee().equals("-1")) {
			sb.append(" AND cs.assigneeExternalId ").append("like '%")
			.append(dto.getAssignee()).append("%'");
		}
		if (dto.getProductName() != null && !dto.getProductName().equals("")) {
			sb.append(" AND productName ").append("like '%")
			.append(dto.getProductName()).append("%'");
		}
		if(dto.getExternalId() != null && !dto.getExternalId().equals("")) {
			sb.append(" AND c.externalId ").append("like '%")
			.append(dto.getExternalId()).append("%'");
		}
		if (dto.getCustomerAddress2() != null && !dto.getCustomerAddress2().equals("")) {
			sb.append(" AND c.customerContact ").append("like '%")
			.append(dto.getCustomerAddress2()).append("%'");
		}
		if (dto.getCustomerName() != null && !dto.getCustomerName().equals("")) {
			sb.append(" AND c.customerName ").append("like '%")
			.append(dto.getCustomerName()).append("%'");
		}
		if (dto.getProductSerialNo() != null && !dto.getProductSerialNo().equals("")) {
			sb.append(" AND c.productSerialNo ").append("like '%")
			.append(dto.getProductSerialNo()).append("%'");
		}

		if(dto.getParentId() != null) {
			sb.append(" AND c.parentExternalId=").append("'").append(dto.getParentId()).append("'");
		}

		if(dto.getCustomerId() > 0) {
			sb.append(" AND c.customerId=").append("'").append(dto.getCustomerId()).append("'");
		}

		if (dto.getScheduleDate() != null) {
			// dto.getScheduleDate().
			sb.append(" AND  cs.startTime BETWEEN ")
			.append("'")
			.append(DateUtil.getDbDateToString(dto.getScheduleDate()))
			.append("' AND")
			.append(" '")
			.append(DateUtil
					.getDbDateToString(new Date((dto.getScheduleDate()
							.getTime() + 24 * 60 * 60 * 1000))))
							.append("'");
		}

		if(dto.isAssigned() || dto.isScheduled()) {
			sb.append(" AND cs.startTime is null AND cs.endTime is null");
		} 

		if(!dto.isScheduled() && dto.isAssigned()) {
			sb.append(" AND cs.startTime is null AND cs.endTime is null");
		} 


		if(!isFromDayView) {
			if(dto.isActionableCall() && !isScheduleSearch) {
				sb.append(" AND (c.actionable=1) ");
			} else if(!dto.isActionableCall() && !dto.isClosed()) {
				sb.append(" AND (c.actionable=0) ");
			}

			if(dto.isClosed()) {
				sb.append(" AND c.closed=1");
			} else {
				sb.append(" AND c.closed=0");
			}
		}
		if(isScheduleSearch) {
			sb.append(" AND c.closed=0");
		}
		/*if(!isFromDayView) {

			if(dto.isActionableCall() && !dto.isClosed()) {
				sb.append(" AND (c.actionable=1) ");
			} else {
				sb.append(" AND (c.actionable=0) ");
			}
		}

		if(dto.isClosed()) {
			sb.append(" AND c.closed=1");
		} else {
			sb.append(" AND c.closed=0");		
		}
		if(!isFromDayView && isScheduleSearch) {
			if(dto.isClosed()) {
				sb.append(" AND c.closed=1");
			} else {
				sb.append(" AND c.closed=0");
			}
		} */

		//sb.append(" AND c.deleted=0");

		if (dto.getOrderBy() != null) {
			sb.append(" order by ").append(dto.getOrderBy() + " ASC");
		}

		sb.append(" limit ").append(dto.getStartIndex()).append(" , ")
		.append(dto.getEndIndex());

		Log.d(TAG, "SQL : " + sb.toString());
		return sb.toString();
	}

	public List<Complaint> getCustomercall_Count(int i) {
		// TODO Auto-generated method stub


		try {


			Log.i("ID is :", ""+i);
			String query="select customerId,customerName from Complaint c where c.customerId = "+i +" AND c.closed ="+ 0;
			GenericRawResults<Complaint> rawResults = getDao(type).queryRaw(query, new CallRowMapper());

			return rawResults.getResults();
		} catch (SQLException e) {

			e.printStackTrace();
			Log.e(TAG, "exception", e);
		}
		return null;

	}

	public List<CustomerWorkflow> getCustomerworkflow(int customer_id) {
		try{
			String query="select c.customerId,cw.entityNames,count(cw.entityNames) from ComplaintWorkflow cw,Complaint c"
					+ " WHERE c.workflowId = cw.id AND c.customerId = "+customer_id +" AND c.closed ="+ 0 +" group by cw.entityNames ";

			GenericRawResults<CustomerWorkflow> rawResults = getDao(type).queryRaw(query, new WorkflowMapper());
			return rawResults.getResults();
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
		return null;
	}

	private class WorkflowMapper implements RawRowMapper<CustomerWorkflow>{

		@Override
		public CustomerWorkflow mapRow(String[] columnNames, String[] columnValues)
				throws SQLException {
			return new CustomerWorkflow(Integer.parseInt(columnValues[0]),columnValues[1], Integer.parseInt(columnValues[2]),0);
		}
	}

	private class CallListRowMapper implements RawRowMapper<ComplaintDTO> {
		@Override
		public ComplaintDTO mapRow(String[] columnNames, String[] columnValues)
				throws SQLException {
			return new ComplaintDTO(columnValues[0], columnValues[1],
					columnValues[2], columnValues[3], columnValues[4],
					columnValues[5], columnValues[6], columnValues[7], 
					columnValues[8], columnValues[9], columnValues[10], 
					columnValues[11], columnValues[12], columnValues[13],
					columnValues[14], columnValues[15], columnValues[16] ,
					columnValues[17] , columnValues[18], columnValues[19] , columnValues[20], columnValues[21],columnValues[22],columnValues[23]);
		}

	}

	public void setSyncing() {
		String s = "update complaint set synching = 1 where deleted = 0 AND closed = 0";
		try {
			getDao(type).updateRaw(s);
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
	}

	public void updateSyncStatus(String externalId) {
		String s = "update complaint set synching = 0 where externalId = "
				.concat("'").concat(externalId).concat("'");
		try {
			getDao(type).updateRaw(s);
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
	}

	public List<Complaint> getUnSynchedCalls(String userExternalId) {
		try {

			String sql = "select c.id, c.externalId from complaint c, complaintSchedule cs where c.id=cs.complaintId AND c.closed=0 AND c.deleted=0 AND c.dirty=0"
					.concat(" AND c.synching = 1 AND cs.assigneeExternalId ")
					.concat("like '%").concat(userExternalId).concat("%'");

			Log.d("=========================================================",
					sql);
			GenericRawResults<Complaint> rawResults = getDao(type).queryRaw(
					sql, new CallRowMapper());
			return rawResults.getResults();
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
		return null;
	}

	private class CallRowMapper implements RawRowMapper<Complaint> {
		@Override
		public Complaint mapRow(String[] columnNames, String[] columnValues)
				throws SQLException {
			return new Complaint(Integer.parseInt(columnValues[0]),
					columnValues[1]);
		}
	}

	public List<Integer> getDeletedOrClosedCalls() {
		Calendar c = Calendar.getInstance();
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		String sql = "SELECT id from complaint where syncDateTime < date('now','-"+monthMaxDays+" day') AND (closed=1 OR deleted=1)";
		GenericRawResults<Integer> rawResults;
		try {
			rawResults = getDao(type).queryRaw(
					sql, new ClosedCallRowMapper());
			return rawResults.getResults();
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
		return null;
	}

	private class ClosedCallRowMapper implements RawRowMapper<Integer> {
		@Override
		public Integer mapRow(String[] columnNames, String[] columnValues)
				throws SQLException {
			return Integer.parseInt(columnValues[0]);
		}
	}


	public int getComplaintCountByWorkflowGroup(String gName) {
		User user = userService.getLoggedInUser();
		if(user != null) {
			String sql = "select count (c.id) from complaint c , complaintschedule cs, complaintworkflow cw " +
					" where c.deleted=0 AND c.closed=0 AND  c.id= cs.complaintId AND c.workflowId=cw.id" +
					" AND cw.entityNames='"+gName+"'" +
					" AND cs.assigneeExternalId like '%"+user.getExternalId()+"%';";
			Log.d(TAG, "SQL EXECUTED TO GET COUNT " + sql);

			GenericRawResults<Integer> rawResults;
			try {
				rawResults = getDao(type).queryRaw(
						sql, new ClosedCallRowMapper());
				return rawResults.getResults().get(0);
			} catch (SQLException e) {
				Log.e(TAG, "exception", e);
			}

		}
		return 0;
	}

	public Complaint getByServerId(int serverId) {
		Complaint c = null;
		if(serverId > 0) {
			c = getSingleEntityByProperty("serverId", serverId);
		}
		return c;
	}


	public boolean isComplaintSynching() {
		String sql = "select count (c.id) from complaint c " +
				" where c.deleted=0 AND c.closed=0 AND c.synching = 1";
		int count = 0;
		GenericRawResults<Integer> rawResults;
		try {
			rawResults = getDao(type).queryRaw(
					sql, new ClosedCallRowMapper());
			count = rawResults.getResults().get(0);
		} catch (SQLException e) {
			Log.e(TAG, "exception", e);
		}
		if(count  > 0) return true; else return false;

	}

	@Override
	public synchronized int update(Complaint k) {
		return super.update(k);
	}





}
