/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.Map;

import cn.rongcapital.ruleengine.model.ReferenceData;

/**
 * the reference data service
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ReferenceDataService {

	/**
	 * to get the reference data
	 * 
	 * @param bizTypeCode
	 *            the bizType code
	 * @param conditions
	 *            the requesting conditions
	 * @return the result
	 */
	ReferenceData getReferenceData(String bizTypeCode, Map<String, String> conditions);

}
