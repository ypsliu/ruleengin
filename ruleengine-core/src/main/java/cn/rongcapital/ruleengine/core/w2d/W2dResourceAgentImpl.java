/**
 * 
 */
package cn.rongcapital.ruleengine.core.w2d;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ruixue.serviceplatform.commons.web.DefaultJacksonJaxbJsonProvider;

/**
 * the resteasy client implementation for W2dResourceAgent
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public class W2dResourceAgentImpl implements W2dResourceAgent {

	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(W2dResourceAgentImpl.class);

	@Value("${referencedata.w2d.apiBasePath}")
	private String w2dApiBasePath;

	private volatile AtomicBoolean httpInitialized = new AtomicBoolean(false);

	private W2dResource w2dResource;

	private ResteasyClient client;

	@Value("${http.proxy.enabled}")
	private boolean proxyEnabled = false;

	@Value("${http.proxy.host}")
	private String proxyHost;

	@Value("${http.proxy.port}")
	private int proxyPort;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.core.w2d.W2dResource#capture(cn.rongcapital.ruleengine.core.w2d.
	 * DataCaptureRequestForm)
	 */
	@Override
	public DataCaptureResponse capture(final DataCaptureRequestForm condition) {
		this.init();
		return this.w2dResource.capture(condition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.core.w2d.W2dResource#capture(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DataCaptureResponse capture(final String name, final String id, final String mobileNumber,
			final String bizLicenceNumber, final String companyName) {
		this.init();
		return this.w2dResource.capture(name, id, mobileNumber, bizLicenceNumber, companyName);
	}

	private void init() {
		if (!this.httpInitialized.get()) {
			LOGGER.debug("initializing the w2d http client, proxyEnabled: {}, proxyHost: {}, proxyPort, basePath: {}",
					this.proxyEnabled, this.proxyHost, this.proxyPort, this.w2dApiBasePath);
			// initialize the HTTP client
			final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// proxy
			if (this.proxyEnabled) {
				final HttpHost proxyHost = new HttpHost(this.proxyHost, this.proxyPort);
				httpClientBuilder.setProxy(proxyHost);
			}
			this.client = new ResteasyClientBuilder()
					.httpEngine(new ApacheHttpClient4Engine(httpClientBuilder.build())).build();
			// json provider
			this.client.register(new DefaultJacksonJaxbJsonProvider());
			// target
			final ResteasyWebTarget target = client.target(this.w2dApiBasePath);
			// resource
			this.w2dResource = target.proxy(W2dResource.class);
			// done
			this.httpInitialized.set(true);
			LOGGER.info("the w2d http client initialized, proxyEnabled: {}, proxyHost: {}, proxyPort, basePath: {}",
					this.proxyEnabled, this.proxyHost, this.proxyPort, this.w2dApiBasePath);
		}
	}

	/**
	 * @param w2dApiBasePath
	 *            the w2dApiBasePath to set
	 */
	public void setW2dApiBasePath(final String w2dApiBasePath) {
		this.w2dApiBasePath = w2dApiBasePath;
	}

	/**
	 * @param proxyEnabled
	 *            the proxyEnabled to set
	 */
	public void setProxyEnabled(final boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}

	/**
	 * @param proxyHost
	 *            the proxyHost to set
	 */
	public void setProxyHost(final String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	public void setProxyPort(final int proxyPort) {
		this.proxyPort = proxyPort;
	}

}
