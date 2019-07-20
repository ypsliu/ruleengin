/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;

import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;

/**
 * 规则管理服务
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface RuleService {

	/**
	 * 创建规则信息
	 * 
	 * @param rule
	 *            要创建的规则信息
	 * @return 新创建的规则信息
	 */
	Rule createRule(Rule rule);

	/**
	 * 根据code获取规则信息
	 * 
	 * @param code
	 *            要获取的规则信息code
	 * @return 规则信息
	 */
	Rule getRule(String code);

	/**
	 * 根据code和版本号获取规则信息
	 * 
	 * @param code
	 *            要获取的规则信息code
	 * @param version
	 *            版本号
	 * @return 规则信息
	 */
	Rule getRule(String code, long version);

	/**
	 * 根据code获取规则的历史信息
	 * 
	 * @param code
	 *            要获取的规则信息code
	 * @return 规则的历史信息
	 */
	List<Rule> getRuleHistories(String code);

	/**
	 * 变更规则信息
	 * 
	 * @param rule
	 *            要变更的规则信息
	 */
	void updateRule(Rule rule);

	/**
	 * 删除规则信息
	 * 
	 * @param code
	 *            要删除的规则信息code
	 */
	void removeRule(String code);

	/**
	 * 根据业务类型code获取规则信息列表
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @return 规则信息列表
	 */
	List<Rule> getRulesByBizType(String bizTypeCode);

	/**
	 * 获取全部规则信息列表
	 * 
	 * @return 规则信息列表
	 */
	List<Rule> getRules();

	/**
	 * 规则信息是否存在
	 * 
	 * @param code
	 *            规则信息code
	 * @return true：存在
	 */
	boolean ruleExisted(String code);

	/**
	 * 创建规则集合
	 * 
	 * @param ruleSet
	 *            要创建的规则集合信息
	 * @return 新创建的规则集合信息
	 */
	RuleSet createRuleSet(RuleSet ruleSet);

	/**
	 * 根据code获取规则集合
	 * 
	 * @param code
	 *            规则集合code
	 * @return 规则集合信息
	 */
	RuleSet getRuleSet(String code);

	/**
	 * 根据code和版本号获取规则集合
	 * 
	 * @param code
	 *            规则集合code
	 * @param version
	 *            规则集合版本号
	 * @return 规则集合
	 */
	RuleSet getRuleSet(String code, long version);

	/**
	 * 获取规则集合的历史信息
	 * 
	 * @param code
	 *            规则集合code
	 * @return 规则集合历史信息
	 */
	List<RuleSet> getRuleSetHistories(String code);

	/**
	 * 更新规则集合
	 * 
	 * @param ruleSet
	 *            要更新的规则集合信息
	 */
	void updateRuleSet(RuleSet ruleSet);

	/**
	 * 删除规则集合
	 * 
	 * @param code
	 *            规则集合code
	 */
	void removeRuleSet(String code);

	/**
	 * 根据业务类型获取规则集合列表
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @return 规则集合列表
	 */
	List<RuleSet> getRuleSetsByBizType(String bizTypeCode);

	/**
	 * 规则集合是否存在
	 * 
	 * @param code
	 *            规则集合code
	 * @return true：存在
	 */
	boolean ruleSetExisted(String code);

	/**
	 * 获取全部规则集合列表
	 * 
	 * @return 规则集合列表
	 */
	List<RuleSet> getRuleSets();

}
