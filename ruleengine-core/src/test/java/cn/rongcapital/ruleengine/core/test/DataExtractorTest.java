/**
 * 
 */
package cn.rongcapital.ruleengine.core.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.core.DataExtractorImpl;
import cn.rongcapital.ruleengine.core.trade.TradeDataProviderAgent;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.TradeDataQueryForm;
import cn.rongcapital.ruleengine.rop.TradeDataQueryResponse;
import cn.rongcapital.ruleengine.service.ConnectionHolder;
import cn.rongcapital.ruleengine.service.DataExtractor;
import cn.rongcapital.ruleengine.service.DbHelper;

/**
 * the unit test for DataExtractor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class DataExtractorTest {

	@Test
	public void testWithRequestDatas() {
		// ruleParams
		List<String> ruleParams = new ArrayList<String>();
		ruleParams.add("key_1");
		ruleParams.add("key_2");
		ruleParams.add("key_3");
		ruleParams.add("key_4");

		// datas
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("key_1", "value-1");
		datas.put("key_2", "value-2");
		datas.put("key_3", "value-3");
		datas.put("key_4", "value-4");

		// DataExtractor
		final DataExtractor de = new DataExtractorImpl();

		// extract
		final Map<String, String> params = de.extractRuleDatas(ruleParams, datas);

		// check
		Assert.assertNotNull(params);
		Assert.assertEquals(4, params.size());

		Assert.assertEquals("value-1", params.get("key_1"));
		Assert.assertEquals("value-2", params.get("key_2"));
		Assert.assertEquals("value-3", params.get("key_3"));
		Assert.assertEquals("value-4", params.get("key_4"));

		System.out.println("test passed");
	}

	@Test
	public void testWithExtractSqls() throws SQLException {
		// bizCode
		final String bizCode = "test-bizCode";
		// test rule
		final Rule rule = new Rule();
		// params
		rule.setParams(new ArrayList<String>());
		rule.getParams().add("key_1");
		rule.getParams().add("key_2");
		rule.getParams().add("key_3");
		rule.getParams().add("key_4");
		// datasource
		rule.setDatasources(new ArrayList<Datasource>());
		rule.getDatasources().add(new Datasource());
		rule.getDatasources().get(0).setCode("datasource-1");
		rule.getDatasources().add(new Datasource());
		rule.getDatasources().get(1).setCode("datasource-2");
		// extractSql
		rule.setExtractSqls(new ArrayList<ExtractSql>());
		rule.getExtractSqls().add(new ExtractSql());
		rule.getExtractSqls().get(0).setDatasourceCode("datasource-1");
		rule.getExtractSqls().get(0).setParams(new ArrayList<String>());
		rule.getExtractSqls().get(0).getParams().add("bizCode");
		rule.getExtractSqls().get(0).setSql("sql-1");
		rule.getExtractSqls().add(new ExtractSql());
		rule.getExtractSqls().get(1).setDatasourceCode("datasource-2");
		rule.getExtractSqls().get(1).setParams(new ArrayList<String>());
		rule.getExtractSqls().get(1).getParams().add("context-key-1");
		rule.getExtractSqls().get(1).setSql("sql-2");

		// mock the Connection
		final Connection connection1 = Mockito.mock(Connection.class);
		final Connection connection2 = Mockito.mock(Connection.class);
		// mock the ConnectionHolder
		final ConnectionHolder connectionHolder = Mockito.mock(ConnectionHolder.class);
		// mock connectionHolder.getConnection()
		Mockito.when(connectionHolder.getConnection(Mockito.any(Datasource.class))).thenAnswer(
				new Answer<Connection>() {

					@Override
					public Connection answer(InvocationOnMock invocation) throws Throwable {
						final Datasource ds = (Datasource) invocation.getArguments()[0];
						if ("datasource-1".equals(ds.getCode())) {
							// datasource-1
							return connection1;
						} else {
							// datasource-2
							return connection2;
						}
					}

				});

		// mock DbHelper
		final DbHelper dbHelper = Mockito.mock(DbHelper.class);
		// mock dbHelper.query()
		Mockito.when(dbHelper.query(Mockito.any(Connection.class), Mockito.eq("sql-1"), Mockito.any(Object[].class)))
				.thenAnswer(new Answer<List<Map<String, Object>>>() {

					@Override
					public List<Map<String, Object>> answer(InvocationOnMock invocation) throws Throwable {
						Assert.assertNotNull(invocation.getArguments()[2]);
						Assert.assertTrue(invocation.getArguments()[2] instanceof Object[]);
						final Object[] params = (Object[]) invocation.getArguments()[2];
						Assert.assertEquals(1, params.length);
						Assert.assertEquals(bizCode, params[0]);
						final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						list.add(new HashMap<String, Object>());
						list.get(0).put("key_1", "value-1");
						list.get(0).put("key_2", "value-2");
						list.get(0).put("key_5", "value-5");
						list.get(0).put("context-key-1", "context-value-1");
						return list;
					}

				});
		Mockito.when(dbHelper.query(Mockito.any(Connection.class), Mockito.eq("sql-2"), Mockito.any(Object[].class)))
				.thenAnswer(new Answer<List<Map<String, Object>>>() {

					@Override
					public List<Map<String, Object>> answer(InvocationOnMock invocation) throws Throwable {
						Assert.assertNotNull(invocation.getArguments()[2]);
						Assert.assertTrue(invocation.getArguments()[2] instanceof Object[]);
						final Object[] params = (Object[]) invocation.getArguments()[2];
						Assert.assertEquals(1, params.length);
						Assert.assertEquals("context-value-1", params[0]);
						final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						list.add(new HashMap<String, Object>());
						list.get(0).put("key_3", "value-3");
						list.get(0).put("key_4", "value-4");
						list.get(0).put("key_6", "value-6");
						return list;
					}

				});

		// DataExtractor
		final DataExtractorImpl de = new DataExtractorImpl();
		de.setTradeDataExtractType("SQL");
		// set the mock ConnectionHolder
		de.setConnectionHolder(connectionHolder);
		// set the mock dbHelper
		de.setDbHelper(dbHelper);

		// extract
		final Map<String, String> datas = de.extractRuleDatas(rule, bizCode);
		// check
		Assert.assertNotNull(datas);
		Assert.assertEquals(4, datas.size());
		Assert.assertEquals("value-1", datas.get("key_1"));
		Assert.assertEquals("value-2", datas.get("key_2"));
		Assert.assertEquals("value-3", datas.get("key_3"));
		Assert.assertEquals("value-4", datas.get("key_4"));
		System.out.println("test passed");
	}

	@Test
	public void testWithExtractApi() {
		// bizCode
		final String bizCode = "test-bizCode";
		// test rule
		final Rule rule = new Rule();
		// params
		rule.setParams(new ArrayList<String>());
		rule.getParams().add("key_1");
		rule.getParams().add("key_2");
		rule.getParams().add("key_3");
		rule.getParams().add("key_4");
		// extractSql
		rule.setExtractSqls(new ArrayList<ExtractSql>());
		rule.getExtractSqls().add(new ExtractSql());
		rule.getExtractSqls().get(0).setTableName("table-1");
		rule.getExtractSqls().get(0).setColumns(new ArrayList<String>());
		rule.getExtractSqls().get(0).getColumns().add("key_1");
		rule.getExtractSqls().get(0).getColumns().add("key_2");
		rule.getExtractSqls().get(0).getColumns().add("key_5");
		rule.getExtractSqls().get(0).getColumns().add("context-key-1");
		rule.getExtractSqls().get(0).setConditions(new HashMap<String, String>());
		rule.getExtractSqls().get(0).getConditions().put("bizCode", "bizCode");
		rule.getExtractSqls().add(new ExtractSql());
		rule.getExtractSqls().get(1).setTableName("table-2");
		rule.getExtractSqls().get(1).setColumns(new ArrayList<String>());
		rule.getExtractSqls().get(1).getColumns().add("key_3");
		rule.getExtractSqls().get(1).getColumns().add("key_4");
		rule.getExtractSqls().get(1).getColumns().add("key_6");
		rule.getExtractSqls().get(1).setConditions(new HashMap<String, String>());
		rule.getExtractSqls().get(1).getConditions().put("context-key-1", "context-key-1");

		// mock TradeDataProviderAgent
		final TradeDataProviderAgent tradeDataProviderAgent = Mockito.mock(TradeDataProviderAgent.class);
		Mockito.when(tradeDataProviderAgent.query(Mockito.any(TradeDataQueryForm.class))).thenAnswer(
				new Answer<RopResponse>() {

					@Override
					public RopResponse answer(InvocationOnMock invocation) throws Throwable {
						final TradeDataQueryResponse r = new TradeDataQueryResponse();
						r.setResults(new ArrayList<Map<String, String>>());
						final TradeDataQueryForm form = (TradeDataQueryForm) invocation.getArguments()[0];
						if ("table-1".equals(form.getTable())) {
							// table-1
							Assert.assertNotNull(form.getColumns());
							Assert.assertEquals(4, form.getColumns().size());
							Assert.assertEquals("key_1", form.getColumns().get(0));
							Assert.assertEquals("key_2", form.getColumns().get(1));
							Assert.assertEquals("key_5", form.getColumns().get(2));
							Assert.assertEquals("context-key-1", form.getColumns().get(3));
							Assert.assertNotNull(form.getConditions());
							Assert.assertEquals(1, form.getConditions().size());
							Assert.assertEquals(bizCode, form.getConditions().get("bizCode"));
							// result
							r.setTable("table-1");
							r.getResults().add(new HashMap<String, String>());
							r.getResults().get(0).put("key_1", "value-1");
							r.getResults().get(0).put("key_2", "value-2");
							r.getResults().get(0).put("key_5", "value-5");
							r.getResults().get(0).put("context-key-1", "context-value-1");
						} else if ("table-2".equals(form.getTable())) {
							// table-2
							Assert.assertNotNull(form.getColumns());
							Assert.assertEquals(3, form.getColumns().size());
							Assert.assertEquals("key_3", form.getColumns().get(0));
							Assert.assertEquals("key_4", form.getColumns().get(1));
							Assert.assertEquals("key_6", form.getColumns().get(2));
							Assert.assertNotNull(form.getConditions());
							Assert.assertEquals(1, form.getConditions().size());
							Assert.assertEquals("context-value-1", form.getConditions().get("context-key-1"));
							// result
							r.setTable("table-2");
							r.getResults().add(new HashMap<String, String>());
							r.getResults().get(0).put("key_3", "value-3");
							r.getResults().get(0).put("key_4", "value-4");
							r.getResults().get(0).put("key_6", "value-6");
						}
						return r;
					}

				});

		// DataExtractor
		final DataExtractorImpl de = new DataExtractorImpl();
		de.setTradeDataExtractType("API");
		// set the mock tradeDataProviderAgent
		de.setTradeDataProviderAgent(tradeDataProviderAgent);

		// extract
		final Map<String, String> datas = de.extractRuleDatas(rule, bizCode);
		// check
		Assert.assertNotNull(datas);
		Assert.assertEquals(4, datas.size());
		Assert.assertEquals("value-1", datas.get("key_1"));
		Assert.assertEquals("value-2", datas.get("key_2"));
		Assert.assertEquals("value-3", datas.get("key_3"));
		Assert.assertEquals("value-4", datas.get("key_4"));
		System.out.println("test passed");
	}

}
