/**
 * 
 */
package cn.rongcapital.ruleengine.web.webitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.api.MatchResource;
import cn.rongcapital.ruleengine.api.ResultResource;
import cn.rongcapital.ruleengine.api.RuleResource;
import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.model.AcceptanceResults;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.EvalRequest;
import cn.rongcapital.ruleengine.model.EvalResult;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.MatchRequestData;
import cn.rongcapital.ruleengine.model.MatchResult;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.model.ScoreResults;

/**
 * all biz web ITest
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class AllBizWebITest extends BaseWebITest {

	@Test
	public void test() {
		// resources
		final RuleResource ruleResource = this.getResource(RuleResource.class);
		final MatchResource matchResource = this.getResource(MatchResource.class);
		final ResultResource resultResource = this.getResource(ResultResource.class);

		// create the bizType
		final BizType bizType = new BizType();
		bizType.setCode("test-biz-type");
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

		// create the rules
		final Rule acceptanceRule = new Rule();
		acceptanceRule.setCode("acceptance-rule");
		acceptanceRule.setName("是否通过规则");
		acceptanceRule.setBizTypeCode(bizType.getCode());
		acceptanceRule.setParams(new ArrayList<String>());
		acceptanceRule.getParams().add("CERTIFICATE_NUMBER");
		acceptanceRule.setExpression("var age = _plugins.IdCardNumberAgeGetter.exec(CERTIFICATE_NUMBER); "
				+ " if (age >= ${minAge} and age < ${maxAge}) { tracer.trace('年龄在合法范围之内'); "
				+ " return 'ACCEPT'; } else { tracer.trace('年龄不合法'); return 'REJECT'; }");
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
		// evaluate it
		final EvalRequest acceptanceEvalRequest = new EvalRequest();
		acceptanceEvalRequest.setMatchType(MatchType.ACCEPTANCE);
		acceptanceEvalRequest.setRuleExpression(acceptanceRule.getExpression());
		acceptanceEvalRequest.setExpressionSegments(new HashMap<String, String>());
		acceptanceEvalRequest.getExpressionSegments().put("minAge", "18");
		acceptanceEvalRequest.getExpressionSegments().put("maxAge", "65");
		acceptanceEvalRequest.setParams(new HashMap<String, String>());
		acceptanceEvalRequest.getParams().put("CERTIFICATE_NUMBER", "123456198812121234"); // 28
		final EvalResult acceptanceEvalResult = matchResource.evaluate(acceptanceEvalRequest);
		// check
		Assert.assertNotNull(acceptanceEvalResult);
		Assert.assertNull(acceptanceEvalResult.getErrorMessage());
		Assert.assertEquals(Acceptance.ACCEPT.toString(), acceptanceEvalResult.getResult());
		// create
		ruleResource.createRule(acceptanceRule);
		// score rule
		final Rule scoreRule = new Rule();
		scoreRule.setCode("score-rule");
		scoreRule.setName("评分规则");
		scoreRule.setBizTypeCode(bizType.getCode());
		scoreRule.setParams(new ArrayList<String>());
		scoreRule.getParams().add("CERTIFICATE_NUMBER");
		scoreRule.setExpression("var age = _plugins.IdCardNumberAgeGetter.exec(CERTIFICATE_NUMBER); "
				+ " if (rules.call('acceptance-rule', 1) != 'ACCEPT') { tracer.trace('年龄不合法'); return 0; }; "
				+ " if (age < ${age-1}) { tracer.trace('年龄小于${age-1}'); return 1; } "
				+ " else if (age < ${age-2}) { tracer.trace('年龄小于${age-2}'); return 2; } "
				+ " else if (age < ${age-3}) { tracer.trace('年龄小于${age-3}'); return 3; } "
				+ " else { tracer.trace('年龄在其他范围内'); return 1; }");
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
		// evaluate it
		final EvalRequest scoreEvalRequest = new EvalRequest();
		scoreEvalRequest.setMatchType(MatchType.SCORE);
		scoreEvalRequest.setRuleExpression(scoreRule.getExpression());
		scoreEvalRequest.setExpressionSegments(new HashMap<String, String>());
		scoreEvalRequest.getExpressionSegments().put("age-1", "23");
		scoreEvalRequest.getExpressionSegments().put("age-2", "30");
		scoreEvalRequest.getExpressionSegments().put("age-3", "50");
		scoreEvalRequest.setParams(new HashMap<String, String>());
		scoreEvalRequest.getParams().put("CERTIFICATE_NUMBER", "123456196812121234"); // 48
		final EvalResult scoreEvalResult = matchResource.evaluate(scoreEvalRequest);
		// check
		Assert.assertNotNull(scoreEvalResult);
		Assert.assertNull(scoreEvalResult.getErrorMessage());
		Assert.assertEquals("3", scoreEvalResult.getResult());
		// create
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

		// test-1: match with request datas
		// 1
		MatchRequestData request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-1");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456199912121234"); // 17
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		MatchResult mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-1");
		// check
		Assert.assertNotNull(mr);
		AcceptanceResults ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-1", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.REJECT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("REJECT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		ScoreResults srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-1", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(0, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("0", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("REJECT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(1).getTraces().get(0));

		// 2
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-2");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456198912121234"); // 27
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-2");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-2", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("ACCEPT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-2", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(2, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("2", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄小于30", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("ACCEPT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", srs.getResults().get(0).getStages().get(1).getTraces().get(0));

		// 3
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-3");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456196912121234"); // 47
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-3");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-3", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("ACCEPT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-3", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(3, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("3", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄小于50", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("ACCEPT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", srs.getResults().get(0).getStages().get(1).getTraces().get(0));

		// 4
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-4");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456194912121234"); // 67
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-4");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-4", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.REJECT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("REJECT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-4", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(0, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("0", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("REJECT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(1).getTraces().get(0));
		System.out.println("test-1: match with request datas passed");

		// test-2: match with ruleSet
		// 1
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-1");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456199912121234"); // 17
		request.setRuleSetCode(ruleSet.getCode());
		request.setRuleSetVersion(ruleSet.getVersion());
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-1");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-1", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.REJECT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("REJECT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-1", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(0, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("0", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("REJECT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(1).getTraces().get(0));

		// 2
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-2");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456198912121234"); // 27
		request.setRuleSetCode(ruleSet.getCode());
		request.setRuleSetVersion(ruleSet.getVersion());
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-2");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-2", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("ACCEPT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-2", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(2, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("2", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄小于30", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("ACCEPT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", srs.getResults().get(0).getStages().get(1).getTraces().get(0));

		// 3
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-3");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456196912121234"); // 47
		request.setRuleSetCode(ruleSet.getCode());
		request.setRuleSetVersion(ruleSet.getVersion());
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-3");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-3", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("ACCEPT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-3", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(3, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("3", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄小于50", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("ACCEPT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄在合法范围之内", srs.getResults().get(0).getStages().get(1).getTraces().get(0));

		// 4
		request = new MatchRequestData();
		request.setBizTypeCode(bizType.getCode());
		request.setBizCode("test-biz-code-4");
		request.setDatas(new HashMap<String, String>());
		request.getDatas().put("CERTIFICATE_NUMBER", "123456194912121234"); // 67
		request.setRuleSetCode(ruleSet.getCode());
		request.setRuleSetVersion(ruleSet.getVersion());
		// match
		matchResource.match(request);
		// get match results
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//
		}
		mr = resultResource.getMatchResult(bizType.getCode(), "test-biz-code-4");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("test-biz-code-4", ars.getBizCode());
		Assert.assertEquals(bizType.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertEquals(Acceptance.REJECT, ars.getResults().get(0).getResult());
		// check stage
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertNotNull(ars.getResults().get(0).getExecutionId());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals("REJECT", ars.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, ars.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("test-biz-code-4", srs.getBizCode());
		Assert.assertEquals(bizType.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertEquals(0, srs.getResults().get(0).getResult().intValue());
		// check stage
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertNotNull(srs.getResults().get(0).getExecutionId());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().size());
		Assert.assertEquals("0", srs.getResults().get(0).getStages().get(0).getResult());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getStageId());
		Assert.assertEquals(0, srs.getResults().get(0).getStages().get(0).getParentStageId());
		Assert.assertEquals(scoreRule.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("REJECT", srs.getResults().get(0).getStages().get(1).getResult());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(1).getStageId());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getParentStageId());
		Assert.assertEquals(acceptanceRule.getCode(), srs.getResults().get(0).getStages().get(1).getRuleCode());
		Assert.assertEquals(1L, srs.getResults().get(0).getStages().get(1).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(1).getTraces());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().get(1).getTraces().size());
		Assert.assertEquals("年龄不合法", srs.getResults().get(0).getStages().get(1).getTraces().get(0));
		System.out.println("test-2: match with ruleSet passed");
	}

}
