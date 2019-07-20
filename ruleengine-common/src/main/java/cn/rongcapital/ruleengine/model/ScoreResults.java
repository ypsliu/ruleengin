/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 评分类型的规则的匹配结果
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ScoreResults extends BaseResults<ScoreResult> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScoreResults [bizCode=" + bizCode + ", bizTypeCode=" + bizTypeCode + ", bizTypeName=" + bizTypeName
				+ ", bizTypeComment=" + bizTypeComment + ", bizType=" + bizType + ", results=" + results + "]";
	}

}
