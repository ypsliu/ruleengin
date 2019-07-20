/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 规则匹配请求数据
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class MatchRequestData implements Serializable {

	/**
	 *
	 */
	private MatchRequestMethod matchRequestMethod = MatchRequestMethod.REQUEST;

	/**
	 * 业务类型code
	 */
	@NotNull
	@JsonProperty("biz_type_code")
	private String bizTypeCode;

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
	 * 业务code
	 */
	@NotNull
	@JsonProperty("biz_code")
	private String bizCode;

	/**
	 * 业务数据，key为字段名称，value为数据内容
	 */
	@NotNull
	private Map<String, String> datas;

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
	 * @return the datas
	 */
	public Map<String, String> getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(Map<String, String> datas) {
		this.datas = datas;
	}

	/**
	 *
	 * @return
	 */
	public MatchRequestMethod getMatchRequestMethod() {
		return matchRequestMethod;
	}

	/**
	 *
	 * @param matchRequestMethod
	 */
	public void setMatchRequestMethod(MatchRequestMethod matchRequestMethod) {
		this.matchRequestMethod = matchRequestMethod;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MatchRequestData [bizTypeCode=" + bizTypeCode + ", ruleSetCode=" + ruleSetCode + ", ruleSetVersion="
				+ ruleSetVersion + ", bizCode=" + bizCode + ", datas=" + datas + "]";
	}
}
