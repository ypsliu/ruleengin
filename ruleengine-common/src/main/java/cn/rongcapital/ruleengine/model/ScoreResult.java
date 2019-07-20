/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 评分类型的匹配结果详情
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ScoreResult extends BaseResult<Integer> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScoreResult [ruleCode=" + ruleCode + ", ruleName=" + ruleName + ", ruleComment=" + ruleComment
				+ ", ruleMatchType=" + ruleMatchType + ", rule=" + rule + ", params=" + params + ", errorMessage="
				+ errorMessage + ", time=" + time + ", result=" + result + ", ruleVersion=" + ruleVersion
				+ ", executionId=" + executionId + ", stages=" + stages + ", sortId=" + sortId + "]";
	}

}
