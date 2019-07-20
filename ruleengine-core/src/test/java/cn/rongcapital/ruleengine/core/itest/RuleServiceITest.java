/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.DuplicateException;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.RuleService;

/**
 * the ITest for RuleService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/rule-itest.xml" })
public class RuleServiceITest {

	@Autowired
	private TestingService testingService;

	@Autowired
	private RuleService ruleService;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private DatasourceManager datasourceManager;

	@Test
	public void test() {
		List<String> params = null;
		// test-1: create the rule
		final Rule r1 = new Rule();
		r1.setCode("code-1");
		r1.setName("name-1");
		r1.setComment("comment-1");
		r1.setBizTypeCode("biz-type-code-1");
		r1.setExpression("expression-1");
		r1.setMatchType(MatchType.ACCEPTANCE);
		r1.setExecutorClass("executorClass-1");
		r1.setDatasources(new ArrayList<Datasource>());
		r1.getDatasources().add(new Datasource());
		r1.getDatasources().get(0).setCode("datasource-code-1");
		params = new ArrayList<String>();
		params.add("p11");
		params.add("p12");
		r1.setParams(params);
		r1.setExtractSqls(new ArrayList<ExtractSql>());
		r1.getExtractSqls().add(new ExtractSql());
		r1.getExtractSqls().get(0).setRuleCode(r1.getCode());
		r1.getExtractSqls().get(0).setDatasourceCode("datasource-code-1");
		r1.getExtractSqls().get(0).setParams(new ArrayList<String>());
		r1.getExtractSqls().get(0).getParams().add("sql-param-1-1-1");
		r1.getExtractSqls().get(0).getParams().add("sql-param-1-1-2");
		r1.getExtractSqls().get(0).setSql("sql-1-1");
		r1.getExtractSqls().get(0).setTableName("table-1-1");
		r1.getExtractSqls().get(0).setColumns(new ArrayList<String>());
		r1.getExtractSqls().get(0).getColumns().add("column-1-1-1");
		r1.getExtractSqls().get(0).getColumns().add("column-1-1-2");
		r1.getExtractSqls().get(0).setConditions(new HashMap<String, String>());
		r1.getExtractSqls().get(0).getConditions().put("column-1-1-1", "column-1-1-1-value");
		r1.getExtractSqls().get(0).getConditions().put("column-1-1-2", "column-1-1-2-value");
		r1.getExtractSqls().add(new ExtractSql());
		r1.getExtractSqls().get(1).setRuleCode(r1.getCode());
		r1.getExtractSqls().get(1).setDatasourceCode("datasource-code-1");
		r1.getExtractSqls().get(1).setParams(new ArrayList<String>());
		r1.getExtractSqls().get(1).getParams().add("sql-param-1-2-1");
		r1.getExtractSqls().get(1).getParams().add("sql-param-1-2-2");
		r1.getExtractSqls().get(1).setSql("sql-1-2");
		r1.getExtractSqls().get(1).setTableName("table-1-2");
		r1.getExtractSqls().get(1).setColumns(new ArrayList<String>());
		r1.getExtractSqls().get(1).getColumns().add("column-1-2-1");
		r1.getExtractSqls().get(1).getColumns().add("column-1-2-2");
		r1.getExtractSqls().get(1).setConditions(new HashMap<String, String>());
		r1.getExtractSqls().get(1).getConditions().put("column-1-2-1", "column-1-2-1-value");
		r1.getExtractSqls().get(1).getConditions().put("column-1-2-2", "column-1-2-2-value");
		r1.setExpressionSegments(new TreeMap<String, String>());
		r1.getExpressionSegments().put("key-1-1", "value-1-1");
		r1.getExpressionSegments().put("key-1-2", "value-1-2");
		this.ruleService.createRule(r1);
		final Rule r2 = new Rule();
		r2.setCode("code-1");
		r2.setName("name-2");
		r2.setComment("comment-2");
		r2.setBizTypeCode("biz-type-code-1");
		r2.setExpression("expression-2");
		r2.setMatchType(MatchType.SCORE);
		r2.setExecutorClass("executorClass-2");
		r2.setDatasources(new ArrayList<Datasource>());
		r2.getDatasources().add(new Datasource());
		r2.getDatasources().get(0).setCode("datasource-code-2");
		params = new ArrayList<String>();
		params.add("p21");
		params.add("p22");
		r2.setParams(params);
		r2.setExtractSqls(new ArrayList<ExtractSql>());
		r2.getExtractSqls().add(new ExtractSql());
		r2.getExtractSqls().get(0).setRuleCode(r2.getCode());
		r2.getExtractSqls().get(0).setDatasourceCode("datasource-code-2");
		r2.getExtractSqls().get(0).setParams(new ArrayList<String>());
		r2.getExtractSqls().get(0).getParams().add("sql-param-2-1-1");
		r2.getExtractSqls().get(0).getParams().add("sql-param-2-1-2");
		r2.getExtractSqls().get(0).setSql("sql-2-1");
		r2.getExtractSqls().get(0).setTableName("table-2-1");
		r2.getExtractSqls().get(0).setColumns(new ArrayList<String>());
		r2.getExtractSqls().get(0).getColumns().add("column-2-1-1");
		r2.getExtractSqls().get(0).getColumns().add("column-2-1-2");
		r2.getExtractSqls().get(0).setConditions(new HashMap<String, String>());
		r2.getExtractSqls().get(0).getConditions().put("column-2-1-1", "column-2-1-1-value");
		r2.getExtractSqls().get(0).getConditions().put("column-2-1-2", "column-2-1-2-value");
		r2.getExtractSqls().add(new ExtractSql());
		r2.getExtractSqls().get(1).setRuleCode(r2.getCode());
		r2.getExtractSqls().get(1).setDatasourceCode("datasource-code-2");
		r2.getExtractSqls().get(1).setParams(new ArrayList<String>());
		r2.getExtractSqls().get(1).getParams().add("sql-param-2-2-1");
		r2.getExtractSqls().get(1).getParams().add("sql-param-2-2-2");
		r2.getExtractSqls().get(1).setSql("sql-2-2");
		r2.getExtractSqls().get(1).setTableName("table-2-2");
		r2.getExtractSqls().get(1).setColumns(new ArrayList<String>());
		r2.getExtractSqls().get(1).getColumns().add("column-2-2-1");
		r2.getExtractSqls().get(1).getColumns().add("column-2-2-2");
		r2.getExtractSqls().get(1).setConditions(new HashMap<String, String>());
		r2.getExtractSqls().get(1).getConditions().put("column-2-2-1", "column-2-2-1-value");
		r2.getExtractSqls().get(1).getConditions().put("column-2-2-2", "column-2-2-2-value");
		r2.setExpressionSegments(new TreeMap<String, String>());
		r2.getExpressionSegments().put("key-2-1", "value-2-1");
		r2.getExpressionSegments().put("key-2-2", "value-2-2");
		// duplicate code
		try {
			this.ruleService.createRule(r2);
			Assert.fail("why no exception thrown?");
		} catch (DuplicateException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		r2.setCode("code-2");
		this.ruleService.createRule(r2);
		final Rule r3 = new Rule();
		r3.setCode("code-3");
		r3.setName("name-3");
		r3.setComment("comment-3");
		r3.setBizTypeCode("biz-type-code-xxxxx");
		r3.setExpression("expression-3");
		r3.setMatchType(MatchType.SCORE);
		r3.setExecutorClass("executorClass-3");
		params = new ArrayList<String>();
		params.add("p31");
		params.add("p32");
		r3.setParams(params);
		// bizType is not existed
		try {
			this.ruleService.createRule(r3);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		r3.setBizTypeCode("biz-type-code-2");
		// datasource code is not existed
		r3.setDatasources(new ArrayList<Datasource>());
		r3.getDatasources().add(new Datasource());
		r3.getDatasources().get(0).setCode("xxxx");
		try {
			this.ruleService.createRule(r3);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		r3.getDatasources().get(0).setCode("datasource-code-2");
		this.ruleService.createRule(r3);
		System.out.println("test-1: create the rule passed");

		// test-2: get rule
		final Rule r11 = this.ruleService.getRule(r1.getCode());
		// check
		Assert.assertNotNull(r11);
		Assert.assertEquals(1L, r11.getVersion());
		Assert.assertEquals(r1.getCode(), r11.getCode());
		Assert.assertEquals(r1.getName(), r11.getName());
		Assert.assertEquals(r1.getComment(), r11.getComment());
		Assert.assertNotNull(r11.getCreationTime());
		Assert.assertNull(r11.getUpdateTime());
		Assert.assertEquals(r1.getBizTypeCode(), r11.getBizTypeCode());
		Assert.assertNotNull(r11.getBizType());
		Assert.assertEquals(r1.getExpression(), r11.getExpression());
		Assert.assertEquals(r1.getMatchType(), r11.getMatchType());
		Assert.assertEquals(r1.getExecutorClass(), r11.getExecutorClass());
		Assert.assertNull(r11.getInputParams());
		Assert.assertNotNull(r11.getParams());
		Assert.assertEquals(2, r11.getParams().size());
		Assert.assertEquals("p11", r11.getParams().get(0));
		Assert.assertEquals("p12", r11.getParams().get(1));
		Assert.assertNull(r11.getDatasourceCodes());
		Assert.assertNotNull(r11.getDatasources());
		Assert.assertEquals(1, r11.getDatasources().size());
		Assert.assertEquals("datasource-code-1", r11.getDatasources().get(0).getCode());
		Assert.assertNotNull(r11.getExtractSqls());
		Assert.assertEquals(2, r11.getExtractSqls().size());

		Assert.assertEquals(r11.getCode(), r11.getExtractSqls().get(0).getRuleCode());
		Assert.assertEquals("datasource-code-1", r11.getExtractSqls().get(0).getDatasourceCode());
		Assert.assertNull(r11.getExtractSqls().get(0).getInputParams());
		Assert.assertEquals("sql-1-1", r11.getExtractSqls().get(0).getSql());
		Assert.assertEquals(2, r11.getExtractSqls().get(0).getParams().size());
		Assert.assertEquals("sql-param-1-1-1", r11.getExtractSqls().get(0).getParams().get(0));
		Assert.assertEquals("sql-param-1-1-2", r11.getExtractSqls().get(0).getParams().get(1));
		Assert.assertEquals("table-1-1", r11.getExtractSqls().get(0).getTableName());
		Assert.assertNull(r11.getExtractSqls().get(0).getColumnsValue());
		Assert.assertNotNull(r11.getExtractSqls().get(0).getColumns());
		Assert.assertEquals(2, r11.getExtractSqls().get(0).getColumns().size());
		Assert.assertEquals("column-1-1-1", r11.getExtractSqls().get(0).getColumns().get(0));
		Assert.assertEquals("column-1-1-2", r11.getExtractSqls().get(0).getColumns().get(1));
		Assert.assertNull(r11.getExtractSqls().get(0).getConditionsValue());
		Assert.assertNotNull(r11.getExtractSqls().get(0).getConditions());
		Assert.assertEquals(2, r11.getExtractSqls().get(0).getConditions().size());
		Assert.assertEquals("column-1-1-1-value", r11.getExtractSqls().get(0).getConditions().get("column-1-1-1"));
		Assert.assertEquals("column-1-1-2-value", r11.getExtractSqls().get(0).getConditions().get("column-1-1-2"));

		Assert.assertEquals(r11.getCode(), r11.getExtractSqls().get(1).getRuleCode());
		Assert.assertEquals("datasource-code-1", r11.getExtractSqls().get(1).getDatasourceCode());
		Assert.assertNull(r11.getExtractSqls().get(1).getInputParams());
		Assert.assertEquals("sql-1-2", r11.getExtractSqls().get(1).getSql());
		Assert.assertEquals(2, r11.getExtractSqls().get(1).getParams().size());
		Assert.assertEquals("sql-param-1-2-1", r11.getExtractSqls().get(1).getParams().get(0));
		Assert.assertEquals("sql-param-1-2-2", r11.getExtractSqls().get(1).getParams().get(1));
		Assert.assertEquals("table-1-2", r11.getExtractSqls().get(1).getTableName());
		Assert.assertNull(r11.getExtractSqls().get(1).getColumnsValue());
		Assert.assertNotNull(r11.getExtractSqls().get(1).getColumns());
		Assert.assertEquals(2, r11.getExtractSqls().get(1).getColumns().size());
		Assert.assertEquals("column-1-2-1", r11.getExtractSqls().get(1).getColumns().get(0));
		Assert.assertEquals("column-1-2-2", r11.getExtractSqls().get(1).getColumns().get(1));
		Assert.assertNull(r11.getExtractSqls().get(1).getConditionsValue());
		Assert.assertNotNull(r11.getExtractSqls().get(1).getConditions());
		Assert.assertEquals(2, r11.getExtractSqls().get(1).getConditions().size());
		Assert.assertEquals("column-1-2-1-value", r11.getExtractSqls().get(1).getConditions().get("column-1-2-1"));
		Assert.assertEquals("column-1-2-2-value", r11.getExtractSqls().get(1).getConditions().get("column-1-2-2"));

		Assert.assertNull(r11.getExprSegments());
		Assert.assertNotNull(r11.getExpressionSegments());
		Assert.assertEquals(2, r11.getExpressionSegments().size());
		Assert.assertEquals("value-1-1", r11.getExpressionSegments().get("key-1-1"));
		Assert.assertEquals("value-1-2", r11.getExpressionSegments().get("key-1-2"));

		final Rule r21 = this.ruleService.getRule(r2.getCode());
		// check
		Assert.assertNotNull(r21);
		Assert.assertEquals(1L, r21.getVersion());
		Assert.assertEquals(r2.getCode(), r21.getCode());
		Assert.assertEquals(r2.getName(), r21.getName());
		Assert.assertEquals(r2.getComment(), r21.getComment());
		Assert.assertNotNull(r21.getCreationTime());
		Assert.assertNull(r21.getUpdateTime());
		Assert.assertEquals(r2.getBizTypeCode(), r21.getBizTypeCode());
		Assert.assertNotNull(r21.getBizType());
		Assert.assertEquals(r2.getExpression(), r21.getExpression());
		Assert.assertEquals(r2.getMatchType(), r21.getMatchType());
		Assert.assertEquals(r2.getExecutorClass(), r21.getExecutorClass());
		Assert.assertNull(r21.getInputParams());
		Assert.assertNotNull(r21.getParams());
		Assert.assertEquals(2, r21.getParams().size());
		Assert.assertEquals("p21", r21.getParams().get(0));
		Assert.assertEquals("p22", r21.getParams().get(1));
		Assert.assertNull(r21.getDatasourceCodes());
		Assert.assertNotNull(r21.getDatasources());
		Assert.assertEquals(1, r21.getDatasources().size());
		Assert.assertEquals("datasource-code-2", r21.getDatasources().get(0).getCode());
		Assert.assertNotNull(r21.getExtractSqls());
		Assert.assertEquals(2, r21.getExtractSqls().size());

		Assert.assertEquals(r21.getCode(), r21.getExtractSqls().get(0).getRuleCode());
		Assert.assertEquals("datasource-code-2", r21.getExtractSqls().get(0).getDatasourceCode());
		Assert.assertNull(r21.getExtractSqls().get(0).getInputParams());
		Assert.assertEquals("sql-2-1", r21.getExtractSqls().get(0).getSql());
		Assert.assertEquals(2, r21.getExtractSqls().get(0).getParams().size());
		Assert.assertEquals("sql-param-2-1-1", r21.getExtractSqls().get(0).getParams().get(0));
		Assert.assertEquals("sql-param-2-1-2", r21.getExtractSqls().get(0).getParams().get(1));
		Assert.assertEquals("table-2-1", r21.getExtractSqls().get(0).getTableName());
		Assert.assertNull(r21.getExtractSqls().get(0).getColumnsValue());
		Assert.assertNotNull(r21.getExtractSqls().get(0).getColumns());
		Assert.assertEquals(2, r21.getExtractSqls().get(0).getColumns().size());
		Assert.assertEquals("column-2-1-1", r21.getExtractSqls().get(0).getColumns().get(0));
		Assert.assertEquals("column-2-1-2", r21.getExtractSqls().get(0).getColumns().get(1));
		Assert.assertNull(r21.getExtractSqls().get(0).getConditionsValue());
		Assert.assertNotNull(r21.getExtractSqls().get(0).getConditions());
		Assert.assertEquals(2, r21.getExtractSqls().get(0).getConditions().size());
		Assert.assertEquals("column-2-1-1-value", r21.getExtractSqls().get(0).getConditions().get("column-2-1-1"));
		Assert.assertEquals("column-2-1-2-value", r21.getExtractSqls().get(0).getConditions().get("column-2-1-2"));

		Assert.assertEquals(r21.getCode(), r21.getExtractSqls().get(1).getRuleCode());
		Assert.assertEquals("datasource-code-2", r21.getExtractSqls().get(1).getDatasourceCode());
		Assert.assertNull(r21.getExtractSqls().get(1).getInputParams());
		Assert.assertEquals("sql-2-2", r21.getExtractSqls().get(1).getSql());
		Assert.assertEquals(2, r21.getExtractSqls().get(1).getParams().size());
		Assert.assertEquals("sql-param-2-2-1", r21.getExtractSqls().get(1).getParams().get(0));
		Assert.assertEquals("sql-param-2-2-2", r21.getExtractSqls().get(1).getParams().get(1));
		Assert.assertEquals("table-2-2", r21.getExtractSqls().get(1).getTableName());
		Assert.assertNull(r21.getExtractSqls().get(1).getColumnsValue());
		Assert.assertNotNull(r21.getExtractSqls().get(1).getColumns());
		Assert.assertEquals(2, r21.getExtractSqls().get(1).getColumns().size());
		Assert.assertEquals("column-2-2-1", r21.getExtractSqls().get(1).getColumns().get(0));
		Assert.assertEquals("column-2-2-2", r21.getExtractSqls().get(1).getColumns().get(1));
		Assert.assertNull(r21.getExtractSqls().get(1).getConditionsValue());
		Assert.assertNotNull(r21.getExtractSqls().get(1).getConditions());
		Assert.assertEquals(2, r21.getExtractSqls().get(1).getConditions().size());
		Assert.assertEquals("column-2-2-1-value", r21.getExtractSqls().get(1).getConditions().get("column-2-2-1"));
		Assert.assertEquals("column-2-2-2-value", r21.getExtractSqls().get(1).getConditions().get("column-2-2-2"));

		Assert.assertNotNull(r21.getExpressionSegments());
		Assert.assertEquals(2, r21.getExpressionSegments().size());
		Assert.assertEquals("value-2-1", r21.getExpressionSegments().get("key-2-1"));
		Assert.assertEquals("value-2-2", r21.getExpressionSegments().get("key-2-2"));
		System.out.println("test-2: get rule passed");

		// test-3: get rules by bizType
		List<Rule> list1 = this.ruleService.getRulesByBizType(r1.getBizTypeCode());
		List<Rule> list2 = this.ruleService.getRulesByBizType(r3.getBizTypeCode());
		// check
		Assert.assertNotNull(list1);
		int checked = 0;
		for (final Rule r : list1) {
			if (r1.getCode().equals(r.getCode())) {
				Assert.assertEquals(1L, r.getVersion());
				Assert.assertNotNull(r.getBizType());
				Assert.assertNull(r.getInputParams());
				Assert.assertNotNull(r.getParams());
				Assert.assertEquals(2, r.getParams().size());
				Assert.assertEquals("p11", r.getParams().get(0));
				Assert.assertEquals("p12", r.getParams().get(1));
				Assert.assertNull(r.getDatasourceCodes());
				Assert.assertNotNull(r.getDatasources());
				Assert.assertEquals(1, r.getDatasources().size());
				Assert.assertEquals("datasource-code-1", r.getDatasources().get(0).getCode());
				Assert.assertNotNull(r.getExpressionSegments());
				Assert.assertEquals(2, r.getExpressionSegments().size());
				Assert.assertEquals("value-1-1", r.getExpressionSegments().get("key-1-1"));
				Assert.assertEquals("value-1-2", r.getExpressionSegments().get("key-1-2"));
				checked++;
			} else if (r2.getCode().equals(r.getCode())) {
				Assert.assertEquals(1L, r.getVersion());
				Assert.assertNotNull(r.getBizType());
				Assert.assertNull(r.getInputParams());
				Assert.assertNotNull(r.getParams());
				Assert.assertEquals(2, r.getParams().size());
				Assert.assertEquals("p21", r.getParams().get(0));
				Assert.assertEquals("p22", r.getParams().get(1));
				Assert.assertNull(r.getDatasourceCodes());
				Assert.assertNotNull(r.getDatasources());
				Assert.assertEquals(1, r.getDatasources().size());
				Assert.assertEquals("datasource-code-2", r.getDatasources().get(0).getCode());
				Assert.assertNotNull(r.getExpressionSegments());
				Assert.assertEquals(2, r.getExpressionSegments().size());
				Assert.assertEquals("value-2-1", r.getExpressionSegments().get("key-2-1"));
				Assert.assertEquals("value-2-2", r.getExpressionSegments().get("key-2-2"));
				checked++;
			}
		}
		Assert.assertEquals(2, checked);
		Assert.assertNotNull(list2);
		checked = 0;
		for (final Rule r : list2) {
			if (r3.getCode().equals(r.getCode())) {
				Assert.assertNotNull(r.getBizType());
				checked++;
			}
		}
		Assert.assertEquals(1, checked);
		System.out.println("test-3: get rules by bizType passed");

		// test-4: update rule
		final Rule oldR1 = this.ruleService.getRule(r1.getCode());
		r1.setName("name-1-1");
		r1.setComment("comment-1-1");
		r1.setBizTypeCode("biz-type-code-1-1");
		r1.setExpression("expression-1-1");
		r1.setMatchType(MatchType.CUSTOM);
		r1.setExecutorClass("executorClass-1-1");
		r1.getParams().add("p13");
		// old not found
		String oldCode = r1.getCode();
		r1.setCode("xxxxxx");
		try {
			this.ruleService.updateRule(r1);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		r1.setCode(oldCode);
		// bizType not found
		r1.setBizTypeCode("xxxxx");
		try {
			this.ruleService.updateRule(r1);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		// datasource not found
		r1.getDatasources().add(new Datasource());
		r1.getDatasources().get(r1.getDatasources().size() - 1).setCode("xxxx");
		try {
			this.ruleService.updateRule(r1);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		r1.getDatasources().get(r1.getDatasources().size() - 1).setCode("datasource-code-2");
		// add sql
		r1.getExtractSqls().add(new ExtractSql());
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setRuleCode(r1.getCode());
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setDatasourceCode("datasource-code-2");
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setParams(new ArrayList<String>());
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).getParams().add("sql-param-1-3-1");
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setSql("sql-1-3");
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setTableName("table-1-3");
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setColumns(new ArrayList<String>());
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).getColumns().add("column-1-3-1");
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).setConditions(new HashMap<String, String>());
		r1.getExtractSqls().get(r1.getExtractSqls().size() - 1).getConditions()
				.put("column-1-3-1", "column-1-3-1-value");
		// add expression Segments
		r1.getExpressionSegments().put("key-1-3", "value-1-3");
		// update
		r1.setBizTypeCode(r3.getBizTypeCode());
		this.ruleService.updateRule(r1);
		// load
		final Rule r12 = this.ruleService.getRule(r1.getCode());
		// check
		Assert.assertNotNull(r12);
		Assert.assertEquals(2L, r12.getVersion());
		Assert.assertEquals(r1.getCode(), r12.getCode());
		Assert.assertEquals("name-1-1", r12.getName());
		Assert.assertEquals("comment-1-1", r12.getComment());
		Assert.assertNotNull(r12.getUpdateTime());
		Assert.assertEquals(r3.getBizTypeCode(), r12.getBizTypeCode());
		Assert.assertEquals("expression-1-1", r12.getExpression());
		Assert.assertEquals(MatchType.CUSTOM, r12.getMatchType());
		Assert.assertEquals("executorClass-1-1", r12.getExecutorClass());
		Assert.assertNull(r12.getInputParams());
		Assert.assertNotNull(r12.getParams());
		Assert.assertEquals(3, r12.getParams().size());
		Assert.assertEquals("p11", r12.getParams().get(0));
		Assert.assertEquals("p12", r12.getParams().get(1));
		Assert.assertEquals("p13", r12.getParams().get(2));
		Assert.assertNull(r12.getDatasourceCodes());
		Assert.assertNotNull(r12.getDatasources());
		Assert.assertEquals(2, r12.getDatasources().size());
		Assert.assertEquals("datasource-code-1", r12.getDatasources().get(0).getCode());
		Assert.assertEquals("datasource-code-2", r12.getDatasources().get(1).getCode());
		Assert.assertEquals(3, r12.getExtractSqls().size());
		Assert.assertEquals(1, r12.getExtractSqls().get(2).getParams().size());
		Assert.assertEquals("sql-param-1-3-1", r12.getExtractSqls().get(2).getParams().get(0));
		Assert.assertEquals("sql-1-3", r12.getExtractSqls().get(2).getSql());

		Assert.assertEquals("table-1-3", r12.getExtractSqls().get(2).getTableName());
		Assert.assertNull(r12.getExtractSqls().get(2).getColumnsValue());
		Assert.assertNotNull(r12.getExtractSqls().get(2).getColumns());
		Assert.assertEquals(1, r12.getExtractSqls().get(2).getColumns().size());
		Assert.assertEquals("column-1-3-1", r12.getExtractSqls().get(2).getColumns().get(0));
		Assert.assertNull(r12.getExtractSqls().get(2).getConditionsValue());
		Assert.assertNotNull(r12.getExtractSqls().get(2).getConditions());
		Assert.assertEquals(1, r12.getExtractSqls().get(2).getConditions().size());
		Assert.assertEquals("column-1-3-1-value", r12.getExtractSqls().get(2).getConditions().get("column-1-3-1"));

		Assert.assertNotNull(r12.getExpressionSegments());
		Assert.assertEquals(3, r12.getExpressionSegments().size());
		Assert.assertEquals("value-1-1", r12.getExpressionSegments().get("key-1-1"));
		Assert.assertEquals("value-1-2", r12.getExpressionSegments().get("key-1-2"));
		Assert.assertEquals("value-1-3", r12.getExpressionSegments().get("key-1-3"));

		// check history
		try {
			this.ruleService.getRule(r1.getCode(), 11111L);
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		final Rule h1 = this.ruleService.getRule(r1.getCode(), 1L);
		// check
		Assert.assertNotNull(h1);
		Assert.assertEquals(1L, h1.getVersion());
		Assert.assertEquals(oldR1.getCode(), h1.getCode());
		Assert.assertEquals(oldR1.getName(), h1.getName());
		Assert.assertEquals(oldR1.getComment(), h1.getComment());
		Assert.assertNotNull(h1.getCreationTime());
		Assert.assertNull(h1.getUpdateTime());
		Assert.assertEquals(oldR1.getBizTypeCode(), h1.getBizTypeCode());
		Assert.assertNotNull(h1.getBizType());
		Assert.assertEquals(oldR1.getExpression(), h1.getExpression());
		Assert.assertEquals(oldR1.getMatchType(), h1.getMatchType());
		Assert.assertEquals(oldR1.getExecutorClass(), h1.getExecutorClass());
		Assert.assertNull(h1.getInputParams());
		Assert.assertNotNull(h1.getParams());
		Assert.assertEquals(2, h1.getParams().size());
		Assert.assertEquals("p11", h1.getParams().get(0));
		Assert.assertEquals("p12", h1.getParams().get(1));
		Assert.assertNull(h1.getDatasourceCodes());
		Assert.assertNotNull(h1.getDatasources());
		Assert.assertEquals(1, h1.getDatasources().size());
		Assert.assertEquals("datasource-code-1", h1.getDatasources().get(0).getCode());
		Assert.assertNotNull(h1.getExtractSqls());
		Assert.assertEquals(2, h1.getExtractSqls().size());

		Assert.assertEquals(h1.getCode(), h1.getExtractSqls().get(0).getRuleCode());
		Assert.assertEquals("datasource-code-1", h1.getExtractSqls().get(0).getDatasourceCode());
		Assert.assertNull(h1.getExtractSqls().get(0).getInputParams());
		Assert.assertEquals("sql-1-1", h1.getExtractSqls().get(0).getSql());
		Assert.assertEquals(2, h1.getExtractSqls().get(0).getParams().size());
		Assert.assertEquals("sql-param-1-1-1", h1.getExtractSqls().get(0).getParams().get(0));
		Assert.assertEquals("sql-param-1-1-2", h1.getExtractSqls().get(0).getParams().get(1));
		Assert.assertEquals("table-1-1", h1.getExtractSqls().get(0).getTableName());
		Assert.assertNull(h1.getExtractSqls().get(0).getColumnsValue());
		Assert.assertNotNull(h1.getExtractSqls().get(0).getColumns());
		Assert.assertEquals(2, h1.getExtractSqls().get(0).getColumns().size());
		Assert.assertEquals("column-1-1-1", h1.getExtractSqls().get(0).getColumns().get(0));
		Assert.assertEquals("column-1-1-2", h1.getExtractSqls().get(0).getColumns().get(1));
		Assert.assertNull(h1.getExtractSqls().get(0).getConditionsValue());
		Assert.assertNotNull(h1.getExtractSqls().get(0).getConditions());
		Assert.assertEquals(2, h1.getExtractSqls().get(0).getConditions().size());
		Assert.assertEquals("column-1-1-1-value", h1.getExtractSqls().get(0).getConditions().get("column-1-1-1"));
		Assert.assertEquals("column-1-1-2-value", h1.getExtractSqls().get(0).getConditions().get("column-1-1-2"));

		Assert.assertEquals(h1.getCode(), h1.getExtractSqls().get(1).getRuleCode());
		Assert.assertEquals("datasource-code-1", h1.getExtractSqls().get(1).getDatasourceCode());
		Assert.assertNull(h1.getExtractSqls().get(1).getInputParams());
		Assert.assertEquals("sql-1-2", h1.getExtractSqls().get(1).getSql());
		Assert.assertEquals(2, h1.getExtractSqls().get(1).getParams().size());
		Assert.assertEquals("sql-param-1-2-1", h1.getExtractSqls().get(1).getParams().get(0));
		Assert.assertEquals("sql-param-1-2-2", h1.getExtractSqls().get(1).getParams().get(1));
		Assert.assertEquals("table-1-2", h1.getExtractSqls().get(1).getTableName());
		Assert.assertNull(h1.getExtractSqls().get(1).getColumnsValue());
		Assert.assertNotNull(h1.getExtractSqls().get(1).getColumns());
		Assert.assertEquals(2, h1.getExtractSqls().get(1).getColumns().size());
		Assert.assertEquals("column-1-2-1", h1.getExtractSqls().get(1).getColumns().get(0));
		Assert.assertEquals("column-1-2-2", h1.getExtractSqls().get(1).getColumns().get(1));
		Assert.assertNull(h1.getExtractSqls().get(1).getConditionsValue());
		Assert.assertNotNull(h1.getExtractSqls().get(1).getConditions());
		Assert.assertEquals(2, h1.getExtractSqls().get(1).getConditions().size());
		Assert.assertEquals("column-1-2-1-value", h1.getExtractSqls().get(1).getConditions().get("column-1-2-1"));
		Assert.assertEquals("column-1-2-2-value", h1.getExtractSqls().get(1).getConditions().get("column-1-2-2"));

		Assert.assertNull(h1.getExprSegments());
		Assert.assertNotNull(h1.getExpressionSegments());
		Assert.assertEquals(2, h1.getExpressionSegments().size());
		Assert.assertEquals("value-1-1", h1.getExpressionSegments().get("key-1-1"));
		Assert.assertEquals("value-1-2", h1.getExpressionSegments().get("key-1-2"));

		// histories
		final List<Rule> histories = this.ruleService.getRuleHistories(r1.getCode());
		// check
		Assert.assertNotNull(histories);
		Assert.assertEquals(1, histories.size());
		Assert.assertEquals(1L, histories.get(0).getVersion());

		System.out.println("test-4: update rule passed");

		// test-5: remove rule
		try {
			this.ruleService.removeRule("xxxxxxxxx");
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		this.ruleService.removeRule(r3.getCode());
		// check
		Assert.assertNull(this.ruleService.getRule(r3.getCode()));
		list1 = this.ruleService.getRulesByBizType(r1.getBizTypeCode());
		list2 = this.ruleService.getRulesByBizType(r2.getBizTypeCode());
		Assert.assertNotNull(list1);
		Assert.assertNotNull(list2);
		Assert.assertEquals(1, list1.size());
		Assert.assertEquals(1, list2.size());
		System.out.println("test-5: remove rule passed");

		// test-6: existed
		Assert.assertTrue(this.ruleService.ruleExisted(r1.getCode()));
		Assert.assertFalse(this.ruleService.ruleExisted(r3.getCode()));
		Assert.assertFalse(this.ruleService.ruleExisted("xxxxxxx"));
		System.out.println("test-6: existed passed");
	}

	@Test
	public void testRuleSet() {
		// prepare rules
		Rule r11 = new Rule();
		r11.setCode("r11");
		r11.setName("r11");
		r11.setBizTypeCode("biz-type-code-1");
		r11.setMatchType(MatchType.ACCEPTANCE);
		r11.setExpression("r11-expr");
		this.ruleService.createRule(r11);
		r11 = this.ruleService.getRule(r11.getCode());
		Rule r12 = new Rule();
		r12.setCode("r12");
		r12.setName("r12");
		r12.setBizTypeCode("biz-type-code-1");
		r12.setMatchType(MatchType.SCORE);
		r12.setExpression("r12-expr");
		this.ruleService.createRule(r12);
		r12 = this.ruleService.getRule(r12.getCode());
		Rule r21 = new Rule();
		r21.setCode("r21");
		r21.setName("r21");
		r21.setBizTypeCode("biz-type-code-2");
		r21.setMatchType(MatchType.ACCEPTANCE);
		r21.setExpression("r21-expr");
		this.ruleService.createRule(r21);
		r21 = this.ruleService.getRule(r21.getCode());
		Rule r22 = new Rule();
		r22.setCode("r22");
		r22.setName("r22");
		r22.setBizTypeCode("biz-type-code-2");
		r22.setMatchType(MatchType.SCORE);
		r22.setExpression("r22-expr");
		this.ruleService.createRule(r22);
		r22 = this.ruleService.getRule(r22.getCode());

		// test-1: create
		final RuleSet rs11 = new RuleSet();
		rs11.setCode("rs11");
		rs11.setName("rs11");
		rs11.setBizTypeCode("biz-type-code-1");
		rs11.setRules(new ArrayList<Rule>());
		rs11.getRules().add(r11);
		this.ruleService.createRuleSet(rs11);
		final RuleSet rs12 = new RuleSet();
		rs12.setCode("rs12");
		rs12.setName("rs12");
		rs12.setBizTypeCode("biz-type-code-1");
		rs12.setRules(new ArrayList<Rule>());
		rs12.getRules().add(r11);
		rs12.getRules().add(r12);
		this.ruleService.createRuleSet(rs12);
		final RuleSet rs21 = new RuleSet();
		rs21.setCode("rs21");
		rs21.setName("rs21");
		rs21.setBizTypeCode("biz-type-code-2");
		rs21.setRules(new ArrayList<Rule>());
		rs21.getRules().add(r21);
		this.ruleService.createRuleSet(rs21);
		final RuleSet rs22 = new RuleSet();
		rs22.setCode("rs22");
		rs22.setName("rs22");
		rs22.setBizTypeCode("biz-type-code-2");
		rs22.setRules(new ArrayList<Rule>());
		rs22.getRules().add(r21);
		rs22.getRules().add(r22);
		this.ruleService.createRuleSet(rs22);
		System.out.println("test-1: create passed");

		// test-2: get by code
		RuleSet rs = this.ruleService.getRuleSet(rs22.getCode());
		// check
		Assert.assertNotNull(rs);
		Assert.assertEquals(rs22.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r21.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r21.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r22.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(r22.getVersion(), rs.getRules().get(1).getVersion());
		System.out.println("test-2: get by code passed");

		// test-3: get by code and version
		rs = this.ruleService.getRuleSet(rs11.getCode(), 1L);
		// check
		Assert.assertNotNull(rs);
		Assert.assertEquals(rs11.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(1, rs.getRules().size());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(0).getVersion());
		System.out.println("test-3: get by code and version passed");

		// test-4: get by bizTypeCode
		List<RuleSet> ruleSets = this.ruleService.getRuleSetsByBizType("biz-type-code-2");
		// check
		Assert.assertNotNull(ruleSets);
		Assert.assertEquals(2, ruleSets.size());
		rs = ruleSets.get(0);
		Assert.assertEquals(rs21.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(1, rs.getRules().size());
		Assert.assertEquals(r21.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r21.getVersion(), rs.getRules().get(0).getVersion());
		rs = ruleSets.get(1);
		Assert.assertEquals(rs22.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r21.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r21.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r22.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(r22.getVersion(), rs.getRules().get(1).getVersion());
		System.out.println("test-4: get by bizTypeCode passed");

		// test-5: update
		// update rule
		this.ruleService.updateRule(r12);
		r12 = this.ruleService.getRule(r12.getCode());
		// the ruleSet should not be changed
		rs = this.ruleService.getRuleSet(rs12.getCode(), 1L);
		Assert.assertNotNull(rs);
		Assert.assertEquals(rs12.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r12.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(1L, rs.getRules().get(1).getVersion());
		// update ruleSet
		rs.getRules().clear();
		rs.getRules().add(r12);
		rs.getRules().add(r11);
		this.ruleService.updateRuleSet(rs);
		// get version 1
		rs = this.ruleService.getRuleSet(rs12.getCode(), 1L);
		Assert.assertNotNull(rs);
		Assert.assertEquals(rs12.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r12.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(1L, rs.getRules().get(1).getVersion());
		// get current version
		rs = this.ruleService.getRuleSet(rs12.getCode());
		Assert.assertNotNull(rs);
		Assert.assertEquals(rs12.getCode(), rs.getCode());
		Assert.assertEquals(2L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r12.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r12.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(1).getVersion());
		// get by bizTypeCode
		ruleSets = this.ruleService.getRuleSetsByBizType("biz-type-code-1");
		// check
		Assert.assertNotNull(ruleSets);
		Assert.assertEquals(2, ruleSets.size());
		rs = ruleSets.get(0);
		Assert.assertEquals(rs11.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(1, rs.getRules().size());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(0).getVersion());
		rs = ruleSets.get(1);
		Assert.assertEquals(rs12.getCode(), rs.getCode());
		Assert.assertEquals(2L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r12.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r12.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(1).getVersion());
		System.out.println("test-5: update passed");

		// test-6: get histories
		ruleSets = this.ruleService.getRuleSetHistories(rs12.getCode());
		// check
		Assert.assertNotNull(ruleSets);
		Assert.assertEquals(1, ruleSets.size());
		// version 1
		rs = ruleSets.get(0);
		Assert.assertNotNull(rs);
		Assert.assertEquals(rs12.getCode(), rs.getCode());
		Assert.assertEquals(1L, rs.getVersion());
		Assert.assertNotNull(rs.getRules());
		Assert.assertNull(rs.getRulesValue());
		Assert.assertEquals(2, rs.getRules().size());
		Assert.assertEquals(r11.getCode(), rs.getRules().get(0).getCode());
		Assert.assertEquals(r11.getVersion(), rs.getRules().get(0).getVersion());
		Assert.assertEquals(r12.getCode(), rs.getRules().get(1).getCode());
		Assert.assertEquals(1L, rs.getRules().get(1).getVersion());
		System.out.println("test-6: get histories passed");
	}

	@Test
	public void testAssignBizTypeRuleSet() {
		// prepare rules
		Rule r11 = new Rule();
		r11.setCode("r11");
		r11.setName("r11");
		r11.setBizTypeCode("biz-type-code-1");
		r11.setMatchType(MatchType.ACCEPTANCE);
		r11.setExpression("r11-expr");
		this.ruleService.createRule(r11);
		r11 = this.ruleService.getRule(r11.getCode());
		Rule r12 = new Rule();
		r12.setCode("r12");
		r12.setName("r12");
		r12.setBizTypeCode("biz-type-code-1");
		r12.setMatchType(MatchType.SCORE);
		r12.setExpression("r12-expr");
		this.ruleService.createRule(r12);

		// prepare ruleSets
		RuleSet ruleSet = new RuleSet();
		ruleSet.setCode("rs11");
		ruleSet.setName("rs11");
		ruleSet.setBizTypeCode("biz-type-code-1");
		ruleSet.setRules(new ArrayList<Rule>());
		ruleSet.getRules().add(r11);
		ruleSet.getRules().add(r12);
		this.ruleService.createRuleSet(ruleSet);
		ruleSet = this.ruleService.getRuleSet(ruleSet.getCode());

		// test-1: before assignment
		BizType bizType = this.bizTypeService.getBizType("biz-type-code-1");
		// check
		Assert.assertNotNull(bizType);
		Assert.assertNull(bizType.getRuleSetCode());
		Assert.assertNull(bizType.getRuleSetVersion());
		Assert.assertNull(bizType.getRuleSet());
		System.out.println("test-1: before assignment passed");

		// test-2: assign
		this.bizTypeService.assignRuleSet(bizType.getCode(), ruleSet.getCode(), ruleSet.getVersion());
		bizType = this.bizTypeService.getBizType("biz-type-code-1");
		// check
		Assert.assertNotNull(bizType);
		Assert.assertNotNull(bizType.getRuleSetCode());
		Assert.assertNotNull(bizType.getRuleSetVersion());
		// check ruleSet
		Assert.assertNotNull(bizType.getRuleSet());
		Assert.assertEquals(ruleSet.getCode(), bizType.getRuleSet().getCode());
		Assert.assertEquals(ruleSet.getVersion(), bizType.getRuleSet().getVersion());
		Assert.assertNotNull(bizType.getRuleSet().getRules());
		Assert.assertEquals(2, bizType.getRuleSet().getRules().size());
		Assert.assertEquals(ruleSet.getRules().get(0).getCode(), bizType.getRuleSet().getRules().get(0).getCode());
		Assert.assertEquals(ruleSet.getRules().get(0).getVersion(), bizType.getRuleSet().getRules().get(0).getVersion());
		Assert.assertEquals(ruleSet.getRules().get(1).getCode(), bizType.getRuleSet().getRules().get(1).getCode());
		Assert.assertEquals(ruleSet.getRules().get(1).getVersion(), bizType.getRuleSet().getRules().get(1).getVersion());
		System.out.println("test-2: assign passed");

		// test-3: after update ruleSet
		this.ruleService.updateRuleSet(ruleSet);
		ruleSet = this.ruleService.getRuleSet(ruleSet.getCode(), 1L);
		bizType = this.bizTypeService.getBizType("biz-type-code-1");
		// check
		Assert.assertNotNull(bizType);
		Assert.assertNotNull(bizType.getRuleSetCode());
		Assert.assertNotNull(bizType.getRuleSetVersion());
		// check ruleSet
		Assert.assertNotNull(bizType.getRuleSet());
		Assert.assertEquals(ruleSet.getCode(), bizType.getRuleSet().getCode());
		Assert.assertEquals(ruleSet.getVersion(), bizType.getRuleSet().getVersion());
		Assert.assertNotNull(bizType.getRuleSet().getRules());
		Assert.assertEquals(2, bizType.getRuleSet().getRules().size());
		Assert.assertEquals(ruleSet.getRules().get(0).getCode(), bizType.getRuleSet().getRules().get(0).getCode());
		Assert.assertEquals(ruleSet.getRules().get(0).getVersion(), bizType.getRuleSet().getRules().get(0).getVersion());
		Assert.assertEquals(ruleSet.getRules().get(1).getCode(), bizType.getRuleSet().getRules().get(1).getCode());
		Assert.assertEquals(ruleSet.getRules().get(1).getVersion(), bizType.getRuleSet().getRules().get(1).getVersion());
		System.out.println("test-3: after update ruleSet passed");

		// test-4: reassign
		this.bizTypeService.assignRuleSet(bizType.getCode(), ruleSet.getCode(), 2L);
		bizType = this.bizTypeService.getBizType("biz-type-code-1");
		ruleSet = this.ruleService.getRuleSet(ruleSet.getCode());
		// check
		Assert.assertNotNull(bizType);
		Assert.assertNotNull(bizType.getRuleSetCode());
		Assert.assertNotNull(bizType.getRuleSetVersion());
		// check ruleSet
		Assert.assertNotNull(bizType.getRuleSet());
		Assert.assertEquals(ruleSet.getCode(), bizType.getRuleSet().getCode());
		Assert.assertEquals(ruleSet.getVersion(), bizType.getRuleSet().getVersion());
		Assert.assertNotNull(bizType.getRuleSet().getRules());
		Assert.assertEquals(2, bizType.getRuleSet().getRules().size());
		Assert.assertEquals(ruleSet.getRules().get(0).getCode(), bizType.getRuleSet().getRules().get(0).getCode());
		Assert.assertEquals(ruleSet.getRules().get(0).getVersion(), bizType.getRuleSet().getRules().get(0).getVersion());
		Assert.assertEquals(ruleSet.getRules().get(1).getCode(), bizType.getRuleSet().getRules().get(1).getCode());
		Assert.assertEquals(ruleSet.getRules().get(1).getVersion(), bizType.getRuleSet().getRules().get(1).getVersion());
		System.out.println("test-4: reassign passed");
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
		final BizType b2 = new BizType();
		b2.setCode("biz-type-code-2");
		b2.setName("name-2");
		b2.setComment("comment-2");
		this.bizTypeService.createBizType(b2);
		// test datasource
		final Datasource d1 = new Datasource();
		d1.setCode("datasource-code-1");
		d1.setName("name-1");
		d1.setComment("comment-1");
		d1.setDriverClass("driverClass-1");
		d1.setUrl("url-1");
		d1.setUser("user-1");
		d1.setPassword("password-1");
		d1.setMaxPoolSize(111);
		d1.setValidationSql("validationSql-1");
		this.datasourceManager.createDatasource(d1);
		final Datasource d2 = new Datasource();
		d2.setCode("datasource-code-2");
		d2.setName("name-2");
		d2.setComment("comment-2");
		d2.setDriverClass("driverClass-2");
		d2.setUrl("url-2");
		d2.setUser("user-2");
		d2.setPassword("password-2");
		d2.setMaxPoolSize(222);
		d2.setValidationSql("validationSql-2");
		this.datasourceManager.createDatasource(d2);
	}

	@After
	public void teardown() {
		this.clearDatas();
	}

	private void clearDatas() {
		this.testingService.clearDatasources();
		this.testingService.clearBizTypes();
		this.testingService.clearRules();
		this.testingService.clearRuleHistories();
		this.testingService.clearRuleSets();
		this.testingService.clearRuleSetHistories();
		this.testingService.clearExtractSqls();
		this.testingService.clearExtractSqlHistories();
	}

	/**
	 * @param testingService
	 *            the testingService to set
	 */
	public void setTestingService(TestingService testingService) {
		this.testingService = testingService;
	}

	/**
	 * @param ruleService
	 *            the ruleService to set
	 */
	public void setRuleService(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	/**
	 * @param bizTypeService
	 *            the bizTypeService to set
	 */
	public void setBizTypeService(BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
	}

	/**
	 * @param datasourceManager
	 *            the datasourceManager to set
	 */
	public void setDatasourceManager(DatasourceManager datasourceManager) {
		this.datasourceManager = datasourceManager;
	}

}
