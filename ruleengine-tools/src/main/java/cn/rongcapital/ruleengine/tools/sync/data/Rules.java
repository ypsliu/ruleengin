/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.data;

import java.util.List;

import cn.rongcapital.ruleengine.model.Rule;

/**
 * the rules
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class Rules {

	/**
	 * the rule list
	 */
	private List<Rule> rules;

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @param rules
	 *            the rules to set
	 */
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rules [rules=" + rules + "]";
	}

}
