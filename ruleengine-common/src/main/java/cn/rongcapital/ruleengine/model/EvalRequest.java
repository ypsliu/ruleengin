/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;

import cn.rongcapital.ruleengine.enums.MatchType;

/**
 * 规则匹配测试请求信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class EvalRequest {

	/**
	 * 测试规则表达式
	 */
	@NotNull
	@JsonProperty("rule_expression")
	private String ruleExpression;

	/**
	 * 测试规则表达式片段
	 */
	@JsonProperty("expression_segments")
	private Map<String, String> expressionSegments;

	/**
	 * 规则匹配类型
	 */
	@NotNull
	@JsonProperty("match_type")
	private MatchType matchType;

	/**
	 * 业务code
	 */
	@JsonProperty("biz_code")
	private String bizCode;

	/**
	 * 测试用匹配参数
	 */
	private Map<String, String> params;

	/**
	 * @return the ruleExpression
	 */
	public String getRuleExpression() {
		return ruleExpression;
	}

	/**
	 * @param ruleExpression
	 *            the ruleExpression to set
	 */
	public void setRuleExpression(String ruleExpression) {
		this.ruleExpression = ruleExpression;
	}

	/**
	 * @return the expressionSegments
	 */
	public Map<String, String> getExpressionSegments() {
		return expressionSegments;
	}

	/**
	 * @param expressionSegments
	 *            the expressionSegments to set
	 */
	public void setExpressionSegments(Map<String, String> expressionSegments) {
		this.expressionSegments = expressionSegments;
	}

	/**
	 * @return the matchType
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * @param matchType
	 *            the matchType to set
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	/**
	 * @return the bizCode
	 */
	public String getBizCode() {
		return bizCode;
	}

	/**
	 * @param bizCode
	 *            the bizCode to set
	 */
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EvalRequest [ruleExpression=" + ruleExpression + ", expressionSegments=" + expressionSegments
				+ ", matchType=" + matchType + ", bizCode=" + bizCode + ", params=" + params + "]";
	}

}
