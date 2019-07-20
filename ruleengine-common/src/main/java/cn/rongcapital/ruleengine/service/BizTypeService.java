/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;

import cn.rongcapital.ruleengine.model.BizType;

/**
 * 业务类型管理服务
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface BizTypeService {

	/**
	 * 创建业务类型
	 * 
	 * @param bizType
	 *            要创建的业务类型信息
	 * @return 新创建的业务类型信息
	 */
	BizType createBizType(BizType bizType);

	/**
	 * 根据code获取业务类型信息
	 * 
	 * @param code
	 *            要获取的业务类型的code
	 * @return 业务类型信息
	 */
	BizType getBizType(String code);

	/**
	 * 根据code获取业务类型的概要信息
	 * 
	 * @param code
	 *            要获取的业务类型的code
	 * @return 业务类型的概要信息
	 */
	BizType getSimpleBizType(String code);

	/**
	 * 获取所有业务类型信息列表
	 * 
	 * @return 所有业务类型信息列表
	 */
	List<BizType> getAllBizTypes();

	/**
	 * 变更业务类型信息
	 * 
	 * @param bizType
	 *            要变更的业务类型信息
	 */
	void updateBizType(BizType bizType);

	/**
	 * 删除业务类型
	 * 
	 * @param code
	 *            要删除的业务类型code
	 */
	void removeBizType(String code);

	/**
	 * 业务类型是否存在
	 * 
	 * @param code
	 *            业务类型code
	 * @return true：存在
	 */
	boolean bizTypeExisted(String code);

	/**
	 * 为业务类型指定规则集合
	 * 
	 * @param bizTypecode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 */
	void assignRuleSet(String bizTypecode, String ruleSetCode, long ruleSetVersion);

}
