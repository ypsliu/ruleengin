/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.service.ReferenceDataProcessor;

/**
 * the default implementation for ReferenceDataProcessor, convert the datas directly
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class ReferenceDataDefaultProcessor implements ReferenceDataProcessor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ReferenceDataProcessor#process(java.lang.String, java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public ReferenceData process(final String bizTypeCode, final Map<String, String> conditions,
			final Map<String, List<Map<String, String>>> rawDatas) {
		// check
		if (StringUtils.isEmpty(bizTypeCode)) {
			throw new InvalidParameterException("the bizTypeCode is null or empty");
		}
		if (conditions == null || conditions.isEmpty()) {
			throw new InvalidParameterException("the condition is null or empty");
		}
		if (rawDatas == null || rawDatas.isEmpty()) {
			throw new InvalidParameterException("the rawDatas is null or empty");
		}
		final ReferenceData rd = new ReferenceData();
		rd.setBizTypeCode(bizTypeCode);
		rd.setConditions(conditions);
		rd.setResponse(rawDatas);
		return rd;
	}

}
