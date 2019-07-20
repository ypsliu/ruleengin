/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.AcceptanceResult;
import cn.rongcapital.ruleengine.model.AcceptanceResults;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.MatchResult;
import cn.rongcapital.ruleengine.model.MatchStage;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.model.ScoreResult;
import cn.rongcapital.ruleengine.model.ScoreResults;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.ResultService;
import cn.rongcapital.ruleengine.service.RuleService;

/**
 * the ITest for ResultService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/result-itest.xml" })
public class ResultServiceITest {

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private RuleService ruleService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private TestingService testingService;

	@Test
	public void test() {
		// prepare
		final BizType b = this.bizTypeService.getSimpleBizType("biz-type-code-1");
		Rule r1 = this.ruleService.getRule("code-1"); // acceptance
		Rule r2 = this.ruleService.getRule("code-2"); // score
		final RuleSet ruleSet = this.ruleService.getRuleSet("test-ruleSet");

		// test-1: save acceptance result
		final List<AcceptanceResult> arList1 = new ArrayList<AcceptanceResult>();
		final AcceptanceResult ar1 = new AcceptanceResult();
		arList1.add(ar1);
		ar1.setParams(new HashMap<String, String>());
		ar1.getParams().put("p11", "v111");
		ar1.getParams().put("p12", "v121");
		ar1.setErrorMessage("errorMessage-1");
		ar1.setResult(Acceptance.ACCEPT);
		ar1.setRuleVersion(r1.getVersion());
		ar1.setSortId(1);
		final AcceptanceResults ars1 = new AcceptanceResults();
		ars1.setBizTypeCode(b.getCode());
		ars1.setResults(arList1);
		ars1.setBizCode("bizCode-1");
		ars1.setRuleSetCode(ruleSet.getCode());
		ars1.setRuleSetVersion(ruleSet.getVersion());
		// no bizType info
		try {
			this.resultService.saveResult(ars1);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// invalid bizType
		ars1.setBizType(new BizType());
		try {
			this.resultService.saveResult(ars1);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		ars1.setBizType(b);
		// not rule info
		try {
			this.resultService.saveResult(ars1);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// invalid rule info
		ar1.setRule(new Rule());
		try {
			this.resultService.saveResult(ars1);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// everything is OK
		ar1.setRule(r1);
		// execution id
		ar1.setExecutionId("test-execution-id-1");
		// stages
		ar1.setStages(new ArrayList<MatchStage>());
		ar1.getStages().add(new MatchStage());
		ar1.getStages().get(0).setRule(r1);
		ar1.getStages().get(0).setStageId(1);
		ar1.getStages().get(0).setParentStageId(0);
		ar1.getStages().get(0).setTraces(new ArrayList<String>());
		ar1.getStages().get(0).getTraces().add("1-1");
		ar1.getStages().get(0).getTraces().add("1-2");
		this.resultService.saveResult(ars1);
		System.out.println("test-1: save acceptance result passed");

		// test-2: save score result
		final List<ScoreResult> srList1 = new ArrayList<ScoreResult>();
		final ScoreResult sr1 = new ScoreResult();
		srList1.add(sr1);
		sr1.setParams(new HashMap<String, String>());
		sr1.getParams().put("p21", "v211");
		sr1.getParams().put("p22", "v221");
		sr1.setErrorMessage("errorMessage-2");
		sr1.setResult(1234);
		sr1.setRuleVersion(r2.getVersion());
		sr1.setSortId(1);
		final ScoreResults srs1 = new ScoreResults();
		srs1.setBizTypeCode(b.getCode());
		srs1.setResults(srList1);
		srs1.setBizCode("bizCode-1");
		srs1.setBizType(b);
		srs1.setRuleSetCode(ruleSet.getCode());
		srs1.setRuleSetVersion(ruleSet.getVersion());
		sr1.setRule(r2);
		// execution id
		sr1.setExecutionId("test-execution-id-2");
		// stages
		sr1.setStages(new ArrayList<MatchStage>());
		sr1.getStages().add(new MatchStage());
		sr1.getStages().get(0).setRule(r2);
		sr1.getStages().get(0).setStageId(1);
		sr1.getStages().get(0).setParentStageId(0);
		sr1.getStages().get(0).setTraces(new ArrayList<String>());
		sr1.getStages().get(0).getTraces().add("2-1");
		sr1.getStages().get(0).getTraces().add("2-2");
		this.resultService.saveResult(srs1);
		System.out.println("test-2: save score result passed");

		// test-3: get acceptance results
		AcceptanceResults ars = this.resultService.getAcceptanceResult(b.getCode(), "bizCode-1");
		// check
		Assert.assertNotNull(ars);
		Assert.assertEquals("bizCode-1", ars.getBizCode());
		Assert.assertEquals(b.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(r1.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertNotNull(ars.getResults().get(0).getParams());
		Assert.assertEquals("v111", ars.getResults().get(0).getParams().get("p11"));
		Assert.assertEquals("v121", ars.getResults().get(0).getParams().get("p12"));
		Assert.assertNotNull(ars.getResults().get(0).getTime());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		Assert.assertEquals(r1.getVersion(), ars.getResults().get(0).getRuleVersion());
		// execution id
		Assert.assertEquals("test-execution-id-1", ars.getResults().get(0).getExecutionId());
		// stages
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals(r1.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(r1.getName(), ars.getResults().get(0).getStages().get(0).getRuleName());
		Assert.assertEquals(r1.getComment(), ars.getResults().get(0).getStages().get(0).getRuleComment());
		Assert.assertEquals(r1.getVersion(), ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertNull(ars.getResults().get(0).getStages().get(0).getTracesValue());
		Assert.assertEquals(2, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("1-1", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("1-2", ars.getResults().get(0).getStages().get(0).getTraces().get(1));
		System.out.println("test-3: get acceptance results passed");

		// test-4: get score results
		ScoreResults srs = this.resultService.getScoreResult(b.getCode(), "bizCode-1");
		// check
		Assert.assertNotNull(srs);
		Assert.assertEquals("bizCode-1", srs.getBizCode());
		Assert.assertEquals(b.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(r2.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertNotNull(srs.getResults().get(0).getParams());
		Assert.assertEquals("v211", srs.getResults().get(0).getParams().get("p21"));
		Assert.assertEquals("v221", srs.getResults().get(0).getParams().get("p22"));
		Assert.assertNotNull(srs.getResults().get(0).getTime());
		Assert.assertEquals(1234, srs.getResults().get(0).getResult().intValue());
		Assert.assertEquals(r2.getVersion(), srs.getResults().get(0).getRuleVersion());
		// execution id
		Assert.assertEquals("test-execution-id-1", ars.getResults().get(0).getExecutionId());
		// stages
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().size());
		Assert.assertEquals(r2.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(r2.getName(), srs.getResults().get(0).getStages().get(0).getRuleName());
		Assert.assertEquals(r2.getComment(), srs.getResults().get(0).getStages().get(0).getRuleComment());
		Assert.assertEquals(r2.getVersion(), srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertNull(srs.getResults().get(0).getStages().get(0).getTracesValue());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("2-1", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("2-2", srs.getResults().get(0).getStages().get(0).getTraces().get(1));
		System.out.println("test-4: test-3: get score results passed");

		// test-5: get all results
		final MatchResult mr = this.resultService.getMatchResult(b.getCode(), "bizCode-1");
		// check
		Assert.assertNotNull(mr);
		ars = mr.getAcceptance();
		Assert.assertNotNull(ars);
		Assert.assertEquals("bizCode-1", ars.getBizCode());
		Assert.assertEquals(b.getCode(), ars.getBizTypeCode());
		Assert.assertNotNull(ars.getResults());
		Assert.assertEquals(1, ars.getResults().size());
		Assert.assertEquals(r1.getCode(), ars.getResults().get(0).getRuleCode());
		Assert.assertNotNull(ars.getResults().get(0).getParams());
		Assert.assertEquals("v111", ars.getResults().get(0).getParams().get("p11"));
		Assert.assertEquals("v121", ars.getResults().get(0).getParams().get("p12"));
		Assert.assertNotNull(ars.getResults().get(0).getTime());
		Assert.assertEquals(Acceptance.ACCEPT, ars.getResults().get(0).getResult());
		Assert.assertEquals(r1.getVersion(), ars.getResults().get(0).getRuleVersion());
		// execution id
		Assert.assertEquals("test-execution-id-1", ars.getResults().get(0).getExecutionId());
		// stages
		Assert.assertNotNull(ars.getResults().get(0).getStages());
		Assert.assertEquals(1, ars.getResults().get(0).getStages().size());
		Assert.assertEquals(r1.getCode(), ars.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(r1.getName(), ars.getResults().get(0).getStages().get(0).getRuleName());
		Assert.assertEquals(r1.getComment(), ars.getResults().get(0).getStages().get(0).getRuleComment());
		Assert.assertEquals(r1.getVersion(), ars.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(ars.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertNull(ars.getResults().get(0).getStages().get(0).getTracesValue());
		Assert.assertEquals(2, ars.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("1-1", ars.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("1-2", ars.getResults().get(0).getStages().get(0).getTraces().get(1));
		srs = mr.getScore();
		Assert.assertNotNull(srs);
		Assert.assertEquals("bizCode-1", srs.getBizCode());
		Assert.assertEquals(b.getCode(), srs.getBizTypeCode());
		Assert.assertNotNull(srs.getResults());
		Assert.assertEquals(1, srs.getResults().size());
		Assert.assertEquals(r2.getCode(), srs.getResults().get(0).getRuleCode());
		Assert.assertNotNull(srs.getResults().get(0).getParams());
		Assert.assertEquals("v211", srs.getResults().get(0).getParams().get("p21"));
		Assert.assertEquals("v221", srs.getResults().get(0).getParams().get("p22"));
		Assert.assertNotNull(srs.getResults().get(0).getTime());
		Assert.assertEquals(1234, srs.getResults().get(0).getResult().intValue());
		Assert.assertEquals(r2.getVersion(), srs.getResults().get(0).getRuleVersion());
		// execution id
		Assert.assertEquals("test-execution-id-1", ars.getResults().get(0).getExecutionId());
		// stages
		Assert.assertNotNull(srs.getResults().get(0).getStages());
		Assert.assertEquals(1, srs.getResults().get(0).getStages().size());
		Assert.assertEquals(r2.getCode(), srs.getResults().get(0).getStages().get(0).getRuleCode());
		Assert.assertEquals(r2.getName(), srs.getResults().get(0).getStages().get(0).getRuleName());
		Assert.assertEquals(r2.getComment(), srs.getResults().get(0).getStages().get(0).getRuleComment());
		Assert.assertEquals(r2.getVersion(), srs.getResults().get(0).getStages().get(0).getRuleVersion());
		Assert.assertNotNull(srs.getResults().get(0).getStages().get(0).getTraces());
		Assert.assertNull(srs.getResults().get(0).getStages().get(0).getTracesValue());
		Assert.assertEquals(2, srs.getResults().get(0).getStages().get(0).getTraces().size());
		Assert.assertEquals("2-1", srs.getResults().get(0).getStages().get(0).getTraces().get(0));
		Assert.assertEquals("2-2", srs.getResults().get(0).getStages().get(0).getTraces().get(1));
		System.out.println("test-5: get all results passed");

		// test-6: rule history
		this.ruleService.updateRule(r1);
		this.ruleService.updateRule(r2);
		r1 = this.ruleService.getRule(r1.getCode());
		r2 = this.ruleService.getRule(r2.getCode());
		ar1.setRuleVersion(r1.getVersion());
		ar1.setExecutionId("test-execution-id-3");
		ar1.getStages().get(0).setRule(r1);
		sr1.setRuleVersion(r2.getVersion());
		sr1.setExecutionId("test-execution-id-4");
		sr1.getStages().get(0).setRule(r2);
		this.resultService.saveResult(ars1);
		this.resultService.saveResult(srs1);
		final MatchResult mr2 = this.resultService.getMatchResult(b.getCode(), "bizCode-1");
		// check
		Assert.assertNotNull(mr2);
		Assert.assertNotNull(mr2.getAcceptance());
		Assert.assertNotNull(mr2.getAcceptance().getResults());
		Assert.assertEquals(r1.getVersion(), mr2.getAcceptance().getResults().get(0).getRuleVersion());
		Assert.assertEquals("test-execution-id-3", mr2.getAcceptance().getResults().get(0).getExecutionId());
		Assert.assertNotNull(mr2.getAcceptance().getResults().get(0).getStages());
		Assert.assertEquals(1, mr2.getAcceptance().getResults().get(0).getStages().size());
		Assert.assertEquals(r1.getVersion(), mr2.getAcceptance().getResults().get(0).getStages().get(0)
				.getRuleVersion());
		Assert.assertNotNull(mr2.getScore());
		Assert.assertNotNull(mr2.getScore().getResults());
		Assert.assertEquals(r2.getVersion(), mr2.getScore().getResults().get(0).getRuleVersion());
		Assert.assertEquals("test-execution-id-4", mr2.getScore().getResults().get(0).getExecutionId());
		Assert.assertNotNull(mr2.getScore().getResults().get(0).getStages());
		Assert.assertEquals(1, mr2.getScore().getResults().get(0).getStages().size());
		Assert.assertEquals(r2.getVersion(), mr2.getScore().getResults().get(0).getStages().get(0).getRuleVersion());
		System.out.println("test-6: rule history passed");
	}

	@Before
	public void setup() {
		// clear datas
		this.clearDatas();

		// test bizTypes
		final BizType b1 = new BizType();
		b1.setCode("biz-type-code-1");
		b1.setName("name-1");
		b1.setComment("comment-1");
		this.bizTypeService.createBizType(b1);

		// test rules
		List<String> params = null;

		final Rule r1 = new Rule();
		r1.setCode("code-1");
		r1.setName("name-1");
		r1.setComment("comment-1");
		r1.setBizTypeCode("biz-type-code-1");
		r1.setExpression("expression-1");
		r1.setMatchType(MatchType.ACCEPTANCE);
		r1.setExecutorClass("executorClass-1");
		params = new ArrayList<String>();
		r1.setParams(params);
		params.add("p11");
		params.add("p12");
		this.ruleService.createRule(r1);

		final Rule r2 = new Rule();
		r2.setCode("code-2");
		r2.setName("name-2");
		r2.setComment("comment-2");
		r2.setBizTypeCode("biz-type-code-1");
		r2.setExpression("expression-2");
		r2.setMatchType(MatchType.SCORE);
		r2.setExecutorClass("executorClass-2");
		params = new ArrayList<String>();
		r2.setParams(params);
		params.add("p21");
		params.add("p22");
		r2.setCode("code-2");
		this.ruleService.createRule(r2);

		// ruleSet
		final RuleSet ruleSet = new RuleSet();
		ruleSet.setCode("test-ruleSet");
		ruleSet.setName("test-ruleSet");
		ruleSet.setBizTypeCode(b1.getCode());
		ruleSet.setRules(new ArrayList<Rule>());
		ruleSet.getRules().add(r1);
		ruleSet.getRules().add(r2);
		this.ruleService.createRuleSet(ruleSet);

		// assign the ruleSet to bizType
		this.bizTypeService.assignRuleSet(b1.getCode(), ruleSet.getCode(), 1L);
	}

	@After
	public void teardown() {
		this.clearDatas();
	}

	private void clearDatas() {
		this.testingService.clearBizTypes();
		this.testingService.clearRules();
		this.testingService.clearRuleHistories();
		this.testingService.clearRuleSets();
		this.testingService.clearRuleSetHistories();
		this.testingService.clearExtractSqls();
		this.testingService.clearExtractSqlHistories();
		this.testingService.clearResults();
		this.testingService.clearMatchStages();
	}

	/**
	 * @param bizTypeService
	 *            the bizTypeService to set
	 */
	public void setBizTypeService(BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
	}

	/**
	 * @param ruleService
	 *            the ruleService to set
	 */
	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	/**
	 * @param testingService
	 *            the testingService to set
	 */
	public void setTestingService(TestingService testingService) {
		this.testingService = testingService;
	}

	/**
	 * @param resultService
	 *            the resultService to set
	 */
	public void setResultService(ResultService resultService) {
		this.resultService = resultService;
	}

}
