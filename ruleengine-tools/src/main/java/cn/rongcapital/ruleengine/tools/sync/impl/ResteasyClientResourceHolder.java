/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javax.ws.rs.ext.Provider;

import cn.rongcapital.ruleengine.tools.sync.ResourceHolder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.tools.sync.DatasSyncSettings;

/**
 * the resteasy client implementation for ResourceHolder
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class ResteasyClientResourceHolder implements ResourceHolder {

	/**
	 * the resources cache
	 */
	private final Map<Class<?>, Object> resourcesCache = new HashMap<Class<?>, Object>();

	/**
	 * the resteasy client
	 */
	private final ResteasyClient client = new ResteasyClientBuilder().httpEngine(
			new ApacheHttpClient4Engine(HttpClientBuilder.create().build())).build();

	/**
	 * the resteasy target
	 */
	private ResteasyWebTarget target;

	private volatile AtomicBoolean initialized = new AtomicBoolean(false);

	private final ReentrantLock lock = new ReentrantLock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResourceHolder#init(cn.rongcapital.ruleengine.tools.sync.
	 * DatasSyncSettings)
	 */
	@Override
	public void init(final DatasSyncSettings settings) {
		if (!this.initialized.get()) {
			// jackson provider
			this.client.register(new DefaultJacksonJaxbJsonProvider(settings.getDatetimeFormat()));
			// target
			this.target = this.client.target(settings.getApiBaseUrl());
			this.initialized.set(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ResourceHolder#getResource(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> R getResource(Class<R> resourceType) {
		// check
		if (resourceType == null) {
			return null;
		}
		if (!this.initialized.get()) {
			throw new RuntimeException("the holder is NOT initialized");
		}
		this.lock.lock();
		try {
			R resource = (R) this.resourcesCache.get(resourceType);
			if (resource == null) {
				resource = this.target.proxy(resourceType);
				this.resourcesCache.put(resourceType, resource);
			}
			return resource;
		} finally {
			this.lock.unlock();
		}
	}

	@Provider
	private class DefaultJacksonJaxbJsonProvider extends JacksonJaxbJsonProvider {

		public DefaultJacksonJaxbJsonProvider(final String datetimeFormat) {
			final ObjectMapper mapper = new ObjectMapper();
			// ignore null fields
			mapper.setSerializationInclusion(Inclusion.NON_NULL);
			// ignore unknown fields
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// date time format
			mapper.setDateFormat(new SimpleDateFormat(datetimeFormat));
			super.setMapper(mapper);
		}

	}

}
