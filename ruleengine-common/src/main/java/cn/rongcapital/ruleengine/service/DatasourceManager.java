/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;

import cn.rongcapital.ruleengine.model.Datasource;

/**
 * 规则数据源管理
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface DatasourceManager {

	/**
	 * 创建数据源信息
	 * 
	 * @param datasource
	 *            要创建的数据源信息
	 * @return 新创建的数据源信息
	 */
	Datasource createDatasource(Datasource datasource);

	/**
	 * 变更数据源信息
	 * 
	 * @param datasource
	 *            要变更的数据源信息
	 */
	void updateDatasource(Datasource datasource);

	/**
	 * 根据数据源code获取数据源信息
	 * 
	 * @param code
	 *            要获取的数据源code
	 * @return 数据源信息
	 */
	Datasource getDatasource(String code);

	/**
	 * 获取全部数据源信息列表
	 * 
	 * @return 数据源信息列表
	 */
	List<Datasource> getDatasources();

	/**
	 * 删除数据源
	 * 
	 * @param code
	 *            要删除的数据源code
	 */
	void removeDatasource(String code);

	/**
	 * 数据源信息是否存在
	 * 
	 * @param code
	 *            数据源信息code
	 * @return true：存在
	 */
	boolean datasourceExisted(String code);

}
