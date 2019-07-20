/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public enum ConditionOperator {

	/**
	 * 等于
	 */
	EQ("=="),

	/**
	 * 不等于
	 */
	NE("!="),

	/**
	 * 小于
	 */
	LT("<"),

	/**
	 * 小于等于
	 */
	LE("<="),

	/**
	 * 大于
	 */
	GT(">"),

	/**
	 * 大于等于
	 */
	GE(">="),

	/**
	 * 子集
	 */
	IN("=~");

	private final String code;

	private ConditionOperator(final String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
