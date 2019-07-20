/**
 * 
 */
package cn.rongcapital.ruleengine.match;

import cn.rongcapital.ruleengine.model.*;

import java.util.Map;

/**
 * the ruleSet executor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface RuleSetExecutor {

	/**
	 * to execute the ruleSet
	 * 
	 * @param bizCode
	 *            the bizCode
	 * @param ruleSet
	 *            the ruleSet to execution
	 * @param params
	 *            the params
	 */
	void execute(String bizCode, RuleSet ruleSet, Map<String, String> params);

}
