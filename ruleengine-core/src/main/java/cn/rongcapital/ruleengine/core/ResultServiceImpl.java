/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.*;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.ResultService;
import cn.rongcapital.ruleengine.service.RuleService;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * the implementation for ResultService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class ResultServiceImpl implements ResultService {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

	@Autowired
	private DatetimeProvider datetimeProvider;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private RuleService ruleService;

	@Autowired
	private ResultDao resultDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getMatchResult(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public MatchResult getMatchResult(final String bizTypeCode, final String bizCode) {
		final BizType bizType = this.bizTypeService.getSimpleBizType(bizTypeCode);
		if (bizType == null) {
			throw new InvalidParameterException("the bizType is NOT existed: " + bizTypeCode);
		}
		return this.getMatchResult(bizTypeCode, bizType.getRuleSetCode(), bizType.getRuleSetVersion(), bizCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getMatchResult(java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.String)
	 *
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public MatchResult getMatchResult(final String bizTypeCode, final String ruleSetCode, final Long ruleSetVersion, final String bizCode) {
		final AcceptanceResults acceptanceResult = this.getAcceptanceResult(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode);
		final ScoreResults scoreResult = this.getScoreResult(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode);
		final TextResults textResult = this.getTextResult(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode);
		if (acceptanceResult == null && scoreResult == null && textResult == null) {
			return null;
		}

		final MatchResult matchResult = new MatchResult();
		matchResult.setAcceptance(acceptanceResult);
		matchResult.setScore(scoreResult);
		matchResult.setText(textResult);
		return matchResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getAcceptanceResult(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public AcceptanceResults getAcceptanceResult(final String bizTypeCode, final String bizCode) {
		final BizType bizType = this.bizTypeService.getSimpleBizType(bizTypeCode);
		if (bizType == null) {
			throw new InvalidParameterException("the bizType is NOT existed: " + bizTypeCode);
		}

		return this.getAcceptanceResult(bizTypeCode, bizType.getRuleSetCode(), bizType.getRuleSetVersion(), bizCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getAcceptanceResult(java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public AcceptanceResults getAcceptanceResult(final String bizTypeCode, final String ruleSetCode, final Long ruleSetVersion, String bizCode) {
		final List<ResultPojo> list = this.distinctResults(this.resultDao.getAcceptanceResults(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode));
		if (list == null || list.isEmpty()) {
			return null;
		}
		// create
		final AcceptanceResults acceptanceResults = new AcceptanceResults();
		// copy infos
		this.copyInfos(list.get(0), acceptanceResults);
		// bizType
		final BizType bizType = this.bizTypeService.getSimpleBizType(acceptanceResults.getBizTypeCode());
		// r.setBizType(bizType);
		acceptanceResults.setBizTypeName(bizType.getName());
		acceptanceResults.setBizTypeComment(bizType.getComment());
		// results
		final List<AcceptanceResult> results = new ArrayList<AcceptanceResult>(list.size());
		for (final ResultPojo pojo : list) {
			// create
			final AcceptanceResult acceptanceResult = new AcceptanceResult();
			// copy infos
			this.copyInfos(pojo, acceptanceResult);
			// rule info
			final Rule rule = this.ruleService.getRule(pojo.getRuleCode(), pojo.getRuleVersion());
			// ar.setRule(rule);
			acceptanceResult.setRuleName(rule.getName());
			acceptanceResult.setRuleComment(rule.getComment());
			// param
			if (pojo.getResult() != null) {
				acceptanceResult.setResult(Acceptance.valueOf(pojo.getResult()));
			}
			// stages
			acceptanceResult.setStages(this.loadStages(pojo.getExecutionId()));

			// add to results
			results.add(acceptanceResult);
		}
		acceptanceResults.setResults(results);
		return acceptanceResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getScoreResult(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ScoreResults getScoreResult(final String bizTypeCode, final String bizCode) {
		final BizType bizType = this.bizTypeService.getSimpleBizType(bizTypeCode);
		if (bizType == null) {
			throw new InvalidParameterException("the bizType is NOT existed: " + bizTypeCode);
		}

		return this.getScoreResult(bizTypeCode, bizType.getRuleSetCode(), bizType.getRuleSetVersion(), bizCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getScoreResult(java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ScoreResults getScoreResult(final String bizTypeCode, final String ruleSetCode, final Long ruleSetVersion, final String bizCode) {
		final List<ResultPojo> list = this.distinctResults(this.resultDao.getScoreResults(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode));
		if (list == null || list.isEmpty()) {
			return null;
		}
		// create
		final ScoreResults scoreResults = new ScoreResults();
		// copy infos
		this.copyInfos(list.get(0), scoreResults);
		// bizType
		final BizType bizType = this.bizTypeService.getSimpleBizType(scoreResults.getBizTypeCode());
		// r.setBizType(bizType);
		scoreResults.setBizTypeName(bizType.getName());
		scoreResults.setBizTypeComment(bizType.getComment());
		// results
		final List<ScoreResult> results = new ArrayList<ScoreResult>(list.size());
		for (final ResultPojo pojo : list) {
			// create
			final ScoreResult scoreResult = new ScoreResult();
			// copy infos
			this.copyInfos(pojo, scoreResult);
			// rule info
			final Rule rule = this.ruleService.getRule(pojo.getRuleCode(), pojo.getRuleVersion());
			// sr.setRule(rule);
			scoreResult.setRuleName(rule.getName());
			scoreResult.setRuleComment(rule.getComment());
			// param
			if (pojo.getResult() != null) {
				scoreResult.setResult(Integer.parseInt(pojo.getResult()));
			}
			// stages
			scoreResult.setStages(this.loadStages(pojo.getExecutionId()));
			// add to results
			results.add(scoreResult);
		}
		scoreResults.setResults(results);
		return scoreResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getTextResult(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public TextResults getTextResult(final String bizTypeCode, final String bizCode) {
		final BizType bizType = this.bizTypeService.getSimpleBizType(bizTypeCode);
		if (bizType == null) {
			throw new InvalidParameterException("the bizType is NOT existed: " + bizTypeCode);
		}

		return this.getTextResult(bizTypeCode, bizType.getRuleSetCode(), bizType.getRuleSetVersion(), bizCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResultService#getTextResult(java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public TextResults getTextResult(final String bizTypeCode, final String ruleSetCode, final Long ruleSetVersion, final String bizCode) {
		final List<ResultPojo> list = this.distinctResults(this.resultDao.getTextResults(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode));
		if (list == null || list.isEmpty()) {
			return null;
		}
		// create
		final TextResults textResults = new TextResults();
		// copy infos
		this.copyInfos(list.get(0), textResults);
		// bizType
		final BizType bizType = this.bizTypeService.getSimpleBizType(textResults.getBizTypeCode());
		// r.setBizType(bizType);
		textResults.setBizTypeName(bizType.getName());
		textResults.setBizTypeComment(bizType.getComment());
		// results
		final List<TextResult> results = new ArrayList<TextResult>(list.size());
		for (final ResultPojo pojo : list) {
			// create
			final TextResult textResult = new TextResult();
			// copy infos
			this.copyInfos(pojo, textResult);
			// rule info
			final Rule rule = this.ruleService.getRule(pojo.getRuleCode(), pojo.getRuleVersion());
			// sr.setRule(rule);
			textResult.setRuleName(rule.getName());
			textResult.setRuleComment(rule.getComment());
			// param
			if (pojo.getResult() != null) {
				textResult.setResult(pojo.getResult());
			}
			// stages
			textResult.setStages(this.loadStages(pojo.getExecutionId()));
			// add to results
			results.add(textResult);
		}
		textResults.setResults(results);
		return textResults;
	}

	/**
	 *
	 * @param results
	 * @return
	 */
	private List<ResultPojo> distinctResults(final List<ResultPojo> results) {
		// check
		if (results == null || results.isEmpty()) {
			return null;
		}
		final List<ResultPojo> list = new ArrayList<ResultPojo>();
		// last ruleCode
		String lastRuleCode = null;
		for (final ResultPojo pojo : results) {
			if (!pojo.getRuleCode().equals(lastRuleCode)) {
				// the first one
				lastRuleCode = pojo.getRuleCode();
				list.add(pojo);
			}
		}

		return list;
	}

	/**
	 *
	 * @param param
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveResult(List<ResultPojo> param) {
		this.resultDao.saveResults(param);
	}

	/**
	 *
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ResultPojo> getResult(final AcceptanceResults param) {

		// precondition checking
		if (param == null || param.getResults() == null || param.getResults().isEmpty()) {
			return new LinkedList<ResultPojo>();
		}
		if (!this.bizTypeService.bizTypeExisted(param.getBizTypeCode())) {
			throw new InvalidParameterException("the bizType is NOT existed: " + param.getBizTypeCode());
		}
		LOGGER.debug("saving the acceptance results: {}", param);
		final List<ResultPojo> results = new ArrayList<ResultPojo>(param.getResults().size());
		for (final AcceptanceResult acceptanceResult : param.getResults()) {
			// check
			if (acceptanceResult.getRuleCode() == null || acceptanceResult.getRuleVersion() <= 0 || acceptanceResult.getRuleMatchType() == null) {
				throw new InvalidParameterException("invalid rule info");
			}
			// create
			final ResultPojo resultPojo = new ResultPojo();
			// copy infos
			this.copyInfos(param, resultPojo);
			this.copyInfos(acceptanceResult, resultPojo);
			// time
			resultPojo.setTime(this.datetimeProvider.nowTime());
			// param
			if (acceptanceResult.getResult() != null) {
				resultPojo.setResult(acceptanceResult.getResult().toString());
			}

			results.add(resultPojo);

			// save traces
			this.saveStages(acceptanceResult);
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ResultService#saveResult(AcceptanceResults
	 * )
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveResult(final AcceptanceResults param) {
		List<ResultPojo> results = getResult(param);

		// batch save
		this.resultDao.saveResults(results);

		// done
		LOGGER.info("the acceptance results saved: {}", param);
	}

	/**
	 *
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ResultPojo> getResult(final ScoreResults param) {
		// precondition checking
		if (param == null || param.getResults() == null || param.getResults().isEmpty()) {
			return new LinkedList<ResultPojo>();
		}
		if (!this.bizTypeService.bizTypeExisted(param.getBizTypeCode())) {
			throw new InvalidParameterException("the bizType is NOT existed: " + param.getBizTypeCode());
		}
		LOGGER.debug("saving the score results: {}", param);
		final List<ResultPojo> results = new ArrayList<ResultPojo>(param.getResults().size());
		for (final ScoreResult scoreResult : param.getResults()) {
			// check
			if (scoreResult.getRuleCode() == null || scoreResult.getRuleVersion() <= 0 || scoreResult.getRuleMatchType() == null) {
				throw new InvalidParameterException("invalid rule info");
			}
			// create
			final ResultPojo resultPojo = new ResultPojo();
			// copy infos
			this.copyInfos(param, resultPojo);
			this.copyInfos(scoreResult, resultPojo);
			// time
			resultPojo.setTime(this.datetimeProvider.nowTime());
			// param
			if (scoreResult.getResult() != null) {
				resultPojo.setResult(String.valueOf(scoreResult.getResult().intValue()));
			}

			results.add(resultPojo);

			// traces
			this.saveStages(scoreResult);
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ResultService#saveResult(ScoreResults)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveResult(final ScoreResults param) {

		final List<ResultPojo> results = getResult(param);

		// batch save
		this.resultDao.saveResults(results);

		// done
		LOGGER.info("the score results saved: {}", param);
	}

	/**
	 *
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ResultPojo> getResult(final TextResults param) {
		// precondition checking
		if (param == null || param.getResults() == null || param.getResults().isEmpty()) {
			return new LinkedList<ResultPojo>();
		}
		if (!this.bizTypeService.bizTypeExisted(param.getBizTypeCode())) {
			throw new InvalidParameterException("the bizType is NOT existed: " + param.getBizTypeCode());
		}
		LOGGER.debug("saving the text results: {}", param);
		final List<ResultPojo> results = new ArrayList<ResultPojo>(param.getResults().size());
		for (final TextResult textResult : param.getResults()) {
			// check
			if (textResult.getRuleCode() == null || textResult.getRuleVersion() <= 0 || textResult.getRuleMatchType() == null) {
				throw new InvalidParameterException("invalid rule info");
			}
			// create
			final ResultPojo resultPojo = new ResultPojo();
			// copy infos
			this.copyInfos(param, resultPojo);
			this.copyInfos(textResult, resultPojo);
			// time
			resultPojo.setTime(this.datetimeProvider.nowTime());
			// param
			if (textResult.getResult() != null) {
				resultPojo.setResult(textResult.getResult());
			}
			// save it
			results.add(resultPojo);
			// this.resultDao.saveResult(resultPojo);
			// traces
			this.saveStages(textResult);
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ResultService#saveResult(TextResults)
	 */
	@Override
	public void saveResult(final TextResults param) {

		final List<ResultPojo> results = getResult(param);

		// batch save
		this.resultDao.saveResults(results);

		// done
		LOGGER.info("the text results saved: {}", param);
	}

	/**
	 *
	 * @param result
	 */
	private void saveStages(final BaseResult<?> result) {
		if (result.getStages() != null) {
			// process stages
			for (final MatchStage matchStage : result.getStages()) {
				// rule info
				matchStage.setRuleCode(matchStage.getRule().getCode());
				matchStage.setRuleVersion(matchStage.getRule().getVersion());
				// encode traces
				this.encodeTraces(matchStage);
				// this.resultDao.saveMatchStage(param.getExecutionId(), matchStage);
			}
			// batch save
			this.resultDao.saveMatchStages(result.getExecutionId(), result.getStages());
		}
	}

	/**
	 *
	 * @param executionId
	 * @return
	 */
	private List<MatchStage> loadStages(final String executionId) {
		final List<MatchStage> stages = this.resultDao.getMatchStages(executionId);
		if (stages != null) {
			for (final MatchStage stage : stages) {
				// rule
				final Rule rule = this.ruleService.getRule(stage.getRuleCode(), stage.getRuleVersion());
				stage.setRuleName(rule.getName());
				stage.setRuleComment(rule.getComment());
				// decode traces
				this.decodeTraces(stage);
			}
		}

		return stages;
	}

	/**
	 *
	 * @param stage
	 */
	private void encodeTraces(final MatchStage stage) {
		if (stage.getTraces() != null && !stage.getTraces().isEmpty()) {
			final StringBuilder buf = new StringBuilder();
			for (final String trace : stage.getTraces()) {
				if (buf.length() > 0) {
					buf.append("\r\n");
				}
				buf.append(trace);
			}

			stage.setTracesValue(buf.toString());
		}
	}

	/**
	 *
	 *  @param stage
	 *
	 */
	private void decodeTraces(final MatchStage stage) {
		if (stage.getTracesValue() != null) {
			final List<String> traces = new ArrayList<String>();
			final String[] traceArray = stage.getTracesValue().split("\\r\\n", -1);
			for (final String trace : traceArray) {
				traces.add(trace);
			}
			stage.setTraces(traces);

			// remove the value
			stage.setTracesValue(null);
		}
	}

	/**
	 *
	 * @param from
	 * @param to
	 */
	private void copyInfos(final BaseResults<?> from, final ResultPojo to) {
		to.setBizCode(from.getBizCode());
		to.setBizTypeCode(from.getBizTypeCode());
		to.setRuleSetCode(from.getRuleSetCode());
		to.setRuleSetVersion(from.getRuleSetVersion());
	}

	/**
	 *
	 * @param from
	 * @param to
	 */
	private void copyInfos(final ResultPojo from, final BaseResults<?> to) {
		to.setBizCode(from.getBizCode());
		to.setBizTypeCode(from.getBizTypeCode());
		to.setRuleSetCode(from.getRuleSetCode());
		to.setRuleSetVersion(from.getRuleSetVersion());
	}

	/**
	 *
	 * @param from
	 * @param to
	 */
	private void copyInfos(final BaseResult<?> from, final ResultPojo to) {
		to.setRuleCode(from.getRuleCode());
		to.setMatchType(from.getRuleMatchType());
		to.setInputParams(this.convertParams(from.getParams()));
		to.setErrorMessage(from.getErrorMessage());
		to.setRuleVersion(from.getRuleVersion());
		to.setExecutionId(from.getExecutionId());
		to.setSortId(from.getSortId());
	}

	/**
	 *
	 * @param from
	 * @param to
	 */
	private void copyInfos(final ResultPojo from, final BaseResult<?> to) {
		to.setRuleCode(from.getRuleCode());
		to.setParams(this.convertParams(from.getInputParams()));
		to.setErrorMessage(from.getErrorMessage());
		to.setTime(from.getTime());
		to.setRuleVersion(from.getRuleVersion());
		to.setExecutionId(from.getExecutionId());
	}

	/**
	 *
	 * @param inputParams
	 * @return
	 */
	private Map<String, String> convertParams(final String inputParams) {
		if (inputParams == null) {
			return null;
		}
		final String[] paramArray = inputParams.split("\\,", -1);
		if (paramArray != null && paramArray.length > 0) {
			final Map<String, String> params = new HashMap<String, String>(paramArray.length);
			for (final String row : paramArray) {
				final String[] subArray = row.split("\\=", 2);
				if (subArray != null) {
					if (subArray.length == 2) {
						params.put(subArray[0], subArray[1]);
					} else {
						params.put(subArray[0], null);
					}
				}
			}
			return params;
		}
		return null;
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	private String convertParams(final Map<String, String> params) {
		if (params == null || params.isEmpty()) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		int count = 0;
		for (final String key : params.keySet()) {
			buf.append(key).append("=").append(params.get(key));
			count++;
			if (count < params.size()) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	/**
	 * @param resultDao
	 *            the resultDao to set
	 */
	public void setResultDao(final ResultDao resultDao) {
		this.resultDao = resultDao;
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
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}
}
