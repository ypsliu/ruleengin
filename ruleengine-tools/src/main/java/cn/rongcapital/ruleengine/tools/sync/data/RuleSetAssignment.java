/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * the ruleSet assignment
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RuleSetAssignment {

	@JsonProperty("biz_type_code")
	private String bizTypeCode;

	@JsonProperty("ruleset_code")
	private String ruleSetCode;

	@JsonProperty("ruleset_version")
	private long ruleSetVersion;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleSetAssignment [bizTypeCode=" + bizTypeCode + ", ruleSetCode=" + ruleSetCode + ", ruleSetVersion="
				+ ruleSetVersion + "]";
	}

}
