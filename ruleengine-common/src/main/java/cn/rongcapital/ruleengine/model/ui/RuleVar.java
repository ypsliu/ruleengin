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
public class RuleVar {

	/**
	 * the var name
	 */
	@NotEmpty
	private String name;

	/**
	 * the var assignment type
	 */
	@NotNull
	private RuleVarType type;

	/**
	 * the var parameters
	 */
	@NotEmpty
	private String params;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public RuleVarType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(RuleVarType type) {
		this.type = type;
	}

	/**
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

}
