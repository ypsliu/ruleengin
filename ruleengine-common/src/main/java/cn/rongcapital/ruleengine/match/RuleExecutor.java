/**
 * 
 */
package cn.rongcapital.ruleengine.match;

import java.util.Map;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.model.Rule;

/**
 * 规则匹配执行者
 * 
 * @author shangchunming@rongcapital.cn
 *
 * @param <R>
 *            规则匹配结果类型
 */
public interface RuleExecutor<R> {

	/**
	 * 执行规则
	 * 
	 * @param rule
	 *            要执行的规则
	 * @param params
	 *            输入参数
	 * @return 匹配结果
	 */
	R execute(Rule rule, Map<String, String> params);

	/**
	 * 测试规则表达式
	 * 
	 * @param expression
	 *            要测试的表达式
	 * @param segments
	 *            表达式片段
	 * @param params
	 *            输入参数
	 * @return 测试结果
	 */
	R evaluate(String expression, Map<String, String> segments, Map<String, String> params);

	/**
	 * 接受的规则匹配类型
	 * 
	 * @return 匹配类型
	 */
	MatchType acceptedMatchType();

}
