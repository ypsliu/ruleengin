/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeName;

import cn.rongcapital.ruleengine.api.TradeDataResource;

/**
 * the trade data query response
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeName(TradeDataResource.QUERY_API_NAME)
public final class TradeDataQueryResponse extends RopResponse {

	/**
	 * 读取的表名称
	 */
	private String table;

	/**
	 * 查询结果列表，错误时为空
	 */
	private List<Map<String, String>> results;

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
	 * @return the results
	 */
	public List<Map<String, String>> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<Map<String, String>> results) {
		this.results = results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradeDataQueryResponse [table=" + table + ", results=" + results + "]";
	}

}
