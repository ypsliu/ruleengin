/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.data;

import java.util.List;

import cn.rongcapital.ruleengine.model.RuleSet;

/**
 * the ruleSets
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RuleSets {

	/**
	 * the ruleSet list
	 */
	private List<RuleSet> ruleSets;

	/**
	 * @return the ruleSets
	 */
	public List<RuleSet> getRuleSets() {
		return ruleSets;
	}

	/**
	 * @param ruleSets
	 *            the ruleSets to set
	 */
	public void setRuleSets(List<RuleSet> ruleSets) {
		this.ruleSets = ruleSets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleSets [ruleSets=" + ruleSets + "]";
	}

}
