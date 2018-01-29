package com.fieldez.android.domain.handler;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.fieldez.android.domain.model.ComplaintStatus;
import com.fieldez.android.domain.model.ComplaintWorkflow;
import com.fieldez.android.domain.model.EntityLabel;
import com.fieldez.android.domain.model.HandlerContext;
import com.fieldez.android.domain.repository.ComplaintStatusRepository;
import com.fieldez.android.domain.repository.ComplaintWorkflowRepository;
import com.fieldez.android.domain.repository.EntityLabelRepository;
import com.fieldez.android.util.Log;
import com.fieldez.android.util.XmlUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class WorkflowResponseHandler implements ResponseHandler {

	public void processResponse(String xml, HandlerContext context) {
		if(xml != null) {
			ComplaintWorkflowRepository cwr = ComplaintWorkflowRepository.getInstance();
			EntityLabelRepository elr = EntityLabelRepository.getInstance();
			ComplaintStatusRepository csr = ComplaintStatusRepository.getInstance();
			List<ComplaintWorkflow> cwf;
			try {
				cwf = XmlUtil.parseComplaintWorkflow(xml);
				Log.d("Workflow, Status, Entity Lables Parsed", cwf.toString());
				for (ComplaintWorkflow complaintWorkflow : cwf) {
					ComplaintWorkflow dbWorkflow = cwr.getById(complaintWorkflow.getId());
					if(dbWorkflow != null) {
						complaintWorkflow.setEntityNames(dbWorkflow.getEntityNames());
						cwr.update(complaintWorkflow);					
					} else {
						cwr.saveOrUpdate(complaintWorkflow);
					}

					if(complaintWorkflow.getStatus() != null) {
						for (ComplaintStatus cs : complaintWorkflow.getStatus()) {
							cs.setWorkflowId(complaintWorkflow.getId());
							csr.saveOrUpdate(cs);
							Collection<EntityLabel> ell = cs.getAttributeLabel();
							if(ell != null) {
								for (EntityLabel el : ell) {
									if(el != null) { 
										el.setStatusId(cs.getId());
										el.setWorkflowId(complaintWorkflow.getId());
										elr.saveOrUpdate(el); 
									}
								}
							}
							List<EntityLabel> dbEl = elr.getEntityByProperty("statusId", cs.getId());
							if(ell != null && dbEl != null) {
								Set<EntityLabel> els = Sets.difference(Sets.newHashSet(dbEl), Sets.newHashSet(ell));
								if(els != null && els.size() > 0) {
									elr.delete(Lists.newArrayList(els));
								}
							}
						}
					}

					if(cwr.getWeakHash() != null && cwr.getWeakHash().size() > 0 && cwr.getWeakHash().containsKey(complaintWorkflow.getId())) {
						cwr.getWeakHash().remove(complaintWorkflow.getId());
					}
					cwr.update(complaintWorkflow);
				}
				context.getUiHandler().receive(context);

			} catch (Exception e) {
				Log.e("--","exception",e);
			}
		}
	}
}
