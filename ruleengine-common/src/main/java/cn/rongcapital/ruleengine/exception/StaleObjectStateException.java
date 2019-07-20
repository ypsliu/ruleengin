/**
 * 
 */
package cn.rongcapital.ruleengine.exception;

/**
 * the StaleObjectStateException
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class StaleObjectStateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2987291969261454957L;

	/**
	 * 
	 */
	public StaleObjectStateException() {
	}

	/**
	 * @param message
	 */
	public StaleObjectStateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StaleObjectStateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StaleObjectStateException(String message, Throwable cause) {
		super(message, cause);
	}

}
