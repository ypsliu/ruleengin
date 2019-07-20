/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class RuleCondition {

	/**
	 * 条件变量
	 */
	@NotEmpty
	private String varName;

	/**
	 * 比较操作
	 */
	@NotNull
	private ConditionOperator operator;

	/**
	 * 值
	 */
	@NotEmpty
	private String value;

	/**
	 * @return the varName
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * @param varName
	 *            the varName to set
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}

	/**
	 * @return the operator
	 */
	public ConditionOperator getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
