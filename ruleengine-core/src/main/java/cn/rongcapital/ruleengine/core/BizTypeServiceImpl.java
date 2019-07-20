/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.rongcapital.ruleengine.exception.DuplicateException;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.RuleService;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the implementation for BizTypeService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class BizTypeServiceImpl implements BizTypeService {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BizTypeServiceImpl.class);

	@Autowired
	private BizTypeDao bizTypeDao;

	@Autowired
	private DatetimeProvider datetimeProvider;

	@Autowired
	private RuleService ruleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * BizTypeService#createBizType(BizType)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "bizTypes", allEntries = true)
	public BizType createBizType(final BizType bizType) {
		if (this.bizTypeExisted(bizType.getCode())) {
			throw new DuplicateException("the bizType code already existed, code: " + bizType.getCode());
		}
		// ignore ruleSet info when create the bizType
		bizType.setRuleSet(null);
		bizType.setRuleSetCode(null);
		bizType.setRuleSetVersion(null);
		bizType.setCreationTime(this.datetimeProvider.nowTime());
		LOGGER.debug("creating the bizType: {}", bizType);
		this.bizTypeDao.createBizType(bizType);
		LOGGER.info("the new bizType created: {}", bizType);
		return bizType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BizTypeService#getBizType(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "bizTypes")
	public BizType getBizType(final String code) {
		final BizType bizType = this.bizTypeDao.getBizType(code);
		if (bizType != null && bizType.getRuleSetCode() != null && bizType.getRuleSetVersion() != null) {
			// load ruleSet
			final RuleSet ruleSet = this.ruleService.getRuleSet(bizType.getRuleSetCode(), bizType.getRuleSetVersion());
			bizType.setRuleSet(ruleSet);
		}
		return bizType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BizTypeService#getSimpleBizType(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "bizTypes")
	public BizType getSimpleBizType(final String code) {
		return this.bizTypeDao.getBizType(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BizTypeService#getAllBizTypes()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "bizTypes")
	public List<BizType> getAllBizTypes() {
		final List<BizType> list = this.bizTypeDao.getAllBizTypes();
		if (list != null) {
			for (final BizType bizType : list) {
				// load ruleSet
				if (bizType.getRuleSetCode() != null && bizType.getRuleSetVersion() != null) {
					// load ruleSet
					final RuleSet ruleSet = this.ruleService.getRuleSet(bizType.getRuleSetCode(),
							bizType.getRuleSetVersion());
					bizType.setRuleSet(ruleSet);
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * BizTypeService#updateBizType(BizType)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "bizTypes", allEntries = true)
	public void updateBizType(final BizType bizType) {
		if (!this.bizTypeExisted(bizType.getCode())) {
			throw new NotFoundException("the bizType is NOT existed, code: " + bizType.getCode());
		}
		bizType.setUpdateTime(this.datetimeProvider.nowTime());
		LOGGER.debug("updating the bizType: {}", bizType);
		this.bizTypeDao.updateBizType(bizType);
		LOGGER.info("the bizType updated: {}", bizType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BizTypeService#removeBizType(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "bizTypes", allEntries = true)
	public void removeBizType(final String code) {
		if (!this.bizTypeExisted(code)) {
			throw new NotFoundException("the bizType is NOT existed, code: " + code);
		}
		this.bizTypeDao.removeBizType(code, this.datetimeProvider.nowTime());
		LOGGER.info("the bizType removed: {}", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BizTypeService#bizTypeExisted(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "bizTypes")
	public boolean bizTypeExisted(final String code) {
		return this.bizTypeDao.countByCode(code) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BizTypeService#assignRuleSet(java.lang.String, java.lang.String, long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "bizTypes", allEntries = true)
	public void assignRuleSet(final String bizTypecode, final String ruleSetCode, final long ruleSetVersion) {
		// check bizType
		if (!this.bizTypeExisted(bizTypecode)) {
			throw new NotFoundException("the bizType is NOT existed, code: " + bizTypecode);
		}
		// check ruleSet
		final RuleSet ruleSet = this.ruleService.getRuleSet(ruleSetCode, ruleSetVersion);
		if (ruleSet == null) {
			throw new InvalidParameterException("the ruleSet is NOT existed, code: " + ruleSetCode + ", version: "
					+ ruleSetVersion);
		}
		if (!ruleSet.getBizTypeCode().equals(bizTypecode)) {
			throw new InvalidParameterException(
					"the bizTypeCode of the ruleSet must be same with this bizType, this bizTypeCode: " + bizTypecode
							+ ", ruleSet.bizTypeCode: " + ruleSet.getBizTypeCode());
		}
		// assign
		LOGGER.debug("assigning the ruleSet, code: {}, ruleSetCode: {}, ruleSetVersion: {}", bizTypecode, ruleSetCode,
				ruleSetVersion);
		this.bizTypeDao.assignRuleSet(bizTypecode, ruleSetCode, ruleSetVersion, this.datetimeProvider.nowTime());
		LOGGER.info("the ruleSet assigned, code: {}, ruleSetCode: {}, ruleSetVersion: {}", bizTypecode, ruleSetCode,
				ruleSetVersion);
	}

	/**
	 * @param bizTypeDao
	 *            the bizTypeDao to set
	 */
	public void setBizTypeDao(final BizTypeDao bizTypeDao) {
		this.bizTypeDao = bizTypeDao;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

	/**
	 * @param ruleService
	 *            the ruleService to set
	 */
	public void setRuleService(final RuleService ruleService) {
		this.ruleService = ruleService;
	}

}
