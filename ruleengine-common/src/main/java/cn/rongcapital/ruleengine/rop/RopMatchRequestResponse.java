/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import cn.rongcapital.ruleengine.api.RopResource;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * ROP的规则匹配请求响应信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeName(RopResource.MATCH_API_NAME)
public final class RopMatchRequestResponse extends RopResponse {

	@JsonProperty("is_success")
	private boolean success = true;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopMatchRequestResponse [success=" + success + "]";
	}

}
