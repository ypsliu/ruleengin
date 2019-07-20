/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.core.w2d.DataCaptureRequestForm;
import cn.rongcapital.ruleengine.core.w2d.DataCaptureResponse;
import cn.rongcapital.ruleengine.core.w2d.W2dResourceAgent;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataStatus;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.ReferenceDataService;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the ITest for ReferenceDataService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/referencedata-itest.xml" })
@Configuration
public class ReferenceDataServiceITest {

	private static final String DATASOURCE_CODE = "w2d-datasource";

	private static final String BIZ_TYPE_CODE = "test-biz-type";

	@Autowired
	private TestingService testingService;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private ReferenceDataService referenceDataService;

	@Autowired
	private W2dResourceAgentProxy w2dResourceAgentProxy;

	@Autowired
	private DatetimeProviderProxy datetimeProviderProxy;

	private W2dResourceAgent w2dResourceAgent;

	private DatetimeProvider datetimeProvider;

	@Test
	public void test() {
		testStatusNotExisted();
		testNotDoneAndNotFinished();
		testNotDoneAndFinished();
		testDoneAndNotExpired();
		testDoneAndExpired();
	}

	private void testStatusNotExisted() {
		// conditions
		final Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put("name", "test-name-1");
		conditions.put("id", "test-id-1");
		conditions.put("mobileNumber", "test-mobileNumber-1");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-1");
		conditions.put("companyName", "test-companyName-1");
		// conditionsValue
		final StringBuilder buf = new StringBuilder();
		buf.append("bizTypeCode=").append(BIZ_TYPE_CODE);
		for (final String key : conditions.keySet()) {
			buf.append(",").append(key).append("=").append(conditions.get(key));
		}
		final String conditionsValue = buf.toString();

		final String taskId = "test-taskId-1";
		final Date time1 = new Date(new Date().getTime() / 1000 * 1000);

		// mock
		final DataCaptureResponse dcr = new DataCaptureResponse();
		dcr.setCode("200");
		dcr.setTaskId(taskId);
		Mockito.when(this.w2dResourceAgent.capture(Mockito.any(DataCaptureRequestForm.class))).thenReturn(dcr);
		Mockito.when(this.datetimeProvider.nowTime()).thenReturn(time1);

		// test
		final ReferenceData rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		// check
		Assert.assertNull(rd);
		final ReferenceDataStatus status = this.testingService.loadReferenceDataStatus(conditionsValue);
		Assert.assertNotNull(status);
		Assert.assertNotNull(status.getId());
		Assert.assertEquals(BIZ_TYPE_CODE, status.getBizTypeCode());
		Assert.assertFalse(status.isDone());
		Assert.assertEquals(taskId, status.getProviderTaskId());
		Assert.assertEquals(conditionsValue, status.getConditionsValue());
		Assert.assertEquals(time1, status.getInsertTime());
		Assert.assertNull(status.getUpdateTime());

		// other requests
		conditions.put("name", "test-name-2");
		conditions.put("id", "test-id-2");
		conditions.put("mobileNumber", "test-mobileNumber-2");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-2");
		conditions.put("companyName", "test-companyName-2");
		dcr.setTaskId("test-taskId-2");
		Mockito.when(this.w2dResourceAgent.capture(Mockito.any(DataCaptureRequestForm.class))).thenReturn(dcr);
		this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		conditions.put("name", "test-name-3");
		conditions.put("id", "test-id-3");
		conditions.put("mobileNumber", "test-mobileNumber-3");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-3");
		conditions.put("companyName", "test-companyName-3");
		dcr.setTaskId("test-taskId-3");
		Mockito.when(this.w2dResourceAgent.capture(Mockito.any(DataCaptureRequestForm.class))).thenReturn(dcr);
		this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		conditions.put("name", "test-name-4");
		conditions.put("id", "test-id-4");
		conditions.put("mobileNumber", "test-mobileNumber-4");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-4");
		conditions.put("companyName", "test-companyName-4");
		dcr.setTaskId("test-taskId-4");
		Mockito.when(this.w2dResourceAgent.capture(Mockito.any(DataCaptureRequestForm.class))).thenReturn(dcr);
		this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		System.out.println("testStatusNotExisted passed");
	}

