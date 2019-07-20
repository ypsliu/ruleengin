/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.executor.DefaultExecutionIdGenerator;
import cn.rongcapital.ruleengine.executor.ExecutionIdGenerator;
import cn.rongcapital.ruleengine.executor.ExecutionTracer;
import cn.rongcapital.ruleengine.executor.JexlAcceptanceExecutor;
import cn.rongcapital.ruleengine.executor.JexlScoreExecutor;
import cn.rongcapital.ruleengine.executor.RuleCallerImpl;
import cn.rongcapital.ruleengine.executor.trace.ThreadLocalExecutionTracer;
import cn.rongcapital.ruleengine.match.AcceptanceExecutor;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.match.TextExecutor;
import cn.rongcapital.ruleengine.model.AcceptanceResult;
import cn.rongcapital.ruleengine.model.MatchStage;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.ScoreResult;
import cn.rongcapital.ruleengine.model.TextResult;
import cn.rongcapital.ruleengine.service.RuleService;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;
import cn.rongcapital.ruleengine.utils.LocalDatetimeProvider;

/**
 * the unit test for RuleCaller
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class RuleCallerTest {

	@Test
	public void testSimple() {
		// mock RuleService
		final RuleService ruleService = Mockito.mock(RuleService.class);

		// mock ExecutionTracer
		final ExecutionTracer executionTracer = Mockito.mock(ExecutionTracer.class);

		// mock AcceptanceExecutor
		final AcceptanceExecutor acceptanceExecutor = Mockito.mock(AcceptanceExecutor.class);

		// mock ScoreExecutor
		final ScoreExecutor scoreExecutor = Mockito.mock(ScoreExecutor.class);
		// mock TextExecutor
		final TextExecutor textExecutor = Mockito.mock(TextExecutor.class);

		// RuleCaller
		final RuleCallerImpl c = new RuleCallerImpl();
		c.setRuleService(ruleService);
		c.setExecutionTracer(executionTracer);
		c.setAcceptanceExecutor(acceptanceExecutor);
		c.setScoreExecutor(scoreExecutor);
		c.setTextExecutor(textExecutor);

		// test-1: rule not found
		Mockito.when(ruleService.getRule(Mockito.anyString(), Mockito.anyLong())).thenThrow(new NotFoundException());
		try {
			c.call("xxx", 1);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: rule not found passed");

		// test-2: unsupported match type
		Mockito.reset(ruleService);
		Rule rule = new Rule();
		rule.setMatchType(MatchType.CUSTOM);
		Mockito.when(ruleService.getRule(Mockito.anyString(), Mockito.anyLong())).thenReturn(rule);
		try {
			c.call("xxx", 1);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-2: unsupported match type passed");

		// rule
		rule.setCode("123");
		// parameters
		final Map<String, String> params = new HashMap<String, String>();
		params.put("key-1", "value-1");
		params.put("key-2", "value-2");

		Mockito.when(executionTracer.getParams()).thenReturn(params);

		// test-3: acceptance rule
		rule.setMatchType(MatchType.ACCEPTANCE);
		Mockito.when(acceptanceExecutor.execute(Mockito.any(Rule.class), Mockito.anyMapOf(String.class, String.class)))
				.thenAnswer(

				new Answer<AcceptanceResult>() {

					@SuppressWarnings("unchecked")
					@Override
					public AcceptanceResult answer(InvocationOnMock invocation) throws Throwable {
						Assert.assertNotNull(invocation.getArguments()[0]);
						Assert.assertNotNull(invocation.getArguments()[1]);
						final Rule r = (Rule) invocation.getArguments()[0];
						Assert.assertEquals(rule.getCode(), r.getCode());
						final Map<String, String> m = (Map<String, String>) invocation.getArguments()[1];
						Assert.assertEquals(params, m);
						final AcceptanceResult result = new AcceptanceResult();
						result.setResult(Acceptance.ACCEPT);
						return result;
					}

				});
		Object result = c.call(rule.getCode(), 1);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(Acceptance.ACCEPT, result);
		Mockito.verify(executionTracer).getParams();
		Mockito.verify(ruleService, Mockito.times(2)).getRule(Mockito.anyString(), Mockito.anyLong());
		Mockito.verify(acceptanceExecutor).execute(Mockito.any(Rule.class),
				Mockito.anyMapOf(String.class, String.class));
		System.out.println("test-3: acceptance rule passed");

		// test-4: score rule
		rule.setMatchType(MatchType.SCORE);
		Mockito.when(scoreExecutor.execute(Mockito.any(Rule.class), Mockito.anyMapOf(String.class, String.class)))
				.thenAnswer(

				new Answer<ScoreResult>() {

					@SuppressWarnings("unchecked")
					@Override
					public ScoreResult answer(InvocationOnMock invocation) throws Throwable {
						Assert.assertNotNull(invocation.getArguments()[0]);
						Assert.assertNotNull(invocation.getArguments()[1]);
						final Rule r = (Rule) invocation.getArguments()[0];
						Assert.assertEquals(rule.getCode(), r.getCode());
						final Map<String, String> m = (Map<String, String>) invocation.getArguments()[1];
						Assert.assertEquals(params, m);
						final ScoreResult result = new ScoreResult();
						result.setResult(5);
						return result;
					}

				});
		result = c.call(rule.getCode(), 1);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(5, result);
		Mockito.verify(executionTracer, Mockito.times(2)).getParams();
		Mockito.verify(ruleService, Mockito.times(3)).getRule(Mockito.anyString(), Mockito.anyLong());
		Mockito.verify(scoreExecutor).execute(Mockito.any(Rule.class), Mockito.anyMapOf(String.class, String.class));
		System.out.println("test-4: score rule passed");

		// test-5: text rule
		rule.setMatchType(MatchType.TEXT);
		Mockito.when(textExecutor.execute(Mockito.any(Rule.class), Mockito.anyMapOf(String.class, String.class)))
				.thenAnswer(

				new Answer<TextResult>() {

					@SuppressWarnings("unchecked")
					@Override
					public TextResult answer(InvocationOnMock invocation) throws Throwable {
						Assert.assertNotNull(invocation.getArguments()[0]);
						Assert.assertNotNull(invocation.getArguments()[1]);
						final Rule r = (Rule) invocation.getArguments()[0];
						Assert.assertEquals(rule.getCode(), r.getCode());
						final Map<String, String> m = (Map<String, String>) invocation.getArguments()[1];
						Assert.assertEquals(params, m);
						final TextResult result = new TextResult();
						result.setResult("test-result");
						return result;
					}

				});
		result = c.call(rule.getCode(), 1);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals("test-result", result);
		Mockito.verify(executionTracer, Mockito.times(3)).getParams();
		Mockito.verify(ruleService, Mockito.times(4)).getRule(Mockito.anyString(), Mockito.anyLong());
		Mockito.verify(textExecutor).execute(Mockito.any(Rule.class), Mockito.anyMapOf(String.class, String.class));
		System.out.println("test-4: score rule passed");
	}

	@Test
	public void testWithScript() {
		// params
		final Map<String, String> params = new HashMap<String, String>();

		// checkAge
		final Rule checkAge = new Rule();
		checkAge.setCode("checkAge");
		checkAge.setVersion(1L);
		checkAge.setMatchType(MatchType.ACCEPTANCE);
		checkAge.setExpression("if (age >= 18 && age <= 65) { tracer.trace('age is OK'); return 'ACCEPT'; } "
				+ " else { tracer.trace('age out of range'); return 'REJECT'; }");

		// ageScore
		final Rule ageScore = new Rule();
		ageScore.setCode("ageScore");
		ageScore.setVersion(2L);
		ageScore.setMatchType(MatchType.SCORE);
		ageScore.setExpression("if (rules.call('checkAge', 1) == 'ACCEPT') { tracer.trace('age is OK'); "
				+ " if (age >= 30 && age <= 50) { tracer.trace('good age'); return 5; } else { "
				+ " tracer.trace('bad age'); return 1; } } else { tracer.trace('invalid age'); return 0; }");

		// testRule
		final Rule testRule = new Rule();
		testRule.setCode("testRule");
		testRule.setVersion(3L);
		testRule.setMatchType(MatchType.SCORE);
		testRule.setExpression("if (rules.call('checkAge', 1) == 'ACCEPT') { tracer.trace('age accepted'); "
				+ " return rules.call('ageScore', 2) * 2; } else { tracer.trace('age not accepted'); return 0; }");

		// mock RuleService
		final RuleService ruleService = Mockito.mock(RuleService.class);
		Mockito.when(ruleService.getRule(checkAge.getCode(), checkAge.getVersion())).thenReturn(checkAge);
		Mockito.when(ruleService.getRule(ageScore.getCode(), ageScore.getVersion())).thenReturn(ageScore);

		// DatetimeProvider
		final DatetimeProvider datetimeProvider = new LocalDatetimeProvider();

		// ExecutionIdGenerator
		final ExecutionIdGenerator executionIdGenerator = new DefaultExecutionIdGenerator();

		// ExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(executionIdGenerator);

		// JexlScoreExecutor
		final JexlScoreExecutor scoreExecutor = new JexlScoreExecutor();
		scoreExecutor.setExecutionTracer(executionTracer);
		scoreExecutor.setDatetimeProvider(datetimeProvider);

		// JexlAcceptanceExecutor
		final JexlAcceptanceExecutor acceptanceExecutor = new JexlAcceptanceExecutor();
		acceptanceExecutor.setExecutionTracer(executionTracer);
		acceptanceExecutor.setDatetimeProvider(datetimeProvider);

		// RuleCaller
		final RuleCallerImpl ruleCaller = new RuleCallerImpl();
		ruleCaller.setExecutionTracer(executionTracer);
		ruleCaller.setRuleService(ruleService);
		ruleCaller.setAcceptanceExecutor(acceptanceExecutor);
		ruleCaller.setScoreExecutor(scoreExecutor);

		// set the caller
		scoreExecutor.setRuleCaller(ruleCaller);

		MatchStage stage = null;

		// test-1: check age failed
		params.put("age", "15");
		ScoreResult result = scoreExecutor.execute(testRule, params);
		// check
		Assert.assertEquals(0, result.getResult().intValue());
		Assert.assertNotNull(result.getExecutionId());
		Assert.assertNotNull(result.getStages());
		Assert.assertEquals(2, result.getStages().size());
		// stage-1: testRule
		stage = result.getStages().get(0);
		Assert.assertEquals(1, stage.getStageId());
		Assert.assertEquals(0, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(testRule.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age not accepted", stage.getTraces().get(0));
		// stage-2: checkAge
		stage = result.getStages().get(1);
		Assert.assertEquals(2, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(checkAge.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age out of range", stage.getTraces().get(0));
		System.out.println("test-1: check age failed passed");

		// test-2: age is OK, but it is bad
		params.put("age", "28");
		result = scoreExecutor.execute(testRule, params);
		// check
		Assert.assertEquals(2, result.getResult().intValue());
		Assert.assertNotNull(result.getExecutionId());
		Assert.assertNotNull(result.getStages());
		Assert.assertEquals(4, result.getStages().size());
		// stage-1: testRule
		stage = result.getStages().get(0);
		Assert.assertEquals(1, stage.getStageId());
		Assert.assertEquals(0, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(testRule.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age accepted", stage.getTraces().get(0));
		// stage-2: checkAge
		stage = result.getStages().get(1);
		Assert.assertEquals(2, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(checkAge.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age is OK", stage.getTraces().get(0));
		// stage-3: ageScore
		stage = result.getStages().get(2);
		Assert.assertEquals(3, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(ageScore.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("age is OK", stage.getTraces().get(0));
		Assert.assertEquals("bad age", stage.getTraces().get(1));
		// stage-4: checkAge
		stage = result.getStages().get(3);
		Assert.assertEquals(4, stage.getStageId());
		Assert.assertEquals(3, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(checkAge.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age is OK", stage.getTraces().get(0));
		System.out.println("test-2: age is OK, but it is bad passed");

		// test-3: age is OK, and it is good
		params.put("age", "38");
		result = scoreExecutor.execute(testRule, params);
		// check
		Assert.assertEquals(10, result.getResult().intValue());
		Assert.assertNotNull(result.getExecutionId());
		Assert.assertNotNull(result.getStages());
		Assert.assertEquals(4, result.getStages().size());
		// stage-1: testRule
		stage = result.getStages().get(0);
		Assert.assertEquals(1, stage.getStageId());
		Assert.assertEquals(0, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(testRule.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age accepted", stage.getTraces().get(0));
		// stage-2: checkAge
		stage = result.getStages().get(1);
		Assert.assertEquals(2, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(checkAge.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age is OK", stage.getTraces().get(0));
		// stage-3: ageScore
		stage = result.getStages().get(2);
		Assert.assertEquals(3, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(ageScore.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("age is OK", stage.getTraces().get(0));
		Assert.assertEquals("good age", stage.getTraces().get(1));
		// stage-4: checkAge
		stage = result.getStages().get(3);
		Assert.assertEquals(4, stage.getStageId());
		Assert.assertEquals(3, stage.getParentStageId());
		Assert.assertNotNull(stage.getRule());
		Assert.assertEquals(checkAge.getCode(), stage.getRule().getCode());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(1, stage.getTraces().size());
		Assert.assertEquals("age is OK", stage.getTraces().get(0));
		System.out.println("test-3: age is OK, and it is good passed");
	}

}
