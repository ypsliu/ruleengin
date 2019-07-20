/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 自定义规则匹配结果详情
 * 
 * @author shangchunming@rongcapital.cn
 *
 * @param <R>
 *            匹配结果的类型
 */
public final class CustomResult<R> extends BaseResult<R> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomResult [ruleCode=" + ruleCode + ", ruleName=" + ruleName + ", ruleComment=" + ruleComment
				+ ", ruleMatchType=" + ruleMatchType + ", rule=" + rule + ", params=" + params + ", errorMessage="
				+ errorMessage + ", time=" + time + ", result=" + result + ", ruleVersion=" + ruleVersion
				+ ", executionId=" + executionId + ", stages=" + stages + ", sortId=" + sortId + "]";
	}

}
