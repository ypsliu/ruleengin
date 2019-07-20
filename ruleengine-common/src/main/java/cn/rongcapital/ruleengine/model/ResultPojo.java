/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import cn.rongcapital.ruleengine.enums.MatchType;

import java.util.Date;

/**
 * the result PO
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ResultPojo {

	/**
	 * 业务编码
	 */
	private String bizCode;

	/**
	 * 业务类型编码
	 */
	private String bizTypeCode;

	/**
	 * 规则编码
	 */
	private String ruleCode;

	/**
	 * 规则匹配类型
	 */
	private MatchType matchType;

	/**
	 * 输入参数列表，用“,”分隔，每个参数用“=”分为两个部分，“=”前为字段名称，后为字段值
	 */
	private String inputParams;

	/**
	 * 匹配结果错误信息
	 */
	private String errorMessage;

	/**
	 * 规则匹配结果时间
	 */
	private Date time;

	/**
	 * 匹配结果
	 */
	private String result;

	/**
	 * 规则的版本号
	 */
	private long ruleVersion;

	/**
	 * 执行id
	 */
	private String executionId;

	/**
	 * 规则集合code
	 */
	private String ruleSetCode;

	/**
	 * 规则集合版本号
	 */
	private long ruleSetVersion;

	/**
	 * 排序id
	 */
	private int sortId;

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
	 * @return the bizTypeCode
	 */
	public String getBizTypeCode() {
		return bizTypeCode;
	}

	/**
	 * @param bizTypeCode
	 *            the bizTypeCode to set
	 */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

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
	 * @return the inputParams
	 */
	public String getInputParams() {
		return inputParams;
	}

	/**
	 * @param inputParams
	 *            the inputParams to set
	 */
	public void setInputParams(String inputParams) {
		this.inputParams = inputParams;
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
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
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
	 * @return the executionId
	 */
	public String getExecutionId() {
		return executionId;
	}

	/**
	 * @param executionId
	 *            the executionId to set
	 */
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	/**
	 * @return the ruleSetCode
	 */
	public String getRuleSetCode() {
		return ruleSetCode;
	}

	/**
	 * @param ruleSetCode
	 *            the ruleSetCode to set
	 */
	public void setRuleSetCode(String ruleSetCode) {
		this.ruleSetCode = ruleSetCode;
	}

	/**
	 * @return the ruleSetVersion
	 */
	public long getRuleSetVersion() {
		return ruleSetVersion;
	}

	/**
	 * @param ruleSetVersion
	 *            the ruleSetVersion to set
	 */
	public void setRuleSetVersion(long ruleSetVersion) {
		this.ruleSetVersion = ruleSetVersion;
	}

	/**
	 * @return the sortId
	 */
	public int getSortId() {
		return sortId;
	}

	/**
	 * @param sortId
	 *            the sortId to set
	 */
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultPojo [bizCode=" + bizCode + ", bizTypeCode=" + bizTypeCode + ", ruleCode=" + ruleCode
				+ ", matchType=" + matchType + ", inputParams=" + inputParams + ", errorMessage=" + errorMessage
				+ ", time=" + time + ", result=" + result + ", ruleVersion=" + ruleVersion + ", executionId="
				+ executionId + ", ruleSetCode=" + ruleSetCode + ", ruleSetVersion=" + ruleSetVersion + ", sortId="
				+ sortId + "]";
	}

}