	private void testNotDoneAndNotFinished() {
		// conditions
		final Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put("name", "test-name-1");
		conditions.put("id", "test-id-1");
		conditions.put("mobileNumber", "test-mobileNumber-1");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-1");
		conditions.put("companyName", "test-companyName-1");

		// final String taskId = "test-taskId-1";
		//
		// final DataCaptureResponse dcr = new DataCaptureResponse();
		// dcr.setCode("200");
		// dcr.setTaskId(taskId);
		//
		// Mockito.when(this.w2dResourceAgent.capture(Mockito.any(DataCaptureRequestForm.class))).thenReturn(dcr);

		// test
		final ReferenceData rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		// check
		Assert.assertNull(rd);
		System.out.println("testNotDoneAndNotFinished passed");
	}

	private void testNotDoneAndFinished() {
		// conditions
		final Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put("name", "test-name-2");
		conditions.put("id", "test-id-2");
		conditions.put("mobileNumber", "test-mobileNumber-2");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-2");
		conditions.put("companyName", "test-companyName-2");

		final String taskId = "test-taskId-2";
		final Date time1 = new Date(new Date().getTime() / 1000 * 1000);
		Mockito.when(this.datetimeProvider.nowTime()).thenReturn(time1);

		// test
		final ReferenceData rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		// check
		Assert.assertNotNull(rd);
		Assert.assertEquals(BIZ_TYPE_CODE, rd.getBizTypeCode());
		Assert.assertEquals(taskId, rd.getProviderTaskId());
		Assert.assertEquals(conditions, rd.getConditions());
		Assert.assertNull(rd.getConditionsValue());
		Assert.assertEquals(time1, rd.getTime());
		Assert.assertNull(rd.getResponseValue());
		Assert.assertNull(rd.getResponseRaw());
		Assert.assertNotNull(rd.getResponse());
		Assert.assertEquals(2, rd.getResponse().size());
		Assert.assertNotNull(rd.getResponse().get("table_1"));
		Assert.assertEquals(1, rd.getResponse().get("table_1").size());
		Assert.assertEquals("value-2-1-1", rd.getResponse().get("table_1").get(0).get("v1"));
		Assert.assertEquals("value-2-1-2", rd.getResponse().get("table_1").get(0).get("v2"));
		Assert.assertNotNull(rd.getResponse().get("table_2"));
		Assert.assertEquals(1, rd.getResponse().get("table_2").size());
		Assert.assertEquals("value-2-2-1", rd.getResponse().get("table_2").get(0).get("v1"));
		Assert.assertEquals("value-2-2-2", rd.getResponse().get("table_2").get(0).get("v2"));
		Assert.assertEquals("value-2-2-3", rd.getResponse().get("table_2").get(0).get("v3"));

		// next conditions
		conditions.put("name", "test-name-3");
		conditions.put("id", "test-id-3");
		conditions.put("mobileNumber", "test-mobileNumber-3");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-3");
		conditions.put("companyName", "test-companyName-3");
		this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		System.out.println("testNotDoneAndFinished passed");
	}

	private void testDoneAndNotExpired() {
		// conditions
		final Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put("name", "test-name-3");
		conditions.put("id", "test-id-3");
		conditions.put("mobileNumber", "test-mobileNumber-3");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-3");
		conditions.put("companyName", "test-companyName-3");

		final String taskId = "test-taskId-3";
		final Date time1 = new Date(new Date().getTime() / 1000 * 1000);
		Mockito.when(this.datetimeProvider.nowTime()).thenReturn(time1);

		// test
		final ReferenceData rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		// check
		Assert.assertNotNull(rd);
		Assert.assertEquals(BIZ_TYPE_CODE, rd.getBizTypeCode());
		Assert.assertEquals(taskId, rd.getProviderTaskId());
		Assert.assertNull(rd.getConditionsValue());
		Assert.assertNull(rd.getResponseValue());
		Assert.assertNull(rd.getResponseRaw());
		Assert.assertNotNull(rd.getResponse());
		Assert.assertEquals(2, rd.getResponse().size());
		Assert.assertNotNull(rd.getResponse().get("table_1"));
		Assert.assertEquals(1, rd.getResponse().get("table_1").size());
		Assert.assertEquals("value-3-1-1", rd.getResponse().get("table_1").get(0).get("v1"));
		Assert.assertEquals("value-3-1-2", rd.getResponse().get("table_1").get(0).get("v2"));
		Assert.assertEquals("value-3-1-3", rd.getResponse().get("table_1").get(0).get("v3"));
		Assert.assertNotNull(rd.getResponse().get("table_2"));
		Assert.assertEquals(1, rd.getResponse().get("table_2").size());
		Assert.assertEquals("value-3-2-1", rd.getResponse().get("table_2").get(0).get("v1"));
		Assert.assertEquals("value-3-2-2", rd.getResponse().get("table_2").get(0).get("v2"));

		System.out.println("testDoneAndNotExpired passed");
	}

