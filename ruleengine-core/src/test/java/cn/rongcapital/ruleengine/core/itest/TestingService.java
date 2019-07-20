/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataStatus;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public class TestingService {

	@Autowired
	private TestingDao testingDao;

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearDatasources() {
		this.testingDao.clearDatasources();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearBizTypes() {
		this.testingDao.clearBizTypes();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearRules() {
		this.testingDao.clearRules();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearRuleHistories() {
		this.testingDao.clearRuleHistories();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearRuleSets() {
		this.testingDao.clearRuleSets();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearRuleSetHistories() {
		this.testingDao.clearRuleSetHistories();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearExtractSqls() {
		this.testingDao.clearExtractSqls();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearExtractSqlHistories() {
		this.testingDao.clearExtractSqlHistoies();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearResults() {
		this.testingDao.clearResults();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearMatchStages() {
		this.testingDao.clearMatchStages();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearReferenceDataStatus() {
		this.testingDao.clearReferenceDataStatus();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clearReferenceDatas() {
		this.testingDao.clearReferenceDatas();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void createDatasource(final Datasource datasource) {
		this.testingDao.createDatasource(datasource);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void createBizType(final BizType bizType) {
		this.testingDao.createBizType(bizType);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public ReferenceDataStatus loadReferenceDataStatus(final String conditionsValue) {
		return this.testingDao.loadStatus(conditionsValue);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public ReferenceData loadReferenceData(final String conditionsValue) {
		return this.testingDao.loadReferenceData(conditionsValue);
	}

	/**
	 * @param testingDao
	 *            the testingDao to set
	 */
	public void setTestingDao(TestingDao testingDao) {
		this.testingDao = testingDao;
	}

}
