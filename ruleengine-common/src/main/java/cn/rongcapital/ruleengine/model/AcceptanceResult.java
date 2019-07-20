/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import cn.rongcapital.ruleengine.enums.Acceptance;

/**
 * 是否接受类型的规则的匹配结果详情
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class AcceptanceResult extends BaseResult<Acceptance> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AcceptanceResult [ruleCode=" + ruleCode + ", ruleName=" + ruleName + ", ruleComment=" + ruleComment
				+ ", ruleMatchType=" + ruleMatchType + ", rule=" + rule + ", params=" + params + ", errorMessage="
				+ errorMessage + ", time=" + time + ", result=" + result + ", ruleVersion=" + ruleVersion
				+ ", executionId=" + executionId + ", stages=" + stages + ", sortId=" + sortId + "]";
	}

}
