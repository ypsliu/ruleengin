/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public enum ConditionsRelationType {

	/**
	 * 必须全部满足
	 */
	AND("&&"),

	/**
	 * 满足其中一个
	 */
	OR("||");

	private final String code;

	private ConditionsRelationType(final String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
