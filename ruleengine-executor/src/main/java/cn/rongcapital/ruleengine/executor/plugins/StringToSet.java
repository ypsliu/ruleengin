package cn.rongcapital.ruleengine.executor.plugins;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.ExecutionPlugin;

/**
 * @author hill 
 * transform String to Set example:"a|b|c"---->{a,b,c}
 */

public class StringToSet implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringToSet.class);
	private String pluginName = "StringToSet";

	@Override
	public String pluginName() {
		return this.pluginName;
	}

	@Override
	public void setPluginName(String pluginName) {
		if (pluginName != null) {
			this.pluginName = pluginName;
		}

	}

	@Override
	public Object exec(Object... params) {
		// check
		if (params == null || params.length < 1 || params[0] == null || !(params[0] instanceof String)) {
			LOGGER.error("invalid params");
			throw new InvalidParameterException("invalid params");
		}
		final String stringParam = (String) params[0];
		if (stringParam == null || stringParam.isEmpty()) {
			LOGGER.error("invalid stringParam: {}", stringParam);
			throw new InvalidParameterException("invalid stringParam: " + stringParam);
		}
		if (stringParam.indexOf("|") < 0) {
			LOGGER.error("stringParam should be splitted by ,: {}", stringParam);
			throw new InvalidParameterException("stringParam should be splitted by |:" + stringParam);
		}
		String[] stringArray = stringParam.split("\\|");
		HashSet<Object> result = new HashSet<Object>();
		for (String temp : stringArray) {
			result.add(temp);
		}
		return result;
	}

}
