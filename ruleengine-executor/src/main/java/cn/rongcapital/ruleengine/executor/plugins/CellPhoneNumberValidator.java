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
 * the cell phone number validator plugin
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class CellPhoneNumberValidator implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(CellPhoneNumberValidator.class);

	/**
	 * the validation pattern
	 */
	private String validationPattern = "^((\\+86)|(86))?((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";

	/**
	 * the pattern
	 */
	private Pattern pattern = Pattern.compile(this.validationPattern);

	private String pluginName = "CellPhoneNumberValidator";

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
		String cellPhoneNumber = (String) params[0];
		if (cellPhoneNumber == null || cellPhoneNumber.isEmpty()) {
			LOGGER.error("invalid cellPhoneNumber: {}", cellPhoneNumber);
			throw new InvalidParameterException("invalid cellPhoneNumber: " + cellPhoneNumber);
		}
		//remove the separator -
		if(cellPhoneNumber.indexOf("-") > -1){
			cellPhoneNumber = cellPhoneNumber.replaceAll("-", "");
		}
		return this.pattern.matcher(cellPhoneNumber).matches();
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
