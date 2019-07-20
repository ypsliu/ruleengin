/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;
import java.util.Map;

import cn.rongcapital.ruleengine.model.Rule;

/**
 * 规则数据读取者
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface DataExtractor {

	/**
	 * 根据规则和业务code读取规则匹配数据
	 * 
	 * @param rule
	 *            规则
	 * @param bizCode
	 *            业务code
	 * @return 规则匹配数据
	 */
	Map<String, String> extractRuleDatas(Rule rule, String bizCode);

	/**
	 * 从请求数据中读取规则匹配数据
	 * 
	 * @param ruleParams
	 *            规则所需数据列表
	 * @param datas
	 *            请求数据，key为数据集合名称，value为数据的键值对
	 * @return 规则匹配数据
	 */
	Map<String, String> extractRuleDatas(List<String> ruleParams, Map<String, String> datas);

}
