/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

/**
 * the execution plugin
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ExecutionPlugin {

	/**
	 * to get the plugin name
	 * 
	 * @return the name
	 */
	String pluginName();

	/**
	 * to set the plugin name
	 * 
	 * @param pluginName
	 *            the plugin name
	 */
	void setPluginName(String pluginName);

	/**
	 * to execute the plugin
	 * 
	 * @param params
	 *            the parameters
	 * @return the return value
	 */
	Object exec(Object... params);

}
