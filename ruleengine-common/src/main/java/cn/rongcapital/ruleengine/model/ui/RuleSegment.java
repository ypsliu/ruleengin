/**
 * 
 */
package cn.rongcapital.ruleengine.model.ui;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class RuleSegment {

	/**
	 * the segment key
	 */
	@NotEmpty
	private String key;

	/**
	 * the segment value
	 */
	@NotEmpty
	private String value;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
