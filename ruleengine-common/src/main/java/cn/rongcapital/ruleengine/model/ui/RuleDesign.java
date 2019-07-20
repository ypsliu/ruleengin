/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 规则编辑器UI用的bean
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class RuleDesign {

	/**
	 * 输入参数列表
	 */
	private List<String> params;

	/**
	 * 内部变量（规则片段）列表
	 */
	private List<RuleSegment> segments;

	/**
	 * 动态参数列表
	 */
	private List<RuleVar> vars;

	/**
	 * 规则步骤列表
	 */
	@NotNull
	private List<RuleStep> steps;

	/**
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<String> params) {
		this.params = params;
	}

	/**
	 * @return the segments
	 */
	public List<RuleSegment> getSegments() {
		return segments;
	}

	/**
	 * @param segments
	 *            the segments to set
	 */
	public void setSegments(List<RuleSegment> segments) {
		this.segments = segments;
	}

	/**
	 * @return the vars
	 */
	public List<RuleVar> getVars() {
		return vars;
	}

	/**
	 * @param vars
	 *            the vars to set
	 */
	public void setVars(List<RuleVar> vars) {
		this.vars = vars;
	}

	/**
	 * @return the steps
	 */
	public List<RuleStep> getSteps() {
		return steps;
	}

	/**
	 * @param steps
	 *            the steps to set
	 */
	public void setSteps(List<RuleStep> steps) {
		this.steps = steps;
	}

}
