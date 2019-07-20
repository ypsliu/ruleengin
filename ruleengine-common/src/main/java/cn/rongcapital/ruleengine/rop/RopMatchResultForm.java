/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import javax.ws.rs.FormParam;

/**
 * ROP的匹配结果请求表单
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RopMatchResultForm extends RopForm {

	/**
	 * 业务类型code
	 */
	@FormParam("biz_type_code")
	private String bizTypeCode;

	/**
	 * 业务code
	 */
	@FormParam("biz_code")
	private String bizCode;

	/**
	 * @return the bizTypeCode
	 */
	public String getBizTypeCode() {
		return bizTypeCode;
	}

	/**
	 * @param bizTypeCode
	 *            the bizTypeCode to set
	 */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	/**
	 * @return the bizCode
	 */
	public String getBizCode() {
		return bizCode;
	}

	/**
	 * @param bizCode
	 *            the bizCode to set
	 */
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopMatchResultForm [bizTypeCode=" + bizTypeCode + ", bizCode=" + bizCode + ", appKey=" + appKey
				+ ", session=" + session + ", method=" + method + ", sourceAppKey=" + sourceAppKey + ", timestamp="
				+ timestamp + ", sign=" + sign + ", from=" + from + ", format=" + format + "]";
	}

}
