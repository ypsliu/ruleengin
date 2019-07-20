/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 文本类型的规则的匹配结果
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class TextResults extends BaseResults<TextResult> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TextResults [bizCode=" + bizCode + ", bizTypeCode=" + bizTypeCode + ", bizTypeName=" + bizTypeName
				+ ", bizTypeComment=" + bizTypeComment + ", bizType=" + bizType + ", results=" + results
				+ ", ruleSetCode=" + ruleSetCode + ", ruleSetVersion=" + ruleSetVersion + "]";
	}

}
