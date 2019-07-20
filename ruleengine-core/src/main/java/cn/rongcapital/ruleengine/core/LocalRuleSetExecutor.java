/**
 *
 */
package cn.rongcapital.ruleengine.core;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.match.RuleSetExecutor;
import cn.rongcapital.ruleengine.model.*;
import cn.rongcapital.ruleengine.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * the local JVM implementation for RuleSetExecutor
 *
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public class LocalRuleSetExecutor implements RuleSetExecutor {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalRuleSetExecutor.class);

	@Autowired
	private ApplicationContext context;

	/*@Autowired
	private ResultService resultService;*/

	/**
	 * the rule execution thread pool size
	 */
	@Value("${match.rule.execution.threadPoolSize}")
	private int ruleThreadPoolSize = 5;

	/**
	 * is it initialized?
	 */
	private volatile AtomicBoolean initiazlied = new AtomicBoolean(false);

	/**
	 * the match thread pool size
	 */
	@Value("${match.threadPoolSize}")
	private int matchThreadPoolSize = 5;

	/*
	 * (non-Javadoc)
	 *
	 * @see RuleSetExecutor#execute(java.lang.String,
	 * RuleSet, java.util.Map)
	 */
	@Override
	public void execute(final String bizCode, final RuleSet ruleSet, final Map<String, String> params) {

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

		// refact ruleSet and set rule's design property to null
		for (Rule rule : ruleSet.getRules()) {
			rule.setDesign(null);
		}

		LOGGER.debug("matching rules, ruleSet: {}, bizCode: {}, datas: {}", ruleSet, bizCode, params);

		// create the results
		final AcceptanceResults acceptanceResult = this.buildAcceptanceResults(ruleSet.getBizTypeCode(), bizCode);
		acceptanceResult.setRuleSetCode(ruleSet.getCode());
		acceptanceResult.setRuleSetVersion(ruleSet.getVersion());

		final ScoreResults scoreResult = this.buildScoreResults(ruleSet.getBizTypeCode(), bizCode);
		scoreResult.setRuleSetCode(ruleSet.getCode());
		scoreResult.setRuleSetVersion(ruleSet.getVersion());

		final TextResults textResult = this.buildTextResults(ruleSet.getBizTypeCode(), bizCode);
		textResult.setRuleSetCode(ruleSet.getCode());
		textResult.setRuleSetVersion(ruleSet.getVersion());

		if (ruleSet.isOrdered()) {
			RuleSetEntity ruleSetEntity = new RuleSetEntity(ruleSet, params, acceptanceResult, scoreResult, textResult);
			MessageChannel ruleSetQueue = context.getBean("ruleSetQueue", MessageChannel.class);
			try {
				Message<String> message = MessageBuilder.withPayload(JsonHelper.toJsonString(ruleSetEntity))
						.setHeader("ruleengine_ruleset_redis_queue", "ruleSet")
						.build();
				ruleSetQueue.send(message);
			} catch (IOException e) {
				LOGGER.error("failed to parse request message from MatchRequestData to json, MatchRequestData: {}", ruleSetEntity);
			}
		}
		else {
			int sortId = 1;
			for (final Rule rule : ruleSet.getRules()) {
				// execute the rule, in case of ruleset is ordered execute the rules in single thread else run in multiple
				RuleEntity ruleEntity = new RuleEntity(rule, params, acceptanceResult, scoreResult, textResult, sortId++);
				MessageChannel ruleSetQueue = context.getBean("ruleSetQueue", MessageChannel.class);
				try {
					Message<String> message = MessageBuilder.withPayload(JsonHelper.toJsonString(ruleEntity))
							.setHeader("ruleengine_ruleset_redis_queue", "rules")
							.build();
					ruleSetQueue.send(message);
				} catch (IOException e) {
					LOGGER.error("failed to parse request message from MatchRequestData to json, MatchRequestData: {}", ruleEntity);
				}
			}
		}
	}

	/**
	 *
	 * @param bizTypeCode
	 * @param bizCode
	 * @return
	 */
	private AcceptanceResults buildAcceptanceResults(final String bizTypeCode, final String bizCode) {
		final AcceptanceResults acceptanceResult = new AcceptanceResults();
		acceptanceResult.setResults(new ArrayList<AcceptanceResult>());
		acceptanceResult.setBizCode(bizCode);
		acceptanceResult.setBizTypeCode(bizTypeCode);
		return acceptanceResult;
	}

	/**
	 *
	 * @param bizTypeCode
	 * @param bizCode
	 * @return
	 */
	private ScoreResults buildScoreResults(final String bizTypeCode, final String bizCode) {
		final ScoreResults scoreResult = new ScoreResults();
		scoreResult.setResults(new ArrayList<ScoreResult>());
		scoreResult.setBizCode(bizCode);
		scoreResult.setBizTypeCode(bizTypeCode);
		return scoreResult;
	}

	/**
	 *
	 * @param bizTypeCode
	 * @param bizCode
	 * @return
	 */
	private TextResults buildTextResults(final String bizTypeCode, final String bizCode) {
		final TextResults textResult = new TextResults();
		textResult.setResults(new ArrayList<TextResult>());
		textResult.setBizCode(bizCode);
		textResult.setBizTypeCode(bizTypeCode);
		return textResult;
	}
}
