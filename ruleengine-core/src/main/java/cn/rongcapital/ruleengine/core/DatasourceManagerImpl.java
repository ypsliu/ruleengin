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
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the implementation for DatasourceManager
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class DatasourceManagerImpl implements DatasourceManager {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceManagerImpl.class);

	@Autowired
	private DatasourceManagerDao datasourceManagerDao;

	@Autowired
	private DatetimeProvider datetimeProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DatasourceManager#createDatasource(cn.rongcapital.ruleengine.model.
	 * Datasource)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "datasources", allEntries = true)
	public Datasource createDatasource(final Datasource datasource) {
		if (this.datasourceExisted(datasource.getCode())) {
			throw new DuplicateException("the datasource code already existed, code: " + datasource.getCode());
		}
		datasource.setCreationTime(this.datetimeProvider.nowTime());
		LOGGER.debug("creating the datasource: {}", datasource);
		this.datasourceManagerDao.createDatasource(datasource);
		LOGGER.info("the new datasource created: {}", datasource);
		return datasource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * DatasourceManager#updateDatasource(cn.rongcapital.ruleengine.model.
	 * Datasource)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "datasources", allEntries = true)
	public void updateDatasource(final Datasource datasource) {
		if (!this.datasourceExisted(datasource.getCode())) {
			throw new NotFoundException("the datasource is NOT existed, code: " + datasource.getCode());
		}
		datasource.setUpdateTime(this.datetimeProvider.nowTime());
		LOGGER.debug("updating the datasource: {}", datasource);
		this.datasourceManagerDao.updateDatasource(datasource);
		LOGGER.info("the datasource updated: {}", datasource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatasourceManager#getDatasource(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "datasources")
	public Datasource getDatasource(final String code) {
		return this.datasourceManagerDao.getDatasource(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatasourceManager#getDatasources()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "datasources")
	public List<Datasource> getDatasources() {
		return this.datasourceManagerDao.getDatasources();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatasourceManager#removeDatasource(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "datasources", allEntries = true)
	public void removeDatasource(final String code) {
		if (!this.datasourceExisted(code)) {
			throw new NotFoundException("the datasource is NOT existed, code: " + code);
		}
		this.datasourceManagerDao.removeDatasource(code, this.datetimeProvider.nowTime());
		LOGGER.info("the datasource removed: {}", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatasourceManager#datasourceExisted(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@Cacheable(value = "datasources")
	public boolean datasourceExisted(final String code) {
		return this.datasourceManagerDao.countByCode(code) > 0;
	}

	/**
	 * @param datasourceManagerDao
	 *            the datasourceManagerDao to set
	 */
	public void setDatasourceManagerDao(final DatasourceManagerDao datasourceManagerDao) {
		this.datasourceManagerDao = datasourceManagerDao;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

}
