/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * the implementation for DbHelper
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class DbHelperImpl implements DbHelper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.executor.DbHelper#update(java.sql.Connection, java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public void update(final Connection connection, final String sql, final Object[] params) {
		PreparedStatement ps = null;
		try {
			// create the statement
			ps = connection.prepareStatement(sql);
			// params
			if (params != null) {
				int index = 1;
				for (final Object p : params) {
					ps.setObject(index++, p);
				}
			}
			// execute
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					//
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.executor.DbHelper#query(java.sql.Connection, java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public List<Map<String, Object>> query(final Connection connection, final String sql, final Object[] params) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// create the statement
			ps = connection.prepareStatement(sql);
			// params
			if (params != null) {
				int index = 1;
				for (final Object p : params) {
					ps.setObject(index++, p);
				}
			}
			// query
			rs = ps.executeQuery();
			// result
			final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			// read columns
			final String[] columns = new String[rs.getMetaData().getColumnCount()];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = rs.getMetaData().getColumnLabel(i + 1);
			}
			// each row
			while (rs.next()) {
				final Map<String, Object> row = new HashMap<String, Object>();
				for (final String col : columns) {
					row.put(col, rs.getObject(col));
				}
				result.add(row);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					//
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					//
				}
			}
		}
	}

}
