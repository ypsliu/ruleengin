/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.service.DataExtractor;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * the ITest for DataExtractor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/dataextractor-itest.xml" })
public class DataExtractorITest {

	@Autowired
	private DataExtractor dataExtractor;

	private DruidDataSource testingDs1;

	private DruidDataSource testingDs2;

	@Test
	public void test() {
		// create datasources
		final Datasource d1 = new Datasource();
		d1.setCode("datasource-1");
		d1.setName("name-1");
		d1.setDriverClass("com.mysql.jdbc.Driver");
		d1.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_1?useUnicode=true&characterEncoding=utf8");
		d1.setUser("root");
		d1.setPassword(null);
		d1.setMaxPoolSize(5);
		d1.setValidationSql("select 1");
		final Datasource d2 = new Datasource();
		d2.setCode("datasource-2");
		d2.setName("name-2");
		d2.setDriverClass("com.mysql.jdbc.Driver");
		d2.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_2?useUnicode=true&characterEncoding=utf8");
		d2.setUser("root");
		d2.setPassword(null);
		d2.setMaxPoolSize(5);
		d2.setValidationSql("select 1");
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
		// datasources
		rule.setDatasources(new ArrayList<Datasource>());
		rule.getDatasources().add(d1);
		rule.getDatasources().add(d2);
		// extractSqls
		rule.setExtractSqls(new ArrayList<ExtractSql>());
		rule.getExtractSqls().add(new ExtractSql());
		rule.getExtractSqls().get(0).setDatasourceCode("datasource-1");
		rule.getExtractSqls().get(0).setParams(new ArrayList<String>());
		rule.getExtractSqls().get(0).getParams().add("bizCode");
		rule.getExtractSqls().get(0).setSql("select key_1, key_2, key_5 from test_table_1 where bizCode = ?");
		rule.getExtractSqls().add(new ExtractSql());
		rule.getExtractSqls().get(1).setDatasourceCode("datasource-2");
		rule.getExtractSqls().get(1).setParams(new ArrayList<String>());
		rule.getExtractSqls().get(1).getParams().add("key_5");
		rule.getExtractSqls().get(1).setSql("select key_3, key_4, key_6 from test_table_2 where key_5 = ?");

		// extract
		final Map<String, String> datas = this.dataExtractor.extractRuleDatas(rule, bizCode);

		// check
		Assert.assertNotNull(datas);
		Assert.assertEquals(4, datas.size());
		Assert.assertEquals("value-1", datas.get("key_1"));
		Assert.assertEquals("value-2", datas.get("key_2"));
		Assert.assertEquals("value-3", datas.get("key_3"));
		Assert.assertEquals("value-4", datas.get("key_4"));
		System.out.println("test passed");
	}

	@Before
	public void setup() {
		this.testingDs1 = new DruidDataSource();
		this.testingDs1.setDriverClassName("com.mysql.jdbc.Driver");
		this.testingDs1.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_1?useUnicode=true&characterEncoding=utf8");
		this.testingDs1.setUsername("root");
		this.testingDs1.setPassword(null);
		this.testingDs2 = new DruidDataSource();
		this.testingDs2.setDriverClassName("com.mysql.jdbc.Driver");
		this.testingDs2.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_2?useUnicode=true&characterEncoding=utf8");
		this.testingDs2.setUsername("root");
		this.testingDs2.setPassword(null);
		this.clearTestingDatas();

		// prepare testing datas
		Connection conn1 = null;
		PreparedStatement ps1 = null;
		Connection conn2 = null;
		PreparedStatement ps2 = null;
		try {
			conn1 = this.testingDs1.getConnection();
			ps1 = conn1.prepareStatement("insert into test_table_1(key_1, key_2, key_5, bizCode) "
					+ " values('value-1', 'value-2', 'value-5', 'test-bizCode')");
			ps1.executeUpdate();
			conn2 = this.testingDs2.getConnection();
			ps2 = conn2.prepareStatement("insert into test_table_2(key_3, key_4, key_5, key_6) "
					+ " values('value-3', 'value-4', 'value-5', 'value-6')");
			ps2.executeUpdate();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (Exception e2) {
					//
				}
			}
			if (conn1 != null) {
				try {
					conn1.close();
				} catch (Exception e2) {
					//
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (Exception e2) {
					//
				}
			}
			if (conn2 != null) {
				try {
					conn2.close();
				} catch (Exception e2) {
					//
				}
			}
		}
	}

	private void clearTestingDatas() {
		Connection conn1 = null;
		PreparedStatement ps1 = null;
		Connection conn2 = null;
		PreparedStatement ps2 = null;
		try {
			conn1 = this.testingDs1.getConnection();
			ps1 = conn1.prepareStatement("delete from test_table_1");
			ps1.executeUpdate();
			conn2 = this.testingDs2.getConnection();
			ps2 = conn2.prepareStatement("delete from test_table_2");
			ps2.executeUpdate();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (Exception e2) {
					//
				}
			}
			if (conn1 != null) {
				try {
					conn1.close();
				} catch (Exception e2) {
					//
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (Exception e2) {
					//
				}
			}
			if (conn2 != null) {
				try {
					conn2.close();
				} catch (Exception e2) {
					//
				}
			}
		}
	}

	@After
	public void teardown() {
		this.clearTestingDatas();
		if (this.testingDs1 != null) {
			this.testingDs1.close();
		}
		if (this.testingDs2 != null) {
			this.testingDs2.close();
		}
	}

	/**
	 * @param dataExtractor
	 *            the dataExtractor to set
	 */
	public void setDataExtractor(DataExtractor dataExtractor) {
		this.dataExtractor = dataExtractor;
	}

}