	private void testDoneAndExpired() {
		// conditions
		final Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put("name", "test-name-4");
		conditions.put("id", "test-id-4");
		conditions.put("mobileNumber", "test-mobileNumber-4");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-4");
		conditions.put("companyName", "test-companyName-4");

		final Date time1 = new Date((new Date().getTime() - 2 * 24 * 3600 * 1000) / 1000 * 1000);
		final Date time2 = new Date(new Date().getTime() / 1000 * 1000);
		Mockito.when(this.datetimeProvider.nowTime()).thenReturn(time1, time2, time2);

		// get the expired result
		ReferenceData rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);
		Assert.assertNotNull(rd);
		Assert.assertEquals(time1, rd.getTime());

		// get again, because it expired, will trigger new request
		final String taskId = "test-taskId-5";
		final DataCaptureResponse dcr = new DataCaptureResponse();
		dcr.setCode("200");
		dcr.setTaskId(taskId);
		Mockito.when(this.w2dResourceAgent.capture(Mockito.any(DataCaptureRequestForm.class))).thenReturn(dcr);
		rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);
		Assert.assertNull(rd);

		// get the result to trigger get the result from provider
		rd = this.referenceDataService.getReferenceData(BIZ_TYPE_CODE, conditions);

		// check
		Assert.assertNotNull(rd);
		Assert.assertEquals(BIZ_TYPE_CODE, rd.getBizTypeCode());
		Assert.assertEquals(taskId, rd.getProviderTaskId());
		Assert.assertNull(rd.getConditionsValue());
		Assert.assertEquals(time2, rd.getTime());
		Assert.assertNull(rd.getResponseValue());
		Assert.assertNull(rd.getResponseRaw());
		Assert.assertNotNull(rd.getResponse());
		Assert.assertEquals(2, rd.getResponse().size());
		Assert.assertNotNull(rd.getResponse().get("table_1"));
		Assert.assertEquals(1, rd.getResponse().get("table_1").size());
		Assert.assertEquals("value-5-1-1", rd.getResponse().get("table_1").get(0).get("v1"));
		Assert.assertNotNull(rd.getResponse().get("table_2"));
		Assert.assertEquals(1, rd.getResponse().get("table_2").size());
		Assert.assertEquals("value-5-2-1", rd.getResponse().get("table_2").get(0).get("v1"));

		System.out.println("testDoneAndExpired passed");
	}

	@Before
	public void setup() {
		this.w2dResourceAgent = Mockito.mock(W2dResourceAgent.class);
		this.w2dResourceAgentProxy.setW2dResourceAgent(this.w2dResourceAgent);
		this.datetimeProvider = Mockito.mock(DatetimeProvider.class);
		this.datetimeProviderProxy.setDatetimeProvider(datetimeProvider);

		this.clearDatas();
		// create bizType
		final BizType bizType = new BizType();
		bizType.setCode(BIZ_TYPE_CODE);
		bizType.setName(BIZ_TYPE_CODE);
		bizType.setCreationTime(new Date());
		this.testingService.createBizType(bizType);
		// create datasource
		final Datasource datasource = new Datasource();
		datasource.setCode(DATASOURCE_CODE);
		datasource.setName(DATASOURCE_CODE);
		datasource.setCreationTime(new Date());
		datasource.setDriverClass("com.mysql.jdbc.Driver");
		datasource.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_w2d?useUnicode=true&characterEncoding=utf8");
		datasource.setUser("root");
		this.testingService.createDatasource(datasource);
	}

	@After
	public void teardown() {
		this.clearDatas();
	}

	private void clearDatas() {
		this.testingService.clearBizTypes();
		this.testingService.clearDatasources();
		this.testingService.clearReferenceDataStatus();
		this.testingService.clearReferenceDatas();
	}

	/**
	 * @param testingService
	 *            the testingService to set
	 */
	public void setTestingService(final TestingService testingService) {
		this.testingService = testingService;
	}

	/**
	 * @param referenceDataService
	 *            the referenceDataService to set
	 */
	public void setReferenceDataService(final ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

}
