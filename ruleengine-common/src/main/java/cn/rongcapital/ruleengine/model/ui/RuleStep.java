/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class RuleStep {

	/**
	 * 步骤说明
	 */
	private String comment;

	/**
	 * 条件关联关系
	 */
	@NotNull
	private ConditionsRelationType relType;

	/**
	 * 条件
	 */
	@NotNull
	private List<RuleCondition> conditions;

	/**
	 * 满足条件时的结果
	 */
	@NotNull
	private RuleResult trueResult;

	/**
	 * 不满足条件时的结果
	 */
	@NotNull
	private RuleResult falseResult;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the relType
	 */
	public ConditionsRelationType getRelType() {
		return relType;
	}

	/**
	 * @param relType
	 *            the relType to set
	 */
	public void setRelType(ConditionsRelationType relType) {
		this.relType = relType;
	}

	/**
	 * @return the conditions
	 */
	public List<RuleCondition> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(List<RuleCondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the trueResult
	 */
	public RuleResult getTrueResult() {
		return trueResult;
	}

	/**
	 * @param trueResult
	 *            the trueResult to set
	 */
	public void setTrueResult(RuleResult trueResult) {
		this.trueResult = trueResult;
	}

	/**
	 * @return the falseResult
	 */
	public RuleResult getFalseResult() {
		return falseResult;
	}

	/**
	 * @param falseResult
	 *            the falseResult to set
	 */
	public void setFalseResult(RuleResult falseResult) {
		this.falseResult = falseResult;
	}

}
