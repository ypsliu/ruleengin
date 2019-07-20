/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 参考数据
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ReferenceData {

	/**
	 * 业务类型code
	 */
	@JsonProperty("biz_type_code")
	private String bizTypeCode;

	/**
	 * 参考数据来源的任务Id
	 */
	@JsonProperty("provider_task_id")
	private String providerTaskId;

	/**
	 * 入库时间
	 */
	private Date time;

	/**
	 * 请求数据
	 */
	private Map<String, String> conditions;

	/**
	 * 请求数据，JSON格式
	 */
	private String conditionsValue;

	/**
	 * 结果数据
	 */
	private Map<String, List<Map<String, String>>> response;

	/**
	 * 结果数据，JSON格式
	 */
	private String responseValue;

	/**
	 * 结果数据原文，JSON格式
	 */
	private String responseRaw;

	/**
	 * 错误code
	 */
	@JsonProperty("error_code")
	private String errorCode;

	/**
	 * 错误信息
	 */
	@JsonProperty("error_message")
	private String errorMessage;

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
	 * @return the providerTaskId
	 */
	public String getProviderTaskId() {
		return providerTaskId;
	}

	/**
	 * @param providerTaskId
	 *            the providerTaskId to set
	 */
	public void setProviderTaskId(String providerTaskId) {
		this.providerTaskId = providerTaskId;
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
	 * @return the conditions
	 */
	public Map<String, String> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(Map<String, String> conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the conditionsValue
	 */
	public String getConditionsValue() {
		return conditionsValue;
	}

	/**
	 * @param conditionsValue
	 *            the conditionsValue to set
	 */
	public void setConditionsValue(String conditionsValue) {
		this.conditionsValue = conditionsValue;
	}

	/**
	 * @return the response
	 */
	public Map<String, List<Map<String, String>>> getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            the response to set
	 */
	public void setResponse(Map<String, List<Map<String, String>>> response) {
		this.response = response;
	}

	/**
	 * @return the responseValue
	 */
	public String getResponseValue() {
		return responseValue;
	}

	/**
	 * @param responseValue
	 *            the responseValue to set
	 */
	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}

	/**
	 * @return the responseRaw
	 */
	public String getResponseRaw() {
		return responseRaw;
	}

	/**
	 * @param responseRaw
	 *            the responseRaw to set
	 */
	public void setResponseRaw(String responseRaw) {
		this.responseRaw = responseRaw;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReferenceData [bizTypeCode=" + bizTypeCode + ", providerTaskId=" + providerTaskId + ", time=" + time
				+ ", conditions=" + conditions + ", conditionsValue=" + conditionsValue + ", response=" + response
				+ ", responseValue=" + responseValue + ", responseRaw=" + responseRaw + ", errorCode=" + errorCode
				+ ", errorMessage=" + errorMessage + "]";
	}

}
