/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

/**
 * the risk resource holder
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ResourceHolder {

	/**
	 * to initialize the holder
	 * 
	 * @param settings
	 *            the settings
	 */
	void init(DatasSyncSettings settings);

	/**
	 * to get the resource by type
	 * 
	 * @param resourceType
	 *            the resource type
	 * @return the resource instance
	 */
	<R> R getResource(Class<R> resourceType);

}
