/**
 * 
 */
package cn.rongcapital.ruleengine.core.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.core.MatchServiceImpl;
import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.match.AcceptanceExecutor;
import cn.rongcapital.ruleengine.match.ExecutionDispatcher;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.model.AcceptanceResult;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.EvalRequest;
import cn.rongcapital.ruleengine.model.EvalResult;
import cn.rongcapital.ruleengine.model.MatchRequestData;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.model.ScoreResult;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DataExtractor;
import cn.rongcapital.ruleengine.service.RuleService;

/**
 * the unit test for MatchService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class MatchServiceTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testMatch() {
		// parameters
		final String bizCode = "test-biz-code";
		final String bizTypeCode = "test-bizType-code";
		// bizType
		final BizType bizType = new BizType();
		bizType.setCode(bizTypeCode);
		// rules
		final Rule acceptanceRule = new Rule();
		acceptanceRule.setCode("acceptanceRule");
		acceptanceRule.setMatchType(MatchType.ACCEPTANCE);
		acceptanceRule.setVersion(111L);
		final Rule scoreRule = new Rule();
		scoreRule.setCode("scoreRule");
		scoreRule.setMatchType(MatchType.SCORE);
		scoreRule.setVersion(222L);
		final List<String> acceptanceParams = new ArrayList<String>();
		acceptanceParams.add("key-1-1");
		acceptanceParams.add("key-1-2");
		acceptanceParams.add("key-2-1");
		acceptanceParams.add("key-2-2");
		acceptanceRule.setParams(acceptanceParams);
		final List<String> scoreParams = new ArrayList<String>();
		scoreParams.add("key-3-1");
		scoreParams.add("key-3-2");
		scoreParams.add("key-4-1");
		scoreParams.add("key-4-2");
		scoreRule.setParams(scoreParams);
		final List<Rule> rules = new ArrayList<Rule>();
		rules.add(acceptanceRule);
		rules.add(scoreRule);
		// ruleSet
		final RuleSet ruleSet = new RuleSet();
		ruleSet.setCode("ruleSet");
		ruleSet.setName("ruleSet");
		ruleSet.setBizTypeCode(bizTypeCode);
		ruleSet.setVersion(333);
		ruleSet.setRules(rules);
		// datas
		final Map<String, String> datas = new HashMap<String, String>();
		datas.put("key-1-1", "value-1-1");
		datas.put("key-1-2", "value-1-2");
		datas.put("key-2-1", "value-2-1");
		datas.put("key-2-2", "value-2-2");
		datas.put("key-3-1", "value-3-1");
		datas.put("key-3-2", "value-3-2");
		datas.put("key-4-1", "value-4-1");
		datas.put("key-4-2", "value-4-2");
		// results
		final AcceptanceResult acceptanceResult = new AcceptanceResult();
		acceptanceResult.setResult(Acceptance.CAREFUL);
		final ScoreResult scoreResult = new ScoreResult();
		scoreResult.setResult(1234);

		// request
		final MatchRequestData request = new MatchRequestData();
		request.setBizCode(bizCode);
		request.setBizTypeCode(bizTypeCode);
		request.setDatas(datas);

		// mocks
		final BizTypeService bizTypeService = Mockito.mock(BizTypeService.class);
		final RuleService ruleService = Mockito.mock(RuleService.class);
		final ExecutionDispatcher executionDispatcher = Mockito.mock(ExecutionDispatcher.class);
		final DataExtractor dataExtractor = Mockito.mock(DataExtractor.class);

		// MatchService
		final MatchServiceImpl ms = new MatchServiceImpl();
		// set the mocks
		ms.setBizTypeService(bizTypeService);
		ms.setRuleService(ruleService);
		ms.setExecutionDispatcher(executionDispatcher);
		ms.setDataExtractor(dataExtractor);

		// test-1: no bizType
		try {
			ms.match(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: no bizType passed");

		// test-2: no rules
		Mockito.when(bizTypeService.getBizType(bizTypeCode)).thenReturn(bizType);
		try {
			ms.match(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-2: no rules passed");

		// test-4: match by request
		bizType.setRuleSetCode(ruleSet.getCode());
		bizType.setRuleSetVersion(ruleSet.getVersion());
		bizType.setRuleSet(ruleSet);
		// match by request
		ms.match(request);

		// wait the async match
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}

		// check
		Mockito.verify(bizTypeService, Mockito.times(3)).getBizType(bizTypeCode);
		Mockito.verify(ruleService, Mockito.never()).getRulesByBizType(bizTypeCode);
		Mockito.verify(executionDispatcher).dispatch(Mockito.anyString(), Mockito.any(RuleSet.class), Mockito.anyMap());
		Mockito.verify(ruleService, Mockito.never()).getRuleSet(Mockito.anyString(), Mockito.anyLong());
		System.out.println("test-4: match by request passed");

		// test-5: match by codes
		// mock dataExtractor.extractRuleDatas(Rule)
		Mockito.when(dataExtractor.extractRuleDatas(Mockito.any(Rule.class), Mockito.eq(bizCode))).then(
				new Answer<Map<String, String>>() {

					@Override
					public Map<String, String> answer(InvocationOnMock invocation) throws Throwable {
						final Rule rule = (Rule) invocation.getArguments()[0];
						final Map<String, String> params = new HashMap<String, String>();
						if (acceptanceRule.getCode().equals(rule.getCode())) {
							// acceptanceParams
							params.put("key-1-1", "value-1-1");
							params.put("key-1-2", "value-1-2");
							params.put("key-2-1", "value-2-1");
							params.put("key-2-2", "value-2-2");
						} else {
							// scoreParams
							params.put("key-3-1", "value-3-1");
							params.put("key-3-2", "value-3-2");
							params.put("key-4-1", "value-4-1");
							params.put("key-4-2", "value-4-2");
						}
						return params;
					}

				});

		// match by codes
		ms.match(bizTypeCode, bizCode);

		// wait the async match
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}

		// check
		Mockito.verify(bizTypeService, Mockito.times(4)).getBizType(bizTypeCode);
		Mockito.verify(ruleService, Mockito.never()).getRulesByBizType(bizTypeCode);
		Mockito.verify(executionDispatcher, Mockito.times(2)).dispatch(Mockito.anyString(), Mockito.any(RuleSet.class),
				Mockito.anyMap());
		Mockito.verify(dataExtractor, Mockito.times(2)).extractRuleDatas(Mockito.any(Rule.class), Mockito.eq(bizCode));
		Mockito.verify(ruleService, Mockito.never()).getRuleSet(Mockito.anyString(), Mockito.anyLong());
		System.out.println("test-5: match by codes passed");

		// test-6: request with ruleSet
		// mock ruleService.getRulesByBizType()
		Mockito.when(ruleService.getRuleSet(ruleSet.getCode(), ruleSet.getVersion())).thenReturn(ruleSet);
		// request
		request.setRuleSetCode(ruleSet.getCode());
		request.setRuleSetVersion(ruleSet.getVersion());
		// match by request
		ms.match(request);
		// wait the async match
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		// check
		Mockito.verify(bizTypeService, Mockito.times(5)).getBizType(bizTypeCode);
		Mockito.verify(ruleService, Mockito.never()).getRulesByBizType(bizTypeCode);
		Mockito.verify(executionDispatcher, Mockito.times(3)).dispatch(Mockito.anyString(), Mockito.any(RuleSet.class),
				Mockito.anyMap());
		Mockito.verify(dataExtractor, Mockito.times(2)).extractRuleDatas(Mockito.any(Rule.class), Mockito.eq(bizCode));
		Mockito.verify(ruleService, Mockito.times(1)).getRuleSet(Mockito.anyString(), Mockito.anyLong());
		System.out.println("test-6: request with ruleSet passed");

		// test-7: codes with ruleSet
		Mockito.when(bizTypeService.getSimpleBizType(bizTypeCode)).thenReturn(bizType);
		// match by codes
		ms.match(bizTypeCode, bizCode, ruleSet.getCode(), ruleSet.getVersion());
		// wait the async match
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		// check
		Mockito.verify(bizTypeService, Mockito.times(5)).getBizType(bizTypeCode);
		Mockito.verify(bizTypeService, Mockito.times(1)).getSimpleBizType(bizTypeCode);
		Mockito.verify(ruleService, Mockito.never()).getRulesByBizType(bizTypeCode);
		Mockito.verify(executionDispatcher, Mockito.times(4)).dispatch(Mockito.anyString(), Mockito.any(RuleSet.class),
				Mockito.anyMap());
		Mockito.verify(dataExtractor, Mockito.times(4)).extractRuleDatas(Mockito.any(Rule.class), Mockito.eq(bizCode));
		Mockito.verify(ruleService, Mockito.times(2)).getRuleSet(Mockito.anyString(), Mockito.anyLong());
		System.out.println("test-7: codes with ruleSet passed");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testEval() {
		// parameters
		final String bizCode = "test-biz-code";
		final String acceptanceExpression = "acceptanceExpression";
		final String scoreExpression = "scoreExpression";
		final Rule acceptanceRule = new Rule();
		acceptanceRule.setCode("acceptanceRule");
		acceptanceRule.setMatchType(MatchType.ACCEPTANCE);
		final Rule scoreRule = new Rule();
		scoreRule.setCode("scoreRule");
		scoreRule.setMatchType(MatchType.SCORE);
		final List<String> acceptanceParams = new ArrayList<String>();
		acceptanceParams.add("key-1-1");
		acceptanceParams.add("key-1-2");
		acceptanceParams.add("key-2-1");
		acceptanceParams.add("key-2-2");
		acceptanceRule.setParams(acceptanceParams);
		acceptanceRule.setExpressionSegments(new HashMap<String, String>());
		acceptanceRule.getExpressionSegments().put("s-1", "v-1");
		final List<String> scoreParams = new ArrayList<String>();
		scoreParams.add("key-3-1");
		scoreParams.add("key-3-2");
		scoreParams.add("key-4-1");
		scoreParams.add("key-4-2");
		scoreRule.setParams(scoreParams);
		scoreRule.setExpressionSegments(new HashMap<String, String>());
		scoreRule.getExpressionSegments().put("s-2", "v-2");
		final List<Rule> rules = new ArrayList<Rule>();
		rules.add(acceptanceRule);
		rules.add(scoreRule);
		// datas
		final Map<String, String> datas = new HashMap<String, String>();
		datas.put("key-1-1", "value-1-1");
		datas.put("key-1-2", "value-1-2");
		datas.put("key-2-1", "value-2-1");
		datas.put("key-2-2", "value-2-2");
		datas.put("key-3-1", "value-3-1");
		datas.put("key-3-2", "value-3-2");
		datas.put("key-4-1", "value-4-1");
		datas.put("key-4-2", "value-4-2");
		// results
		final AcceptanceResult acceptanceResult = new AcceptanceResult();
		acceptanceResult.setResult(Acceptance.CAREFUL);
		final ScoreResult scoreResult = new ScoreResult();
		scoreResult.setResult(1234);

		// mocks
		final AcceptanceExecutor acceptanceExecutor = Mockito.mock(AcceptanceExecutor.class);
		final ScoreExecutor scoreExecutor = Mockito.mock(ScoreExecutor.class);

		// MatchService
		final MatchServiceImpl ms = new MatchServiceImpl();
		// set the mocks
		ms.setAcceptanceExecutor(acceptanceExecutor);
		ms.setScoreExecutor(scoreExecutor);

		// test-1: request is null
		try {
			ms.evaluate(null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: request is null passed");

		// test-2: expression is null
		final EvalRequest req = new EvalRequest();
		req.setBizCode(bizCode);
		req.setMatchType(MatchType.ACCEPTANCE);
		req.setParams(datas);
		try {
			ms.evaluate(req);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-2: expression is null passed");

		// test-3: acceptance
		final EvalRequest acceptanceRequest = new EvalRequest();
		acceptanceRequest.setRuleExpression(acceptanceExpression);
		acceptanceRequest.setExpressionSegments(new HashMap<String, String>());
		acceptanceRequest.getExpressionSegments().put("s-1", "v-1");
		acceptanceRequest.setMatchType(MatchType.ACCEPTANCE);
		acceptanceRequest.setBizCode(bizCode);
		acceptanceRequest.setParams(datas);
		// mock acceptanceExecutor.evaluate()
		Mockito.when(acceptanceExecutor.evaluate(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenAnswer(

		new Answer<AcceptanceResult>() {

			@Override
			public AcceptanceResult answer(final InvocationOnMock invocation) throws Throwable {
				// check
				final String expression = (String) invocation.getArguments()[0];
				Assert.assertNotNull(expression);
				Assert.assertEquals(acceptanceExpression, expression);
				// segments
				final Map<String, String> segments = (Map<String, String>) invocation.getArguments()[1];
				Assert.assertNotNull(segments);
				Assert.assertEquals(1, segments.size());
				Assert.assertEquals("v-1", segments.get("s-1"));
				// params
				final Map<String, String> params = (Map<String, String>) invocation.getArguments()[2];
				Assert.assertNotNull(params);
				Assert.assertEquals(8, params.size());
				Assert.assertEquals("value-1-1", params.get("key-1-1"));
				Assert.assertEquals("value-1-2", params.get("key-1-2"));
				Assert.assertEquals("value-2-1", params.get("key-2-1"));
				Assert.assertEquals("value-2-2", params.get("key-2-2"));
				Thread.sleep(5);
				return acceptanceResult;
			}

		});
		// evaluate
		final EvalResult acceptanceEvalResult = ms.evaluate(acceptanceRequest);
		// check
		Assert.assertNotNull(acceptanceEvalResult);
		Assert.assertEquals(bizCode, acceptanceEvalResult.getBizCode());
		Assert.assertNull(acceptanceEvalResult.getErrorMessage());
		Assert.assertEquals(acceptanceResult.getResult().toString(), acceptanceEvalResult.getResult());
		Assert.assertTrue(acceptanceEvalResult.getTime() > 0);
		Mockito.verify(acceptanceExecutor).evaluate(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap());
		System.out.println("test-3: acceptance passed");

		// test-4: score
		final EvalRequest scoreRequest = new EvalRequest();
		scoreRequest.setRuleExpression(scoreExpression);
		scoreRequest.setExpressionSegments(new HashMap<String, String>());
		scoreRequest.getExpressionSegments().put("s-2", "v-2");
		scoreRequest.setMatchType(MatchType.SCORE);
		scoreRequest.setBizCode(bizCode);
		scoreRequest.setParams(datas);
		// mock scoreExecutor.execute()
		Mockito.when(scoreExecutor.evaluate(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenAnswer(

		new Answer<ScoreResult>() {

			@Override
			public ScoreResult answer(final InvocationOnMock invocation) throws Throwable {
				// check
				final String expression = (String) invocation.getArguments()[0];
				Assert.assertNotNull(expression);
				Assert.assertEquals(scoreExpression, expression);
				// segments
				final Map<String, String> segments = (Map<String, String>) invocation.getArguments()[1];
				Assert.assertNotNull(segments);
				Assert.assertEquals(1, segments.size());
				Assert.assertEquals("v-2", segments.get("s-2"));
				// params
				final Map<String, String> params = (Map<String, String>) invocation.getArguments()[2];
				Assert.assertNotNull(params);
				Assert.assertEquals(8, params.size());
				Assert.assertEquals("value-3-1", params.get("key-3-1"));
				Assert.assertEquals("value-3-2", params.get("key-3-2"));
				Assert.assertEquals("value-4-1", params.get("key-4-1"));
				Assert.assertEquals("value-4-2", params.get("key-4-2"));
				Thread.sleep(5);
				return scoreResult;
			}

		});
		// evaluate
		final EvalResult scoreEvalResult = ms.evaluate(scoreRequest);
		// check
		Assert.assertNotNull(scoreEvalResult);
		Assert.assertEquals(bizCode, scoreEvalResult.getBizCode());
		Assert.assertNull(scoreEvalResult.getErrorMessage());
		Assert.assertEquals(scoreResult.getResult().toString(), scoreEvalResult.getResult());
		Assert.assertTrue(scoreEvalResult.getTime() > 0);
		Mockito.verify(scoreExecutor).evaluate(Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap());
		System.out.println("test-4: score passed");
	}

}
