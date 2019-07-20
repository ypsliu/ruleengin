/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import cn.rongcapital.ruleengine.model.ui.RuleDesign;

/**
 * the builder for rule expression
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface RuleExpressionBuilder {

	/**
	 * build the rule expression by rule design
	 * 
	 * @param design
	 *            the rule design
	 * @return the expression
	 */
	String build(RuleDesign design);

}
