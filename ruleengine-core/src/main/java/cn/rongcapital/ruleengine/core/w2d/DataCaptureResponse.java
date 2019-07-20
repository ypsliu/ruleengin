/**
 * 
 */
package cn.rongcapital.ruleengine.core.w2d;

/**
 * the response of the data capture request
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class DataCaptureResponse {

	/**
	 * the code
	 */
	private String code;

	/**
	 * the message
	 */
	private String message;

	/**
	 * the taskId
	 */
	private String taskId;

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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId
	 *            the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataCaptureResponse [code=" + code + ", message=" + message + ", taskId=" + taskId + "]";
	}

}
