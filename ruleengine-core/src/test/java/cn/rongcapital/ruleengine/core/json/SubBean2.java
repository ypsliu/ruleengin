/**
 * 
 */
package cn.rongcapital.ruleengine.core.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeName("sub_bean_2")
public class SubBean2 extends BaseBean {

	public static final String TYPE_NAME = "sub_bean_2";

	@JsonProperty("value_2_1")
	private String value21;

	@JsonProperty("value_2_2")
	private String value22;

	/**
	 * @return the value21
	 */
	public String getValue21() {
		return value21;
	}

	/**
	 * @param value21
	 *            the value21 to set
	 */
	public void setValue21(String value21) {
		this.value21 = value21;
	}

	/**
	 * @return the value22
	 */
	public String getValue22() {
		return value22;
	}

	/**
	 * @param value22
	 *            the value22 to set
	 */
	public void setValue22(String value22) {
		this.value22 = value22;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubBean2 [value21=" + value21 + ", value22=" + value22 + "]";
	}

}
