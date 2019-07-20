/**
 * 
 */
package cn.rongcapital.ruleengine.core.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.core.w2d.DataCaptureResponse;
import cn.rongcapital.ruleengine.core.w2d.W2dReferenceDataProviderClient;
import cn.rongcapital.ruleengine.core.w2d.W2dResourceAgent;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * the unit test for W2dReferenceDataProviderClient
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class W2dReferenceDataProviderClientTest {

	@Test
	public void testRequest() {
		// mock W2dResourceAgent
		final W2dResourceAgent w2dResourceAgent = Mockito.mock(W2dResourceAgent.class);

		// W2dReferenceDataProviderClient
		final W2dReferenceDataProviderClient c = new W2dReferenceDataProviderClient();
		c.setW2dResourceAgent(w2dResourceAgent);

		// conditions
		final Map<String, String> conditions = new HashMap<String, String>();

		// test-1: invalid conditions
		// try {
		// c.request(null);
		// Assert.fail("why no exception thrown?");
		// } catch (InvalidParameterException e) {
		// // OK
		// } catch (Exception e) {
		// Assert.fail("why the other exception thrown?");
		// }
		// try {
		// c.request(conditions);
		// Assert.fail("why no exception thrown?");
		// } catch (InvalidParameterException e) {
		// // OK
		// } catch (Exception e) {
		// Assert.fail("why the other exception thrown?");
		// }
		// conditions.put("name", "test-name");
		// conditions.put("id", "test-id");
		// conditions.put("mobileNumber", "test-mobileNumber");
		// conditions.put("bizLicenceNumber", "test-bizLicenceNumber");
		// conditions.put("companyName", "test-companyName");
		// try {
		// c.request(conditions);
		// Assert.fail("why no exception thrown?");
		// } catch (RuntimeException e) {
		// // OK
		// } catch (Exception e) {
		// Assert.fail("why the other exception thrown?");
		// }
		// System.out.println("test-1: invalid conditions passed");

		// test-2: 400
		conditions.put("name", "test-name");
		conditions.put("id", "test-id");
		conditions.put("mobileNumber", "test-mobileNumber");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber");
		conditions.put("companyName", "test-companyName");
		final DataCaptureResponse response = new DataCaptureResponse();
		response.setCode("400");
		response.setMessage("bad parameter(s)");
		Mockito.when(
				w2dResourceAgent.capture(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).then(new Answer<DataCaptureResponse>() {

			@Override
			public DataCaptureResponse answer(InvocationOnMock invocation) throws Throwable {
				return response;
			}

		});
		try {
			c.request(conditions);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown? " + e.getMessage());
		}
		System.out.println("test-2: 400 passed");

		// test-3: 500
		Mockito.reset(w2dResourceAgent);
		response.setCode("500");
		response.setMessage("internal error");
		Mockito.when(
				w2dResourceAgent.capture(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).then(new Answer<DataCaptureResponse>() {

			@Override
			public DataCaptureResponse answer(InvocationOnMock invocation) throws Throwable {
				return response;
			}

		});
		try {
			c.request(conditions);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		System.out.println("test-3: 500 passed");

		// test-4: unknown error
		Mockito.reset(w2dResourceAgent);
		response.setCode("xxx");
		response.setMessage("xxx error");
		Mockito.when(
				w2dResourceAgent.capture(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).then(new Answer<DataCaptureResponse>() {

			@Override
			public DataCaptureResponse answer(InvocationOnMock invocation) throws Throwable {
				return response;
			}

		});
		try {
			c.request(conditions);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		System.out.println("test-4: unknown error passed");

		// test-5: OK
		Mockito.reset(w2dResourceAgent);
		response.setCode("200");
		response.setMessage(null);
		response.setTaskId("test-taskId");
		Mockito.when(
				w2dResourceAgent.capture(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
						Mockito.anyString(), Mockito.anyString())).then(new Answer<DataCaptureResponse>() {

			@Override
			public DataCaptureResponse answer(InvocationOnMock invocation) throws Throwable {
				return response;
			}

		});
		final String taskId = c.request(conditions);
		// check
		Assert.assertEquals(response.getTaskId(), taskId);
		System.out.println("test-5: OK passed");
	}

	// private void checkForm(final DataCaptureRequestForm form) {
	// Assert.assertNotNull(form);
	// Assert.assertEquals("test-name", form.getName());
	// Assert.assertEquals("test-id", form.getId());
	// Assert.assertEquals("test-mobileNumber", form.getMobileNumber());
	// Assert.assertEquals("test-bizLicenceNumber", form.getBizLicenceNumber());
	// Assert.assertEquals("test-companyName", form.getCompanyName());
	// }

	@Test
	public void testCheckFinished() {
		// W2dReferenceDataProviderClient
		final W2dReferenceDataProviderClient c = new W2dReferenceDataProviderClient();

		// mock DatasourceManager
		final DatasourceManager datasourceManager = Mockito.mock(DatasourceManager.class);

		// mock ConnectionHolder
		final ConnectionHolder connectionHolder = Mockito.mock(ConnectionHolder.class);

		// mock DbHelper
		final DbHelper dbHelper = Mockito.mock(DbHelper.class);

		// parameters
		final String datasourceCode = "test-datasourceCode";
		final String taskId = "test-taskId";
		final String checkFinishedSql = "test-sql";
		final Datasource datasource = new Datasource();
		datasource.setCode(datasourceCode);

		// test-1: validation
		try {
			c.checkFinished(null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			c.checkFinished(datasourceCode, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			c.checkFinished(datasourceCode, taskId);
			Assert.fail("why no exception thrown?");
		} catch (NullPointerException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		c.setDatasourceManager(datasourceManager);
		try {
			c.checkFinished(datasourceCode, taskId);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		System.out.println("test-1: validation passed");

		c.setCheckFinishedSql(checkFinishedSql);

		// test-2: no result
		Mockito.when(datasourceManager.getDatasource(datasourceCode)).thenReturn(datasource);
		c.setConnectionHolder(connectionHolder);
		Mockito.when(
				dbHelper.query(Mockito.any(Connection.class), Mockito.eq(checkFinishedSql), Mockito.any(Object[].class)))
				.thenReturn(Collections.emptyList());
		c.setDbHelper(dbHelper);
		Assert.assertFalse(c.checkFinished(datasourceCode, taskId));
		System.out.println("test-2: no result passed");

		// test-3: empty row
		Mockito.reset(dbHelper);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		Mockito.when(
				dbHelper.query(Mockito.any(Connection.class), Mockito.eq(checkFinishedSql), Mockito.any(Object[].class)))
				.thenReturn(list);
		Assert.assertFalse(c.checkFinished(datasourceCode, taskId));
		System.out.println("test-3: empty row passed");

		// test-4: no status column
		list.get(0).put("xxx", "xxx");
		Assert.assertFalse(c.checkFinished(datasourceCode, taskId));
		System.out.println("test-4: no status column passed");

		// test-5: not finish
		list.get(0).clear();
		list.get(0).put("status", "yyy");
		Assert.assertFalse(c.checkFinished(datasourceCode, taskId));
		System.out.println("test-5: not finish passed");

		// test-6: finish
		list.get(0).put("status", "finish");
		Assert.assertTrue(c.checkFinished(datasourceCode, taskId));
		System.out.println("test-6: finish passed");
	}

	@Test
	public void testQueryDatas() {
		// W2dReferenceDataProviderClient
		final W2dReferenceDataProviderClient c = new W2dReferenceDataProviderClient();

		// mock DatasourceManager
		final DatasourceManager datasourceManager = Mockito.mock(DatasourceManager.class);

		// mock ConnectionHolder
		final ConnectionHolder connectionHolder = Mockito.mock(ConnectionHolder.class);

		// mock DbHelper
		final DbHelper dbHelper = Mockito.mock(DbHelper.class);

		// parameters
		final String datasourceCode = "test-datasourceCode";
		final String taskId = "test-taskId";
		final String tableNames = "table_1,table_2";
		final Datasource datasource = new Datasource();
		datasource.setCode(datasourceCode);

		// test-1: validation
		try {
			c.queryDatas(null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			c.queryDatas(datasourceCode, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			c.queryDatas(datasourceCode, taskId);
			Assert.fail("why no exception thrown?");
		} catch (NullPointerException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		c.setDatasourceManager(datasourceManager);
		try {
			c.queryDatas(datasourceCode, taskId);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		System.out.println("test-1: validation passed");

		// test-2: no tableNames
		Mockito.when(datasourceManager.getDatasource(datasourceCode)).thenReturn(datasource);
		c.setConnectionHolder(connectionHolder);
		Map<String, List<Map<String, String>>> result = c.queryDatas(datasourceCode, taskId);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
		System.out.println("test-2: no tableNames passed");

		// test-3: no result
		c.setTableNames(tableNames);
		Mockito.when(
				dbHelper.query(Mockito.any(Connection.class),
						Mockito.eq("select * from `table_1` where `task_id` = ?"), Mockito.any(Object[].class)))
				.thenReturn(Collections.emptyList());
		Mockito.when(
				dbHelper.query(Mockito.any(Connection.class),
						Mockito.eq("select * from `table_2` where `task_id` = ?"), Mockito.any(Object[].class)))
				.thenReturn(Collections.emptyList());
		c.setDbHelper(dbHelper);
		result = c.queryDatas(datasourceCode, taskId);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertNotNull(result.get("table_1"));
		Assert.assertTrue(result.get("table_1").isEmpty());
		Assert.assertNotNull(result.get("table_2"));
		Assert.assertTrue(result.get("table_2").isEmpty());
		System.out.println("test-3: no result passed");

		// test-4: has datas
		final List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		list1.add(new HashMap<String, Object>());
		list1.get(0).put("v1", "value-1-1");
		list1.get(0).put("v2", "value-1-2");
		final List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		list2.add(new HashMap<String, Object>());
		list2.get(0).put("v1", "value-2-1");
		list2.get(0).put("v2", "value-2-2");
		Mockito.reset(dbHelper);
		c.setTableNames(tableNames);
		Mockito.when(
				dbHelper.query(Mockito.any(Connection.class),
						Mockito.eq("select * from `table_1` where `task_id` = ?"), Mockito.any(Object[].class)))
				.thenReturn(list1);
		Mockito.when(
				dbHelper.query(Mockito.any(Connection.class),
						Mockito.eq("select * from `table_2` where `task_id` = ?"), Mockito.any(Object[].class)))
				.thenReturn(list2);
		result = c.queryDatas(datasourceCode, taskId);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertNotNull(result.get("table_1"));
		Assert.assertEquals(1, result.get("table_1").size());
		Assert.assertEquals("value-1-1", result.get("table_1").get(0).get("v1"));
		Assert.assertEquals("value-1-2", result.get("table_1").get(0).get("v2"));
		Assert.assertNotNull(result.get("table_2"));
		Assert.assertEquals(1, result.get("table_2").size());
		Assert.assertEquals("value-2-1", result.get("table_2").get(0).get("v1"));
		Assert.assertEquals("value-2-2", result.get("table_2").get(0).get("v2"));
		System.out.println("test-4: has datas passed");
	}

}
