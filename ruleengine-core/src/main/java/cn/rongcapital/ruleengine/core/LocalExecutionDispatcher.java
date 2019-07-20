/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.match.ExecutionDispatcher;
import cn.rongcapital.ruleengine.match.RuleSetExecutor;
import cn.rongcapital.ruleengine.model.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * the local JVM implementation for ExecutionDispatcher
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class LocalExecutionDispatcher implements ExecutionDispatcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalExecutionDispatcher.class);

	@Autowired
	private RuleSetExecutor ruleSetExecutor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionDispatcher#dispatch(java.lang.String,
	 * RuleSet, java.util.Map)
	 */
	@Override
	public void dispatch(final String bizCode, final RuleSet ruleSet, final Map<String, String> params) {
		// check
		if (bizCode == null || bizCode.isEmpty()) {
			LOGGER.error("the bizCode is null or empty, dispatch the execution failed");
			throw new InvalidParameterException("the bizCode is null or empty");
		}
		if (ruleSet == null) {
			LOGGER.error("the ruleSet is null, dispatch the execution failed");
			throw new InvalidParameterException("the ruleSet is null");
		}
		if (ruleSet.getRules() == null || ruleSet.getRules().isEmpty()) {
			LOGGER.error("the rules of the ruleSet is null or empty, dispatch the execution failed");
			throw new InvalidParameterException("the rules of the ruleSet is null or empty");
		}
		this.ruleSetExecutor.execute(bizCode, ruleSet, params);
		LOGGER.info("the ruleSet dispatched, bizCode: {}, ruleSet: {}, params: {}", bizCode, ruleSet, params);
	}

	/**
	 * @param ruleSetExecutor
	 *            the ruleSetExecutor to set
	 */
	public void setRuleSetExecutor(final RuleSetExecutor ruleSetExecutor) {
		this.ruleSetExecutor = ruleSetExecutor;
	}
}
