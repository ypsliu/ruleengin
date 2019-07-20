/**
 * 
 */
package cn.rongcapital.ruleengine.core.test;

import cn.rongcapital.ruleengine.core.LocalRuleSetExecutor;
import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.match.AcceptanceExecutor;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.model.*;
import cn.rongcapital.ruleengine.service.ResultService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the unit test for LocalRuleSetExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class LocalRuleSetExecutorTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
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

		// LocalRuleSetExecutor
		final LocalRuleSetExecutor ruleSetExecutor = new LocalRuleSetExecutor();

		// test-1: validation
		try {
			ruleSetExecutor.execute(null, null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		try {
			ruleSetExecutor.execute(bizCode, null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		try {
			ruleSetExecutor.execute(bizCode, new RuleSet(), null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		try {
			ruleSetExecutor.execute(bizCode, ruleSet, null);
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// wait the async match
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		System.out.println("test-1: validation passed");

		// mocks
		final AcceptanceExecutor acceptanceExecutor = Mockito.mock(AcceptanceExecutor.class);
		final ScoreExecutor scoreExecutor = Mockito.mock(ScoreExecutor.class);
		final ResultService resultService = Mockito.mock(ResultService.class);
//		ruleSetExecutor.setAcceptanceExecutor(acceptanceExecutor);
//		ruleSetExecutor.setScoreExecutor(scoreExecutor);
//		ruleSetExecutor.setResultService(resultService); FIXME

		// mock acceptanceExecutor.execute()
		Mockito.when(acceptanceExecutor.execute(Mockito.any(Rule.class), Mockito.anyMap())).thenAnswer(

		new Answer<AcceptanceResult>() {

			@Override
			public AcceptanceResult answer(final InvocationOnMock invocation) throws Throwable {
				// check
				final Rule rule = (Rule) invocation.getArguments()[0];
				Assert.assertNotNull(rule);
				Assert.assertEquals(acceptanceRule.getCode(), rule.getCode());
				Assert.assertEquals(acceptanceRule.getVersion(), rule.getVersion());
				final Map<String, String> params = (Map<String, String>) invocation.getArguments()[1];
				Assert.assertNotNull(params);
				Assert.assertEquals(8, params.size());
				Assert.assertEquals("value-1-1", params.get("key-1-1"));
				Assert.assertEquals("value-1-2", params.get("key-1-2"));
				Assert.assertEquals("value-2-1", params.get("key-2-1"));
				Assert.assertEquals("value-2-2", params.get("key-2-2"));
				Assert.assertEquals("value-3-1", params.get("key-3-1"));
				Assert.assertEquals("value-3-2", params.get("key-3-2"));
				Assert.assertEquals("value-4-1", params.get("key-4-1"));
				Assert.assertEquals("value-4-2", params.get("key-4-2"));
				System.out.println("acceptanceExecutor.execute() invoked");
				return acceptanceResult;
			}

		});
		// mock scoreExecutor.execute()
		Mockito.when(scoreExecutor.execute(Mockito.any(Rule.class), Mockito.anyMap())).thenAnswer(

		new Answer<ScoreResult>() {

			@Override
			public ScoreResult answer(final InvocationOnMock invocation) throws Throwable {
				// check
				final Rule rule = (Rule) invocation.getArguments()[0];
				Assert.assertNotNull(rule);
				Assert.assertEquals(scoreRule.getCode(), rule.getCode());
				Assert.assertEquals(scoreRule.getVersion(), rule.getVersion());
				final Map<String, String> params = (Map<String, String>) invocation.getArguments()[1];
				Assert.assertNotNull(params);
				Assert.assertEquals(8, params.size());
				Assert.assertEquals("value-1-1", params.get("key-1-1"));
				Assert.assertEquals("value-1-2", params.get("key-1-2"));
				Assert.assertEquals("value-2-1", params.get("key-2-1"));
				Assert.assertEquals("value-2-2", params.get("key-2-2"));
				Assert.assertEquals("value-3-1", params.get("key-3-1"));
				Assert.assertEquals("value-3-2", params.get("key-3-2"));
				Assert.assertEquals("value-4-1", params.get("key-4-1"));
				Assert.assertEquals("value-4-2", params.get("key-4-2"));
				System.out.println("scoreExecutor.execute() invoked");
				return scoreResult;
			}

		});
		// mock resultService.saveResult(AcceptanceResults)
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				final AcceptanceResults result = (AcceptanceResults) invocation.getArguments()[0];
				Assert.assertNotNull(result);
				Assert.assertEquals(bizCode, result.getBizCode());
				Assert.assertEquals(bizTypeCode, result.getBizTypeCode());
				Assert.assertNotNull(result.getResults());
				Assert.assertEquals(1, result.getResults().size());
				Assert.assertEquals(Acceptance.CAREFUL, result.getResults().get(0).getResult());
				Assert.assertEquals(acceptanceRule.getVersion(), result.getResults().get(0).getRuleVersion());
				System.out.println("resultService.saveResult(AcceptanceResults) invoked");
				return null;
			}

		}).when(resultService).saveResult(Mockito.any(AcceptanceResults.class));
		// mock resultService.saveResult(ScoreResults)
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				final ScoreResults result = (ScoreResults) invocation.getArguments()[0];
				Assert.assertNotNull(result);
				Assert.assertEquals(bizCode, result.getBizCode());
				Assert.assertEquals(bizTypeCode, result.getBizTypeCode());
				Assert.assertNotNull(result.getResults());
				Assert.assertEquals(1, result.getResults().size());
				Assert.assertEquals(1234, result.getResults().get(0).getResult().intValue());
				Assert.assertEquals(scoreRule.getVersion(), result.getResults().get(0).getRuleVersion());
				System.out.println("resultService.saveResult(AcceptanceResults) invoked");
				return null;
			}

		}).when(resultService).saveResult(Mockito.any(ScoreResults.class));

		// test-2: execution
		ruleSetExecutor.execute(bizCode, ruleSet, datas);
		// wait the async match
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		// check
		Mockito.verify(acceptanceExecutor).execute(Mockito.any(), Mockito.anyMap());
		Mockito.verify(scoreExecutor).execute(Mockito.any(), Mockito.anyMap());
		Mockito.verify(resultService).saveResult(Mockito.any(AcceptanceResults.class));
		Mockito.verify(resultService).saveResult(Mockito.any(ScoreResults.class));
		System.out.println("test-2: execution passed");
	}

}
