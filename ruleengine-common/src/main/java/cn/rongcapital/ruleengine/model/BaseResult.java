/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

import cn.rongcapital.ruleengine.enums.MatchType;

/**
 * 匹配结果的超类
 * 
 * @author shangchunming@rongcapital.cn
 *
 * @param <R>
 *            结果的具体类型
 */
public abstract class BaseResult<R> {

	/**
	 * 规则编码
	 */
	@JsonProperty("rule_code")
	protected String ruleCode;

	/**
	 * 规则名称
	 */
	@JsonProperty("rule_name")
	protected String ruleName;

	/**
	 * 规则说明
	 */
	@JsonProperty("rule_comment")
	protected String ruleComment;

	/**
	 * 规则匹配类型
	 */
	protected MatchType ruleMatchType;

	/**
	 * 规则，参考: Rule
	 */
	protected Rule rule;

	/**
	 * 输入参数，key为字段名称，value为字段的值
	 */
	@JsonProperty("match_params")
	protected Map<String, String> params;

	/**
	 * 匹配结果错误信息
	 */
	@JsonProperty("error_message")
	protected String errorMessage;

	/**
	 * 规则匹配结果时间
	 */
	protected Date time;

	/**
	 * 匹配结果
	 */
	protected R result;

	/**
	 * 规则的版本号
	 */
	@JsonProperty("rule_version")
	protected long ruleVersion;

	/**
	 * 规则匹配id
	 */
	@JsonProperty("execution_id")
	protected String executionId;

	/**
	 * 规则匹配阶段列表
	 */
	protected List<MatchStage> stages;

	/**
	 * 排序id
	 */
	protected Integer sortId;

	/**
	 * @return the ruleCode
	 */
	public final String getRuleCode() {
		return ruleCode;
	}

	/**
	 * @param ruleCode
	 *            the ruleCode to set
	 */
	public final void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	/**
	 * @return the ruleName
	 */
	public final String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName
	 *            the ruleName to set
	 */
	public final void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the ruleComment
	 */
	public final String getRuleComment() {
		return ruleComment;
	}

	/**
	 * @param ruleComment
	 *            the ruleComment to set
	 */
	public final void setRuleComment(String ruleComment) {
		this.ruleComment = ruleComment;
	}

	/**
	 * @return the rule
	 */
	public final Rule getRule() {
		return rule;
	}

	/**
	 * @param rule
	 *            the rule to set
	 */
	public final void setRule(Rule rule) {
		this.rule = rule;
	}

	/**
	 * @return the params
	 */
	public final Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public final void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * @return the errorMessage
	 */
	public final String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public final void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the time
	 */
	public final Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public final void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the result
	 */
	public final R getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public final void setResult(R result) {
		this.result = result;
	}

	/**
	 * @return the ruleVersion
	 */
	public final long getRuleVersion() {
		return ruleVersion;
	}

	/**
	 * @param ruleVersion
	 *            the ruleVersion to set
	 */
	public final void setRuleVersion(long ruleVersion) {
		this.ruleVersion = ruleVersion;
	}

	/**
	 * @return the executionId
	 */
	public final String getExecutionId() {
		return executionId;
	}

	/**
	 * @param executionId
	 *            the executionId to set
	 */
	public final void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	/**
	 * @return the stages
	 */
	public final List<MatchStage> getStages() {
		return stages;
	}

	/**
	 * @param stages
	 *            the stages to set
	 */
	public final void setStages(List<MatchStage> stages) {
		this.stages = stages;
	}

	/**
	 * @return the sortId
	 */
	public Integer getSortId() {
		return sortId;
	}

	/**
	 * @param sortId
	 *            the sortId to set
	 */
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	/**
	 * @return the ruleMatchType
	 */
	public MatchType getRuleMatchType() {
		return ruleMatchType;
	}

	/**
	 * @param ruleMatchType
	 *            the ruleMatchType to set
	 */
	public void setRuleMatchType(MatchType ruleMatchType) {
		this.ruleMatchType = ruleMatchType;
	}

}
