/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.sql.Connection;
import java.sql.SQLException;

import cn.rongcapital.ruleengine.model.Datasource;

/**
 * JDBC连接服务
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ConnectionHolder {

	/**
	 * 根据数据源信息获取JDBC连接
	 * 
	 * @param datasource
	 *            数据源信息
	 * @return JDBC连接
	 * @throws SQLException
	 */
	Connection getConnection(Datasource datasource) throws SQLException;

}
