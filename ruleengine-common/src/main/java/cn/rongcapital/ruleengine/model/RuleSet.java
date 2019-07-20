/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 规则集合
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RuleSet extends BaseEntity {

	/**
	 * 业务类型编码
	 */
	@NotNull
	@JsonProperty("biz_type_code")
	private String bizTypeCode;

	/**
	 * 业务类型，参考: BizType
	 */
	private BizType bizType;

	/**
	 * 规则列表
	 */
	@NotNull
	private List<Rule> rules;

	/**
	 * 规则列表值，多个规则code用“;”分隔，每个规则用“,”分隔成两个部分，前面是规则code，后面是规则的版本号
	 */
	private String rulesValue;

	/**
	 * 版本号
	 */
	private long version;

	/**
	 * 所属规则是否按顺序匹配
	 */
	private boolean ordered = false;

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
	 * @return the bizType
	 */
	public BizType getBizType() {
		return bizType;
	}

	/**
	 * @param bizType
	 *            the bizType to set
	 */
	public void setBizType(BizType bizType) {
		this.bizType = bizType;
	}

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @param rules
	 *            the rules to set
	 */
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * @return the rulesValue
	 */
	public String getRulesValue() {
		return rulesValue;
	}

	/**
	 * @param rulesValue
	 *            the rulesValue to set
	 */
	public void setRulesValue(String rulesValue) {
		this.rulesValue = rulesValue;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the ordered
	 */
	public boolean isOrdered() {
		return ordered;
	}

	/**
	 * @param ordered
	 *            the ordered to set
	 */
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleSet [bizTypeCode=" + bizTypeCode + ", bizType=" + bizType + ", rules=" + rules + ", rulesValue="
				+ rulesValue + ", version=" + version + ", ordered=" + ordered + ", code=" + code + ", name=" + name
				+ ", comment=" + comment + ", creationTime=" + creationTime + ", updateTime=" + updateTime + "]";
	}

}
