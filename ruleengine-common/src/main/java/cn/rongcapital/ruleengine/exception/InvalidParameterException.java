/**
 * 
 */
package cn.rongcapital.ruleengine.exception;

/**
 * 非法参数异常
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class InvalidParameterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7558039518589366792L;

	/**
	 * 
	 */
	public InvalidParameterException() {
	}

	/**
	 * @param message
	 */
	public InvalidParameterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidParameterException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

}
