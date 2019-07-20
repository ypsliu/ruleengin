/**
 * 
 */
package cn.rongcapital.ruleengine.core.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.core.ReferenceDataDao;
import cn.rongcapital.ruleengine.core.ReferenceDataServiceImpl;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataStatus;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.ReferenceDataProcessor;
import cn.rongcapital.ruleengine.service.ReferenceDataProviderClient;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the unit test for ReferenceDataService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class ReferenceDataServiceTest {

	@Test
	public void test() {
		// mock ReferenceDataDao
		final ReferenceDataDao referenceDataDao = Mockito.mock(ReferenceDataDao.class);
		// mock ReferenceDataProviderClient
		final ReferenceDataProviderClient referenceDataProviderClient = Mockito.mock(ReferenceDataProviderClient.class);
		// mock ReferenceDataProcessor
		final ReferenceDataProcessor referenceDataProcessor = Mockito.mock(ReferenceDataProcessor.class);
		// mock DatetimeProvider
		final DatetimeProvider datetimeProvider = Mockito.mock(DatetimeProvider.class);
		// mock BizTypeService
		final BizTypeService bizTypeService = Mockito.mock(BizTypeService.class);
		// mock DatasourceManager
		final DatasourceManager datasourceManager = Mockito.mock(DatasourceManager.class);

		final ObjectMapper jsonMapper = new ObjectMapper();

		// ReferenceDataService
		final ReferenceDataServiceImpl rds = new ReferenceDataServiceImpl();
		rds.setReferenceDataDao(referenceDataDao);
		rds.setReferenceDataProviderClient(referenceDataProviderClient);
		rds.setReferenceDataProcessor(referenceDataProcessor);
		rds.setDatetimeProvider(datetimeProvider);
		rds.setBizTypeService(bizTypeService);
		rds.setDatasourceManager(datasourceManager);

		// parameters
		final String bizTypeCode = "test-bizTypeCode";
		final String providerDatasourceCode = "test-providerDatasourceCode";
		final Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put("name", "test-name");
		conditions.put("id", "test-id");
		conditions.put("mobileNumber", "test-mobileNumber");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber");
		conditions.put("companyName", "test-companyName");
		final StringBuilder buf = new StringBuilder();
		buf.append("bizTypeCode=").append(bizTypeCode);
		for (final String key : conditions.keySet()) {
			buf.append(",").append(key).append("=").append(conditions.get(key));
		}
		final String conditionsValue = buf.toString();

		final String taskId1 = "test-taskId-1";
		final Date time1 = new Date();
		final Long statusId1 = 1234L;

		// test-1: validation
		try {
			rds.getReferenceData(null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			rds.getReferenceData(bizTypeCode, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			rds.getReferenceData(bizTypeCode, conditions);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		System.out.println("test-1: validation passed");

		rds.setProviderDatasourceCode(providerDatasourceCode);
		Mockito.when(bizTypeService.bizTypeExisted(bizTypeCode)).thenReturn(true);
		Mockito.when(datasourceManager.datasourceExisted(providerDatasourceCode)).thenReturn(true);

		// test-2: the status is not existed
		Mockito.when(referenceDataDao.loadStatusWithLock(conditionsValue)).thenReturn(null);
		Mockito.when(referenceDataProviderClient.request(conditions)).thenReturn(taskId1);
		Mockito.when(datetimeProvider.nowTime()).thenReturn(time1);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Assert.assertNotNull(invocation.getArguments()[0]);
				final ReferenceDataStatus status = (ReferenceDataStatus) invocation.getArguments()[0];
				Assert.assertEquals(bizTypeCode, status.getBizTypeCode());
				Assert.assertFalse(status.isDone());
				Assert.assertEquals(conditionsValue, status.getConditionsValue());
				Assert.assertEquals(time1, status.getInsertTime());
				Assert.assertEquals(taskId1, status.getProviderTaskId());
				return null;
			}

		}).when(referenceDataDao).createStatus(Mockito.any(ReferenceDataStatus.class));
		// test
		ReferenceData rd = rds.getReferenceData(bizTypeCode, conditions);
		// check
		Assert.assertNull(rd);
		Mockito.verify(referenceDataDao).loadStatusWithLock(conditionsValue);
		Mockito.verify(referenceDataProviderClient).request(conditions);
		Mockito.verify(datetimeProvider).nowTime();
		Mockito.verify(referenceDataDao).createStatus(Mockito.any(ReferenceDataStatus.class));
		System.out.println("test-2: the status is not existed passed");

		// test-3: not done and not finished
		final ReferenceDataStatus status = new ReferenceDataStatus();
		status.setId(statusId1);
		status.setProviderTaskId(taskId1);
		Mockito.when(referenceDataDao.loadStatusWithLock(conditionsValue)).thenReturn(status);
		Mockito.when(referenceDataProviderClient.checkFinished(providerDatasourceCode, taskId1)).thenReturn(false);
		// test
		rd = rds.getReferenceData(bizTypeCode, conditions);
		// check
		Assert.assertNull(rd);
		Mockito.verify(referenceDataDao, Mockito.times(2)).loadStatusWithLock(conditionsValue);
		Mockito.verify(referenceDataProviderClient).request(conditions);
		Mockito.verify(referenceDataProviderClient).checkFinished(providerDatasourceCode, taskId1);
		Mockito.verify(datetimeProvider).nowTime();
		Mockito.verify(referenceDataDao).createStatus(Mockito.any(ReferenceDataStatus.class));
		System.out.println("test-3: not done and not finished passed");

		// test-4: not done and finished
		Mockito.when(referenceDataProviderClient.checkFinished(providerDatasourceCode, taskId1)).thenReturn(true);
		final Map<String, List<Map<String, String>>> datas1 = new HashMap<String, List<Map<String, String>>>();
		datas1.put("ds1", new ArrayList<Map<String, String>>());
		datas1.get("ds1").add(new HashMap<String, String>());
		datas1.get("ds1").get(0).put("v1", "value-1");
		Mockito.when(referenceDataProviderClient.queryDatas(providerDatasourceCode, taskId1)).thenReturn(datas1);
		final ReferenceData rd1 = new ReferenceData();
		rd1.setResponse(datas1);
		Mockito.when(referenceDataProcessor.process(bizTypeCode, conditions, datas1)).thenReturn(rd1);
		final Date time2 = new Date();
		final Date time3 = new Date();
		Mockito.when(datetimeProvider.nowTime()).thenReturn(time2, time3);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Assert.assertNotNull(invocation.getArguments()[0]);
				final ReferenceData rd = (ReferenceData) invocation.getArguments()[0];
				Assert.assertEquals(taskId1, rd.getProviderTaskId());
				Assert.assertEquals(bizTypeCode, rd.getBizTypeCode());
				Assert.assertEquals(conditionsValue, rd.getConditionsValue());
				try {
					Assert.assertEquals(datas1, jsonMapper.readValue(rd.getResponseRaw(), Map.class));
				} catch (Exception e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(time2, rd.getTime());
				return null;
			}

		}).when(referenceDataDao).createReferenceData(Mockito.any(ReferenceData.class));
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Assert.assertNotNull(invocation.getArguments()[0]);
				final ReferenceDataStatus status = (ReferenceDataStatus) invocation.getArguments()[0];
				Assert.assertTrue(status.isDone());
				Assert.assertEquals(time3, status.getUpdateTime());
				Assert.assertNotNull(status.getId());
				Assert.assertEquals(statusId1, status.getId());
				return null;
			}

		}).when(referenceDataDao).updateStatus(Mockito.any(ReferenceDataStatus.class));
		// test
		rd = rds.getReferenceData(bizTypeCode, conditions);
		// check
		Assert.assertNotNull(rd);
		Assert.assertEquals(bizTypeCode, rd.getBizTypeCode());
		Assert.assertEquals(taskId1, rd.getProviderTaskId());
		Assert.assertEquals(conditions, rd.getConditions());
		Assert.assertNull(rd.getConditionsValue());
		Assert.assertEquals(time2, rd.getTime());
		Assert.assertEquals(datas1, rd.getResponse());
		Assert.assertNull(rd.getResponseValue());
		Assert.assertNull(rd.getResponseRaw());
		Mockito.verify(referenceDataDao, Mockito.times(3)).loadStatusWithLock(conditionsValue);
		Mockito.verify(referenceDataProviderClient).request(conditions);
		Mockito.verify(referenceDataProviderClient, Mockito.times(2)).checkFinished(providerDatasourceCode, taskId1);
		Mockito.verify(datetimeProvider, Mockito.times(3)).nowTime();
		Mockito.verify(referenceDataDao).createStatus(Mockito.any(ReferenceDataStatus.class));
		Mockito.verify(referenceDataProviderClient).queryDatas(providerDatasourceCode, taskId1);
		Mockito.verify(referenceDataProcessor).process(bizTypeCode, conditions, datas1);
		Mockito.verify(referenceDataDao).createReferenceData(Mockito.any(ReferenceData.class));
		Mockito.verify(referenceDataDao).updateStatus(Mockito.any(ReferenceDataStatus.class));
		System.out.println("test-4: not done and finished passed");

		// test-5: done and not expired
		status.setDone(true);
		final ReferenceData rd2 = new ReferenceData();
		rd2.setTime(time3);
		try {
			rd2.setResponseValue(jsonMapper.writeValueAsString(datas1));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Mockito.when(referenceDataDao.loadReferenceData(conditionsValue)).thenReturn(rd2);
		// test
		rd = rds.getReferenceData(bizTypeCode, conditions);
		// check
		Assert.assertNotNull(rd);
		Assert.assertNull(rd.getConditionsValue());
		Assert.assertEquals(time3, rd.getTime());
		Assert.assertEquals(datas1, rd.getResponse());
		Assert.assertNull(rd.getResponseValue());
		Assert.assertNull(rd.getResponseRaw());
		Mockito.verify(referenceDataDao, Mockito.times(4)).loadStatusWithLock(conditionsValue);
		Mockito.verify(referenceDataProviderClient).request(conditions);
		Mockito.verify(referenceDataProviderClient, Mockito.times(2)).checkFinished(providerDatasourceCode, taskId1);
		Mockito.verify(datetimeProvider, Mockito.times(4)).nowTime();
		Mockito.verify(referenceDataDao).createStatus(Mockito.any(ReferenceDataStatus.class));
		Mockito.verify(referenceDataProviderClient).queryDatas(providerDatasourceCode, taskId1);
		Mockito.verify(referenceDataProcessor).process(bizTypeCode, conditions, datas1);
		Mockito.verify(referenceDataDao).createReferenceData(Mockito.any(ReferenceData.class));
		Mockito.verify(referenceDataDao).updateStatus(Mockito.any(ReferenceDataStatus.class));
		Mockito.verify(referenceDataDao).loadReferenceData(conditionsValue);
		System.out.println("test-5: done and not expired passed");

		// test-6: done and expired
		final ReferenceData rd3 = new ReferenceData();
		final Date time4 = new Date(time3.getTime() - 2 * 24 * 3600 * 1000);
		final Date time5 = new Date();
		rd3.setTime(time4);
		try {
			rd3.setResponseValue(jsonMapper.writeValueAsString(datas1));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Mockito.when(referenceDataDao.loadReferenceData(conditionsValue)).thenReturn(rd3);

		final String taskId2 = "test-taskId-2";
		Mockito.when(referenceDataProviderClient.request(conditions)).thenReturn(taskId2);
		Mockito.when(datetimeProvider.nowTime()).thenReturn(time5);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Assert.assertNotNull(invocation.getArguments()[0]);
				final ReferenceDataStatus status = (ReferenceDataStatus) invocation.getArguments()[0];
				Assert.assertEquals(bizTypeCode, status.getBizTypeCode());
				Assert.assertFalse(status.isDone());
				Assert.assertEquals(conditionsValue, status.getConditionsValue());
				Assert.assertEquals(time5, status.getInsertTime());
				Assert.assertEquals(taskId2, status.getProviderTaskId());
				return null;
			}

		}).when(referenceDataDao).createStatus(Mockito.any(ReferenceDataStatus.class));

		// test
		rd = rds.getReferenceData(bizTypeCode, conditions);
		// check
		Assert.assertNull(rd);
		Mockito.verify(referenceDataDao, Mockito.times(5)).loadStatusWithLock(conditionsValue);
		Mockito.verify(referenceDataProviderClient, Mockito.times(2)).request(conditions);
		Mockito.verify(referenceDataProviderClient, Mockito.times(2)).checkFinished(providerDatasourceCode, taskId1);
		Mockito.verify(datetimeProvider, Mockito.times(6)).nowTime();
		Mockito.verify(referenceDataDao, Mockito.times(2)).createStatus(Mockito.any(ReferenceDataStatus.class));
		Mockito.verify(referenceDataProviderClient).queryDatas(providerDatasourceCode, taskId1);
		Mockito.verify(referenceDataProcessor).process(bizTypeCode, conditions, datas1);
		Mockito.verify(referenceDataDao).createReferenceData(Mockito.any(ReferenceData.class));
		Mockito.verify(referenceDataDao).updateStatus(Mockito.any(ReferenceDataStatus.class));
		Mockito.verify(referenceDataDao, Mockito.times(2)).loadReferenceData(conditionsValue);
		System.out.println("test-6: done and expired passed");
	}

}
