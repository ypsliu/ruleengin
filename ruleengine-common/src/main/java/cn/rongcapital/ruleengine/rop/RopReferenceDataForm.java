/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * the ROP ReferenceData request form
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RopReferenceDataForm extends RopForm {

	/**
	 * 业务类型code
	 */
	@FormParam("biz_type_code")
	@NotNull
	private String bizTypeCode;

	/**
	 * 参考数据条件，是一个JSON格式的map，key为条件名称，value条件内容
	 */
	@FormParam("conditions")
	@NotEmpty
	private String conditions;

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
	public String getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopReferenceDataForm [bizTypeCode=" + bizTypeCode + ", conditions=" + conditions + ", appKey=" + appKey
				+ ", session=" + session + ", method=" + method + ", sourceAppKey=" + sourceAppKey + ", timestamp="
				+ timestamp + ", sign=" + sign + ", from=" + from + ", format=" + format + "]";
	}

}
