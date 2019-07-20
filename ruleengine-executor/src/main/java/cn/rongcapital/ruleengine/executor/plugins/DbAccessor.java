/**
 * 
 */
package cn.rongcapital.ruleengine.executor.plugins;

import java.sql.Connection;
import java.sql.SQLException;

import cn.rongcapital.ruleengine.executor.ExecutionPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * the database accessor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class DbAccessor implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbAccessor.class);

	/**
	 * the database connection holder
	 */
	private ConnectionHolder connectionHolder;

	/**
	 * the datasource manager
	 */
	private DatasourceManager datasourceManager;

	/**
	 * the database helper
	 */
	private DbHelper dbHelper;

	private String pluginName = "DbAccessor";

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#pluginName()
	 */
	@Override
	public String pluginName() {
		return this.pluginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#execute(java.lang.Object[])
	 */
	@Override
	public Object exec(final Object... params) {
		// check
		if (this.datasourceManager == null) {
			LOGGER.error("the datasource manager is null");
			throw new RuntimeException("the datasource manager is null");
		}
		if (this.connectionHolder == null) {
			LOGGER.error("the connection holder is null");
			throw new RuntimeException("the connection holder is null");
		}
		if (this.dbHelper == null) {
			LOGGER.error("the dbHelper is null");
			throw new RuntimeException("the dbHelper is null");
		}
		if (params == null || params.length < 3) {
			LOGGER.error("invalid params");
			throw new RuntimeException("invalid params");
		}
		// datasourceCode
		if (params[0] == null || !(params[0] instanceof String)) {
			LOGGER.error("invalid datasource code: {}", params[0]);
			throw new RuntimeException("invalid datasource code: " + params[0]);
		}
		final String datasourceCode = (String) params[0];
		final Datasource datasource = this.datasourceManager.getDatasource(datasourceCode);
		if (datasource == null) {
			LOGGER.error("the datasource is NOT existed, code: {}", datasourceCode);
			throw new RuntimeException("the datasource is NOT existed, code: " + datasourceCode);
		}
		// action
		if (params[1] == null || !(params[1] instanceof String)) {
			LOGGER.error("invalid action: {}", params[1]);
			throw new RuntimeException("invalid action: " + params[1]);
		}
		final String action = (String) params[1];
		if (!"update".equalsIgnoreCase(action) && !"query".equalsIgnoreCase(action)) {
			LOGGER.error("invalid action: {}", action);
			throw new RuntimeException("invalid action: " + action);
		}
		// SQL
		if (params[2] == null || !(params[2] instanceof String)) {
			LOGGER.error("invalid SQL: {}", params[2]);
			throw new RuntimeException("invalid SQL: " + params[2]);
		}
		final String sql = (String) params[2];
		if (sql.isEmpty()) {
			LOGGER.error("invalid SQL: {}", sql);
			throw new RuntimeException("invalid SQL: " + sql);
		}
		// params
		Object[] sqlParams = null;
		final int sqlParamsSize = params.length - 3;
		if (sqlParamsSize > 0) {
			// copy params
			sqlParams = new Object[sqlParamsSize];
			for (int i = 0; i < sqlParamsSize; i++) {
				sqlParams[i] = params[i + 3];
			}
		}
		LOGGER.debug("executing SQL, datasource: {}, action: {}, sql: {}, params: {}", datasourceCode, action, sql,
				sqlParams);
		Connection conn = null;
		try {
			// get connection
			conn = this.connectionHolder.getConnection(datasource);
			if ("update".equalsIgnoreCase(action)) {
				this.dbHelper.update(conn, sql, sqlParams);
			} else {
				return this.dbHelper.query(conn, sql, sqlParams);
			}
		} catch (Exception e) {
			LOGGER.error("execute the SQL failed, error: " + e.getMessage(), e);
			throw new RuntimeException("execute the SQL failed, error: " + e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}
		return null;
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
	 * @param dbHelper
	 *            the dbHelper to set
	 */
	public void setDbHelper(final DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#setPluginName(java.lang.String)
	 */
	@Override
	public void setPluginName(final String pluginName) {
		if (pluginName != null) {
			this.pluginName = pluginName;
		}
	}

}
