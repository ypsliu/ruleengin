/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;
import java.util.Map;

import cn.rongcapital.ruleengine.model.ReferenceData;

/**
 * the reference data processor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ReferenceDataProcessor {

	/**
	 * to process the raw reference datas
	 * 
	 * @param bizTypeCode
	 *            the bizType code
	 * @param conditions
	 *            the request conditions
	 * @param rawDatas
	 *            the raw reference datas
	 * @return the reference data
	 */
	ReferenceData process(String bizTypeCode, Map<String, String> conditions,
                          Map<String, List<Map<String, String>>> rawDatas);

}
