/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import java.util.Map;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.match.TextExecutor;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.TextResult;

/**
 * 用JEXL实现的TextExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class JexlTextExecutor extends JexlBaseExecutor implements TextExecutor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleExecutor#execute(Rule,
	 * java.util.Map)
	 */
	@Override
	public TextResult execute(final Rule rule, final Map<String, String> params) {
		if (rule == null) {
			LOGGER.warn("the rule is null, execute the score rule failed");
			return null;
		}
		LOGGER.debug("executing the score rule, rule: {}, params: {}", rule, params);
		final TextResult result = new TextResult();
		result.setRuleCode(rule.getCode());
		result.setRuleVersion(rule.getVersion());
		result.setRuleMatchType(rule.getMatchType());
		result.setParams(params);
		// set the parameters
		this.executionTracer.setParams(params);
		try {
			// begin stage
			this.executionTracer.beginStage(rule);
			final Object r = this.execute(this.buildScript(rule.getExpression(), rule.getExpressionSegments()), params);
			if (r != null) {
				result.setResult(r.toString());
				this.executionTracer.setResult(result.getResult().toString());
			}
			result.setErrorMessage(null);
		} catch (Throwable e) {
			LOGGER.error(
					"execute the score rule failed, rule: " + rule + ", params: " + params + ", error: "
							+ e.getMessage(), e);
			result.setErrorMessage(e.getMessage());
			this.executionTracer.setErrorMessage(result.getErrorMessage());
		} finally {
			result.setTime(this.getNowTime());
			result.setExecutionId(this.executionTracer.getExecutionId());
			result.setStages(this.executionTracer.getStages());
			// end stage
			this.executionTracer.endStage();
		}
		LOGGER.info("the score rule executed, rule: {}, params: {}, result: {}", rule, params, result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleExecutor#evaluate(java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	public TextResult evaluate(final String expression, final Map<String, String> segments,
			final Map<String, String> params) {
		if (expression == null) {
			LOGGER.warn("the expression is null, evaluate the score rule expression failed");
			return null;
		}
		LOGGER.debug("evaluating the score rule, expression: {}, params: {}", expression, params);
		final TextResult result = new TextResult();
		result.setParams(params);
		// set the parameters
		this.executionTracer.setParams(params);
		try {
			// begin stage
			this.executionTracer.beginStage(null);
			final Object r = this.execute(this.buildScript(expression, segments), params);
			if (r != null) {
				result.setResult(r.toString());
				this.executionTracer.setResult(result.getResult().toString());
			}
			result.setErrorMessage(null);
		} catch (Exception e) {
			LOGGER.error("execute the score rule failed, expression: " + expression + ", params: " + params
					+ ", error: " + e.getMessage(), e);
			result.setErrorMessage(e.getMessage());
			this.executionTracer.setErrorMessage(result.getErrorMessage());
		} finally {
			result.setTime(this.getNowTime());
			result.setExecutionId(this.executionTracer.getExecutionId());
			result.setStages(this.executionTracer.getStages());
			// end stage
			this.executionTracer.endStage();
		}
		LOGGER.info("the score rule evaluated, expression: {}, params: {}, result: {}", expression, params, result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleExecutor#acceptedMatchType()
	 */
	@Override
	public MatchType acceptedMatchType() {
		return MatchType.TEXT;
	}

}
