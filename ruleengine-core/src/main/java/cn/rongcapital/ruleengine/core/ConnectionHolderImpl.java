/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.ConnectionHolder;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * the implementation for ConnectionHolder
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class ConnectionHolderImpl implements ConnectionHolder {

	private Map<String, DruidDataSource> datasources = new HashMap<String, DruidDataSource>();

	private final ReentrantLock lock = new ReentrantLock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ConnectionHolder#getConnection(Datasource
	 * )
	 */
	@Override
	public Connection getConnection(final Datasource datasource) throws SQLException {
		// check
		if (datasource == null) {
			return null;
		}
		this.lock.lock();
		try {
			DruidDataSource ds = this.datasources.get(datasource.getCode());
			if (ds == null) {
				ds = new DruidDataSource();
				ds.setDriverClassName(datasource.getDriverClass());
				ds.setUrl(datasource.getUrl());
				ds.setUsername(datasource.getUser());
				ds.setPassword(datasource.getPassword());
				ds.setMaxActive(datasource.getMaxPoolSize());
				ds.setValidationQuery(datasource.getValidationSql());
				ds.setMinIdle(1);
				ds.setTestOnBorrow(false);
				ds.setTestOnReturn(false);
				ds.setTestWhileIdle(true);
				ds.setRemoveAbandoned(true);
				ds.setTimeBetweenEvictionRunsMillis(60000);
				ds.setMinEvictableIdleTimeMillis(25200000);
				ds.setRemoveAbandoned(false);
				ds.setFilters("mergeStat");
				this.datasources.put(datasource.getCode(), ds);
			}
			return ds.getConnection();
		} finally {
			lock.unlock();
		}
	}

}
