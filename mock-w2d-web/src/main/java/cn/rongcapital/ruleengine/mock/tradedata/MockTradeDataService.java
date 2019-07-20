/**
 * 
 */
package cn.rongcapital.ruleengine.mock.tradedata;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class MockTradeDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockTradeDataService.class);

	@Autowired
	private DbHelper dbHelper;

	@Autowired
	private ConnectionHolder connectionHolder;

	private volatile AtomicBoolean initialized = new AtomicBoolean(false);

	private final Datasource datasource = new Datasource();

	@Value("${db.driverName}")
	private String driverClass;

	@Value("${db.jdbcUrl}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.password}")
	private String password;

	@Value("${db.validationSql}")
	private String validationSql;

	@Value("${db.maxPoolSize}")
	private int maxPoolSize;

	public List<Map<String, String>> query(final String tableName, final List<String> columns,
			final Map<String, String> conditions) {
		this.init();
		// build the SQL and params
		final Object[] params = new Object[conditions.size()];
		final StringBuilder sql = new StringBuilder();
		sql.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if (i > 0) {
				sql.append(", ");
			}
			sql.append("`").append(columns.get(i)).append("`");
		}
		sql.append(" from `").append(tableName).append("` where ");
		int index = 0;
		for (final String col : conditions.keySet()) {
			if (index > 0) {
				sql.append(" and ");
			}
			sql.append("`").append(col).append("` = ? ");
			final String value = conditions.get(col);
			if (!StringUtils.isEmpty(value)) {
				params[index] = value;
			}
			index++;
		}
		Connection conn = null;
		try {
			conn = this.connectionHolder.getConnection(this.datasource);
			LOGGER.info("querying the datas, sql: {}, params: {}", sql, Arrays.toString(params));
			List<Map<String, Object>> rows = this.dbHelper.query(conn, sql.toString(), params);
			LOGGER.info("the datas queried, sql: {}, params: {}, result: {}", sql, Arrays.toString(params), rows);
			if (rows != null && !rows.isEmpty()) {
				final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
				for (final Map<String, Object> row : rows) {
					final Map<String, String> map = new HashMap<String, String>();
					if (row != null) {
						for (final String key : row.keySet()) {
							final Object value = row.get(key);
							if (value != null) {
								map.put(key, value.toString());
							} else {
								map.put(key, null);
							}
						}
					}
					result.add(map);
				}
				return result;
			}
		} catch (Exception e) {
			LOGGER.error("query data failed, error: " + e.getMessage(), e);
			throw new RuntimeException("query data failed, error: " + e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					//
				}
			}
		}
		return null;
	}

	private void init() {
		if (this.initialized.get()) {
			return;
		}
		// create the datasource
		this.datasource.setCode("test-datasource");
		this.datasource.setDriverClass(driverClass);
		this.datasource.setUrl(url);
		this.datasource.setUser(user);
		this.datasource.setPassword(password);
		this.datasource.setMaxPoolSize(maxPoolSize);
		this.datasource.setValidationSql(validationSql);
		this.initialized.set(true);
	}

	/**
	 * @param dbHelper
	 *            the dbHelper to set
	 */
	public void setDbHelper(DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/**
	 * @param connectionHolder
	 *            the connectionHolder to set
	 */
	public void setConnectionHolder(ConnectionHolder connectionHolder) {
		this.connectionHolder = connectionHolder;
	}

	/**
	 * @param driverClass
	 *            the driverClass to set
	 */
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param validationSql
	 *            the validationSql to set
	 */
	public void setValidationSql(String validationSql) {
		this.validationSql = validationSql;
	}

	/**
	 * @param maxPoolSize
	 *            the maxPoolSize to set
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

}
