/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;
import java.util.Map;

/**
 * the client of the reference data provider
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ReferenceDataProviderClient {

	/**
	 * to request the reference data source to capture datas
	 * 
	 * @param conditions
	 *            the requesting conditions
	 * @return the provider taskId
	 */
	String request(Map<String, String> conditions);

	/**
	 * to check the reference data is finished
	 * 
	 * @param datasourceCode
	 *            the datasource code
	 * @param taskId
	 *            the provider taskId
	 * @return true: finished
	 */
	boolean checkFinished(String datasourceCode, String taskId);

	/**
	 * to query the reference datas from source
	 * 
	 * @param datasourceCode
	 *            the datasource code
	 * @param taskId
	 *            the taskId
	 * @return the query results, never null, the key is source name, the value is the data rows
	 */
	Map<String, List<Map<String, String>>> queryDatas(String datasourceCode, String taskId);

}
