/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataStatus;
import cn.rongcapital.ruleengine.service.BizTypeService;
import cn.rongcapital.ruleengine.service.DatasourceManager;
import cn.rongcapital.ruleengine.service.ReferenceDataProcessor;
import cn.rongcapital.ruleengine.service.ReferenceDataProviderClient;
import cn.rongcapital.ruleengine.service.ReferenceDataService;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the implementation for ReferenceDataService
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class ReferenceDataServiceImpl implements ReferenceDataService {

	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataServiceImpl.class);

	/**
	 * JSON mapper
	 */
	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Autowired
	private ReferenceDataDao referenceDataDao;

	@Autowired
	private ReferenceDataProviderClient referenceDataProviderClient;

	@Autowired
	private ReferenceDataProcessor referenceDataProcessor;

	@Autowired
	private DatetimeProvider datetimeProvider;

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private DatasourceManager datasourceManager;

	/**
	 * the datasource code of the provider
	 */
	@Value("${referencedata.provider.datasourceCode}")
	private String providerDatasourceCode;

	/**
	 * the reference data expired time in hours, default is 24
	 */
	@Value("${referencedata.expiredInHours}")
	private long expiredInHours = 24;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ReferenceDataService#getReferenceData(java.lang.String, java.util.Map)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ReferenceData getReferenceData(final String bizTypeCode, final Map<String, String> conditions) {
		// check
		if (StringUtils.isEmpty(bizTypeCode)) {
			LOGGER.error("the bizTypeCode is null or empty");
			throw new InvalidParameterException("the bizTypeCode is null or empty");
		}
		if (conditions == null || conditions.isEmpty()) {
			LOGGER.error("the conditions is null or empty");
			throw new InvalidParameterException("the conditions is null or empty");
		}
		if (!this.bizTypeService.bizTypeExisted(bizTypeCode)) {
			LOGGER.error("the bizType is NOT existed, bizTypeCode: {}", bizTypeCode);
			throw new InvalidParameterException("the bizType is NOT existed, bizTypeCode: " + bizTypeCode);
		}
		if (!this.datasourceManager.datasourceExisted(this.providerDatasourceCode)) {
			LOGGER.error("the provider datasource is NOT existed, datasourceCode: {}", this.providerDatasourceCode);
			throw new InvalidParameterException("the provider datasource is NOT existed, datasourceCode: "
					+ this.providerDatasourceCode);
		}

		// the conditions value
		final String conditionsValue = this.buildConditionsValue(bizTypeCode, conditions);

		// load the status with lock
		final ReferenceDataStatus status = this.referenceDataDao.loadStatusWithLock(conditionsValue);

		if (status != null) {
			// the status is existed, check done
			if (status.isDone()) {
				// it is done, load the ReferenceData
				final ReferenceData rd = this.loadReferenceData(conditionsValue);
				// check expired
				if (!this.isStatusExpired(rd.getTime())) {
					// not expired, return it
					rd.setConditions(conditions);
					return rd;
				} else {
					// expired, send the request to provider
					LOGGER.warn("the reference data is expired, request it again, bizTypeCode: {}, conditions: {}",
							bizTypeCode, conditions);
					this.requestToProvider(bizTypeCode, conditions, conditionsValue);
					// return null
					return null;
				}
			} else {
				// not done, check finished
				LOGGER.debug(
						"the reference data is not done, checking finish from provider, datasourceCode: {}, taskId: {}",
						this.providerDatasourceCode, status.getProviderTaskId());
				if (!this.referenceDataProviderClient.checkFinished(this.providerDatasourceCode,
						status.getProviderTaskId())) {
					// not finished
					LOGGER.info("the reference data is not done and not finished, datasourceCode: {}, taskId: {}",
							this.providerDatasourceCode, status.getProviderTaskId());
					return null;
				} else {
					// finished, query the result
					LOGGER.info(
							"the reference data is not done but finished, querying datas from provider, datasourceCode: {}, taskId: {}",
							this.providerDatasourceCode, status.getProviderTaskId());
					return this.queryFromProvider(bizTypeCode, conditions, conditionsValue, status);
				}
			}
		} else {
			// not existed, request to provider
			LOGGER.info("new reference data request accepted, bizTypeCode: {}, conditions: {}", bizTypeCode, conditions);
			this.requestToProvider(bizTypeCode, conditions, conditionsValue);
			// return null
			return null;
		}
	}

	private ReferenceData queryFromProvider(final String bizTypeCode, final Map<String, String> conditions,
			final String conditionsValue, final ReferenceDataStatus status) {
		// query
		final Map<String, List<Map<String, String>>> datas = this.referenceDataProviderClient.queryDatas(
				this.providerDatasourceCode, status.getProviderTaskId());
		LOGGER.debug("got the datas, datasource: {}, taskId: {}, datas: {}", this.providerDatasourceCode,
				status.getProviderTaskId(), datas);
		// process it
		final ReferenceData rd = this.referenceDataProcessor.process(bizTypeCode, conditions, datas);
		// save the result
		rd.setProviderTaskId(status.getProviderTaskId());
		rd.setBizTypeCode(bizTypeCode);
		rd.setConditions(conditions);
		rd.setConditionsValue(conditionsValue);
		rd.setResponseRaw(this.convertResponse(datas));
		rd.setResponseValue(this.convertResponse(rd.getResponse()));
		rd.setTime(this.datetimeProvider.nowTime());
		this.referenceDataDao.createReferenceData(rd);
		// update status
		status.setDone(true);
		status.setUpdateTime(this.datetimeProvider.nowTime());
		this.referenceDataDao.updateStatus(status);
		// remove the internal info
		rd.setConditionsValue(null);
		rd.setResponseValue(null);
		rd.setResponseRaw(null);
		return rd;
	}

	private void requestToProvider(final String bizTypeCode, final Map<String, String> conditions,
			final String conditionsValue) {
		// request the provider to get the taskId
		final String providerTaskId = this.referenceDataProviderClient.request(conditions);
		// create the status
		final ReferenceDataStatus status = new ReferenceDataStatus();
		status.setBizTypeCode(bizTypeCode);
		status.setDone(false);
		status.setConditionsValue(conditionsValue);
		status.setInsertTime(this.datetimeProvider.nowTime());
		status.setProviderTaskId(providerTaskId);
		// save the status
		this.referenceDataDao.createStatus(status);
	}

	private boolean isStatusExpired(final Date time) {
		if (time != null) {
			final Date now = this.datetimeProvider.nowTime();
			final Date expiredTime = new Date(time.getTime() + this.expiredInHours * 3600 * 1000);
			return expiredTime.before(now);
		}
		return true;
	}

	private ReferenceData loadReferenceData(final String conditionsValue) {
		final ReferenceData rd = this.referenceDataDao.loadReferenceData(conditionsValue);
		if (rd != null) {
			rd.setResponse(this.convertResponse(rd.getResponseValue()));
			// remove internal info
			rd.setResponseValue(null);
			rd.setResponseRaw(null);
			rd.setConditionsValue(null);
		}
		return rd;
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<Map<String, String>>> convertResponse(final String responseValue) {
		try {
			return this.jsonMapper.readValue(responseValue, Map.class);
		} catch (Exception e) {
			throw new InvalidParameterException("responseValue不是一个合法的JSON格式的MAP", e);
		}
	}

	private String convertResponse(final Map<String, List<Map<String, String>>> response) {
		if (response != null) {
			try {
				return this.jsonMapper.writeValueAsString(response);
			} catch (Exception e) {
				LOGGER.error("convert the response to json failed, error: " + e.getMessage(), e);
			}
		}
		return null;
	}

	private String buildConditionsValue(final String bizTypeCode, final Map<String, String> conditions) {
		final StringBuilder buf = new StringBuilder();
		buf.append("bizTypeCode=").append(bizTypeCode);
		final TreeMap<String, String> map = new TreeMap<String, String>();
		map.putAll(conditions);
		for (final String key : map.keySet()) {
			buf.append(",").append(key).append("=");
			final String value = map.get(key);
			if (value != null) {
				buf.append(value);
			}
		}
		return buf.toString();
	}

	/**
	 * @param referenceDataDao
	 *            the referenceDataDao to set
	 */
	public void setReferenceDataDao(final ReferenceDataDao referenceDataDao) {
		this.referenceDataDao = referenceDataDao;
	}

	/**
	 * @param referenceDataProviderClient
	 *            the referenceDataProviderClient to set
	 */
	public void setReferenceDataProviderClient(final ReferenceDataProviderClient referenceDataProviderClient) {
		this.referenceDataProviderClient = referenceDataProviderClient;
	}

	/**
	 * @param referenceDataProcessor
	 *            the referenceDataProcessor to set
	 */
	public void setReferenceDataProcessor(final ReferenceDataProcessor referenceDataProcessor) {
		this.referenceDataProcessor = referenceDataProcessor;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

	/**
	 * @param providerDatasourceCode
	 *            the providerDatasourceCode to set
	 */
	public void setProviderDatasourceCode(final String providerDatasourceCode) {
		if (!StringUtils.isEmpty(providerDatasourceCode)) {
			this.providerDatasourceCode = providerDatasourceCode;
		}
	}

	/**
	 * @param bizTypeService
	 *            the bizTypeService to set
	 */
	public void setBizTypeService(final BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
	}

	/**
	 * @param expiredInHours
	 *            the expiredInHours to set
	 */
	public void setExpiredInHours(final long expiredInHours) {
		if (expiredInHours > 0) {
			this.expiredInHours = expiredInHours;
		} else {
			LOGGER.warn("invalid setting expiredInHours: {}, default value will be used: {}", expiredInHours,
					this.expiredInHours);
		}
	}

	/**
	 * @param datasourceManager
	 *            the datasourceManager to set
	 */
	public void setDatasourceManager(final DatasourceManager datasourceManager) {
		this.datasourceManager = datasourceManager;
	}

}
