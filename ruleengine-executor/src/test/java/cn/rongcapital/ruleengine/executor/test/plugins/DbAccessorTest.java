/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.executor.plugins.DbAccessor;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * the unit test for DbAccessor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class DbAccessorTest {

	@Test
	public void test() {
		// mock ConnectionHolder
		final ConnectionHolder connectionHolder = Mockito.mock(ConnectionHolder.class);
		// mock DatasourceManager
		final DatasourceManager datasourceManager = Mockito.mock(DatasourceManager.class);
		// mock DbHelper
		final DbHelper dbHelper = Mockito.mock(DbHelper.class);

		// DbAccessor
		final DbAccessor a = new DbAccessor();

		// test-1: validation
		try {
			a.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		a.setDatasourceManager(datasourceManager);
		try {
			a.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		a.setConnectionHolder(connectionHolder);
		try {
			a.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		a.setDbHelper(dbHelper);
		try {
			a.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		Object[] params = new Object[2];
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		params = new Object[4];
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// datasourceCode
		params[0] = 1;
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// datasourceCode
		params[0] = "test-datasourceCode";
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		final Datasource ds = new Datasource();
		ds.setCode("test-datasourceCode");
		Mockito.when(datasourceManager.getDatasource("test-datasourceCode")).thenReturn(ds);
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// action
		params[1] = 1;
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// action
		params[1] = "111";
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// action
		params[1] = "update";
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// action
		params[1] = "query";
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// SQL
		params[2] = 1;
		try {
			a.exec(params);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// SQL
		params[2] = "test-sql";
		// params
		params[3] = "test-param";
		final Connection conn = Mockito.mock(Connection.class);
		try {
			Mockito.when(connectionHolder.getConnection(ds)).thenReturn(conn);
		} catch (SQLException e1) {
			Assert.fail();
		}
		try {
			a.exec(params);
		} catch (Exception e) {
			Assert.fail("why the exception thrown?");
		}
		System.out.println("test-1: validation passed");

		// test-2: update
		params[1] = "update";
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				// check
				Assert.assertNotNull(invocation.getArguments());
				Assert.assertEquals(3, invocation.getArguments().length);
				Assert.assertNotNull(invocation.getArguments()[0]);
				Assert.assertTrue(invocation.getArguments()[0] instanceof Connection);
				Assert.assertEquals("test-sql", invocation.getArguments()[1]);
				Assert.assertNotNull(invocation.getArguments()[2]);
				Assert.assertTrue(invocation.getArguments()[2] instanceof Object[]);
				final Object[] params = (Object[]) invocation.getArguments()[2];
				Assert.assertEquals(1, params.length);
				Assert.assertEquals("test-param", params[0]);
				return null;
			}

		}).when(dbHelper).update(Mockito.any(Connection.class), Mockito.anyString(), Mockito.any());
		a.exec(params);
		Mockito.verify(dbHelper, Mockito.times(1)).update(Mockito.any(Connection.class), Mockito.anyString(),
				Mockito.any());
		System.out.println("test-2: update passed");

		// test-3: query
		params[1] = "query";
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				// check
				Assert.assertNotNull(invocation.getArguments());
				Assert.assertEquals(3, invocation.getArguments().length);
				Assert.assertNotNull(invocation.getArguments()[0]);
				Assert.assertTrue(invocation.getArguments()[0] instanceof Connection);
				Assert.assertEquals("test-sql", invocation.getArguments()[1]);
				Assert.assertNotNull(invocation.getArguments()[2]);
				Assert.assertTrue(invocation.getArguments()[2] instanceof Object[]);
				final Object[] params = (Object[]) invocation.getArguments()[2];
				Assert.assertEquals(1, params.length);
				Assert.assertEquals("test-param", params[0]);
				return null;
			}

		}).when(dbHelper).query(Mockito.any(Connection.class), Mockito.anyString(), Mockito.any());
		a.exec(params);
		Mockito.verify(dbHelper, Mockito.times(2)).query(Mockito.any(Connection.class), Mockito.anyString(),
				Mockito.any());
		System.out.println("test-3: query passed");
	}

}
