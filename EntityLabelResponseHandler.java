package com.fieldez.android.domain.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Entity;

import com.fieldez.android.domain.model.ComplaintWorkflow;
import com.fieldez.android.domain.model.EntityLabel;
import com.fieldez.android.domain.model.HandlerContext;
import com.fieldez.android.domain.repository.ComplaintWorkflowRepository;
import com.fieldez.android.domain.repository.EntityLabelRepository;
import com.fieldez.android.util.LabelUtil;
import com.fieldez.android.util.Log;
import com.fieldez.android.util.XmlUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EntityLabelResponseHandler implements ResponseHandler {

	private static final String TAG = EntityLabelResponseHandler.class.getSimpleName();
	public void processResponse(String xml, HandlerContext context) {
		if(xml != null && !xml.equals("") && !xml.contains("<errorCode>")) {
			EntityLabelRepository elr = EntityLabelRepository.getInstance();
			ComplaintWorkflowRepository workflowRepository = ComplaintWorkflowRepository.getInstance();

			List<EntityLabel> ElList;
			ComplaintWorkflow cw = workflowRepository.getById(((EntityLabel)context.getModel()).getWorkflowId());
			try {
				ElList = XmlUtil.parseEntityLables(xml, cw.getId());
				for (EntityLabel entityLabel : ElList) {
					entityLabel.setWorkflowId(((EntityLabel)context.getModel()).getWorkflowId());
					if(entityLabel.getAttributeName().equals(LabelUtil.COMPLAINT_CALL)) {
						cw.setEntityNames(entityLabel.getDisplayLabel());
					}
					Map<String, Object> map = new HashMap<String, Object>(2);
					map.put("attributeName", entityLabel.getAttributeName());
					map.put("workflowId", ((EntityLabel)context.getModel()).getWorkflowId());
					List<EntityLabel> ellls = elr.getByFieldValues(map);
					if(ellls != null && ellls.size() > 0) {
						EntityLabel elssl = ellls.get(0);
						entityLabel.setId(elssl.getId());
					}
					elr.saveOrUpdate(entityLabel);
				}
				cw.setEntityLabelDownloaded(true);
				workflowRepository.update(cw);

				Map<String, Object> map = new HashMap<String, Object>(2);
				map.put("statusId", 0);
				map.put("workflowId", ((EntityLabel)context.getModel()).getWorkflowId());
				List<EntityLabel> dbEl = elr.getByFieldValues(map);
				Set<EntityLabel> els = Sets.difference(Sets.newHashSet(dbEl), Sets.newHashSet(ElList));
				if(els != null && els.size() > 0) {
					elr.delete(Lists.newArrayList(els));
				}
				context.getUiHandler().receive(context);
			} catch (Exception e) {
				Log.e(TAG, "Exception", e);
			}
		}
	}

}
