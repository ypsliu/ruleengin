/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.exception.DuplicateException;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.exception.StaleObjectStateException;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.model.ui.RuleDesign;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.RuleExpressionBuilder;
import cn.rongcapital.ruleengine.service.RuleService;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;
import cn.rongcapital.ruleengine.utils.JsonHelper;

/**
 * the implementation for RuleService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class RuleServiceImpl implements RuleService {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleServiceImpl.class);

	@Autowired
	private RuleDao ruleDao;

	@Autowired
	private DatetimeProvider datetimeProvider;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private DatasourceManager datasourceManager;

	@Autowired
	private RuleExpressionBuilder ruleExpressionBuilder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#createRule(Rule)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "rules", allEntries = true)
	public Rule createRule(final Rule rule) {
		// check
		if (this.ruleExisted(rule.getCode())) {
			throw new DuplicateException("the rule code already existed, code: " + rule.getCode());
		}
		if (!this.bizTypeService.bizTypeExisted(rule.getBizTypeCode())) {
			throw new NotFoundException("the bizType is NOT existed, code: " + rule.getBizTypeCode());
		}
		// datasources
		rule.setDatasourceCodes(this.getDatasourceCodes(rule.getDatasources()));
		// convert the params to inputParams
		rule.setInputParams(this.convertParams(rule.getParams(), true));
		// the expression Segments
		rule.setExprSegments(this.convertSegments(rule.getExpressionSegments()));
		// creation time
		rule.setCreationTime(this.datetimeProvider.nowTime());
		// version
		rule.setVersion(1L);
		// extractSqls version
		if (rule.getExtractSqls() != null) {
			for (final ExtractSql sql : rule.getExtractSqls()) {
				sql.setRuleVersion(rule.getVersion());
			}
		}
		// build the expression
		rule.setExpression(this.ruleExpressionBuilder.build(rule.getDesign()));
		// set the design value
		try {
			rule.setDesignValue(JsonHelper.toJsonString(rule.getDesign()));
		} catch (IOException e) {
			LOGGER.error("conver the rule design to string error: " + e.getMessage(), e);
			throw new InvalidParameterException("conver the rule design to string error: " + e.getMessage(), e);
		}
		LOGGER.debug("creating the rule: {}", rule);
		this.ruleDao.createRule(rule);
		this.updateExtractSqls(rule.getCode(), rule.getExtractSqls());
		LOGGER.info("the rule created: {}", rule);
		// remove the inputParams
		rule.setInputParams(null);
		// remove the datasourceCodes info
		rule.setDatasourceCodes(null);
		// remove the expression Segments
		rule.setExprSegments(null);
		// remove the design value
		rule.setDesignValue(null);
		return rule;
	}

	private String convertSegments(final Map<String, String> segments) {
		if (segments == null || segments.isEmpty()) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		for (final String key : segments.keySet()) {
			if (buf.length() > 0) {
				buf.append(",");
			}
			buf.append(key).append("=");
			if (segments.get(key) != null) {
				buf.append(segments.get(key));
			}
		}
		return buf.toString();
	}

	private Map<String, String> convertSegments(final String segments) {
		if (segments == null || !segments.contains("=")) {
			return null;
		}
		final Map<String, String> map = new TreeMap<String, String>();
		final String[] a = segments.split("\\,", -1);
		for (final String s : a) {
			final String[] b = s.split("\\=", 2);
			map.put(b[0], b[1]);
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRule(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "rules")
	public Rule getRule(final String code) {
		final Rule rule = this.ruleDao.getRule(code);
		if (rule != null) {
			// set the bizType
			final BizType b = this.bizTypeService.getSimpleBizType(rule.getBizTypeCode());
			if (b != null) {
				rule.setBizType(b);
			}
			// datasources
			rule.setDatasources(this.getDatasources(rule.getDatasourceCodes()));
			// remove the datasourceCodes info
			rule.setDatasourceCodes(null);
			// convert the inputParams to params
			rule.setParams(this.convertParams(rule.getInputParams(), true));
			// remove the inputParams
			rule.setInputParams(null);
			// convert the expression Segments
			rule.setExpressionSegments(this.convertSegments(rule.getExprSegments()));
			// remove the expression Segments
			rule.setExprSegments(null);
			// extract SQLs
			rule.setExtractSqls(this.loadExtractSqls(rule.getCode()));
			// rule design
			try {
				rule.setDesign(JsonHelper.toObject(rule.getDesignValue(), RuleDesign.class));
			} catch (IOException e) {
				LOGGER.error("convert the rule design value to object error: " + e.getMessage(), e);
				throw new RuntimeException("convert the rule design value to object error: " + e.getMessage(), e);
			}
			// remove the rule desing value
			rule.setDesignValue(null);
			return rule;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#updateRule(Rule)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "rules", allEntries = true)
	public void updateRule(final Rule rule) {
		// check
		if (!this.bizTypeService.bizTypeExisted(rule.getBizTypeCode())) {
			throw new NotFoundException("the bizType is NOT existed, code: " + rule.getBizTypeCode());
		}
		// the old rule
		final Rule old = this.getRule(rule.getCode());
		if (old == null) {
			throw new NotFoundException("the rule is NOT existed, code: " + rule.getCode());
		}
		// set the version to old version
		rule.setVersion(old.getVersion());
		// datasources
		rule.setDatasourceCodes(this.getDatasourceCodes(rule.getDatasources()));
		// convert the params to inputParams
		rule.setInputParams(this.convertParams(rule.getParams(), true));
		// the expression Segments
		rule.setExprSegments(this.convertSegments(rule.getExpressionSegments()));
		// build the expression
		rule.setExpression(this.ruleExpressionBuilder.build(rule.getDesign()));
		// set the design value
		try {
			rule.setDesignValue(JsonHelper.toJsonString(rule.getDesign()));
		} catch (IOException e) {
			LOGGER.error("conver the rule design to string error: " + e.getMessage(), e);
			throw new InvalidParameterException("conver the rule design to string error: " + e.getMessage(), e);
		}
		// update time
		rule.setUpdateTime(this.datetimeProvider.nowTime());
		LOGGER.debug("updating the rule: {}", rule);
		// create the rule history
		old.setDatasourceCodes(this.getDatasourceCodes(old.getDatasources()));
		old.setInputParams(this.convertParams(old.getParams(), true));
		old.setExprSegments(this.convertSegments(old.getExpressionSegments()));
		this.ruleDao.createRuleHistory(old);
		// create the extractSqls histories
		if (old.getExtractSqls() != null) {
			for (final ExtractSql sql : old.getExtractSqls()) {
				sql.setInputParams(this.convertParams(sql.getParams(), false));
				sql.setColumnsValue(this.convertParams(sql.getColumns(), true));
				sql.setConditionsValue(this.convertSegments(sql.getConditions()));
				this.ruleDao.createExtractSqlHistory(sql);
			}
		}
		// update the rule
		if (this.ruleDao.updateRule(rule) != 1) {
			throw new StaleObjectStateException();
		}
		// set the extractSqls ruleVersion to old rule version
		if (rule.getExtractSqls() != null) {
			for (final ExtractSql sql : rule.getExtractSqls()) {
				sql.setRuleVersion(rule.getVersion() + 1);
			}
		}
		// update the extractSqls
		this.updateExtractSqls(rule.getCode(), rule.getExtractSqls());
		// remove the inputParams
		rule.setInputParams(null);
		// remove the datasourceCodes info
		rule.setDatasourceCodes(null);
		// remove the expression Segments
		rule.setExprSegments(null);
		// remove rule design
		rule.setDesignValue(null);
		LOGGER.info("the rule updated: {}", rule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#removeRule(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "rules", allEntries = true)
	public void removeRule(final String code) {
		if (!this.ruleExisted(code)) {
			throw new NotFoundException("the rule is NOT existed, code: " + code);
		}
		LOGGER.debug("removing the rule, code: {}", code);
		this.ruleDao.removeExtractSqls(code);
		this.ruleDao.removeRule(code, this.datetimeProvider.nowTime());
		LOGGER.info("the rule removed, code: {}", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRulesByBizType(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "rules")
	public List<Rule> getRulesByBizType(final String bizTypeCode) {
		final List<Rule> list = this.ruleDao.getRulesByBizType(bizTypeCode);
		if (list != null) {
			for (final Rule r : list) {
				// set the bizType
				r.setBizType(this.bizTypeService.getSimpleBizType(r.getBizTypeCode()));
				// datasources
				r.setDatasources(this.getDatasources(r.getDatasourceCodes()));
				// remove the datasourceCodes info
				r.setDatasourceCodes(null);
				// convert the inputParams to params
				r.setParams(this.convertParams(r.getInputParams(), true));
				// remove the inputParams
				r.setInputParams(null);
				// convert the expression Segments
				r.setExpressionSegments(this.convertSegments(r.getExprSegments()));
				// remove the expression Segments
				r.setExprSegments(null);
				// extract SQLs
				r.setExtractSqls(this.loadExtractSqls(r.getCode()));
				// remove rule design info for list view
				r.setDesignValue(null);
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRules()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "rules")
	public List<Rule> getRules() {
		final List<Rule> list = this.ruleDao.getAllRules();
		if (list != null) {
			for (final Rule r : list) {
				// set the bizType
				r.setBizType(this.bizTypeService.getSimpleBizType(r.getBizTypeCode()));
				// datasources
				r.setDatasources(this.getDatasources(r.getDatasourceCodes()));
				// remove the datasourceCodes info
				r.setDatasourceCodes(null);
				// convert the inputParams to params
				r.setParams(this.convertParams(r.getInputParams(), true));
				// remove the inputParams
				r.setInputParams(null);
				// convert the expression Segments
				r.setExpressionSegments(this.convertSegments(r.getExprSegments()));
				// remove the expression Segments
				r.setExprSegments(null);
				// extract SQLs
				r.setExtractSqls(this.loadExtractSqls(r.getCode()));
				// remove rule design info for list view
				r.setDesignValue(null);
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#ruleExisted(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "rules")
	public boolean ruleExisted(final String code) {
		return this.ruleDao.countByCode(code) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRule(java.lang.String, long)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Rule getRule(final String code, final long version) {
		// load from current
		Rule rule = this.getRule(code);
		if (rule == null) {
			throw new NotFoundException("the rule is NOT existed, code: " + code);
		}
		if (rule.getVersion() == version) {
			// it is the current version
			return rule;
		}
		// load from history
		rule = this.ruleDao.getRuleHistory(code, version);
		if (rule == null) {
			throw new NotFoundException("the rule is NOT existed, code: " + code + ", version: " + version);
		}
		// set the bizType
		final BizType b = this.bizTypeService.getSimpleBizType(rule.getBizTypeCode());
		if (b != null) {
			rule.setBizType(b);
		}
		// datasources
		rule.setDatasources(this.getDatasources(rule.getDatasourceCodes()));
		// remove the datasourceCodes info
		rule.setDatasourceCodes(null);
		// convert the inputParams to params
		rule.setParams(this.convertParams(rule.getInputParams(), true));
		// remove the inputParams
		rule.setInputParams(null);
		// convert the expression Segments
		rule.setExpressionSegments(this.convertSegments(rule.getExprSegments()));
		// remove the expression Segments
		rule.setExprSegments(null);
		// extract SQLs
		rule.setExtractSqls(this.loadExtractSqlHistories(rule.getCode(), version));
		// rule design
		try {
			rule.setDesign(JsonHelper.toObject(rule.getDesignValue(), RuleDesign.class));
		} catch (IOException e) {
			LOGGER.error("convert the rule design value to object error: " + e.getMessage(), e);
			throw new RuntimeException("convert the rule design value to object error: " + e.getMessage(), e);
		}
		// remove the rule desing value
		rule.setDesignValue(null);
		return rule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRuleHistories(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Rule> getRuleHistories(final String code) {
		final List<Rule> histories = this.ruleDao.getRuleHistories(code);
		if (histories != null) {
			for (final Rule rule : histories) {
				// set the bizType
				final BizType b = this.bizTypeService.getSimpleBizType(rule.getBizTypeCode());
				if (b != null) {
					rule.setBizType(b);
				}
				// datasources
				rule.setDatasources(this.getDatasources(rule.getDatasourceCodes()));
				// remove the datasourceCodes info
				rule.setDatasourceCodes(null);
				// convert the inputParams to params
				rule.setParams(this.convertParams(rule.getInputParams(), true));
				// remove the inputParams
				rule.setInputParams(null);
				// convert the expression Segments
				rule.setExpressionSegments(this.convertSegments(rule.getExprSegments()));
				// remove the expression Segments
				rule.setExprSegments(null);
				// extract SQLs
				rule.setExtractSqls(this.loadExtractSqlHistories(rule.getCode(), rule.getVersion()));
				// remove the rule design info for list view
				rule.setDesignValue(null);
			}
		}
		return histories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#createRuleSet(RuleSet)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = { "bizTypes", "ruleSets" }, allEntries = true)
	public RuleSet createRuleSet(final RuleSet ruleSet) {
		// check
		if (this.ruleSetExisted(ruleSet.getCode())) {
			throw new DuplicateException("the ruleSet code already existed, code: " + ruleSet.getCode());
		}
		if (!this.bizTypeService.bizTypeExisted(ruleSet.getBizTypeCode())) {
			throw new NotFoundException("the bizType is NOT existed, code: " + ruleSet.getBizTypeCode());
		}
		if (ruleSet.getRules() == null || ruleSet.getRules().isEmpty()) {
			throw new InvalidParameterException("the rules is null or empty");
		}
		final StringBuilder buf = new StringBuilder();
		final Set<String> ruleCodes = new HashSet<String>();
		for (final Rule rule : ruleSet.getRules()) {
			if (ruleCodes.contains(rule.getCode())) {
				// skip the duplicate rule
				continue;
			}
			Rule old = null;
			if (rule.getVersion() > 0) {
				old = this.getRule(rule.getCode(), rule.getVersion());
			} else {
				old = this.ruleDao.getRule(rule.getCode());
			}
			if (old == null) {
				throw new NotFoundException("the rule is NOT existed, code: " + rule.getCode());
			}
			// check the bizType
			if (!old.getBizTypeCode().equals(ruleSet.getBizTypeCode())) {
				throw new InvalidParameterException("invalid bizTypeCode, ruleSet.bizTypeCode: "
						+ ruleSet.getBizTypeCode() + ", rule.bizTypeCode: " + old.getBizTypeCode());
			}
			// keep the code
			ruleCodes.add(rule.getCode());
			// set the version
			rule.setVersion(old.getVersion());
			if (buf.length() > 0) {
				buf.append(";");
			}
			buf.append(rule.getCode()).append(",").append(rule.getVersion());
		}
		// set the rulesValue
		ruleSet.setRulesValue(buf.toString());
		// creation time
		ruleSet.setCreationTime(this.datetimeProvider.nowTime());
		// version
		ruleSet.setVersion(1L);
		// save it
		LOGGER.debug("creating the ruleSet: {}", ruleSet);
		this.ruleDao.createRuleSet(ruleSet);
		LOGGER.info("the ruleSet created: {}", ruleSet);
		// remove the rulesValue
		ruleSet.setRulesValue(null);
		return ruleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRuleSet(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "ruleSets")
	public RuleSet getRuleSet(final String code) {
		final RuleSet ruleSet = this.ruleDao.getRuleSet(code);
		if (ruleSet != null) {
			// load rules
			this.loadRules(ruleSet);
			// remove rulesValue
			ruleSet.setRulesValue(null);
		}
		return ruleSet;
	}

	private void loadRules(final RuleSet ruleSet) {
		if (!StringUtils.isEmpty(ruleSet.getRulesValue())) {
			final String[] a = ruleSet.getRulesValue().split("\\;", -1);
			if (a != null && a.length > 0) {
				final List<Rule> rules = new ArrayList<Rule>();
				for (final String b : a) {
					final String[] c = b.split("\\,", 2);
					if (c == null || c.length != 2) {
						throw new RuntimeException("invalid format, can not load the rule by: " + b);
					}
					long version = Long.parseLong(c[1]);
					final Rule rule;
					if (version > 0) {
						rule = this.getRule(c[0], version);
					} else {
						rule = this.getRule(c[0]);
					}
					rules.add(rule);
				}
				ruleSet.setRules(rules);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRuleSet(java.lang.String, long)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "ruleSets")
	public RuleSet getRuleSet(final String code, final long version) {
		RuleSet ruleSet = this.getRuleSet(code);
		if (ruleSet == null) {
			throw new NotFoundException("the ruleSet is NOT existed, code: " + code);
		}
		if (ruleSet.getVersion() == version) {
			// it is the current version
			return ruleSet;
		}
		// load from history
		ruleSet = this.ruleDao.getRuleSetHistory(code, version);
		if (ruleSet == null) {
			throw new NotFoundException("the ruleSet is NOT existed, code: " + code + ", version: " + version);
		}
		// load rules
		this.loadRules(ruleSet);
		// remove rulesValue
		ruleSet.setRulesValue(null);
		return ruleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRuleSetHistories(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<RuleSet> getRuleSetHistories(final String code) {
		final List<RuleSet> histories = this.ruleDao.getRuleSetHistories(code);
		if (histories != null) {
			for (final RuleSet ruleSet : histories) {
				// load rules
				this.loadRules(ruleSet);
				// remove rulesValue
				ruleSet.setRulesValue(null);
			}
		}
		return histories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#updateRuleSet(RuleSet)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = { "bizTypes", "ruleSets" }, allEntries = true)
	public void updateRuleSet(final RuleSet ruleSet) {
		// check
		if (ruleSet.getRules() == null && ruleSet.getRules().isEmpty()) {
			throw new InvalidParameterException("the rules is null or empty");
		}
		if (!this.bizTypeService.bizTypeExisted(ruleSet.getBizTypeCode())) {
			throw new NotFoundException("the bizType is NOT existed, code: " + ruleSet.getBizTypeCode());
		}
		// the old ruleSet
		final RuleSet old = this.ruleDao.getRuleSet(ruleSet.getCode());
		if (old == null) {
			throw new NotFoundException("the ruleSet is NOT existed, code: " + ruleSet.getCode());
		}
		// the rulesValue
		final StringBuilder buf = new StringBuilder();
		final Set<String> ruleCodes = new HashSet<String>();
		for (final Rule rule : ruleSet.getRules()) {
			if (ruleCodes.contains(rule.getCode())) {
				// skip the duplicate rule
				continue;
			}
			Rule r = null;
			if (rule.getVersion() > 0) {
				r = this.getRule(rule.getCode(), rule.getVersion());
			} else {
				r = this.ruleDao.getRule(rule.getCode());
			}
			if (r == null) {
				throw new NotFoundException("the rule is NOT existed, code: " + rule.getCode());
			}
			// check the bizType
			if (!r.getBizTypeCode().equals(ruleSet.getBizTypeCode())) {
				throw new InvalidParameterException("invalid bizTypeCode, ruleSet.bizTypeCode: "
						+ ruleSet.getBizTypeCode() + ", rule.bizTypeCode: " + r.getBizTypeCode());
			}
			// keep the code
			ruleCodes.add(rule.getCode());
			// set the version
			rule.setVersion(r.getVersion());
			if (buf.length() > 0) {
				buf.append(";");
			}
			buf.append(rule.getCode()).append(",").append(rule.getVersion());
		}
		ruleSet.setRulesValue(buf.toString());
		// set the version to old version
		ruleSet.setVersion(old.getVersion());
		// update time
		ruleSet.setUpdateTime(this.datetimeProvider.nowTime());
		LOGGER.debug("updating the ruleSet: {}", ruleSet);
		// history
		this.ruleDao.createRuleSetHistory(old);
		// update
		if (this.ruleDao.updateRuleSet(ruleSet) != 1) {
			throw new StaleObjectStateException();
		}
		// remove the rulesValue
		ruleSet.setRulesValue(null);
		LOGGER.info("the ruleSet updated: {}", ruleSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#removeRuleSet(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = { "bizTypes", "ruleSets" }, allEntries = true)
	public void removeRuleSet(final String code) {
		if (!this.ruleSetExisted(code)) {
			throw new NotFoundException("the ruleSet is NOT existed, code: " + code);
		}
		LOGGER.debug("removing the ruleSet, code: ", code);
		this.ruleDao.removeRuleSet(code, this.datetimeProvider.nowTime());
		LOGGER.info("the ruleSet removed, code: ", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRuleSetsByBizType(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "ruleSets")
	public List<RuleSet> getRuleSetsByBizType(final String bizTypeCode) {
		final List<RuleSet> ruleSets = this.ruleDao.getRuleSetsByBizType(bizTypeCode);
		if (ruleSets != null) {
			for (final RuleSet ruleSet : ruleSets) {
				// load rules
				this.loadRules(ruleSet);
				// remove rulesValue
				ruleSet.setRulesValue(null);
			}
		}
		return ruleSets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#ruleSetExisted(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "ruleSets")
	public boolean ruleSetExisted(final String code) {
		return this.ruleDao.countRuleSetByCode(code) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RuleService#getRuleSets()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "ruleSets")
	public List<RuleSet> getRuleSets() {
		final List<RuleSet> ruleSets = this.ruleDao.getAllRuleSets();
		if (ruleSets != null) {
			for (final RuleSet ruleSet : ruleSets) {
				// load rules
				this.loadRules(ruleSet);
				// remove rulesValue
				ruleSet.setRulesValue(null);
			}
		}
		return ruleSets;
	}

	private String convertParams(final List<String> params, final boolean unique) {
		if (params == null || params.isEmpty()) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		final Set<String> set = new HashSet<String>();
		for (int i = 0; i < params.size(); i++) {
			final String p = params.get(i);
			if (unique) {
				if (set.contains(p)) {
					continue;
				}
				set.add(p);
			}
			buf.append(p);
			if (i < params.size() - 1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	private List<String> convertParams(final String inputParams, final boolean unique) {
		if (inputParams == null) {
			return null;
		}
		final List<String> params = new ArrayList<String>();
		final String[] a = inputParams.split("\\,", -1);
		if (a != null) {
			final Set<String> set = new HashSet<String>();
			for (final String p : a) {
				if (unique) {
					if (set.contains(p)) {
						continue;
					}
					set.add(p);
				}
				params.add(p);
			}
		}
		return params;
	}

	private List<Datasource> getDatasources(final String datasourceCodes) {
		if (datasourceCodes == null) {
			return null;
		}
		final List<Datasource> list = new ArrayList<Datasource>();
		final String[] a = datasourceCodes.split("\\,", -1);
		if (a != null) {
			final Set<String> codeSet = new HashSet<String>();
			for (final String code : a) {
				if (codeSet.contains(code)) {
					continue;
				}
				final Datasource ds = this.datasourceManager.getDatasource(code);
				if (ds == null) {
					throw new NotFoundException("the datasource is NOT existed, code: " + code);
				}
				list.add(ds);
				codeSet.add(code);
			}
		}
		return list;
	}

	private String getDatasourceCodes(final List<Datasource> datasources) {
		if (datasources == null || datasources.isEmpty()) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		final Set<String> codeSet = new HashSet<String>();
		for (int i = 0; i < datasources.size(); i++) {
			final Datasource ds = datasources.get(i);
			if (codeSet.contains(ds.getCode())) {
				continue;
			}
			if (!this.datasourceManager.datasourceExisted(ds.getCode())) {
				throw new NotFoundException("the datasource is NOT existed, code: " + ds.getCode());
			}
			buf.append(ds.getCode());
			if (i < datasources.size() - 1) {
				buf.append(",");
			}
			codeSet.add(ds.getCode());
		}
		return buf.toString();
	}

	private void updateExtractSqls(final String ruleCode, final List<ExtractSql> extractSqls) {
		this.ruleDao.removeExtractSqls(ruleCode);
		if (extractSqls != null) {
			for (final ExtractSql es : extractSqls) {
				// check
				if (es.getDatasourceCode() == null) {
					throw new InvalidParameterException("the datasource code of the extractSql is null");
				}
				if (es.getSql() == null) {
					throw new InvalidParameterException("the sql of the extractSql is null");
				}
				// set the ruleCode
				es.setRuleCode(ruleCode);
				// params
				es.setInputParams(this.convertParams(es.getParams(), false));
				// columns
				es.setColumnsValue(this.convertParams(es.getColumns(), true));
				// conditions
				es.setConditionsValue(this.convertSegments(es.getConditions()));
				this.ruleDao.createExtractSql(es);
				// remove the params info
				es.setInputParams(null);
				// remove the columns
				es.setColumnsValue(null);
				// remove the conditions
				es.setConditionsValue(null);
			}
		}
	}

	private List<ExtractSql> loadExtractSqls(final String ruleCode) {
		final List<ExtractSql> sqls = this.ruleDao.getExtractSqls(ruleCode);
		if (sqls != null) {
			for (final ExtractSql es : sqls) {
				// params
				es.setParams(this.convertParams(es.getInputParams(), false));
				es.setInputParams(null);
				// columns
				es.setColumns(this.convertParams(es.getColumnsValue(), true));
				es.setColumnsValue(null);
				// conditions
				es.setConditions(this.convertSegments(es.getConditionsValue()));
				es.setConditionsValue(null);
			}
		}
		return sqls;
	}

	private List<ExtractSql> loadExtractSqlHistories(final String ruleCode, final long ruleVersion) {
		final List<ExtractSql> sqls = this.ruleDao.getExtractSqlFromHistory(ruleCode, ruleVersion);
		if (sqls != null) {
			for (final ExtractSql es : sqls) {
				// params
				es.setParams(this.convertParams(es.getInputParams(), false));
				es.setInputParams(null);
				// columns
				es.setColumns(this.convertParams(es.getColumnsValue(), true));
				es.setColumnsValue(null);
				// conditions
				es.setConditions(this.convertSegments(es.getConditionsValue()));
				es.setConditionsValue(null);
			}
		}
		return sqls;
	}

	/**
	 * @param ruleDao
	 *            the ruleDao to set
	 */
	public void setRuleDao(final RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

	/**
	 * @param bizTypeService
	 *            the bizTypeService to set
	 */
	public void setBizTypeService(final BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
	}

	/**
	 * @param datasourceManager
	 *            the datasourceManager to set
	 */
	public void setDatasourceManager(final DatasourceManager datasourceManager) {
		this.datasourceManager = datasourceManager;
	}

}
