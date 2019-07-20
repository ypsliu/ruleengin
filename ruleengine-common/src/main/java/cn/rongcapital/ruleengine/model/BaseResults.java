/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 匹配结果的超类
 * 
 * @author shangchunming@rongcapital.cn
 *
 * @param <R>
 *            匹配结果详情类型
 */
public abstract class BaseResults<R> {

	/**
	 * 业务编码
	 */
	@JsonProperty("biz_code")
	protected String bizCode;

	/**
	 * 业务类型编码
	 */
	@JsonProperty("biz_type_code")
	protected String bizTypeCode;

	/**
	 * 业务类型名称
	 */
	@JsonProperty("biz_type_name")
	protected String bizTypeName;

	/**
	 * 业务类型说明
	 */
	@JsonProperty("biz_type_comment")
	protected String bizTypeComment;

	/**
	 * 业务类型，参考: BizType
	 */
	protected BizType bizType;

	/**
	 * 匹配结果详情列表
	 */
	@JsonProperty("matchresults")
	protected List<R> results;

	/**
	 * 规则集合code
	 */
	@JsonProperty("ruleset_code")
	protected String ruleSetCode;

	/**
	 * 规则集合版本号
	 */
	@JsonProperty("ruleset_version")
	protected Long ruleSetVersion;

	/**
	 * @return the bizCode
	 */
	public final String getBizCode() {
		return bizCode;
	}

	/**
	 * @param bizCode
	 *            the bizCode to set
	 */
	public final void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	/**
	 * @return the bizTypeCode
	 */
	public final String getBizTypeCode() {
		return bizTypeCode;
	}

	/**
	 * @param bizTypeCode
	 *            the bizTypeCode to set
	 */
	public final void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	/**
	 * @return the bizTypeName
	 */
	public final String getBizTypeName() {
		return bizTypeName;
	}

	/**
	 * @param bizTypeName
	 *            the bizTypeName to set
	 */
	public final void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	/**
	 * @return the bizTypeComment
	 */
	public final String getBizTypeComment() {
		return bizTypeComment;
	}

	/**
	 * @param bizTypeComment
	 *            the bizTypeComment to set
	 */
	public final void setBizTypeComment(String bizTypeComment) {
		this.bizTypeComment = bizTypeComment;
	}

	/**
	 * @return the bizType
	 */
	public final BizType getBizType() {
		return bizType;
	}

	/**
	 * @param bizType
	 *            the bizType to set
	 */
	public final void setBizType(BizType bizType) {
		this.bizType = bizType;
	}

	/**
	 * @return the results
	 */
	public final List<R> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public final void setResults(List<R> results) {
		this.results = results;
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

}
