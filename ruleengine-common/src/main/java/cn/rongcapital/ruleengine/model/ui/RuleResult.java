/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

import javax.validation.constraints.NotNull;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class RuleResult {

	/**
	 * 追踪信息
	 */
	private String trace;

	/**
	 * 结果
	 */
	private String result;

	/**
	 * 返回类型
	 */
	@NotNull
	private ReturnType returnType;

	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}

	/**
	 * @param trace
	 *            the trace to set
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the returnType
	 */
	public ReturnType getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
	}

}
