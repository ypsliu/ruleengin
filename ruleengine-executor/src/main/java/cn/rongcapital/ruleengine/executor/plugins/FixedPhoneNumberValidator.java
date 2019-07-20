/**
 * 
 */
package cn.rongcapital.ruleengine.executor.plugins;

import java.util.regex.Pattern;

import cn.rongcapital.ruleengine.executor.ExecutionPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;

/**
 * the fixed phone number validator plugin
 * 
 * @author hill
 *
 */
public final class FixedPhoneNumberValidator implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(FixedPhoneNumberValidator.class);

	/**
	 * the validation pattern
	 */
	private String validationPattern = "(^(\\d{3,4})?\\d{7,8})$";

	/**
	 * the pattern
	 */
	private Pattern pattern = Pattern.compile(this.validationPattern);

	private String pluginName = "FixedPhoneNumberValidator";

	@Override
	public String pluginName() {
		return this.pluginName;
	}

	@Override
	public Object exec(final Object... params) {
		// check
		if (params == null || params.length < 1 || params[0] == null || !(params[0] instanceof String)) {
			LOGGER.error("invalid params");
			throw new InvalidParameterException("invalid params");
		}
		String fixedPhoneNumber = (String) params[0];
		if (fixedPhoneNumber == null || fixedPhoneNumber.isEmpty()) {
			LOGGER.error("invalid fixedPhoneNumber: {}", fixedPhoneNumber);
			throw new InvalidParameterException("invalid fixedPhoneNumber: " + fixedPhoneNumber);
		}
		//remove the separator -
		if(fixedPhoneNumber.indexOf("-") > -1){
			fixedPhoneNumber = fixedPhoneNumber.replaceAll("-", "");
		}
		return this.pattern.matcher(fixedPhoneNumber).matches();
	}

	/**
	 * @return the validationPattern
	 */
	public String getValidationPattern() {
		return validationPattern;
	}

	/**
	 * @param validationPattern
	 *            the validationPattern to set
	 */
	public void setValidationPattern(final String validationPattern) {
		if (validationPattern != null && !validationPattern.isEmpty()) {
			this.validationPattern = validationPattern;
			this.pattern = Pattern.compile(this.validationPattern);
		} else {
			LOGGER.warn("invalid pattern, the default pattern will be used: {}", this.validationPattern);
		}
	}

	@Override
	public void setPluginName(final String pluginName) {
		if (pluginName != null) {
			this.pluginName = pluginName;
		}
	}

}
