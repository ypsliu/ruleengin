/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

/**
 * the rule caller
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface RuleCaller {

	/**
	 * the context key
	 */
	String CONTEXT_KEY = "rules";

	/**
	 * to call the rule
	 * 
	 * @param ruleCode
	 *            the rule code
	 *            rule version
	 * @return the result
	 */
	Object call(String ruleCode, long ruleVersion);

}
