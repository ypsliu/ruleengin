/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 匹配阶段信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class MatchStage {

	/**
	 * 阶段id
	 */
	@JsonProperty("stage_id")
	private int stageId;

	/**
	 * 上一级阶段id
	 */
	@JsonProperty("parent_stage_id")
	private int parentStageId;

	/**
	 * 规则编码
	 */
	@JsonProperty("rule_code")
	private String ruleCode;

	/**
	 * 规则名称
	 */
	@JsonProperty("rule_name")
	private String ruleName;

	/**
	 * 规则说明
	 */
	@JsonProperty("rule_comment")
	private String ruleComment;

	/**
	 * 规则版本号
	 */
	@JsonProperty("rule_version")
	private long ruleVersion;

	/**
	 * 规则，参考: Rule
	 */
	private Rule rule;

	/**
	 * 追踪信息
	 */
	private List<String> traces;

	/**
	 * 追踪信息
	 */
	private String tracesValue;

	/**
	 * 该阶段的结果
	 */
	private String result;

	/**
	 * 该阶段的错误消息
	 */
	@JsonProperty("error_message")
	private String errorMessage;

	/**
	 * 该阶段的执行时间
	 */
	@JsonProperty("time_in_ms")
	private int timeInMs;

	/**
	 * 该阶段的开始时间戳
	 */
	private Long beginAt;

	/**
	 * @return the ruleCode
	 */
	public String getRuleCode() {
		return ruleCode;
	}

	/**
	 * @param ruleCode
	 *            the ruleCode to set
	 */
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName
	 *            the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the ruleComment
	 */
	public String getRuleComment() {
		return ruleComment;
	}

	/**
	 * @param ruleComment
	 *            the ruleComment to set
	 */
	public void setRuleComment(String ruleComment) {
		this.ruleComment = ruleComment;
	}

	/**
	 * @return the rule
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * @param rule
	 *            the rule to set
	 */
	public void setRule(Rule rule) {
		this.rule = rule;
	}

	/**
	 * @return the stageId
	 */
	public int getStageId() {
		return stageId;
	}

	/**
	 * @param stageId
	 *            the stageId to set
	 */
	public void setStageId(int stageId) {
		this.stageId = stageId;
	}

	/**
	 * @return the parentStageId
	 */
	public int getParentStageId() {
		return parentStageId;
	}

	/**
	 * @param parentStageId
	 *            the parentStageId to set
	 */
	public void setParentStageId(int parentStageId) {
		this.parentStageId = parentStageId;
	}

	/**
	 * @return the traces
	 */
	public List<String> getTraces() {
		return traces;
	}

	/**
	 * @param traces
	 *            the traces to set
	 */
	public void setTraces(List<String> traces) {
		this.traces = traces;
	}

	/**
	 * @return the tracesValue
	 */
	public String getTracesValue() {
		return tracesValue;
	}

	/**
	 * @param tracesValue
	 *            the tracesValue to set
	 */
	public void setTracesValue(String tracesValue) {
		this.tracesValue = tracesValue;
	}

	/**
	 * @return the ruleVersion
	 */
	public long getRuleVersion() {
		return ruleVersion;
	}

	/**
	 * @param ruleVersion
	 *            the ruleVersion to set
	 */
	public void setRuleVersion(long ruleVersion) {
		this.ruleVersion = ruleVersion;
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
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the timeInMs
	 */
	public int getTimeInMs() {
		return timeInMs;
	}

	/**
	 * @param timeInMs
	 *            the timeInMs to set
	 */
	public void setTimeInMs(int timeInMs) {
		this.timeInMs = timeInMs;
	}

	/**
	 * @return the beginAt
	 */
	public Long getBeginAt() {
		return beginAt;
	}

	/**
	 * @param beginAt
	 *            the beginAt to set
	 */
	public void setBeginAt(Long beginAt) {
		this.beginAt = beginAt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MatchStage [stageId=" + stageId + ", parentStageId=" + parentStageId + ", ruleCode=" + ruleCode
				+ ", ruleName=" + ruleName + ", ruleComment=" + ruleComment + ", ruleVersion=" + ruleVersion
				+ ", rule=" + rule + ", traces=" + traces + ", tracesValue=" + tracesValue + ", result=" + result
				+ ", errorMessage=" + errorMessage + ", timeInMs=" + timeInMs + ", beginAt=" + beginAt + "]";
	}

}
