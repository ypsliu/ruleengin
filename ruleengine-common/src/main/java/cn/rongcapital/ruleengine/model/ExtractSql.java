/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 规则数据读取用SQL
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ExtractSql {

	/**
	 * 规则code
	 */
	@JsonProperty("rule_code")
	private String ruleCode;

	/**
	 * 数据源code
	 */
	@JsonProperty("datasource_code")
	private String datasourceCode;

	/**
	 * 输入参数列表，多个参数用“,”分隔
	 */
	private String inputParams;

	/**
	 * 输入参数列表
	 */
	private List<String> params;

	/**
	 * 读取数据的SQL
	 */
	private String sql;

	/**
	 * 数据的表名
	 */
	@JsonProperty("table_name")
	private String tableName;

	/**
	 * 数据的结果字段列表
	 */
	private List<String> columns;

	/**
	 * 数据的结果字段列表，多个字段用“,”分隔
	 */
	private String columnsValue;

	/**
	 * 查询条件，key为字段名称，value为字段的取值，value可以使用上下文中的数据，将被自动替换
	 */
	private Map<String, String> conditions;

	/**
	 * 查询条件，格式为key1=value1,key2=value2，key为字段名称，value为字段的取值
	 */
	private String conditionsValue;

	/**
	 * 规则的版本号
	 */
	@JsonProperty("rule_version")
	private Long ruleVersion;

	/**
	 * @return the ruleCode
	 */
	public String getRuleCode() {
		return ruleCode;
	}

	/**
	 * @param ruleCode
	 *            the ruleCode to set
	 */
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	/**
	 * @return the datasourceCode
	 */
	public String getDatasourceCode() {
		return datasourceCode;
	}

	/**
	 * @param datasourceCode
	 *            the datasourceCode to set
	 */
	public void setDatasourceCode(String datasourceCode) {
		this.datasourceCode = datasourceCode;
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
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the columns
	 */
	public List<String> getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/**
	 * @return the columnsValue
	 */
	public String getColumnsValue() {
		return columnsValue;
	}

	/**
	 * @param columnsValue
	 *            the columnsValue to set
	 */
	public void setColumnsValue(String columnsValue) {
		this.columnsValue = columnsValue;
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
	 * @return the ruleVersion
	 */
	public Long getRuleVersion() {
		return ruleVersion;
	}

	/**
	 * @param ruleVersion
	 *            the ruleVersion to set
	 */
	public void setRuleVersion(Long ruleVersion) {
		this.ruleVersion = ruleVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExtractSql [ruleCode=" + ruleCode + ", datasourceCode=" + datasourceCode + ", inputParams="
				+ inputParams + ", params=" + params + ", sql=" + sql + ", tableName=" + tableName + ", columns="
				+ columns + ", columnsValue=" + columnsValue + ", conditions=" + conditions + ", conditionsValue="
				+ conditionsValue + ", ruleVersion=" + ruleVersion + "]";
	}

}
