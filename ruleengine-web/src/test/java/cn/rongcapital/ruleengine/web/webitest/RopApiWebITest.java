/**
 * 
 */
package cn.rongcapital.ruleengine.web.webitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.api.RopResource;
import cn.rongcapital.ruleengine.api.RuleResource;
import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.model.AcceptanceResults;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.model.ScoreResults;
import cn.rongcapital.ruleengine.rop.RopErrorResponse;
import cn.rongcapital.ruleengine.rop.RopForm;
import cn.rongcapital.ruleengine.rop.RopMatchRequestForm;
import cn.rongcapital.ruleengine.rop.RopMatchResultForm;
import cn.rongcapital.ruleengine.rop.RopMatchResultResponse;
import cn.rongcapital.ruleengine.rop.RopReferenceDataForm;
import cn.rongcapital.ruleengine.rop.RopReferenceDataResponse;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.RopUtils;

/**
 * the web ITest for ROP APIs
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class RopApiWebITest extends BaseWebITest {

	private final String bizTypeCode = "test-biz-type";
	private final String acceptanceRuleCode = "acceptance-rule";
	private final String scoreRuleCode = "score-rule";

	private boolean datasPrepared = false;

	// @Test
	public void testReferenceDataRequestValidation() {
		// resources
		final RopResource ropResource = this.getResource(RopResource.class);

		RopResponse response = null;
		RopErrorResponse ropError = null;

		// test-1: null request
		response = ropResource.getReferenceData(null);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-1: null request passed");

		// test-2: no empty request
		RopReferenceDataForm request = new RopReferenceDataForm();
		response = ropResource.getReferenceData(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-2: no empty request passed");

		// test-3: verify sign failed
		request.setBizTypeCode(this.bizTypeCode);
		request.setConditions("xxx");
		this.fillRopReferenceDataRequestForm(request);
		request.setSign("bad-sign");
		response = ropResource.getReferenceData(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("E401", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-3: verify sign failed passed");

		// test-4: verify OK
		this.fillRopReferenceDataRequestForm(request);
		response = ropResource.getReferenceData(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("E400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-4: verify OK passed");
	}

	@Test
	public void testMatchRequestValidation() {
		// resources
		final RopResource ropResource = this.getResource(RopResource.class);

		RopResponse response = null;
		RopErrorResponse ropError = null;

		// test-1: null request
		response = ropResource.match(null);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-1: null request passed");

		// test-2: no empty request
		RopMatchRequestForm request = new RopMatchRequestForm();
		response = ropResource.match(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-2: no empty request passed");

		// test-3: verify sign failed
		request.setBizTypeCode(this.bizTypeCode);
		request.setBizCode("test-biz-code");
		this.fillRopMatchRequestForm(request);
		request.setSign("bad-sign");
		response = ropResource.match(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("E401", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-3: verify sign failed passed");

		// test-4: verify OK
		this.fillRopMatchRequestForm(request);
		response = ropResource.match(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("E400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-4: verify OK passed");
	}

	@Test
	public void testMatchResultRequestValidation() {
		// resources
		final RopResource ropResource = this.getResource(RopResource.class);

		RopResponse response = null;
		RopErrorResponse ropError = null;

		// test-1: null request
		response = ropResource.getResults(null);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-1: null request passed");

		// test-2: no empty request
		RopMatchResultForm request = new RopMatchResultForm();
		response = ropResource.getResults(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("400", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-2: no empty request passed");

		// test-3: verify sign failed
		request.setBizCode("test-biz-code");
		request.setBizTypeCode(bizTypeCode);
		this.fillRopMatchResultRequestForm(request);
		request.setSign("bad-sign");
		response = ropResource.getResults(request);
		if (!(response instanceof RopErrorResponse)) {
			Assert.fail("why no error");
		}
		ropError = (RopErrorResponse) response;
		// check
		Assert.assertEquals("E401", ropError.getCode());
		Assert.assertNotNull(ropError.getMsg());
		System.out.println("test-3: verify sign failed passed");

		// test-4: verify OK
		this.fillRopMatchResultRequestForm(request);
		response = ropResource.getResults(request);
		// check
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof RopMatchResultResponse);
		System.out.println("test-4: verify OK passed");
	}

	private void fillRopMatchResultRequestForm(final RopMatchResultForm form) {
		this.fillRopForm(form);
		final TreeMap<String, String> params = RopUtils.buildBaseParamsMap(form);
		params.put("biz_code", form.getBizCode());
		params.put("biz_type_code", form.getBizTypeCode());
		form.setSign(RopUtils.sign(params, webProperties.getProperty("rop.secret")));
	}

	private void fillRopMatchRequestForm(final RopMatchRequestForm form) {
		this.fillRopForm(form);
		final TreeMap<String, String> params = RopUtils.buildBaseParamsMap(form);
		params.put("biz_type_code", form.getBizTypeCode());
		params.put("biz_code", form.getBizCode());
		params.put("datas", form.getDatas());
		form.setSign(RopUtils.sign(params, webProperties.getProperty("rop.secret")));
	}

	private void fillRopReferenceDataRequestForm(final RopReferenceDataForm form) {
		this.fillRopForm(form);
		final TreeMap<String, String> params = RopUtils.buildBaseParamsMap(form);
		params.put("biz_type_code", form.getBizTypeCode());
		params.put("conditions", form.getConditions());
		form.setSign(RopUtils.sign(params, webProperties.getProperty("rop.secret")));
	}

	private void fillRopForm(final RopForm form) {
		form.setAppKey(webProperties.getProperty("rop.app_key"));
		form.setFormat("json");
		form.setFrom("router");
		form.setMethod("test-method");
		form.setSession("test-session");
		form.setSourceAppKey("test-source-app-key");
		form.setTimestamp("test-timestamp");
	}

	// @Test
	public void testReferenceData() {
		// prepare datas
		this.prepareDatas();

		// resources
		final RopResource ropResource = this.getResource(RopResource.class);

		// conditions
		final Map<String, String> conditions = new HashMap<String, String>();

		// json mapper
		final ObjectMapper jsonMapper = new ObjectMapper();

		// request
		final RopReferenceDataForm request = new RopReferenceDataForm();
		request.setBizTypeCode(this.bizTypeCode);

		// response
		RopResponse response;

		// test-1 status not existed
		conditions.put("name", "test-name-1");
		conditions.put("id", "test-id-1");
		conditions.put("mobileNumber", "test-mobileNumber-1");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-1");
		conditions.put("companyName", "test-companyName-1");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		// request
		response = ropResource.getReferenceData(request);
		// check
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof RopReferenceDataResponse);
		RopReferenceDataResponse pr = (RopReferenceDataResponse) response;
		Assert.assertNotNull(pr.getPending());
		Assert.assertTrue(pr.getPending().booleanValue());
		Assert.assertNull(pr.getResult());
		System.out.println("test-1 status not existed passed");

		// other requests
		conditions.put("name", "test-name-2");
		conditions.put("id", "test-id-2");
		conditions.put("mobileNumber", "test-mobileNumber-2");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-2");
		conditions.put("companyName", "test-companyName-2");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		ropResource.getReferenceData(request);
		// 3
		conditions.put("name", "test-name-3");
		conditions.put("id", "test-id-3");
		conditions.put("mobileNumber", "test-mobileNumber-3");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-3");
		conditions.put("companyName", "test-companyName-3");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		ropResource.getReferenceData(request);

		// test-2: not done and not finished
		conditions.put("name", "test-name-1");
		conditions.put("id", "test-id-1");
		conditions.put("mobileNumber", "test-mobileNumber-1");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-1");
		conditions.put("companyName", "test-companyName-1");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		// request
		response = ropResource.getReferenceData(request);
		// check
		Assert.assertNotNull(response);
		pr = (RopReferenceDataResponse) response;
		Assert.assertNotNull(pr.getPending());
		Assert.assertTrue(pr.getPending().booleanValue());
		Assert.assertNull(pr.getResult());
		System.out.println("test-2: not done and not finished passed");

		// test-3: not done and finished
		conditions.put("name", "test-name-2");
		conditions.put("id", "test-id-2");
		conditions.put("mobileNumber", "test-mobileNumber-2");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-2");
		conditions.put("companyName", "test-companyName-2");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		// request
		response = ropResource.getReferenceData(request);
		// check
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof RopReferenceDataResponse);
		RopReferenceDataResponse rr = (RopReferenceDataResponse) response;
		Assert.assertNull(rr.getPending());
		Assert.assertNotNull(rr.getResult());
		Assert.assertEquals(this.bizTypeCode, rr.getResult().getBizTypeCode());
		Assert.assertEquals("test-taskId-2", rr.getResult().getProviderTaskId());
		Assert.assertNull(rr.getResult().getResponseValue());
		Assert.assertNull(rr.getResult().getResponseRaw());
		Assert.assertNotNull(rr.getResult().getResponse());
		Assert.assertEquals(2, rr.getResult().getResponse().size());
		Assert.assertNotNull(rr.getResult().getResponse().get("table_1"));
		Assert.assertEquals(1, rr.getResult().getResponse().get("table_1").size());
		Assert.assertEquals("value-2-1-1", rr.getResult().getResponse().get("table_1").get(0).get("v1"));
		Assert.assertEquals("value-2-1-2", rr.getResult().getResponse().get("table_1").get(0).get("v2"));
		Assert.assertNotNull(rr.getResult().getResponse().get("table_2"));
		Assert.assertEquals(1, rr.getResult().getResponse().get("table_2").size());
		Assert.assertEquals("value-2-2-1", rr.getResult().getResponse().get("table_2").get(0).get("v1"));
		Assert.assertEquals("value-2-2-2", rr.getResult().getResponse().get("table_2").get(0).get("v2"));
		Assert.assertEquals("value-2-2-3", rr.getResult().getResponse().get("table_2").get(0).get("v3"));
		System.out.println("test-3: not done and finished passed");

		// next condition
		conditions.put("name", "test-name-3");
		conditions.put("id", "test-id-3");
		conditions.put("mobileNumber", "test-mobileNumber-3");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-3");
		conditions.put("companyName", "test-companyName-3");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		// request
		ropResource.getReferenceData(request);

		// test-4: done and not expired
		conditions.put("name", "test-name-3");
		conditions.put("id", "test-id-3");
		conditions.put("mobileNumber", "test-mobileNumber-3");
		conditions.put("bizLicenceNumber", "test-bizLicenceNumber-3");
		conditions.put("companyName", "test-companyName-3");
		try {
			request.setConditions(jsonMapper.writeValueAsString(conditions));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		this.fillRopReferenceDataRequestForm(request);
		// request
		response = ropResource.getReferenceData(request);
		// check
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof RopReferenceDataResponse);
		rr = (RopReferenceDataResponse) response;
		Assert.assertNull(rr.getPending());
		Assert.assertNotNull(rr.getResult());
		Assert.assertEquals(this.bizTypeCode, rr.getResult().getBizTypeCode());
		Assert.assertEquals("test-taskId-3", rr.getResult().getProviderTaskId());
		Assert.assertNull(rr.getResult().getResponseValue());
		Assert.assertNull(rr.getResult().getResponseRaw());
		Assert.assertNotNull(rr.getResult().getResponse());
		Assert.assertEquals(2, rr.getResult().getResponse().size());
		Assert.assertNotNull(rr.getResult().getResponse().get("table_1"));
		Assert.assertEquals(1, rr.getResult().getResponse().get("table_1").size());
		Assert.assertEquals("value-3-1-1", rr.getResult().getResponse().get("table_1").get(0).get("v1"));
		Assert.assertEquals("value-3-1-2", rr.getResult().getResponse().get("table_1").get(0).get("v2"));
		Assert.assertEquals("value-3-1-3", rr.getResult().getResponse().get("table_1").get(0).get("v3"));
		Assert.assertNotNull(rr.getResult().getResponse().get("table_2"));
		Assert.assertEquals(1, rr.getResult().getResponse().get("table_2").size());
		Assert.assertEquals("value-3-2-1", rr.getResult().getResponse().get("table_2").get(0).get("v1"));
		Assert.assertEquals("value-3-2-2", rr.getResult().getResponse().get("table_2").get(0).get("v2"));
		System.out.println("test-4: done and not expired passed");
	}

	@Test
	public void testMatch() {
		// prepare datas
		this.prepareDatas();

		// resources
		final RopResource ropResource = this.getResource(RopResource.class);

		// test-1: match with request datas
		// 1
		RopMatchRequestForm matchRequest = new RopMatchRequestForm();
		matchRequest.setBizTypeCode(this.bizTypeCode);
		matchRequest.setBizCode("test-biz-code-1");
		matchRequest.setDatas("{\"CERTIFICATE_NUMBER\":\"123456199912121234\"}"); // 17
		this.fillRopMatchRequestForm(matchRequest);
		// match
		RopResponse requestResponse = ropResource.match(matchRequest);
		// check match request response
		if (requestResponse instanceof RopErrorResponse) {
			Assert.fail("match request failed: " + requestResponse);
		}
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		RopMatchResultForm resultRequest = new RopMatchResultForm();
		resultRequest.setBizCode("test-biz-code-1");
		this.fillRopMatchResultRequestForm(resultRequest);
		RopResponse resultResponse = ropResource.getResults(resultRequest);
		// check result response
		if (resultResponse instanceof RopErrorResponse) {
			Assert.fail("get result failed: " + requestResponse);
		}
		RopMatchResultResponse mr = (RopMatchResultResponse) resultResponse;
		// check
		Assert.assertNotNull(mr);
		AcceptanceResults ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-1", ars.getBizCode());
		Assert.assertEquals(this.bizTypeCode, ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(this.acceptanceRuleCode, ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.REJECT, ars.getResults().get(0).getResult());
		ScoreResults srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-1", srs.getBizCode());
		Assert.assertEquals(this.bizTypeCode, srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(this.scoreRuleCode, srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(1, srs.getResults().get(0).getResult().intValue());

		// 2
		matchRequest = new RopMatchRequestForm();
		matchRequest.setBizTypeCode(this.bizTypeCode);
		matchRequest.setBizCode("test-biz-code-2");
		matchRequest.setDatas("{\"CERTIFICATE_NUMBER\":\"123456198912121234\"}"); // 27
		this.fillRopMatchRequestForm(matchRequest);
		// match
		requestResponse = ropResource.match(matchRequest);
		// check match request response
		if (requestResponse instanceof RopErrorResponse) {
			Assert.fail("match request failed: " + requestResponse);
		}
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		resultRequest = new RopMatchResultForm();
		resultRequest.setBizCode("test-biz-code-2");
		this.fillRopMatchResultRequestForm(resultRequest);
		resultResponse = ropResource.getResults(resultRequest);
		// check result response
		if (resultResponse instanceof RopErrorResponse) {
			Assert.fail("get result failed: " + requestResponse);
		}
		mr = (RopMatchResultResponse) resultResponse;
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-2", ars.getBizCode());
		Assert.assertEquals(this.bizTypeCode, ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(this.acceptanceRuleCode, ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-2", srs.getBizCode());
		Assert.assertEquals(this.bizTypeCode, srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(this.scoreRuleCode, srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(2, srs.getResults().get(0).getResult().intValue());

		// 3
		matchRequest = new RopMatchRequestForm();
		matchRequest.setBizTypeCode(this.bizTypeCode);
		matchRequest.setBizCode("test-biz-code-3");
		matchRequest.setDatas("{\"CERTIFICATE_NUMBER\":\"123456196912121234\"}"); // 47
		this.fillRopMatchRequestForm(matchRequest);
		// match
		requestResponse = ropResource.match(matchRequest);
		// check match request response
		if (requestResponse instanceof RopErrorResponse) {
			Assert.fail("match request failed: " + requestResponse);
		}
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		resultRequest = new RopMatchResultForm();
		resultRequest.setBizCode("test-biz-code-3");
		this.fillRopMatchResultRequestForm(resultRequest);
		resultResponse = ropResource.getResults(resultRequest);
		// check result response
		if (resultResponse instanceof RopErrorResponse) {
			Assert.fail("get result failed: " + requestResponse);
		}
		mr = (RopMatchResultResponse) resultResponse;
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-3", ars.getBizCode());
		Assert.assertEquals(this.bizTypeCode, ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(this.acceptanceRuleCode, ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-3", srs.getBizCode());
		Assert.assertEquals(this.bizTypeCode, srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(this.scoreRuleCode, srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(3, srs.getResults().get(0).getResult().intValue());

		// 4
		matchRequest = new RopMatchRequestForm();
		matchRequest.setBizTypeCode(this.bizTypeCode);
		matchRequest.setBizCode("test-biz-code-4");
		matchRequest.setDatas("{\"CERTIFICATE_NUMBER\":\"123456194912121234\"}"); // 67
		this.fillRopMatchRequestForm(matchRequest);
		// match
		requestResponse = ropResource.match(matchRequest);
		// check match request response
		if (requestResponse instanceof RopErrorResponse) {
			Assert.fail("match request failed: " + requestResponse);
		}
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		resultRequest = new RopMatchResultForm();
		resultRequest.setBizCode("test-biz-code-4");
		this.fillRopMatchResultRequestForm(resultRequest);
		resultResponse = ropResource.getResults(resultRequest);
		// check result response
		if (resultResponse instanceof RopErrorResponse) {
			Assert.fail("get result failed: " + requestResponse);
		}
		mr = (RopMatchResultResponse) resultResponse;
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-4", ars.getBizCode());
		Assert.assertEquals(this.bizTypeCode, ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(this.acceptanceRuleCode, ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.REJECT, ars.getResults().get(0).getResult());
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-4", srs.getBizCode());
		Assert.assertEquals(this.bizTypeCode, srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(this.scoreRuleCode, srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(1, srs.getResults().get(0).getResult().intValue());
		System.out.println("test-1: match with request datas passed");

	}

	private void prepareDatas() {
		if (this.datasPrepared) {
			return;
		}

		// resources
		final RuleResource ruleResource = this.getResource(RuleResource.class);

		// create the bizType
		final BizType bizType = new BizType();
		bizType.setCode(bizTypeCode);
		bizType.setName("测试业务类型");
		ruleResource.createBizType(bizType);

		// create the datasources
		final Datasource ds1 = new Datasource();
		ds1.setCode("datasource-1");
		ds1.setName("测试数据源1");
		ds1.setDriverClass("com.mysql.jdbc.Driver");
		ds1.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_1?useUnicode=true&characterEncoding=utf8");
		ds1.setUser("root");
		ruleResource.createDatasource(ds1);
		final Datasource ds2 = new Datasource();
		ds2.setCode("datasource-2");
		ds2.setName("测试数据源2");
		ds2.setDriverClass("com.mysql.jdbc.Driver");
		ds2.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_2?useUnicode=true&characterEncoding=utf8");
		ds2.setUser("root");
		ruleResource.createDatasource(ds2);
		final Datasource w2dDatasource = new Datasource();
		w2dDatasource.setCode("w2d-datasource");
		w2dDatasource.setName("爬虫测试数据源");
		w2dDatasource.setDriverClass("com.mysql.jdbc.Driver");
		w2dDatasource.setUrl("jdbc:mysql://localhost:3306/ruleengine_test_w2d?useUnicode=true&characterEncoding=utf8");
		w2dDatasource.setUser("root");
		ruleResource.createDatasource(w2dDatasource);

		// create the rules
		final Rule acceptanceRule = new Rule();
		acceptanceRule.setCode(this.acceptanceRuleCode);
		acceptanceRule.setName("是否通过规则");
		acceptanceRule.setBizTypeCode(bizType.getCode());
		acceptanceRule.setParams(new ArrayList<String>());
		acceptanceRule.getParams().add("CERTIFICATE_NUMBER");
		acceptanceRule
				.setExpression("var age = _plugins.IdCardNumberAgeGetter.exec(CERTIFICATE_NUMBER); if (age >= ${minAge} and age < ${maxAge}) \"ACCEPT\" else \"REJECT\"");
		acceptanceRule.setMatchType(MatchType.ACCEPTANCE);
		acceptanceRule.setDatasources(new ArrayList<Datasource>());
		acceptanceRule.getDatasources().add(ds1);
		acceptanceRule.getDatasources().add(ds2);
		acceptanceRule.setExtractSqls(new ArrayList<ExtractSql>());
		acceptanceRule.getExtractSqls().add(new ExtractSql());
		acceptanceRule.getExtractSqls().get(0).setRuleCode(acceptanceRule.getCode());
		acceptanceRule.getExtractSqls().get(0).setDatasourceCode(ds1.getCode());
		acceptanceRule.getExtractSqls().get(0).setParams(new ArrayList<String>());
		acceptanceRule.getExtractSqls().get(0).getParams().add("bizCode");
		acceptanceRule.getExtractSqls().get(0).setSql("select userId from req_table where bizCode = ?");
		acceptanceRule.getExtractSqls().get(0).setTableName("ORDER_INFO");
		acceptanceRule.getExtractSqls().get(0).setColumns(new ArrayList<String>());
		acceptanceRule.getExtractSqls().get(0).getColumns().add("USER_ID");
		acceptanceRule.getExtractSqls().get(0).setConditions(new HashMap<String, String>());
		acceptanceRule.getExtractSqls().get(0).getConditions().put("ORDER_ID", "bizCode");
		acceptanceRule.getExtractSqls().add(new ExtractSql());
		acceptanceRule.getExtractSqls().get(1).setRuleCode(acceptanceRule.getCode());
		acceptanceRule.getExtractSqls().get(1).setDatasourceCode(ds2.getCode());
		acceptanceRule.getExtractSqls().get(1).setParams(new ArrayList<String>());
		acceptanceRule.getExtractSqls().get(1).getParams().add("userId");
		acceptanceRule.getExtractSqls().get(1).setSql("select userId, age from user_table where userId = ?");
		acceptanceRule.getExtractSqls().get(1).setTableName("USER_INFO");
		acceptanceRule.getExtractSqls().get(1).setColumns(new ArrayList<String>());
		acceptanceRule.getExtractSqls().get(1).getColumns().add("CERTIFICATE_NUMBER");
		acceptanceRule.getExtractSqls().get(1).setConditions(new HashMap<String, String>());
		acceptanceRule.getExtractSqls().get(1).getConditions().put("USER_ID", "USER_ID");
		acceptanceRule.setExpressionSegments(new TreeMap<String, String>());
		acceptanceRule.getExpressionSegments().put("minAge", "18");
		acceptanceRule.getExpressionSegments().put("maxAge", "65");
		// create it
		ruleResource.createRule(acceptanceRule);
		// score rule
		final Rule scoreRule = new Rule();
		scoreRule.setCode(this.scoreRuleCode);
		scoreRule.setName("评分规则");
		scoreRule.setBizTypeCode(bizType.getCode());
		scoreRule.setParams(new ArrayList<String>());
		scoreRule.getParams().add("CERTIFICATE_NUMBER");
		scoreRule
				.setExpression("var age = _plugins.IdCardNumberAgeGetter.exec(CERTIFICATE_NUMBER); if (age < ${age-1}) 1 else if (age < ${age-2}) 2 else if (age < ${age-3}) 3 else 1");
		scoreRule.setMatchType(MatchType.SCORE);
		scoreRule.setDatasources(new ArrayList<Datasource>());
		scoreRule.getDatasources().add(ds1);
		scoreRule.getDatasources().add(ds2);
		scoreRule.setExtractSqls(new ArrayList<ExtractSql>());
		scoreRule.getExtractSqls().add(new ExtractSql());
		scoreRule.getExtractSqls().get(0).setRuleCode(scoreRule.getCode());
		scoreRule.getExtractSqls().get(0).setDatasourceCode(ds1.getCode());
		scoreRule.getExtractSqls().get(0).setParams(new ArrayList<String>());
		scoreRule.getExtractSqls().get(0).getParams().add("bizCode");
		scoreRule.getExtractSqls().get(0).setSql("select userId from req_table where bizCode = ?");
		scoreRule.getExtractSqls().get(0).setTableName("ORDER_INFO");
		scoreRule.getExtractSqls().get(0).setColumns(new ArrayList<String>());
		scoreRule.getExtractSqls().get(0).getColumns().add("USER_ID");
		scoreRule.getExtractSqls().get(0).setConditions(new HashMap<String, String>());
		scoreRule.getExtractSqls().get(0).getConditions().put("ORDER_ID", "bizCode");
		scoreRule.getExtractSqls().add(new ExtractSql());
		scoreRule.getExtractSqls().get(1).setRuleCode(scoreRule.getCode());
		scoreRule.getExtractSqls().get(1).setDatasourceCode(ds2.getCode());
		scoreRule.getExtractSqls().get(1).setParams(new ArrayList<String>());
		scoreRule.getExtractSqls().get(1).getParams().add("userId");
		scoreRule.getExtractSqls().get(1).setSql("select userId, age from user_table where userId = ?");
		scoreRule.getExtractSqls().get(1).setTableName("USER_INFO");
		scoreRule.getExtractSqls().get(1).setColumns(new ArrayList<String>());
		scoreRule.getExtractSqls().get(1).getColumns().add("CERTIFICATE_NUMBER");
		scoreRule.getExtractSqls().get(1).setConditions(new HashMap<String, String>());
		scoreRule.getExtractSqls().get(1).getConditions().put("USER_ID", "USER_ID");
		scoreRule.setExpressionSegments(new TreeMap<String, String>());
		scoreRule.getExpressionSegments().put("age-1", "23");
		scoreRule.getExpressionSegments().put("age-2", "30");
		scoreRule.getExpressionSegments().put("age-3", "50");
		// create it
		ruleResource.createRule(scoreRule);

		// ruleSet
		RuleSet ruleSet = new RuleSet();
		ruleSet.setCode("test-ruleSet");
		ruleSet.setName("test-ruleSet");
		ruleSet.setBizTypeCode(bizType.getCode());
		ruleSet.setRules(new ArrayList<Rule>());
		ruleSet.getRules().add(acceptanceRule);
		ruleSet.getRules().add(scoreRule);
		ruleResource.createRuleSet(ruleSet);
		ruleSet = ruleResource.getRuleSet(ruleSet.getCode());
		// assign ruleSet
		ruleResource.assignRuleSet(bizType.getCode(), ruleSet.getCode(), ruleSet.getVersion());

		this.datasPrepared = true;
	}

}
