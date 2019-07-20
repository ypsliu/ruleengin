/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 自定义规则匹配结果
 * 
 * @author shangchunming@rongcapital.cn
 *
 * @param <R>
 *            匹配结果类型
 */
public final class CustomResults<R> extends BaseResults<R> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomResults [bizCode=" + bizCode + ", bizTypeCode=" + bizTypeCode + ", bizType=" + bizType
				+ ", results=" + results + "]";
	}

}
