/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.service.PythonExecutor;
import cn.rongcapital.ruleengine.service.ReferenceDataProcessor;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the python implementation for ReferenceDataProcessor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class ReferenceDataPythodProcessor implements ReferenceDataProcessor {

	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataPythodProcessor.class);

	@Autowired
	private PythonExecutor pythonExecutor;

	@Autowired
	private DatetimeProvider datetimeProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ReferenceDataProcessor#process(java.lang.String, java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public ReferenceData process(final String bizTypeCode, final Map<String, String> conditions,
			final Map<String, List<Map<String, String>>> rawDatas) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param pythonExecutor
	 *            the pythonExecutor to set
	 */
	public void setPythonExecutor(final PythonExecutor pythonExecutor) {
		this.pythonExecutor = pythonExecutor;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

}
