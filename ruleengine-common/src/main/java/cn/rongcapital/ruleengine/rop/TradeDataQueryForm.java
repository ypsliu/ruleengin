/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;

/**
 * the trade data query form
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class TradeDataQueryForm extends RopForm {

	/**
	 * 读取的表名称，目前只支持一个（即单表查询）
	 */
	@FormParam("table")
	private String table;

	/**
	 * 返回的字段列表
	 */
	private List<String> columns;

	/**
	 * 返回的字段列表，多个字段用“,”分隔
	 */
	@FormParam("columns")
	private String columnsValue;

	/**
	 * 查询的条件，目前只支持“=”查询
	 */
	private Map<String, String> conditions;

	/**
	 * 查询的条件，用“;”分成多组，每组用“,”分隔，前面是字段名称，后面是取值，目前只支持“=”查询
	 */
	@FormParam("conditions")
	private String conditionsValue;

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(String table) {
		this.table = table;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradeDataQueryForm [table=" + table + ", columns=" + columns + ", columnsValue=" + columnsValue
				+ ", conditions=" + conditions + ", conditionsValue=" + conditionsValue + ", appKey=" + appKey
				+ ", session=" + session + ", method=" + method + ", sourceAppKey=" + sourceAppKey + ", timestamp="
				+ timestamp + ", sign=" + sign + ", from=" + from + ", format=" + format + "]";
	}

}
