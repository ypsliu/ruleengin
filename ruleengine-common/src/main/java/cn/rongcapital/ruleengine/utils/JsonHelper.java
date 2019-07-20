/**
 * 
 */
package cn.rongcapital.ruleengine.utils;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

/**
 * the JSON helper
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class JsonHelper {

	private static final ObjectMapper jsonMapper = new ObjectMapper();

	static {
		jsonMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		jsonMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * to convert the object to JSON string
	 * 
	 * @param obj
	 *            the object
	 * @return the JSON string
	 * @throws IOException
	 */
	public static String toJsonString(final Object obj) throws IOException {
		if (obj == null) {
			return null;
		}
		return jsonMapper.writeValueAsString(obj);
	}

	/**
	 * to convert the JSON string to object
	 * 
	 * @param json
	 *            the JSON string
	 * @param type
	 *            the object type
	 * @return the object
	 * @throws IOException
	 */
	public static <T> T toObject(final String json, Class<T> type) throws IOException {
		if (json == null || type == null) {
			return null;
		}
		return jsonMapper.readValue(json, type);
	}

}
