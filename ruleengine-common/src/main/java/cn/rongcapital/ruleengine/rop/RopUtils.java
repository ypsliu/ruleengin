/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * the utils for ROP
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RopUtils {

	/**
	 * to verify the sign
	 * 
	 * @param form
	 *            the ROP reference data request form
	 * @param secret
	 *            the secret
	 * @return true: passed
	 */
	public static boolean verifySign(final RopReferenceDataForm form, final String secret) {
		// check
		if (form == null) {
			return false;
		}
		// base map
		final TreeMap<String, String> map = buildBaseParamsMap(form);
		// parameters
		map.put("biz_type_code", form.getBizTypeCode());
		map.put("conditions", form.getConditions());
		// sign
		final String sign = sign(map, secret);
		// check
		return sign.equals(form.getSign());
	}

	/**
	 * to verify the sign
	 * 
	 * @param form
	 *            the ROP match request form
	 * @param secret
	 *            the secret
	 * @return true: passed
	 */
	public static boolean verifySign(final RopMatchRequestForm form, final String secret) {
		// check
		if (form == null) {
			return false;
		}
		// base map
		final TreeMap<String, String> map = buildBaseParamsMap(form);
		// parameters
		map.put("biz_type_code", form.getBizTypeCode());
		map.put("biz_code", form.getBizCode());
		map.put("datas", form.getDatas());
		// sign
		final String sign = sign(map, secret);
		// check
		return sign.equals(form.getSign());
	}

	/**
	 * to verify the sign
	 * 
	 * @param form
	 *            the trade data query request form
	 * @param secret
	 *            the secret
	 * @return true: passed
	 */
	public static boolean verifySign(final TradeDataQueryForm form, final String secret) {
		// check
		if (form == null) {
			return false;
		}
		// base map
		final TreeMap<String, String> map = buildBaseParamsMap(form);
		// parameters
		map.put("table", form.getTable());
		map.put("columns", form.getColumnsValue());
		map.put("conditions", form.getConditionsValue());
		// sign
		final String sign = sign(map, secret);
		// check
		return sign.equals(form.getSign());
	}

	/**
	 * to verify the sign
	 * 
	 * @param form
	 *            the ROP match result form
	 * @param secret
	 *            the secret
	 * @return true: passed
	 */
	public static boolean verifySign(final RopMatchResultForm form, final String secret) {
		// check
		if (form == null) {
			return false;
		}
		// base map
		final TreeMap<String, String> map = buildBaseParamsMap(form);
		// parameters
		map.put("biz_type_code", form.getBizTypeCode());
		map.put("biz_code", form.getBizCode());
		// sign
		final String sign = sign(map, secret);
		// check
		return sign.equals(form.getSign());
	}

	/**
	 * to sign the parameters
	 * 
	 * @param params
	 *            the parameters map
	 * @param secret
	 *            the secret
	 * @return the sign string
	 */
	public static String sign(final Map<String, String> params, final String secret) {
		final StringBuilder buf = new StringBuilder();
		// add the secret
		buf.append(secret);
		// add the parameters
		for (final String key : params.keySet()) {
			final String value = params.get(key);
			if (value != null && value.trim().length() > 0) {
				buf.append(key).append(value);
			}
		}
		// add the secret
		buf.append(secret);
		// MD5
		return md5(buf.toString());
	}

	/**
	 * to build the base parameters map
	 * 
	 * @param form
	 *            the form
	 * @return the map
	 */
	public static TreeMap<String, String> buildBaseParamsMap(final RopForm form) {
		final TreeMap<String, String> map = new TreeMap<String, String>();
		// app_key
		map.put("app_key", form.getAppKey());
		// session
		map.put("session", form.getSession());
		// method
		map.put("method", form.getMethod());
		// sourceappkey
		map.put("sourceappkey", form.getSourceAppKey());
		// timestamp
		map.put("timestamp", form.getTimestamp());
		// from
		map.put("from", form.getFrom());
		// format
		map.put("format", form.getFormat());
		return map;
	}

	/**
	 * MD5
	 * 
	 * @param str
	 *            the input string
	 * @return the md5 string
	 */
	public static String md5(final String str) {
		if (str != null) {
			try {
				return DigestUtils.md5Hex(str.getBytes("UTF-8")).toUpperCase();
			} catch (UnsupportedEncodingException e) {
				//
			}
		}
		return null;
	}

}
