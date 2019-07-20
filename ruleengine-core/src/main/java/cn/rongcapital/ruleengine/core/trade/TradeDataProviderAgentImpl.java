/**
 * 
 */
package cn.rongcapital.ruleengine.core.trade;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.api.TradeDataResource;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.RopUtils;
import cn.rongcapital.ruleengine.rop.TradeDataQueryForm;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

import com.ruixue.serviceplatform.commons.web.DefaultJacksonJaxbJsonProvider;

/**
 * the resteasy client implementation for TradeDataProviderAgent
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public class TradeDataProviderAgentImpl implements TradeDataProviderAgent {

	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeDataProviderAgentImpl.class);

	@Value("${tradedata.apiBasePath}")
	private String tradeDataApiBasePath;

	@Value("${tradedata.queryApiMethod}")
	private String tradeDataQueryApiMethod;

	private volatile AtomicBoolean httpInitialized = new AtomicBoolean(false);

	private TradeDataResource tradeDataResource;

	private ResteasyClient client;

	@Value("${tradedata.rop.app_key}")
	private String ropAppKey;

	@Value("${tradedata.rop.secret}")
	private String ropSecret;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	@Autowired
	private DatetimeProvider datetimeProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * TradeDataResource#query(TradeDataQueryForm)
	 */
	@Override
	public RopResponse query(final TradeDataQueryForm request) {
		// check
		if (request == null) {
			throw new InvalidParameterException("the request is null");
		}
		if (StringUtils.isEmpty(request.getTable())) {
			throw new InvalidParameterException("the request.table is null");
		}
		if (request.getColumns() == null || request.getColumns().isEmpty()) {
			throw new InvalidParameterException("the request.columns is null");
		}
		if (request.getConditions() == null || request.getConditions().isEmpty()) {
			throw new InvalidParameterException("the request.conditions is null");
		}
		this.init();
		LOGGER.info("querying tradeData, request: {}", request);
		// convert the columns
		request.setColumnsValue(this.convertColumns(request.getColumns()));
		// convert the conditions
		request.setConditionsValue(this.convertConditions(request.getConditions()));
		// sign
		this.sign(request);
		// query
		return this.tradeDataResource.query(request);
	}

	private void sign(final TradeDataQueryForm request) {
		// app key
		request.setAppKey(this.ropAppKey);
		// timestamp
		request.setTimestamp(this.dateFormat.format(this.datetimeProvider.nowTime()));
		// method
		request.setMethod(this.tradeDataQueryApiMethod);
		// session
		request.setSession(UUID.randomUUID().toString()); // FIXME get the session from ROP?
		// format
		request.setFormat("json");
		final TreeMap<String, String> map = RopUtils.buildBaseParamsMap(request);
		// biz fields
		map.put("table", request.getTable());
		map.put("columns", request.getColumnsValue());
		map.put("conditions", request.getConditionsValue());
		// sign
		request.setSign(RopUtils.sign(map, this.ropSecret));
	}

	private String convertColumns(final List<String> columns) {
		final StringBuilder buf = new StringBuilder();
		for (final String column : columns) {
			if (buf.length() > 0) {
				buf.append(",");
			}
			buf.append(column);
		}
		return buf.toString();
	}

	private String convertConditions(final Map<String, String> conditions) {
		final StringBuilder buf = new StringBuilder();
		for (final String key : conditions.keySet()) {
			if (buf.length() > 0) {
				buf.append(";");
			}
			buf.append(key).append(",");
			if (conditions.get(key) != null) {
				buf.append(conditions.get(key));
			}
		}
		return buf.toString();
	}

	private void init() {
		if (!this.httpInitialized.get()) {
			LOGGER.debug("initializing the tradeData http client, basePath: {}", this.tradeDataApiBasePath);
			// initialize the HTTP client
			this.client = new ResteasyClientBuilder().httpEngine(
					new ApacheHttpClient4Engine(HttpClientBuilder.create().build())).build();
			// json provider
			this.client.register(new DefaultJacksonJaxbJsonProvider());
			// target
			final ResteasyWebTarget target = client.target(this.tradeDataApiBasePath);
			// resource
			this.tradeDataResource = target.proxy(TradeDataResource.class);
			// done
			this.httpInitialized.set(true);
			LOGGER.info("the tradeData http client initialized, basePath: {}", this.tradeDataApiBasePath);
		}
	}

	/**
	 * @param tradeDataApiBasePath
	 *            the tradeDataApiBasePath to set
	 */
	public void setTradeDataApiBasePath(final String tradeDataApiBasePath) {
		this.tradeDataApiBasePath = tradeDataApiBasePath;
	}

	/**
	 * @param ropAppKey
	 *            the ropAppKey to set
	 */
	public void setRopAppKey(final String ropAppKey) {
		this.ropAppKey = ropAppKey;
	}

	/**
	 * @param ropSecret
	 *            the ropSecret to set
	 */
	public void setRopSecret(final String ropSecret) {
		this.ropSecret = ropSecret;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

	/**
	 * @param tradeDataQueryApiMethod
	 *            the tradeDataQueryApiMethod to set
	 */
	public void setTradeDataQueryApiMethod(final String tradeDataQueryApiMethod) {
		this.tradeDataQueryApiMethod = tradeDataQueryApiMethod;
	}

}
