/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.executor.DefaultExecutionIdGenerator;
import cn.rongcapital.ruleengine.executor.ExecutionPlugin;
import cn.rongcapital.ruleengine.executor.JexlBaseExecutor;
import cn.rongcapital.ruleengine.executor.JexlScoreExecutor;
import cn.rongcapital.ruleengine.executor.plugins.IdCardNumberAgeGetter;
import cn.rongcapital.ruleengine.executor.trace.ThreadLocalExecutionTracer;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.ScoreResult;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the unit test for JexlScoreExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class JexlScoreExecutorTest {

	@Test
	public void testExecute() {
		// date format
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// mock the DatetimeProvider
		final DatetimeProvider datetimeProvider = Mockito.mock(DatetimeProvider.class);
		try {
			Mockito.when(datetimeProvider.nowTime()).thenReturn(format.parse("1899-12-12"));
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}

		// execution plugins
		final List<ExecutionPlugin> plugins = new ArrayList<ExecutionPlugin>();
		final IdCardNumberAgeGetter icnag = new IdCardNumberAgeGetter();
		icnag.setDatetimeProvider(datetimeProvider);
		plugins.add(icnag);

		// expression
		final String expression = "var age = "
				+ JexlBaseExecutor.PLUGINS_KEY
				+ ".IdCardNumberAgeGetter.exec(idCardNum); if (age < 23) 1 else if (age < 30) 2 else if (age < 50) 3 else 1";
		// rule
		final Rule rule = new Rule();
		rule.setCode("test-score-rule-age");
		rule.setMatchType(MatchType.SCORE);

		// params
		final Map<String, String> params = new HashMap<String, String>();

		// JexlScoreExecutor
		final JexlScoreExecutor e = new JexlScoreExecutor();
		e.setPlugins(plugins);

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(new DefaultExecutionIdGenerator());
		e.setExecutionTracer(executionTracer);

		// rule is null
		Assert.assertNull(e.execute(null, params));

		ScoreResult result = null;

		// expression is null
		rule.setExpression(null);
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getResult());
		Assert.assertNotNull(result.getErrorMessage());
		System.out.println(">> expression is null: " + result.getErrorMessage());

		// invalid expression
		rule.setExpression("if age > 5 then xxx");
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getResult());
		Assert.assertNotNull(result.getErrorMessage());
		System.out.println(">> invalid expression: " + result.getErrorMessage());

		// no params
		rule.setExpression(expression);
		result = e.execute(rule, null);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getResult());
		Assert.assertNotNull(result.getErrorMessage());
		System.out.println(">> no params: " + result.getErrorMessage());

		// invalid params
		rule.setExpression(expression);
		params.put("age1", "41");
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getResult());
		Assert.assertNotNull(result.getErrorMessage());
		System.out.println(">> invalid params: " + result.getErrorMessage());

		// execute
		rule.setExpression(expression);
		params.put("idCardNum", "123456185812121234");
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(3, result.getResult().intValue());

		// age < 23
		params.put("idCardNum", "123456187712121234");
		// execute
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(1, result.getResult().intValue());

		// age < 30
		params.put("idCardNum", "123456187112121234");
		// execute
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(2, result.getResult().intValue());

		// age > 50
		params.put("idCardNum", "123456184012121234");
		// execute
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(1, result.getResult().intValue());

		System.out.println("testExecute passed");
	}

	@Test
	public void testEvaluate() {
		// date format
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// mock the DatetimeProvider
		final DatetimeProvider datetimeProvider = Mockito.mock(DatetimeProvider.class);
		try {
			Mockito.when(datetimeProvider.nowTime()).thenReturn(format.parse("1899-12-12"));
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}

		// execution plugins
		final List<ExecutionPlugin> plugins = new ArrayList<ExecutionPlugin>();
		final IdCardNumberAgeGetter icnag = new IdCardNumberAgeGetter();
		icnag.setDatetimeProvider(datetimeProvider);
		plugins.add(icnag);

		final String expression = "var age = "
				+ JexlBaseExecutor.PLUGINS_KEY
				+ ".IdCardNumberAgeGetter.exec(idCardNum); if (age < 23) 1 else if (age < 30) 2 else if (age < 50) 3 else 1";

		// params
		final Map<String, String> params = new HashMap<String, String>();
		params.put("idCardNum", "123456185812121234");

		// JexlScoreExecutor
		final JexlScoreExecutor e = new JexlScoreExecutor();
		e.setPlugins(plugins);

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(new DefaultExecutionIdGenerator());
		e.setExecutionTracer(executionTracer);

		// evaluate
		ScoreResult result = e.evaluate(expression, null, params);

		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(3, result.getResult().intValue());

		// age < 23
		params.put("idCardNum", "123456187712121234");
		// evaluate
		result = e.evaluate(expression, null, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(1, result.getResult().intValue());

		// age < 30
		params.put("idCardNum", "123456187112121234");
		// evaluate
		result = e.evaluate(expression, null, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(2, result.getResult().intValue());

		// age > 50
		params.put("idCardNum", "123456184012121234");
		// evaluate
		result = e.evaluate(expression, null, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(1, result.getResult().intValue());

		System.out.println("testEvaluate passed");
	}

	@Test
	public void testExpressionSegments() {
		// rule
		final Rule rule = new Rule();
		rule.setCode("test-score-rule-age");
		rule.setMatchType(MatchType.SCORE);
		rule.setExpression("${segment-1} + ${segment-2}");
		rule.setExpressionSegments(new TreeMap<String, String>());
		rule.getExpressionSegments().put("segment-1", "1");
		rule.getExpressionSegments().put("segment-2", "2");

		// JexlScoreExecutor
		final JexlScoreExecutor e = new JexlScoreExecutor();

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(new DefaultExecutionIdGenerator());
		e.setExecutionTracer(executionTracer);

		// execute
		final ScoreResult sr = e.execute(rule, null);

		// check
		Assert.assertNotNull(sr);
		Assert.assertEquals(3, sr.getResult().intValue());

		System.out.println("testExpressionParams passed");
	}

}
