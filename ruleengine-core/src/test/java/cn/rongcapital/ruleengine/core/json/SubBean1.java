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
@JsonTypeName("sub_bean_1")
public class SubBean1 extends BaseBean {

	public static final String TYPE_NAME = "sub_bean_1";

	@JsonProperty("value_1_1")
	private String value11;

	@JsonProperty("value_1_2")
	private String value12;

	/**
	 * @return the value11
	 */
	public String getValue11() {
		return value11;
	}

	/**
	 * @param value11
	 *            the value11 to set
	 */
	public void setValue11(String value11) {
		this.value11 = value11;
	}

	/**
	 * @return the value12
	 */
	public String getValue12() {
		return value12;
	}

	/**
	 * @param value12
	 *            the value12 to set
	 */
	public void setValue12(String value12) {
		this.value12 = value12;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubBean1 [value11=" + value11 + ", value12=" + value12 + "]";
	}

}
