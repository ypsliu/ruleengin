/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.match.AcceptanceExecutor;
import cn.rongcapital.ruleengine.match.ExecutionDispatcher;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.model.*;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DataExtractor;
import cn.rongcapital.ruleengine.service.MatchService;
import cn.rongcapital.ruleengine.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the implementation for MatchService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class MatchServiceImpl implements MatchService {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchServiceImpl.class);

	@Autowired
	private RuleService ruleService;

	@Autowired
	private ScoreExecutor scoreExecutor;

	@Autowired
	private DataExtractor dataExtractor;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private AcceptanceExecutor acceptanceExecutor;

	@Autowired
	@Qualifier("localExecutionDispatcher")
	private ExecutionDispatcher executionDispatcher;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * MatchService#match(MatchRequestData)
	 */
	@Override
	public void match(final MatchRequestData request) {
		// bizType
		final BizType bizType = this.bizTypeService.getBizType(request.getBizTypeCode());
		if (bizType == null) {
			LOGGER.error("the bizType is NOT existed, bizTypeCode: {}", request.getBizTypeCode());
			throw new InvalidParameterException("the bizType is NOT existed, bizTypeCode: " + request.getBizTypeCode());
		}
		// RuleSet
		RuleSet ruleSet = bizType.getRuleSet();
		if (request.getRuleSetCode() != null && request.getRuleSetVersion() != null) {
			// ruleSetCode and ruleSetVersion has been set, use it as the ruleSet
			ruleSet = this.ruleService.getRuleSet(request.getRuleSetCode(), request.getRuleSetVersion());
			// check bizType
			if (ruleSet != null && !request.getBizTypeCode().equals(ruleSet.getBizTypeCode())) {
				// invalid ruleSet bizType
				LOGGER.error("invalid bizType of the ruleSet, request.bizTypeCode: {}, ruleSet.bizTypeCode: {}", request.getBizTypeCode(), ruleSet.getBizTypeCode());
				throw new InvalidParameterException("invalid bizType of the ruleSet, request.bizTypeCode: " + request.getBizTypeCode() + ", ruleSet.bizTypeCode: " + ruleSet.getBizTypeCode());
			}
		}
		// check ruleSet
		if (ruleSet == null || ruleSet.getRules() == null || ruleSet.getRules().isEmpty()) {
			LOGGER.error("the ruleSet is null or empty");
			throw new InvalidParameterException("the ruleSet is null or empty");
		}

		// dispatch
		// for example: [bizTypeCode=maketing-system-tag, ruleSetCode=null, ruleSetVersion=null, bizCode=1, datas={keyid=1}]
		this.executionDispatcher.dispatch(request.getBizCode(), ruleSet, request.getDatas());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MatchService#match(java.lang.String, java.lang.String)
	 */
	@Override
	public void match(final String bizTypeCode, final String bizCode) {
		// check
		if (bizTypeCode == null) {
			LOGGER.error("the bizTypeCode is null");
			throw new InvalidParameterException("the bizTypeCode is null");
		}
		if (bizCode == null) {
			LOGGER.error("the bizCode is null");
			throw new InvalidParameterException("the bizCode is null");
		}

		// bizType
		final BizType bizType = this.bizTypeService.getBizType(bizTypeCode);
		if (bizType == null) {
			LOGGER.error("the bizType is NOT existed, bizTypeCode: {}", bizTypeCode);
			throw new InvalidParameterException("the bizType is NOT existed, bizTypeCode: " + bizTypeCode);
		}
		if (bizType.getRuleSet() == null || bizType.getRuleSet().getRules() == null || bizType.getRuleSet().getRules().isEmpty()) {
			throw new InvalidParameterException("the ruleSet of the bizType is null or empty, bizTypeCode: " + bizTypeCode);
		}
		// rules
		final List<Rule> rules = bizType.getRuleSet().getRules();
		if (rules == null || rules.isEmpty()) {
			LOGGER.error("the rules of the bizType is NOT existed, bizTypeCode: {}", bizTypeCode);
			throw new InvalidParameterException("the rules of the bizType is NOT existed, bizTypeCode: " + bizTypeCode);
		}
		// get params by each rule
		final Map<String, String> params = new HashMap<String, String>();
		for (final Rule rule : rules) {
			final Map<String, String> ps = this.dataExtractor.extractRuleDatas(rule, bizCode);
			if (ps != null) {
				params.putAll(ps);
			}
		}
		// dispatch
		this.executionDispatcher.dispatch(bizCode, bizType.getRuleSet(), params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MatchService#match(java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 *
	 */
	@Override
	public void match(final String bizTypeCode, final String bizCode, final String ruleSetCode, final Long ruleSetVersion) {
		if (ruleSetCode == null || ruleSetVersion == null) {
			// no ruleSetCode
			this.match(bizTypeCode, bizCode);
			return;
		}
		// check
		if (bizTypeCode == null) {
			LOGGER.error("the bizTypeCode is null");
			throw new InvalidParameterException("the bizTypeCode is null");
		}
		if (bizCode == null) {
			LOGGER.error("the bizCode is null");
			throw new InvalidParameterException("the bizCode is null");
		}
		// bizType
		final BizType bizType = this.bizTypeService.getSimpleBizType(bizTypeCode);
		if (bizType == null) {
			LOGGER.error("the bizType is NOT existed, bizTypeCode: {}", bizTypeCode);
			throw new InvalidParameterException("the bizType is NOT existed, bizTypeCode: " + bizTypeCode);
		}
		// ruleSet
		final RuleSet ruleSet = this.ruleService.getRuleSet(ruleSetCode, ruleSetVersion);
		// check bizType
		if (ruleSet != null && !bizTypeCode.equals(ruleSet.getBizTypeCode())) {
			// invalid ruleSet bizType
			LOGGER.error("invalid bizType of the ruleSet, request.bizTypeCode: {}, ruleSet.bizTypeCode: {}", bizTypeCode, ruleSet.getBizTypeCode());
			throw new InvalidParameterException("invalid bizType of the ruleSet, request.bizTypeCode: " + bizTypeCode + ", ruleSet.bizTypeCode: " + ruleSet.getBizTypeCode());
		}
		// check rules
		if (ruleSet.getRules() == null || ruleSet.getRules().isEmpty()) {
			LOGGER.error("the ruleSet is null or empty");
			throw new InvalidParameterException("the ruleSet is null or empty");
		}
		// rules
		final List<Rule> rules = bizType.getRuleSet().getRules();
		if (rules == null || rules.isEmpty()) {
			LOGGER.error("the rules of the bizType is NOT existed, bizTypeCode: {}", bizTypeCode);
			throw new InvalidParameterException("the rules of the bizType is NOT existed, bizTypeCode: " + bizTypeCode);
		}
		// get params by each rule
		final Map<String, String> params = new HashMap<String, String>();
		for (final Rule rule : rules) {
			final Map<String, String> ps = this.dataExtractor.extractRuleDatas(rule, bizCode);
			if (ps != null) {
				params.putAll(ps);
			}
		}
		// dispatch
		this.executionDispatcher.dispatch(bizCode, bizType.getRuleSet(), params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MatchService#evaluate(EvalRequest)
	 */
	@Override
	public EvalResult evaluate(final EvalRequest request) {
		// check
		if (request == null) {
			throw new InvalidParameterException("the eval request is null");
		}
		if (request.getRuleExpression() == null) {
			throw new InvalidParameterException("the eval request expression is null");
		}
		LOGGER.debug("evaluating rule, request: {}", request);
		// result
		final EvalResult result = new EvalResult();
		result.setBizCode(request.getBizCode());
		final long ts = System.currentTimeMillis();
		// execute
		if (MatchType.ACCEPTANCE == request.getMatchType()) {
			// acceptance
			final AcceptanceResult ar = this.acceptanceExecutor.evaluate(request.getRuleExpression(), request.getExpressionSegments(), request.getParams());
			result.setErrorMessage(ar.getErrorMessage());
			if (ar.getResult() != null) {
				result.setResult(ar.getResult().toString());
			}
		} else if (MatchType.SCORE == request.getMatchType()) {
			// score
			final ScoreResult sr = this.scoreExecutor.evaluate(request.getRuleExpression(), request.getExpressionSegments(), request.getParams());
			result.setErrorMessage(sr.getErrorMessage());
			if (sr.getResult() != null) {
				result.setResult(sr.getResult().toString());
			}
		}

		// execution time
		result.setTime(System.currentTimeMillis() - ts);

		// result
		LOGGER.info("the rule evaluated, request: {}, result: {}", request, result);
		return result;
	}

	/**
	 * @param bizTypeService
	 *            the bizTypeService to set
	 */
	public void setBizTypeService(final BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
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
	 * @param executionDispatcher
	 *            the executionDispatcher to set
	 */
	public void setExecutionDispatcher(final ExecutionDispatcher executionDispatcher) {
		this.executionDispatcher = executionDispatcher;
	}

	/**
	 * @param dataExtractor
	 *            the dataExtractor to set
	 */
	public void setDataExtractor(final DataExtractor dataExtractor) {
		this.dataExtractor = dataExtractor;
	}
}
