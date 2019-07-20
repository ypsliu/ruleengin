/**
 * 
 */
package cn.rongcapital.ruleengine.core.w2d;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.DbHelper;
import cn.rongcapital.ruleengine.service.ReferenceDataProviderClient;

/**
 * the w2d implementation for ReferenceDataProviderClient
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public class W2dReferenceDataProviderClient implements ReferenceDataProviderClient {

	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(W2dReferenceDataProviderClient.class);

	@Autowired
	private DbHelper dbHelper;

	@Autowired
	private ConnectionHolder connectionHolder;

	@Autowired
	private DatasourceManager datasourceManager;

	@Autowired
	private W2dResourceAgent w2dResourceAgent;

	@Value("${referencedata.w2d.checkFinishedSql}")
	private String checkFinishedSql = "select `status` from `task_info` where `id` = ?";

	@Value("${referencedata.w2d.statusColumn}")
	private String statusColumn = "status";

	@Value("${referencedata.w2d.finishedFlag}")
	private String finishedFlag = "FINISH";

	private final List<String> tableNames = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ReferenceDataProviderClient#request(java.util.Map)
	 */
	@Override
	public String request(final Map<String, String> conditions) {
		// // POST request form
		// final DataCaptureRequestForm form = DataCaptureRequestForm.fromConditionMap(conditions);
		// LOGGER.info("requesting w2d capture, request: {}", form);
		// // request
		// DataCaptureResponse response = this.w2dResourceAgent.capture(form);
		// LOGGER.info("the w2d capture requested, request: {}, response: {}", form, response);

		// check conditions
		if (conditions == null || conditions.isEmpty()) {
			throw new InvalidParameterException("the conditions is null or empty");
		}
		if (!conditions.containsKey("name")) {
			throw new InvalidParameterException("the conditions.name required");
		}
		if (!conditions.containsKey("id")) {
			throw new InvalidParameterException("the conditions.id required");
		}
		if (!conditions.containsKey("mobile_number")) {
			throw new InvalidParameterException("the conditions.mobile_number required");
		}
		if (!conditions.containsKey("biz_licence_number")) {
			throw new InvalidParameterException("the conditions.biz_licence_number required");
		}

		// GET request
		LOGGER.info("requesting w2d capture, conditions: {}", conditions);
		DataCaptureResponse response = this.w2dResourceAgent.capture(conditions.get("name"), conditions.get("id"),
				conditions.get("mobile_number"), conditions.get("biz_licence_number"), conditions.get("company_name"));
		LOGGER.info("the w2d capture requested, conditions: {}, response: {}", conditions, response);
		// process the response
		if (response == null) {
			throw new RuntimeException("no response");
		}
		if ("200".equals(response.getCode())) {
			return response.getTaskId();
		} else if ("400".equals(response.getCode())) {
			throw new InvalidParameterException("bad parameter, code: " + response.getCode() + ", message: "
					+ response.getMessage());
		} else if ("500".equals(response.getCode())) {
			throw new RuntimeException("w2d system error, code: " + response.getCode() + ", message: "
					+ response.getMessage());
		}
		throw new RuntimeException("w2d unknown error, code: " + response.getCode() + ", message: "
				+ response.getMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ReferenceDataProviderClient#checkFinished(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean checkFinished(final String datasourceCode, final String taskId) {
		// check
		if (StringUtils.isEmpty(datasourceCode)) {
			throw new InvalidParameterException("the datasourceCode is null or empty");
		}
		if (StringUtils.isEmpty(taskId)) {
			throw new InvalidParameterException("the taskId is null or empty");
		}
		// get the datasource
		final Datasource ds = this.datasourceManager.getDatasource(datasourceCode);
		if (ds == null) {
			throw new InvalidParameterException("the datasource is NOT existed");
		}
		Connection conn = null;
		try {
			conn = this.connectionHolder.getConnection(ds);
			LOGGER.info("checking finished, datasourceCode: {}, sql: {}, taskId: {}", datasourceCode,
					this.checkFinishedSql, taskId);
			final List<Map<String, Object>> list = this.dbHelper.query(conn, this.checkFinishedSql,
					new Object[] { taskId });
			LOGGER.info("finished checked, datasourceCode: {}, sql: {}, taskId: {}, result: {}", datasourceCode,
					this.checkFinishedSql, taskId, list);
			if (list.isEmpty()) {
				return false;
			}
			final Map<String, Object> row = list.get(0);
			if (row == null || row.isEmpty()) {
				return false;
			}
			return row.get(this.statusColumn) != null
					&& this.finishedFlag.equalsIgnoreCase(row.get(this.statusColumn).toString());
		} catch (SQLException e) {
			LOGGER.error("check finished failed, datasourceCode: " + datasourceCode + ", sql: " + this.checkFinishedSql
					+ ", taskId: " + taskId + ", error: " + e.getMessage(), e);
			throw new RuntimeException("check finished failed, datasourceCode: " + datasourceCode + ", sql: "
					+ this.checkFinishedSql + ", taskId: " + taskId + ", error: " + e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ReferenceDataProviderClient#queryDatas(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Map<String, List<Map<String, String>>> queryDatas(final String datasourceCode, final String taskId) {
		// check
		if (StringUtils.isEmpty(datasourceCode)) {
			throw new InvalidParameterException("the datasourceCode is null or empty");
		}
		if (StringUtils.isEmpty(taskId)) {
			throw new InvalidParameterException("the taskId is null or empty");
		}
		// get the datasource
		final Datasource ds = this.datasourceManager.getDatasource(datasourceCode);
		if (ds == null) {
			throw new InvalidParameterException("the datasource is NOT existed");
		}
		Connection conn = null;
		try {
			conn = this.connectionHolder.getConnection(ds);
			final Map<String, List<Map<String, String>>> result = new HashMap<String, List<Map<String, String>>>();
			for (final String tableName : this.tableNames) {
				// each table
				result.put(tableName, new ArrayList<Map<String, String>>());
				// SQL
				final String sql = "select * from `" + tableName + "` where `task_id` = ?";
				LOGGER.info("querying the datas, datasourceCode: {}, sql: {}, taskId: {}", datasourceCode, sql, taskId);
				final List<Map<String, Object>> list = this.dbHelper.query(conn, sql, new Object[] { taskId });
				LOGGER.info("the datas query success, datasourceCode: {}, sql: {}, taskId: {}", datasourceCode, sql,
						taskId);
				LOGGER.debug("the query result: {}", list);
				if (!list.isEmpty()) {
					for (final Map<String, Object> row : list) {
						// each row
						final Map<String, String> map = new HashMap<String, String>();
						for (final String col : row.keySet()) {
							// each column
							Object value = row.get(col);
							if (!StringUtils.isEmpty(value)) {
								// only not empty value
								map.put(col, value.toString());
							}
						}
						result.get(tableName).add(map);
					}
				}
			}
			return result;
		} catch (SQLException e) {
			LOGGER.error("query datas failed, datasourceCode: " + datasourceCode + ", sql: " + this.checkFinishedSql
					+ ", taskId: " + taskId + ", error: " + e.getMessage(), e);
			throw new RuntimeException("query datas failed, datasourceCode: " + datasourceCode + ", sql: "
					+ this.checkFinishedSql + ", taskId: " + taskId + ", error: " + e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}
	}

	/**
	 * @param dbHelper
	 *            the dbHelper to set
	 */
	public void setDbHelper(final DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/**
	 * @param connectionHolder
	 *            the connectionHolder to set
	 */
	public void setConnectionHolder(final ConnectionHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}

	/**
	 * @param datasourceManager
	 *            the datasourceManager to set
	 */
	public void setDatasourceManager(final DatasourceManager datasourceManager) {
		this.datasourceManager = datasourceManager;
	}

	/**
	 * @param w2dResourceAgent
	 *            the w2dResourceAgent to set
	 */
	public void setW2dResourceAgent(final W2dResourceAgent w2dResourceAgent) {
		this.w2dResourceAgent = w2dResourceAgent;
	}

	/**
	 * @param checkFinishedSql
	 *            the checkFinishedSql to set
	 */
	public void setCheckFinishedSql(final String checkFinishedSql) {
		if (!StringUtils.isEmpty(checkFinishedSql)) {
			this.checkFinishedSql = checkFinishedSql;
		}
	}

	/**
	 * @param finishedFlag
	 *            the finishedFlag to set
	 */
	public void setFinishedFlag(final String finishedFlag) {
		if (!StringUtils.isEmpty(finishedFlag)) {
			this.finishedFlag = finishedFlag;
		}
	}

	/**
	 * @param statusColumn
	 *            the statusColumn to set
	 */
	public void setStatusColumn(final String statusColumn) {
		if (!StringUtils.isEmpty(statusColumn)) {
			this.statusColumn = statusColumn;
		}
	}

	/**
	 * 
	 * @param tableNames
	 *            the tableNames to set
	 */
	@Value("${referencedata.w2d.tableNames}")
	public void setTableNames(final String tableNames) {
		if (!StringUtils.isEmpty(tableNames)) {
			this.tableNames.clear();
			final String[] a = tableNames.split("\\,", -1);
			for (final String tableName : a) {
				this.tableNames.add(tableName.trim());
			}
		}
	}

}
