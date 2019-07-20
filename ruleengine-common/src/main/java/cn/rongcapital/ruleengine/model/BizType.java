/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 业务类型
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonIgnoreProperties(value={"ruleSet"})
public final class BizType extends BaseEntity {

	/**
	 * 规则集合code
	 */
	@JsonProperty("ruleset_code")
	private String ruleSetCode;

	/**
	 * 规则集合版本号
	 */
	@JsonProperty("ruleset_version")
	private Long ruleSetVersion;

	/**
	 * 规则集合
	 */
	@JsonProperty("ruleset")
	@JsonIgnore
	private RuleSet ruleSet;

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
	public Long getRuleSetVersion() {
		return ruleSetVersion;
	}

	/**
	 * @param ruleSetVersion
	 *            the ruleSetVersion to set
	 */
	public void setRuleSetVersion(Long ruleSetVersion) {
		this.ruleSetVersion = ruleSetVersion;
	}

	/**
	 * @return the ruleSet
	 */
	public RuleSet getRuleSet() {
		return ruleSet;
	}

	/**
	 * @param ruleSet
	 *            the ruleSet to set
	 */
	public void setRuleSet(RuleSet ruleSet) {
		this.ruleSet = ruleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BizType [ruleSetCode=" + ruleSetCode + ", ruleSetVersion=" + ruleSetVersion + ", ruleSet=" + ruleSet
				+ ", code=" + code + ", name=" + name + ", comment=" + comment + ", creationTime=" + creationTime
				+ ", updateTime=" + updateTime + "]";
	}

}
