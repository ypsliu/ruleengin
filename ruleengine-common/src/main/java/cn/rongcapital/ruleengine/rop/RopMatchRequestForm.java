/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

/**
 * ROP的规则匹配请求表单
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class RopMatchRequestForm extends RopForm {

	/**
	 * 业务类型code
	 */
	@FormParam("biz_type_code")
	@NotNull
	private String bizTypeCode;

	/**
	 * 业务code
	 */
	@FormParam("biz_code")
	@NotNull
	private String bizCode;

	/**
	 * 业务数据，是一个JSON格式的map，key为字段名称，value为数据内容
	 */
	@FormParam("datas")
	private String datas;

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

	/**
	 * @return the datas
	 */
	public String getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(String datas) {
		this.datas = datas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopMatchRequestForm [bizTypeCode=" + bizTypeCode + ", bizCode=" + bizCode + ", datas=" + datas
				+ ", appKey=" + appKey + ", session=" + session + ", method=" + method + ", sourceAppKey="
				+ sourceAppKey + ", timestamp=" + timestamp + ", sign=" + sign + ", from=" + from + ", format="
				+ format + "]";
	}

}
