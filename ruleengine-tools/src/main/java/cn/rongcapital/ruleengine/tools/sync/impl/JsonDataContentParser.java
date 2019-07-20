/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.impl;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.tools.sync.DataContentParser;

/**
 * the JSON implementation for DataContentParser
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class JsonDataContentParser implements DataContentParser {

	private final ObjectMapper mapper = new ObjectMapper();

	public JsonDataContentParser() {
		// ignore null fields
		this.mapper.setSerializationInclusion(Inclusion.NON_NULL);
		// ignore unknown fields
		this.mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// date time format
		this.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // FIXME
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DataContentParser#parseContent(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T parseContent(final String content, final Class<T> type) {
		// check
		if (content == null || type == null) {
			return null;
		}
		try {
			return mapper.readValue(content, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
