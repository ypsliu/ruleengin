/**
 * 
 */
package cn.rongcapital.ruleengine.core.w2d;

import java.util.Map;

import javax.ws.rs.QueryParam;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;

/**
 * the data capture request form
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class DataCaptureRequestForm {

	/**
	 * 姓名
	 */
	@QueryParam("name")
	private String name = "";

	/**
	 * 身份证号码
	 */
	@QueryParam("id")
	private String id = "";

	/**
	 * 手机号
	 */
	@QueryParam("mobileNumber")
	private String mobileNumber = "";

	/**
	 * 营业执照号
	 */
	@QueryParam("bizLicenceNumber")
	private String bizLicenceNumber = "";

	/**
	 * 公司名称
	 */
	@QueryParam("companyName")
	private String companyName = "";

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the bizLicenceNumber
	 */
	public String getBizLicenceNumber() {
		return bizLicenceNumber;
	}

	/**
	 * @param bizLicenceNumber
	 *            the bizLicenceNumber to set
	 */
	public void setBizLicenceNumber(String bizLicenceNumber) {
		this.bizLicenceNumber = bizLicenceNumber;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataCaptureRequestForm [name=" + name + ", id=" + id + ", mobileNumber=" + mobileNumber
				+ ", bizLicenceNumber=" + bizLicenceNumber + ", companyName=" + companyName + "]";
	}

	/**
	 * to create the DataCaptureRequestForm by conditions map
	 * 
	 * @param conditions
	 *            the conditions map
	 * @return the DataCaptureRequestForm
	 */
	public static DataCaptureRequestForm fromConditionMap(final Map<String, String> conditions) {
		// check
		if (conditions == null || conditions.isEmpty()) {
			throw new InvalidParameterException("the condition is null or empty");
		}
		final DataCaptureRequestForm form = new DataCaptureRequestForm();
		if (conditions.get("id") != null) {
			form.setId(conditions.get("id"));
		}
		if (conditions.get("name") != null) {
			form.setName(conditions.get("name"));
		}
		if (conditions.get("mobileNumber") != null) {
			form.setMobileNumber(conditions.get("mobileNumber"));
		}
		if (conditions.get("bizLicenceNumber") != null) {
			form.setBizLicenceNumber(conditions.get("bizLicenceNumber"));
		}
		if (conditions.get("companyName") != null) {
			form.setCompanyName(conditions.get("companyName"));
		}
		return form;
	}

}
