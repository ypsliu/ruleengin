/**
 * 
 */
package cn.rongcapital.ruleengine.web.resource;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.rongcapital.ruleengine.api.RuleResource;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.RuleService;

/**
 * the implementation for RuleResource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Controller
public final class RuleResourceImpl implements RuleResource {

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private DatasourceManager datasourceManager;

	@Autowired
	private RuleService ruleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#createBizType(cn.rongcapital.ruleengine.model.BizType)
	 */
	@Override
	public BizType createBizType(final BizType bizType) {
		return this.bizTypeService.createBizType(bizType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#updateBizType(java.lang.String,
	 * cn.rongcapital.ruleengine.model.BizType)
	 */
	@Override
	public void updateBizType(final String code, final BizType bizType) {
		bizType.setCode(code);
		this.bizTypeService.updateBizType(bizType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#removeBizType(java.lang.String)
	 */
	@Override
	public void removeBizType(final String code) {
		this.bizTypeService.removeBizType(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getAllBizTypes()
	 */
	@Override
	public List<BizType> getAllBizTypes() {
		final List<BizType> list = this.bizTypeService.getAllBizTypes();
		if (list == null) {
			return Collections.emptyList();
		}
		// clear ruleSet details
		for (final BizType bizType : list) {
			bizType.setRuleSet(null);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getBizType(java.lang.String)
	 */
	@Override
	public BizType getBizType(final String code) {
		final BizType b = this.bizTypeService.getBizType(code);
		if (b == null) {
			throw new NotFoundException("the bizType is NOT existed, code: " + code);
		}
		// clear ruleSet details
		b.setRuleSet(null);
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#assignRuleSet(java.lang.String, java.lang.String, long)
	 */
	@Override
	public void assignRuleSet(final String bizTypecode, final String ruleSetCode, final long ruleSetVersion) {
		this.bizTypeService.assignRuleSet(bizTypecode, ruleSetCode, ruleSetVersion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#createRule(cn.rongcapital.ruleengine.model.Rule)
	 */
	@Override
	public Rule createRule(final Rule rule) {
		return this.ruleService.createRule(rule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#updateRule(java.lang.String,
	 * cn.rongcapital.ruleengine.model.Rule)
	 */
	@Override
	public void updateRule(final String code, final Rule rule) {
		rule.setCode(code);
		this.ruleService.updateRule(rule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#removeRule(java.lang.String)
	 */
	@Override
	public void removeRule(final String code) {
		this.ruleService.removeRule(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRulesByBizType(java.lang.String)
	 */
	@Override
	public List<Rule> getRulesByBizType(final String bizTypeCode) {
		final List<Rule> list = this.ruleService.getRulesByBizType(bizTypeCode);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRules()
	 */
	@Override
	public List<Rule> getRules() {
		final List<Rule> list = this.ruleService.getRules();
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRule(java.lang.String)
	 */
	@Override
	public Rule getRule(final String code) {
		final Rule r = this.ruleService.getRule(code);
		if (r == null) {
			throw new NotFoundException("the rule is NOT existed, code: " + code);
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleHistories(java.lang.String)
	 */
	@Override
	public List<Rule> getRuleHistories(final String code) {
		final List<Rule> list = this.ruleService.getRuleHistories(code);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleHistory(java.lang.String, long)
	 */
	@Override
	public Rule getRuleHistory(final String code, final long version) {
		return this.ruleService.getRule(code, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.rongcapital.ruleengine.api.RuleResource#createDatasource(cn.rongcapital.ruleengine.model.Datasource)
	 */
	@Override
	public Datasource createDatasource(final Datasource datasource) {
		return this.datasourceManager.createDatasource(datasource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#updateDatasource(java.lang.String,
	 * cn.rongcapital.ruleengine.model.Datasource)
	 */
	@Override
	public void updateDatasource(final String code, Datasource datasource) {
		datasource.setCode(code);
		this.datasourceManager.updateDatasource(datasource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getDatasource(java.lang.String)
	 */
	@Override
	public Datasource getDatasource(final String code) {
		final Datasource ds = this.datasourceManager.getDatasource(code);
		if (ds == null) {
			throw new NotFoundException("the datasource is NOT existed, code: " + code);
		}
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getDatasources()
	 */
	@Override
	public List<Datasource> getDatasources() {
		final List<Datasource> list = this.datasourceManager.getDatasources();
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#removeDatasource(java.lang.String)
	 */
	@Override
	public void removeDatasource(final String code) {
		this.datasourceManager.removeDatasource(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#createRuleSet(cn.rongcapital.ruleengine.model.RuleSet)
	 */
	@Override
	public RuleSet createRuleSet(final RuleSet ruleSet) {
		return this.ruleService.createRuleSet(ruleSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#updateRuleSet(java.lang.String,
	 * cn.rongcapital.ruleengine.model.RuleSet)
	 */
	@Override
	public void updateRuleSet(final String code, final RuleSet ruleSet) {
		ruleSet.setCode(code);
		this.ruleService.updateRuleSet(ruleSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#removeRuleSet(java.lang.String)
	 */
	@Override
	public void removeRuleSet(final String code) {
		this.ruleService.removeRuleSet(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleSetsByBizType(java.lang.String)
	 */
	@Override
	public List<RuleSet> getRuleSetsByBizType(final String bizTypeCode) {
		final List<RuleSet> list = this.ruleService.getRuleSetsByBizType(bizTypeCode);
		if (list == null) {
			return Collections.emptyList();
		}
		// clear details
		for (final RuleSet rs : list) {
			this.clearRuleDetails(rs.getRules());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleSet(java.lang.String)
	 */
	@Override
	public RuleSet getRuleSet(final String code) {
		final RuleSet ruleSet = this.ruleService.getRuleSet(code);
		if (ruleSet == null) {
			throw new NotFoundException("the ruleSet is NOT existed, code: " + code);
		}
		// clear details
		this.clearRuleDetails(ruleSet.getRules());
		return ruleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleSetHistories(java.lang.String)
	 */
	@Override
	public List<RuleSet> getRuleSetHistories(final String code) {
		final List<RuleSet> list = this.ruleService.getRuleSetHistories(code);
		if (list == null) {
			return Collections.emptyList();
		}
		// clear details
		for (final RuleSet rs : list) {
			this.clearRuleDetails(rs.getRules());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleSetHistory(java.lang.String, long)
	 */
	@Override
	public RuleSet getRuleSetHistory(final String code, final long version) {
		final RuleSet ruleSet = this.ruleService.getRuleSet(code, version);
		if (ruleSet != null) {
			// clear details
			this.clearRuleDetails(ruleSet.getRules());
		}
		return ruleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RuleResource#getRuleSets()
	 */
	@Override
	public List<RuleSet> getRuleSets() {
		final List<RuleSet> list = this.ruleService.getRuleSets();
		if (list == null) {
			return Collections.emptyList();
		}
		// clear details
		for (final RuleSet rs : list) {
			this.clearRuleDetails(rs.getRules());
		}
		return list;
	}

	private void clearRuleDetails(final List<Rule> rules) {
		if (rules != null) {
			for (final Rule rule : rules) {
				this.clearRuleDetails(rule);
			}
		}
	}

	private void clearRuleDetails(final Rule rule) {
		rule.setBizType(null);
		rule.setCreationTime(null);
		rule.setDatasources(null);
		rule.setExpression(null);
		rule.setExpressionSegments(null);
		rule.setExtractSqls(null);
		rule.setParams(null);
		rule.setUpdateTime(null);
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

	/**
	 * @param ruleService
	 *            the ruleService to set
	 */
	public void setRuleService(final RuleService ruleService) {
		this.ruleService = ruleService;
	}

}
