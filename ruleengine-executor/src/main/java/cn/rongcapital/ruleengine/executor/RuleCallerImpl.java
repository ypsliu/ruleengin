/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.match.AcceptanceExecutor;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.match.TextExecutor;
import cn.rongcapital.ruleengine.model.AcceptanceResult;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.ScoreResult;
import cn.rongcapital.ruleengine.model.TextResult;
import cn.rongcapital.ruleengine.service.RuleService;

/**
 * the implementation for RuleCaller
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RuleCallerImpl implements RuleCaller {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleCallerImpl.class);

	/**
	 * the rule service
	 */
	private RuleService ruleService;

	/**
	 * the execution tracer
	 */
	private ExecutionTracer executionTracer;

	/**
	 * the AcceptanceExecutor
	 */
	private AcceptanceExecutor acceptanceExecutor;

	/**
	 * the ScoreExecutor
	 */
	private ScoreExecutor scoreExecutor;

	/**
	 * the TextExecutor
	 */
	private TextExecutor textExecutor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleCaller#call(java.lang.String, long)
	 */
	@Override
	public Object call(final String ruleCode, final long ruleVersion) {
		final Rule rule = this.ruleService.getRule(ruleCode, ruleVersion);
		if (MatchType.ACCEPTANCE == rule.getMatchType()) {
			// acceptance rule
			final AcceptanceResult ar = this.acceptanceExecutor.execute(rule, this.executionTracer.getParams());
			if (ar.getErrorMessage() == null) {
				return ar.getResult();
			} else {
				throw new RuntimeException("call the rule failed, ruleCode: " + ruleCode + ", error: "
						+ ar.getErrorMessage());
			}
		} else if (MatchType.SCORE == rule.getMatchType()) {
			// score
			final ScoreResult sr = this.scoreExecutor.execute(rule, this.executionTracer.getParams());
			if (sr.getErrorMessage() == null) {
				return sr.getResult();
			} else {
				throw new RuntimeException("call the rule failed, ruleCode: " + ruleCode + ", error: "
						+ sr.getErrorMessage());
			}
		} else if (MatchType.TEXT == rule.getMatchType()) {
			// score
			final TextResult sr = this.textExecutor.execute(rule, this.executionTracer.getParams());
			if (sr.getErrorMessage() == null) {
				return sr.getResult();
			} else {
				throw new RuntimeException("call the rule failed, ruleCode: " + ruleCode + ", error: "
						+ sr.getErrorMessage());
			}
		} else {
			LOGGER.error("the match type is unsupported: {}", rule.getMatchType());
			throw new InvalidParameterException("the match type is unsupported: " + rule.getMatchType());
		}
	}

	/**
	 * @param executionTracer
	 *            the executionTracer to set
	 */
	public void setExecutionTracer(final ExecutionTracer executionTracer) {
		this.executionTracer = executionTracer;
	}

	/**
	 * @param ruleService
	 *            the ruleService to set
	 */
	public void setRuleService(final RuleService ruleService) {
		this.ruleService = ruleService;
	}

	/**
	 * @param acceptanceExecutor
	 *            the acceptanceExecutor to set
	 */
	public void setAcceptanceExecutor(final AcceptanceExecutor acceptanceExecutor) {
		this.acceptanceExecutor = acceptanceExecutor;
	}

	/**
	 * @param scoreExecutor
	 *            the scoreExecutor to set
	 */
	public void setScoreExecutor(final ScoreExecutor scoreExecutor) {
		this.scoreExecutor = scoreExecutor;
	}

	/**
	 * @param textExecutor
	 *            the textExecutor to set
	 */
	public void setTextExecutor(final TextExecutor textExecutor) {
		this.textExecutor = textExecutor;
	}

}
