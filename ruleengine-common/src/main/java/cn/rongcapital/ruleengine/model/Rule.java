/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.model.ui.RuleDesign;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 规则
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class Rule extends BaseEntity {

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
	 * 输入参数列表，用“,”分隔
	 */
	private String inputParams;

	/**
	 * 输入参数列表
	 */
	private List<String> params;

	/**
	 * 匹配表达式
	 */
	// @NotNull
	private String expression;

	/**
	 * 匹配类型，参考: MatchType
	 */
	@NotNull
	@JsonProperty("match_type")
	private MatchType matchType;

	/**
	 * 匹配执行者类名，需包含完整包名和类名
	 */
	@JsonProperty("executor_class")
	private String executorClass;

	/**
	 * 规则的数据源信息code，多个数据源code用“,”分隔
	 */
	private String datasourceCodes;

	/**
	 * 规则的数据源信息列表
	 */
	private List<Datasource> datasources;

	/**
	 * 规则数据读取用SQL列表
	 */
	@JsonProperty("extract_sqls")
	private List<ExtractSql> extractSqls;

	/**
	 * 表达式所用片段
	 */
	@JsonProperty("expression_segments")
	private Map<String, String> expressionSegments;

	/**
	 * 表达式所用片段，格式为：key1=value1,key2=value2
	 */
	private String exprSegments;

	/**
	 * 版本号
	 */
	private long version;

	/**
	 * 表达式编辑器用规则描述
	 */
	private RuleDesign design;

	/**
	 * 表达式编辑器用规则描述
	 */
	private String designValue;

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
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<String> params) {
		this.params = params;
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression
	 *            the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
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
	 * @return the executorClass
	 */
	public String getExecutorClass() {
		return executorClass;
	}

	/**
	 * @param executorClass
	 *            the executorClass to set
	 */
	public void setExecutorClass(String executorClass) {
		this.executorClass = executorClass;
	}

	/**
	 * @return the datasourceCodes
	 */
	public String getDatasourceCodes() {
		return datasourceCodes;
	}

	/**
	 * @param datasourceCodes
	 *            the datasourceCodes to set
	 */
	public void setDatasourceCodes(String datasourceCodes) {
		this.datasourceCodes = datasourceCodes;
	}

	/**
	 * @return the datasources
	 */
	public List<Datasource> getDatasources() {
		return datasources;
	}

	/**
	 * @param datasources
	 *            the datasources to set
	 */
	public void setDatasources(List<Datasource> datasources) {
		this.datasources = datasources;
	}

	/**
	 * @return the extractSqls
	 */
	public List<ExtractSql> getExtractSqls() {
		return extractSqls;
	}

	/**
	 * @param extractSqls
	 *            the extractSqls to set
	 */
	public void setExtractSqls(List<ExtractSql> extractSqls) {
		this.extractSqls = extractSqls;
	}

	/**
	 * @return the expressionSegments
	 */
	public Map<String, String> getExpressionSegments() {
		return expressionSegments;
	}

	/**
	 * @param expressionSegments
	 *            the expressionSegments to set
	 */
	public void setExpressionSegments(Map<String, String> expressionSegments) {
		this.expressionSegments = expressionSegments;
	}

	/**
	 * @return the exprSegments
	 */
	public String getExprSegments() {
		return exprSegments;
	}

	/**
	 * @param exprSegments
	 *            the exprSegments to set
	 */
	public void setExprSegments(String exprSegments) {
		this.exprSegments = exprSegments;
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
	 * @return the design
	 */
	public RuleDesign getDesign() {
		return design;
	}

	/**
	 * @param design
	 *            the design to set
	 */
	public void setDesign(RuleDesign design) {
		this.design = design;
	}

	/**
	 * @return the designValue
	 */
	public String getDesignValue() {
		return designValue;
	}

	/**
	 * @param designValue
	 *            the designValue to set
	 */
	public void setDesignValue(String designValue) {
		this.designValue = designValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rule [bizTypeCode=" + bizTypeCode + ", bizType=" + bizType + ", inputParams=" + inputParams
				+ ", params=" + params + ", expression=" + expression + ", matchType=" + matchType + ", executorClass="
				+ executorClass + ", datasourceCodes=" + datasourceCodes + ", datasources=" + datasources
				+ ", extractSqls=" + extractSqls + ", expressionSegments=" + expressionSegments + ", exprSegments="
				+ exprSegments + ", version=" + version + ", design=" + design + ", designValue=" + designValue
				+ ", code=" + code + ", name=" + name + ", comment=" + comment + ", creationTime=" + creationTime
				+ ", updateTime=" + updateTime + "]";
	}

}
