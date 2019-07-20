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

import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.executor.DefaultExecutionIdGenerator;
import cn.rongcapital.ruleengine.executor.ExecutionPlugin;
import cn.rongcapital.ruleengine.executor.JexlAcceptanceExecutor;
import cn.rongcapital.ruleengine.executor.JexlBaseExecutor;
import cn.rongcapital.ruleengine.executor.plugins.IdCardNumberAgeGetter;
import cn.rongcapital.ruleengine.executor.trace.ThreadLocalExecutionTracer;
import cn.rongcapital.ruleengine.model.AcceptanceResult;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the unit test for JexlAcceptanceExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class JexlAcceptanceExecutorTest {

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
		final String expression = "var age = " + JexlBaseExecutor.PLUGINS_KEY
				+ ".IdCardNumberAgeGetter.exec(idCardNum); if (age < 18 || age > 65) \"REJECT\" else \"ACCEPT\"";
		// rule
		final Rule rule = new Rule();
		rule.setCode("test-acceptance-rule-age");
		rule.setMatchType(MatchType.ACCEPTANCE);

		// params
		final Map<String, String> params = new HashMap<String, String>();

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(new DefaultExecutionIdGenerator());

		// JexlAcceptanceExecutor
		final JexlAcceptanceExecutor e = new JexlAcceptanceExecutor();
		e.setPlugins(plugins);
		e.setExecutionTracer(executionTracer);

		// rule is null
		Assert.assertNull(e.execute(null, params));

		AcceptanceResult result = null;

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

		params.put("idCardNum", "123456187712121234");
		rule.setExpression(expression);
		// execute
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(Acceptance.ACCEPT, result.getResult());

		// age < 18
		params.put("idCardNum", "123456188312121234");
		// execute
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(Acceptance.REJECT, result.getResult());

		// age > 65
		params.put("idCardNum", "123456182912121234");
		// execute
		result = e.execute(rule, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(rule.getCode(), result.getRuleCode());
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(Acceptance.REJECT, result.getResult());

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

		// expression
		final String expression = "var age = " + JexlBaseExecutor.PLUGINS_KEY
				+ ".IdCardNumberAgeGetter.exec(idCardNum); if (age < 18 || age > 65) \"REJECT\" else \"ACCEPT\"";

		// params
		final Map<String, String> params = new HashMap<String, String>();
		params.put("idCardNum", "123456187712121234");

		// JexlAcceptanceExecutor
		final JexlAcceptanceExecutor e = new JexlAcceptanceExecutor();
		e.setPlugins(plugins);

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(new DefaultExecutionIdGenerator());
		e.setExecutionTracer(executionTracer);

		// evaluate
		AcceptanceResult result = e.evaluate(expression, null, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(Acceptance.ACCEPT, result.getResult());

		// age < 18
		params.put("idCardNum", "123456188312121234");
		// evaluate
		result = e.evaluate(expression, null, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(Acceptance.REJECT, result.getResult());

		// age > 65
		params.put("idCardNum", "123456182912121234");
		// evaluate
		result = e.evaluate(expression, null, params);
		// check
		Assert.assertNotNull(result);
		Assert.assertNull(result.getErrorMessage());
		Assert.assertNotNull(result.getResult());
		Assert.assertEquals(Acceptance.REJECT, result.getResult());

		System.out.println("testEvaluate passed");
	}

	@Test
	public void testExpressionSegments() {
		// rule
		final Rule rule = new Rule();
		rule.setCode("test-acceptance-rule-age");
		rule.setMatchType(MatchType.ACCEPTANCE);
		rule.setExpression("if (${segment-1} + ${segment-2} == 3) \"ACCEPT\" else \"REJECT\"");
		rule.setExpressionSegments(new TreeMap<String, String>());
		rule.getExpressionSegments().put("segment-1", "1");
		rule.getExpressionSegments().put("segment-2", "2");

		// JexlAcceptanceExecutor
		final JexlAcceptanceExecutor e = new JexlAcceptanceExecutor();

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer executionTracer = new ThreadLocalExecutionTracer();
		executionTracer.setExecutionIdGenerator(new DefaultExecutionIdGenerator());
		e.setExecutionTracer(executionTracer);

		// execute
		final AcceptanceResult ar = e.execute(rule, null);

		// check
		Assert.assertNotNull(ar);
		Assert.assertEquals(Acceptance.ACCEPT, ar.getResult());

		System.out.println("testExpressionParams passed");
	}

}
