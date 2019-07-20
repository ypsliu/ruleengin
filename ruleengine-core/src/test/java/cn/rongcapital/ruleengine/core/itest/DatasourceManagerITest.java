/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.exception.DuplicateException;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.DatasourceManager;

/**
 * the ITest for DatasourceManager
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/datasource-itest.xml" })
public class DatasourceManagerITest {

	@Autowired
	private DatasourceManager datasourceManager;

	@Autowired
	private TestingService testingService;

	@Test
	public void test() {
		// test-1: create datasource
		final Datasource d1 = new Datasource();
		d1.setCode("code-1");
		d1.setName("name-1");
		d1.setComment("comment-1");
		d1.setDriverClass("driverClass-1");
		d1.setUrl("url-1");
		d1.setUser("user-1");
		d1.setPassword("password-1");
		d1.setMaxPoolSize(111);
		d1.setValidationSql("validationSql-1");
		this.datasourceManager.createDatasource(d1);

		final Datasource d2 = new Datasource();
		d2.setCode("code-1");
		d2.setName("name-2");
		d2.setComment("comment-2");
		d2.setDriverClass("driverClass-2");
		d2.setUrl("url-2");
		d2.setUser("user-2");
		d2.setPassword("password-2");
		d2.setMaxPoolSize(222);
		d2.setValidationSql("validationSql-2");

		// duplicate code
		try {
			this.datasourceManager.createDatasource(d2);
			Assert.fail("the no exception thrown?");
		} catch (DuplicateException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("the other exception thrown?");
		}
		d2.setCode("code-2");
		this.datasourceManager.createDatasource(d2);
		System.out.println("test-1: create datasource passed");

		// test-2: get
		final Datasource d11 = this.datasourceManager.getDatasource(d1.getCode());
		final Datasource d21 = this.datasourceManager.getDatasource(d2.getCode());
		// check
		Assert.assertNotNull(d11);
		Assert.assertEquals(d1.getCode(), d11.getCode());
		Assert.assertEquals(d1.getName(), d11.getName());
		Assert.assertEquals(d1.getComment(), d11.getComment());
		Assert.assertNotNull(d11.getCreationTime());
		Assert.assertNull(d11.getUpdateTime());
		Assert.assertEquals(d1.getDriverClass(), d11.getDriverClass());
		Assert.assertEquals(d1.getUrl(), d11.getUrl());
		Assert.assertEquals(d1.getUser(), d11.getUser());
		Assert.assertEquals(d1.getPassword(), d11.getPassword());
		Assert.assertEquals(d1.getMaxPoolSize(), d11.getMaxPoolSize());
		Assert.assertEquals(d1.getValidationSql(), d11.getValidationSql());
		Assert.assertNotNull(d21);
		Assert.assertEquals(d2.getCode(), d21.getCode());
		Assert.assertEquals(d2.getName(), d21.getName());
		Assert.assertEquals(d2.getComment(), d21.getComment());
		Assert.assertNotNull(d21.getCreationTime());
		Assert.assertNull(d21.getUpdateTime());
		Assert.assertEquals(d2.getDriverClass(), d21.getDriverClass());
		Assert.assertEquals(d2.getUrl(), d21.getUrl());
		Assert.assertEquals(d2.getUser(), d21.getUser());
		Assert.assertEquals(d2.getPassword(), d21.getPassword());
		Assert.assertEquals(d2.getMaxPoolSize(), d21.getMaxPoolSize());
		Assert.assertEquals(d2.getValidationSql(), d21.getValidationSql());
		System.out.println("test-2: get passed");

		// test-3: get all
		List<Datasource> list = this.datasourceManager.getDatasources();
		// check
		Assert.assertNotNull(list);
		int checked = 0;
		for (final Datasource d : list) {
			if (d1.getCode().equals(d.getCode())) {
				checked++;
			} else if (d2.getCode().equals(d.getCode())) {
				checked++;
			}
		}
		Assert.assertEquals(2, checked);
		System.out.println("test-3: get all passed");

		// test-4: update
		d1.setName("name-1-1");
		d1.setComment("comment-1-1");
		d1.setDriverClass("driverClass-1-1");
		d1.setUrl("url-1-1");
		d1.setUser("user-1-1");
		d1.setPassword("password-1-1");
		d1.setMaxPoolSize(333);
		d1.setValidationSql("validationSql-1-1");
		// old not found
		String oldCode = d1.getCode();
		try {
			d1.setCode("xxxxxxx");
			this.datasourceManager.updateDatasource(d1);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		d1.setCode(oldCode);
		this.datasourceManager.updateDatasource(d1);
		// get
		final Datasource d12 = this.datasourceManager.getDatasource(d1.getCode());
		// check
		Assert.assertNotNull(d12);
		Assert.assertEquals(d1.getCode(), d12.getCode());
		Assert.assertEquals(d1.getName(), d12.getName());
		Assert.assertEquals(d1.getComment(), d12.getComment());
		Assert.assertNotNull(d12.getCreationTime());
		Assert.assertNotNull(d12.getUpdateTime());
		Assert.assertEquals(d1.getDriverClass(), d12.getDriverClass());
		Assert.assertEquals(d1.getUrl(), d12.getUrl());
		Assert.assertEquals(d1.getUser(), d12.getUser());
		Assert.assertEquals(d1.getPassword(), d12.getPassword());
		Assert.assertEquals(d1.getMaxPoolSize(), d12.getMaxPoolSize());
		Assert.assertEquals(d1.getValidationSql(), d12.getValidationSql());
		System.out.println("test-4: update passed");

		// test-5: remove
		try {
			this.datasourceManager.removeDatasource("xxx");
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		this.datasourceManager.removeDatasource(d2.getCode());
		// check
		Assert.assertNotNull(this.datasourceManager.getDatasource(d1.getCode()));
		Assert.assertNull(this.datasourceManager.getDatasource(d2.getCode()));
		list = this.datasourceManager.getDatasources();
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(d1.getCode(), list.get(0).getCode());
		System.out.println("test-5: remove passed");

		// test-6: existed
		Assert.assertTrue(this.datasourceManager.datasourceExisted(d1.getCode()));
		Assert.assertFalse(this.datasourceManager.datasourceExisted(d2.getCode()));
		Assert.assertFalse(this.datasourceManager.datasourceExisted("dfsf"));
		System.out.println("test-6: existed passed");
	}

	@Before
	public void setup() {
		this.clearDatas();
	}

	@After
	public void teardown() {
		this.clearDatas();
	}

	private void clearDatas() {
		this.testingService.clearDatasources();
	}

	/**
	 * @param testingService
	 *            the testingService to set
	 */
	public void setTestingService(TestingService testingService) {
		this.testingService = testingService;
	}

	/**
	 * @param datasourceManager
	 *            the datasourceManager to set
	 */
	public void setDatasourceManager(DatasourceManager datasourceManager) {
		this.datasourceManager = datasourceManager;
	}

}
