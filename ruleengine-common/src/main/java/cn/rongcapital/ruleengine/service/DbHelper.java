/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * the database helper
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface DbHelper {

	/**
	 * the execute the update SQL
	 * 
	 * @param connection
	 *            the database connection
	 * @param sql
	 *            the SQL to execute
	 * @param params
	 *            the execution parameters
	 */
	void update(Connection connection, String sql, Object[] params);

	/**
	 * to execute the query SQL
	 * 
	 * @param connection
	 *            the database connection
	 * @param sql
	 *            the SQL to execute
	 * @param params
	 *            the execution parameters
	 * @return the result, never be null
	 */
	List<Map<String, Object>> query(Connection connection, String sql, Object[] params);

}
