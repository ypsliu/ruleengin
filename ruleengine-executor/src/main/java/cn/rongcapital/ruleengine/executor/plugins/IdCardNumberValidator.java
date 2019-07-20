/**
 * 
 */
package cn.rongcapital.ruleengine.executor.plugins;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.ExecutionPlugin;

/**
 * the IdCardNumber validator plugin
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class IdCardNumberValidator implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdCardNumberValidator.class);

	/**
	 * the validation pattern for 15
	 */
	private String validationPattern15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

	/**
	 * the validation pattern for 18
	 */
	private String validationPattern18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";

	/**
	 * the pattern 15
	 */
	private Pattern pattern15 = Pattern.compile(this.validationPattern15);

	/**
	 * the pattern 18
	 */
	private Pattern pattern18 = Pattern.compile(this.validationPattern18);

	private String pluginName = "IdCardNumberValidator";

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#pluginName()
	 */
	@Override
	public String pluginName() {
		return this.pluginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#execute(java.lang.Object[])
	 */
	@Override
	public Object exec(final Object... params) {
		// check
		if (params == null || params.length < 1 || params[0] == null || !(params[0] instanceof String)) {
			LOGGER.error("invalid params");
			throw new InvalidParameterException("invalid params");
		}
		final String idCardNumber = (String) params[0];
		if (idCardNumber == null || (idCardNumber.length() != 15 && idCardNumber.length() != 18)) {
			LOGGER.error("invalid idCardNumber: {}", idCardNumber);
			throw new InvalidParameterException("invalid idCardNumber: " + idCardNumber);
		}
		if (idCardNumber.length() == 15) {
			// 15
			return this.pattern15.matcher(idCardNumber).matches();
		} else {
			// 18
			return this.pattern18.matcher(idCardNumber).matches();
		}
	}

	/**
	 * @return the validationPattern15
	 */
	public String getValidationPattern15() {
		return validationPattern15;
	}

	/**
	 * @param validationPattern15
	 *            the validationPattern15 to set
	 */
	public void setValidationPattern15(final String validationPattern15) {
		if (validationPattern15 != null && !validationPattern15.isEmpty()) {
			this.validationPattern15 = validationPattern15;
			this.pattern15 = Pattern.compile(this.validationPattern15);
		} else {
			LOGGER.warn("invalid pattern15, the default pattern will be used: {}", this.validationPattern15);
		}
	}

	/**
	 * @return the validationPattern18
	 */
	public String getValidationPattern18() {
		return validationPattern18;
	}

	/**
	 * @param validationPattern18
	 *            the validationPattern18 to set
	 */
	public void setValidationPattern18(final String validationPattern18) {
		if (validationPattern18 != null && !validationPattern18.isEmpty()) {
			this.validationPattern18 = validationPattern18;
			this.pattern18 = Pattern.compile(this.validationPattern18);
		} else {
			LOGGER.warn("invalid pattern18, the default pattern will be used: {}", this.validationPattern18);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#setPluginName(java.lang.String)
	 */
	@Override
	public void setPluginName(final String pluginName) {
		if (pluginName != null) {
			this.pluginName = pluginName;
		}
	}

}
