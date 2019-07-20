/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import cn.rongcapital.ruleengine.api.RopResource;
import cn.rongcapital.ruleengine.model.ReferenceData;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * ROP的参考数据响应信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeName(RopResource.REFERENCE_API_NAME)
public final class RopReferenceDataResponse extends RopResponse {

	@JsonProperty("is_pending")
	private Boolean pending;

	private ReferenceData result;

	/**
	 * @return the pending
	 */
	public Boolean getPending() {
		return pending;
	}

	/**
	 * @param pending
	 *            the pending to set
	 */
	public void setPending(Boolean pending) {
		this.pending = pending;
	}

	/**
	 * @return the result
	 */
	public ReferenceData getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ReferenceData result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopReferenceDataResponse [pending=" + pending + ", result=" + result + "]";
	}

}
