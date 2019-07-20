/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 是否接受类型的规则的匹配结果
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class AcceptanceResults extends BaseResults<AcceptanceResult> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AcceptanceResults [bizCode=" + bizCode + ", bizTypeCode=" + bizTypeCode + ", bizTypeName="
				+ bizTypeName + ", bizTypeComment=" + bizTypeComment + ", bizType=" + bizType + ", results=" + results
				+ "]";
	}

}
