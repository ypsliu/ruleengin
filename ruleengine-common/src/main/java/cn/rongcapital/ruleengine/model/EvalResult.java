/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 规则匹配测试结果信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class EvalResult {

	/**
	 * 业务编码
	 */
	@JsonProperty("biz_code")
	private String bizCode;

	/**
	 * 匹配结果错误信息
	 */
	@JsonProperty("error_message")
	private String errorMessage;

	/**
	 * 规则匹配执行时长，单位为毫秒
	 */
	private long time;

	/**
	 * 匹配结果
	 */
	private String result;

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
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EvalResult [bizCode=" + bizCode + ", errorMessage=" + errorMessage + ", time=" + time + ", result="
				+ result + "]";
	}

}
