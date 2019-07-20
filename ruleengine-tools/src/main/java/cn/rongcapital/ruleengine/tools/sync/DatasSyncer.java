/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;


/**
 * the rule datas syncer
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface DatasSyncer {

	/**
	 * to sync datas
	 * 
	 * @param settings
	 *            the settings
	 */
	void syncDatas(DatasSyncSettings settings);

}
