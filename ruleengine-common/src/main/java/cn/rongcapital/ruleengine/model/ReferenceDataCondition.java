/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 参考数据条件
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ReferenceDataCondition {

	/**
	 * 业务类型code
	 */
	@NotEmpty
	@JsonProperty("biz_type_code")
	private String bizTypeCode;

	/**
	 * 条件
	 */
	@NotNull
	private Map<String, String> conditions;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReferenceDataCondition [bizTypeCode=" + bizTypeCode + ", conditions=" + conditions + "]";
	}

}
