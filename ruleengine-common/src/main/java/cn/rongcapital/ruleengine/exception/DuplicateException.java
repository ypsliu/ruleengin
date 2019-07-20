/**
 * 
 */
package cn.rongcapital.ruleengine.exception;

/**
 * 内容重复异常
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class DuplicateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6009187073302899748L;

	/**
	 * 
	 */
	public DuplicateException() {
	}

	/**
	 * @param message
	 */
	public DuplicateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DuplicateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

}
