/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * ROP的错误响应信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeName("error_response")
public final class RopErrorResponse extends RopResponse {

	/**
	 * 错误代码
	 */
	private String code;

	/**
	 * 错误信息
	 */
	private String msg;

	/**
	 * 子错误代码
	 */
	@JsonProperty("sub_code")
	private String subCode;

	/**
	 * 子错误信息
	 */
	@JsonProperty("sub_msg")
	private String subMsg;

	/**
	 * 参数列表
	 */
	private List<String> args;

	/**
	 * to create the RopErrorResponse
	 */
	public RopErrorResponse() {
		super();
	}

	/**
	 * to create the RopErrorResponse
	 * 
	 * @param code
	 * @param msg
	 */
	public RopErrorResponse(final String code, final String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the subCode
	 */
	public String getSubCode() {
		return subCode;
	}

	/**
	 * @param subCode
	 *            the subCode to set
	 */
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	/**
	 * @return the subMsg
	 */
	public String getSubMsg() {
		return subMsg;
	}

	/**
	 * @param subMsg
	 *            the subMsg to set
	 */
	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}

	/**
	 * @return the args
	 */
	public List<String> getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(List<String> args) {
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopErrorResponse [code=" + code + ", msg=" + msg + ", subCode=" + subCode + ", subMsg=" + subMsg
				+ ", args=" + args + "]";
	}

}
