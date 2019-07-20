/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import java.util.Map;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.ScoreResult;

/**
 * 用JEXL实现的ScoreExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class JexlScoreExecutor extends JexlBaseExecutor implements ScoreExecutor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleExecutor#execute(Rule,
	 * java.util.Map)
	 */
	@Override
	public ScoreResult execute(final Rule rule, final Map<String, String> params) {
		if (rule == null) {
			LOGGER.warn("the rule is null, execute the score rule failed");
			return null;
		}
		LOGGER.debug("executing the score rule, rule: {}, params: {}", rule, params);
		final ScoreResult result = new ScoreResult();
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
				result.setResult(Integer.valueOf(r.toString())); // FIXME
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
	public ScoreResult evaluate(final String expression, final Map<String, String> segments,
			final Map<String, String> params) {
		if (expression == null) {
			LOGGER.warn("the expression is null, evaluate the score rule expression failed");
			return null;
		}
		LOGGER.debug("evaluating the score rule, expression: {}, params: {}", expression, params);
		final ScoreResult result = new ScoreResult();
		result.setParams(params);
		// set the parameters
		this.executionTracer.setParams(params);
		try {
			// begin stage
			this.executionTracer.beginStage(null);
			final Object r = this.execute(this.buildScript(expression, segments), params);
			if (r != null) {
				result.setResult(((Number) r).intValue()); // FIXME
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
		return MatchType.SCORE;
	}

}
