/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.core.DbHelperImpl;
import cn.rongcapital.ruleengine.service.DbHelper;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * the ITest for DbHelper
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class DbHelperITest {

	@Test
	public void test() {
		// DruidDataSource
		final DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_1?useUnicode=true&characterEncoding=utf8");
		ds.setUsername("root");
		ds.setPassword(null);
		ds.setMaxActive(2);

		// DbHelper
		final DbHelper dbHelper = new DbHelperImpl();

		Connection conn = null;
		Object[] params = null;

		// clear
		try {
			conn = ds.getConnection();
			// delete all
			dbHelper.update(conn, "delete from test_table_2", null);
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		// test-1: insert
		try {
			conn = ds.getConnection();
			params = new Object[2];
			params[0] = "key-1";
			params[1] = "value-1";
			dbHelper.update(conn, "insert into test_table_2(`key`, `value`) values(?, ?)", params);
			params[0] = "key-2";
			params[1] = "value-2";
			dbHelper.update(conn, "insert into test_table_2(`key`, `value`) values(?, ?)", params);
			System.out.println("test-1: insert passed");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		// test-2: select
		try {
			conn = ds.getConnection();
			// list all
			List<Map<String, Object>> list = dbHelper.query(conn, "select * from test_table_2 order by `id`", null);
			// check
			Assert.assertNotNull(list);
			Assert.assertEquals(2, list.size());
			Assert.assertEquals("key-1", list.get(0).get("key"));
			Assert.assertEquals("value-1", list.get(0).get("value"));
			Assert.assertEquals("key-2", list.get(1).get("key"));
			Assert.assertEquals("value-2", list.get(1).get("value"));
			// query by key
			params = new Object[1];
			params[0] = "key-1";
			list = dbHelper.query(conn, "select * from test_table_2 where `key` = ? ", params);
			// check
			Assert.assertNotNull(list);
			Assert.assertEquals(1, list.size());
			Assert.assertEquals("key-1", list.get(0).get("key"));
			Assert.assertEquals("value-1", list.get(0).get("value"));
			System.out.println("test-2: select passed");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		// test-3: update
		try {
			conn = ds.getConnection();
			params = new Object[2];
			params[1] = "key-1";
			params[0] = "value-1-1";
			dbHelper.update(conn, "update test_table_2 set `value` = ? where `key` = ?", params);
			params[1] = "key-2";
			params[0] = "value-2-1";
			dbHelper.update(conn, "update test_table_2 set `value` = ? where `key` = ?", params);
			// list all
			List<Map<String, Object>> list = dbHelper.query(conn, "select * from test_table_2 order by `id`", null);
			// check
			Assert.assertNotNull(list);
			Assert.assertEquals(2, list.size());
			Assert.assertEquals("key-1", list.get(0).get("key"));
			Assert.assertEquals("value-1-1", list.get(0).get("value"));
			Assert.assertEquals("key-2", list.get(1).get("key"));
			Assert.assertEquals("value-2-1", list.get(1).get("value"));
			// query by key
			params = new Object[1];
			params[0] = "key-1";
			list = dbHelper.query(conn, "select * from test_table_2 where `key` = ? ", params);
			// check
			Assert.assertNotNull(list);
			Assert.assertEquals(1, list.size());
			Assert.assertEquals("key-1", list.get(0).get("key"));
			Assert.assertEquals("value-1-1", list.get(0).get("value"));
			System.out.println("test-3: update passed");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		// test-4: delete
		try {
			conn = ds.getConnection();
			// delete by key
			params = new Object[1];
			params[0] = "key-1";
			dbHelper.update(conn, "delete from test_table_2 where `key` = ?", params);
			// list all
			List<Map<String, Object>> list = dbHelper.query(conn, "select * from test_table_2 order by `id`", null);
			// check
			Assert.assertNotNull(list);
			Assert.assertEquals(1, list.size());
			Assert.assertEquals("key-2", list.get(0).get("key"));
			Assert.assertEquals("value-2-1", list.get(0).get("value"));
			// delete all
			dbHelper.update(conn, "delete from test_table_2", null);
			// query all
			list = dbHelper.query(conn, "select * from test_table_2 order by `id`", null);
			// check
			Assert.assertNotNull(list);
			Assert.assertEquals(0, list.size());
			System.out.println("test-4: delete passed");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					//
				}
			}
		}

		ds.close();
	}

}
