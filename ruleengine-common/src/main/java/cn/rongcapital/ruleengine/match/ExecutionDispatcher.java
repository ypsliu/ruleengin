/**
 * 
 */
package cn.rongcapital.ruleengine.match;

import java.util.Map;

import cn.rongcapital.ruleengine.model.RuleSet;

/**
 * the execution dispatcher
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ExecutionDispatcher {

	/**
	 * to dispatch the execution
	 * 
	 * @param bizCode
	 *            the bizCode
	 * @param ruleSet
	 *            the ruleSet to execution
	 * @param params
	 *            the params
	 */
	void dispatch(String bizCode, RuleSet ruleSet, Map<String, String> params);

}
