/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.data;

import java.util.List;

/**
 * the ruleSet assignments
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RuleSetAssignments {

	private List<RuleSetAssignment> ruleSetAssignments;

	/**
	 * @return the ruleSetAssignments
	 */
	public List<RuleSetAssignment> getRuleSetAssignments() {
		return ruleSetAssignments;
	}

	/**
	 * @param ruleSetAssignments
	 *            the ruleSetAssignments to set
	 */
	public void setRuleSetAssignments(List<RuleSetAssignment> ruleSetAssignments) {
		this.ruleSetAssignments = ruleSetAssignments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleSetAssignments [ruleSetAssignments=" + ruleSetAssignments + "]";
	}

}
