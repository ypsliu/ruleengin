/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

/**
 * ROP的表单的超类
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public abstract class RopForm {

	/**
	 * app_key
	 */
	@FormParam("app_key")
	@NotNull
	protected String appKey;

	/**
	 * session
	 */
	@FormParam("session")
	protected String session;

	/**
	 * method
	 */
	@FormParam("method")
	@NotNull
	protected String method;

	/**
	 * sourceappkey
	 */
	@FormParam("sourceappkey")
	protected String sourceAppKey;

	/**
	 * timestamp
	 */
	@FormParam("timestamp")
	@NotNull
	protected String timestamp;

	/**
	 * sign
	 */
	@FormParam("sign")
	@NotNull
	protected String sign;

	/**
	 * from
	 */
	@FormParam("from")
	protected String from;

	/**
	 * format
	 */
	@FormParam("format")
	@NotNull
	protected String format;

	/**
	 * @return the appKey
	 */
	public String getAppKey() {
		return appKey;
	}

	/**
	 * @param appKey
	 *            the appKey to set
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * @return the session
	 */
	public String getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(String session) {
		this.session = session;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the sourceAppKey
	 */
	public String getSourceAppKey() {
		return sourceAppKey;
	}

	/**
	 * @param sourceAppKey
	 *            the sourceAppKey to set
	 */
	public void setSourceAppKey(String sourceAppKey) {
		this.sourceAppKey = sourceAppKey;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

}
