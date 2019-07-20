/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.core.trade.TradeDataProviderAgent;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.TradeDataQueryForm;
import cn.rongcapital.ruleengine.rop.TradeDataQueryResponse;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DataExtractor;
import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * the implementation for DataExtractor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class DataExtractorImpl implements DataExtractor {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataExtractorImpl.class);

	@Autowired
	private ConnectionHolder connectionHolder;

	@Autowired
	private DbHelper dbHelper;

	private TradeDataProviderAgent tradeDataProviderAgent;

	private TradeDataExtractType tradeDataExtractType = TradeDataExtractType.SQL;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DataExtractor#extractRuleDatas(Rule,
	 * java.lang.String)
	 */
	@Override
	public Map<String, String> extractRuleDatas(final Rule rule, final String bizCode) {
		// check
		if (rule == null) {
			return null;
		}
		LOGGER.debug("extracting rule datas, bizCode: {}, rule: {}", bizCode, rule);
		final Map<String, String> datas = new HashMap<String, String>();
		// step-1: put all params of the rule
		if (rule.getParams() != null) {
			for (final String key : rule.getParams()) {
				datas.put(key, null);
			}
		}
		if (datas.isEmpty()) {
			// the params of the rule is empty
			return datas;
		}
		// the context datas
		final Map<String, String> context = new HashMap<String, String>();
		// set the bizCode to context
		context.put("bizCode", bizCode);

		// step-2: extract trade data
		if (TradeDataExtractType.SQL == this.tradeDataExtractType) {
			// via SQL
			if (rule.getDatasources() != null && rule.getExtractSqls() != null) {
				for (final ExtractSql es : rule.getExtractSqls()) {
					for (final Datasource ds : rule.getDatasources()) {
						if (es.getDatasourceCode().equals(ds.getCode())) {
							// query
							final Map<String, String> result = this.executeQuery(ds, es, context);
							if (result != null) {
								// put the result to datas
								for (final String key : datas.keySet()) {
									if (datas.get(key) == null && result.get(key) != null) {
										// put to datas
										datas.put(key, result.get(key));
									}
								}
							}
						}
					}
				}
			}
		} else {
			// via API
			for (final ExtractSql es : rule.getExtractSqls()) {
				// copy the conditions
				final Map<String, String> conditions = new HashMap<String, String>();
				conditions.putAll(es.getConditions());
				// put the context values to conditions
				for (final String key : conditions.keySet()) {
					conditions.put(key, context.get(conditions.get(key)));
				}
				// build the query form
				final TradeDataQueryForm request = new TradeDataQueryForm();
				request.setTable(es.getTableName());
				request.setColumns(es.getColumns());
				request.setConditions(conditions);
				// query
				final RopResponse response = this.tradeDataProviderAgent.query(request);
				if (response != null && response instanceof TradeDataQueryResponse) {
					// got the result
					final TradeDataQueryResponse tdqr = (TradeDataQueryResponse) response;
					if (tdqr.getResults() != null) {
						for (final Map<String, String> row : tdqr.getResults()) {
							if (row != null) {
								// fill the datas by row
								for (final String key : datas.keySet()) {
									if (datas.get(key) == null && row.get(key) != null) {
										// put to datas
										datas.put(key, row.get(key));
									}
								}
								// put the row to context
								context.putAll(row);
							}
						}
					}
				} else {
					LOGGER.error("query the tradeData failed, request: {}, response: {}", request, response);
					throw new RuntimeException("query the tradeData via API failed");
				}
			}
		}
		return datas;
	}

	private Map<String, String> executeQuery(final Datasource datasource, final ExtractSql extractSql,
			final Map<String, String> context) {
		final Connection conn;
		try {
			conn = this.connectionHolder.getConnection(datasource);
		} catch (Exception e) {
			LOGGER.error("can not get the connection, datasource: " + datasource + ", error: {}" + e.getMessage(), e);
			return null;
		}
		if (conn == null) {
			return null;
		}

		try {
			// params
			Object[] params = null;
			if (extractSql.getParams() != null) {
				params = new Object[extractSql.getParams().size()];
				int index = 0;
				for (final String paramKey : extractSql.getParams()) {
					params[index++] = context.get(paramKey);
				}
			}

			// query
			final List<Map<String, Object>> rows = this.dbHelper.query(conn, extractSql.getSql(), params);

			// process rows
			final Map<String, String> result = new HashMap<String, String>();
			if (rows != null) {
				for (final Map<String, Object> row : rows) {
					for (final String key : row.keySet()) {
						Object value = row.get(key);
						if (value != null) {
							result.put(key, value.toString());
						} else {
							result.put(key, null);
						}
					}
				}
			}

			// put the result to context
			context.putAll(result);
			// return the result
			return result;
		} catch (Exception e) {
			LOGGER.error("execute the sql faild, datasource: " + datasource + ", extractSql: " + extractSql
					+ ", error: " + e.getMessage(), e);
			return null;
		} finally {
			try {
				conn.close();
			} catch (Exception e2) {
				//
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DataExtractor#extractRuleDatas(java.util.List, java.util.Map)
	 */
	@Override
	public Map<String, String> extractRuleDatas(final List<String> ruleParams, final Map<String, String> datas) {
		// check
		if (ruleParams == null || ruleParams.isEmpty() || datas == null || datas.isEmpty()) {
			return null;
		}
		final Map<String, String> params = new HashMap<String, String>();
		for (final String paramKey : ruleParams) {
			if (params.get(paramKey) == null) {
				params.put(paramKey, datas.get(paramKey));
			}
		}
		return params;
	}

	private enum TradeDataExtractType {

		/**
		 * via execute the SQLs
		 */
		SQL,

		/**
		 * via invoke the APIs
		 */
		API

	}

	/**
	 * @param connectionHolder
	 *            the connectionHolder to set
	 */
	public void setConnectionHolder(final ConnectionHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}

	/**
	 * @param dbHelper
	 *            the dbHelper to set
	 */
	public void setDbHelper(final DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/**
	 * @param tradeDataProviderAgent
	 *            the tradeDataProviderAgent to set
	 */
	public void setTradeDataProviderAgent(final TradeDataProviderAgent tradeDataProviderAgent) {
		this.tradeDataProviderAgent = tradeDataProviderAgent;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	@Value("${tradedata.extractType}")
	public void setTradeDataExtractType(final String type) {
		if (StringUtils.isEmpty(type)) {
			LOGGER.warn("the tradeDataExtractType is null or empty, the default will be used: {}",
					this.tradeDataExtractType);
		}
		final TradeDataExtractType det = TradeDataExtractType.valueOf(type.toUpperCase());
		if (det == null) {
			LOGGER.warn("invalid tradeDataExtractType {}, the default will be used: {}", type,
					this.tradeDataExtractType);
		}
		this.tradeDataExtractType = det;
	}

}
