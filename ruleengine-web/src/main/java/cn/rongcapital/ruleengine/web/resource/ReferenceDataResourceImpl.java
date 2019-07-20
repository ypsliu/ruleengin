/**
 * 
 */
package cn.rongcapital.ruleengine.web.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.rongcapital.ruleengine.api.ReferenceDataResource;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataCondition;
import cn.rongcapital.ruleengine.service.ReferenceDataService;

/**
 * the implementation for ReferenceDataResource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Controller
public final class ReferenceDataResourceImpl implements ReferenceDataResource {

	@Autowired
	private ReferenceDataService referenceDataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.rongcapital.ruleengine.api.ReferenceDataResource#getReferenceData(cn.rongcapital.ruleengine.model.
	 * ReferenceDataCondition)
	 */
	@Override
	public ReferenceData getReferenceData(final ReferenceDataCondition condition) {
		return this.referenceDataService.getReferenceData(condition.getBizTypeCode(), condition.getConditions());
	}

	/**
	 * @param referenceDataService
	 *            the referenceDataService to set
	 */
	public void setReferenceDataService(final ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

}
