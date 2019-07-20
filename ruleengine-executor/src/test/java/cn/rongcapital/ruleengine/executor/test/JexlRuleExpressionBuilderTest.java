/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test;

import java.util.ArrayList;

import org.junit.Test;

import cn.rongcapital.ruleengine.executor.JexlRuleExpressionBuilder;
import cn.rongcapital.ruleengine.model.ui.ConditionOperator;
import cn.rongcapital.ruleengine.model.ui.ConditionsRelationType;
import cn.rongcapital.ruleengine.model.ui.ReturnType;
import cn.rongcapital.ruleengine.model.ui.RuleCondition;
import cn.rongcapital.ruleengine.model.ui.RuleDesign;
import cn.rongcapital.ruleengine.model.ui.RuleResult;
import cn.rongcapital.ruleengine.model.ui.RuleSegment;
import cn.rongcapital.ruleengine.model.ui.RuleStep;
import cn.rongcapital.ruleengine.model.ui.RuleVar;
import cn.rongcapital.ruleengine.model.ui.RuleVarType;

/**
 * the unit test for JexlRuleExpressionBuilder
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class JexlRuleExpressionBuilderTest {

	@Test
	public void test() {
		// design
		final RuleDesign d = new RuleDesign();
		// params
		d.setParams(new ArrayList<String>());
		d.getParams().add("p1");
		d.getParams().add("p2");
		// segments
		d.setSegments(new ArrayList<RuleSegment>());
		d.getSegments().add(new RuleSegment());
		d.getSegments().get(0).setKey("s1");
		d.getSegments().get(0).setValue("sv1");
		d.getSegments().add(new RuleSegment());
		d.getSegments().get(1).setKey("s2");
		d.getSegments().get(1).setValue("sv2");
		// vars
		d.setVars(new ArrayList<RuleVar>());
		d.getVars().add(new RuleVar());
		d.getVars().get(0).setName("v1");
		d.getVars().get(0).setType(RuleVarType.LET);
		d.getVars().get(0).setParams("123");
		d.getVars().add(new RuleVar());
		d.getVars().get(1).setName("v2");
		d.getVars().get(1).setType(RuleVarType.LET);
		d.getVars().get(1).setParams("'string'");
		d.getVars().add(new RuleVar());
		d.getVars().get(2).setName("v3");
		d.getVars().get(2).setType(RuleVarType.PLUGINS);
		d.getVars().get(2).setParams("getAge1,p1");
		d.getVars().add(new RuleVar());
		d.getVars().get(3).setName("v4");
		d.getVars().get(3).setType(RuleVarType.PLUGINS);
		d.getVars().get(3).setParams("getAge2,s1,444");
		d.getVars().add(new RuleVar());
		d.getVars().get(4).setName("v5");
		d.getVars().get(4).setType(RuleVarType.RULES);
		d.getVars().get(4).setParams("rule1,2");
		// steps
		d.setSteps(new ArrayList<RuleStep>());
		d.getSteps().add(new RuleStep());
		d.getSteps().get(0).setComment("step-1");
		d.getSteps().get(0).setRelType(ConditionsRelationType.AND);
		d.getSteps().get(0).setConditions(new ArrayList<RuleCondition>());
		d.getSteps().get(0).getConditions().add(new RuleCondition());
		d.getSteps().get(0).getConditions().get(0).setVarName("v1");
		d.getSteps().get(0).getConditions().get(0).setOperator(ConditionOperator.GE);
		d.getSteps().get(0).getConditions().get(0).setValue("123");
		d.getSteps().get(0).getConditions().add(new RuleCondition());
		d.getSteps().get(0).getConditions().get(1).setVarName("v2");
		d.getSteps().get(0).getConditions().get(1).setOperator(ConditionOperator.EQ);
		d.getSteps().get(0).getConditions().get(1).setValue("'string-value'");
		d.getSteps().get(0).getConditions().add(new RuleCondition());
		d.getSteps().get(0).getConditions().get(2).setVarName("v3");
		d.getSteps().get(0).getConditions().get(2).setOperator(ConditionOperator.LE);
		d.getSteps().get(0).getConditions().get(2).setValue("s2");
		d.getSteps().get(0).setTrueResult(new RuleResult());
		d.getSteps().get(0).getTrueResult().setTrace("step-1-passed");
		d.getSteps().get(0).getTrueResult().setResult("ACCEPT");
		d.getSteps().get(0).setFalseResult(new RuleResult());
		d.getSteps().get(0).getFalseResult().setReturnType(ReturnType.NEXT_STEP);

		d.getSteps().add(new RuleStep());
		d.getSteps().get(1).setComment("step-2");
		d.getSteps().get(1).setRelType(ConditionsRelationType.OR);
		d.getSteps().get(1).setConditions(new ArrayList<RuleCondition>());
		d.getSteps().get(1).getConditions().add(new RuleCondition());
		d.getSteps().get(1).getConditions().get(0).setVarName("v4");
		d.getSteps().get(1).getConditions().get(0).setOperator(ConditionOperator.NE);
		d.getSteps().get(1).getConditions().get(0).setValue("true");
		d.getSteps().get(1).getConditions().add(new RuleCondition());
		d.getSteps().get(1).getConditions().get(1).setVarName("v5");
		d.getSteps().get(1).getConditions().get(1).setOperator(ConditionOperator.IN);
		d.getSteps().get(1).getConditions().get(1).setValue("v1");
		d.getSteps().get(1).setTrueResult(new RuleResult());
		d.getSteps().get(1).getTrueResult().setTrace("step-2-passed");
		d.getSteps().get(1).getTrueResult().setResult("ACCEPT");
		d.getSteps().get(1).setFalseResult(new RuleResult());
		d.getSteps().get(1).getFalseResult().setTrace("step-2-fail");
		d.getSteps().get(1).getFalseResult().setResult("REJECT");

		d.getSteps().add(new RuleStep());
		d.getSteps().get(2).setComment("step-3");
		d.getSteps().get(2).setRelType(ConditionsRelationType.OR);
		d.getSteps().get(2).setConditions(new ArrayList<RuleCondition>());
		d.getSteps().get(2).getConditions().add(new RuleCondition());
		d.getSteps().get(2).getConditions().get(0).setVarName("v4");
		d.getSteps().get(2).getConditions().get(0).setOperator(ConditionOperator.NE);
		d.getSteps().get(2).getConditions().get(0).setValue("true");
		d.getSteps().get(2).getConditions().add(new RuleCondition());
		d.getSteps().get(2).getConditions().get(1).setVarName("v5");
		d.getSteps().get(2).getConditions().get(1).setOperator(ConditionOperator.IN);
		d.getSteps().get(2).getConditions().get(1).setValue("v1");
		d.getSteps().get(2).setTrueResult(new RuleResult());
		d.getSteps().get(2).getTrueResult().setTrace("step-3-passed");
		d.getSteps().get(2).getTrueResult().setResult("ACCEPT");
		d.getSteps().get(2).setFalseResult(new RuleResult());
		d.getSteps().get(2).getFalseResult().setTrace("step-3-fail");
		d.getSteps().get(2).getFalseResult().setResult("REJECT");
		d.getSteps().get(2).getFalseResult().setReturnType(ReturnType.RESULT);

		// JexlRuleExpressionBuilder
		final JexlRuleExpressionBuilder b = new JexlRuleExpressionBuilder();

		// build
		final String expr = b.build(d);
		System.out.println(expr);
	}

}
