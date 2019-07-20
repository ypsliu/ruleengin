/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import cn.rongcapital.ruleengine.model.EvalRequest;
import cn.rongcapital.ruleengine.model.EvalResult;
import cn.rongcapital.ruleengine.model.MatchRequestData;

/**
 * 规则匹配服务
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface MatchService {

	/**
	 * 根据匹配请求数据匹配规则
	 * 
	 * @param request
	 *            匹配请求数据
	 */
	void match(MatchRequestData request);

	/**
	 * 根据业务类型code和业务code匹配规则，会匹配该业务类型的默认规则集合
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务code
	 */
	void match(String bizTypeCode, String bizCode);

	/**
	 * 根据业务类型code、业务code、规则集合code、规则集合版本号 匹配规则
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 */
	void match(String bizTypeCode, String bizCode, String ruleSetCode, Long ruleSetVersion);

	/**
	 * 测试规则匹配
	 * 
	 * @param request
	 *            请求信息
	 * @return 结果信息
	 */
	EvalResult evaluate(EvalRequest request);
}
