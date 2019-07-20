/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import cn.rongcapital.ruleengine.model.*;

import java.util.List;

/**
 * 规则匹配结果服务
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ResultService {

	/**
	 * 根据业务code获取所有规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	MatchResult getMatchResult(String bizTypeCode, String bizCode);

	/**
	 * 根据业务code获取所有规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	MatchResult getMatchResult(String bizTypeCode, String ruleSetCode, Long ruleSetVersion, String bizCode);

	/**
	 * 根据业务code获取是否接受类型的规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	AcceptanceResults getAcceptanceResult(String bizTypeCode, String bizCode);

	/**
	 * 根据业务code获取是否接受类型的规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	AcceptanceResults getAcceptanceResult(String bizTypeCode, String ruleSetCode, Long ruleSetVersion, String bizCode);

	/**
	 * 根据业务code获取评分类型的规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	ScoreResults getScoreResult(String bizTypeCode, String bizCode);

	/**
	 * 根据业务code获取评分类型的规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	ScoreResults getScoreResult(String bizTypeCode, String ruleSetCode, Long ruleSetVersion, String bizCode);

	/**
	 * 根据业务code获取文本类型的规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	TextResults getTextResult(String bizTypeCode, String bizCode);

	/**
	 * 根据业务code获取文本类型的规则匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务code
	 * @return 匹配结果
	 */
	TextResults getTextResult(String bizTypeCode, String ruleSetCode, Long ruleSetVersion, String bizCode);

	/**
	 *
	 * @param result
	 * @return
	 */
	List<ResultPojo> getResult(AcceptanceResults result);

	/**
	 * 保存是否接受类型的规则匹配结果
	 * 
	 * @param result
	 *            要保存的匹配结果
	 */
	void saveResult(AcceptanceResults result);

	/**
	 *
	 * @param result
	 * @return
	 */
	List<ResultPojo> getResult(ScoreResults result);

	/**
	 * 保存评分类型的规则匹配结果
	 * 
	 * @param result
	 *            要保存的匹配结果
	 */
	void saveResult(ScoreResults result);

	/**
	 *
	 * @param result
	 * @return
	 */
	List<ResultPojo> getResult(TextResults result);

	/**
	 *
	 * @param param
	 */
	void saveResult(List<ResultPojo> param);

	/**
	 * 保存文本类型的规则匹配结果
	 * 
	 * @param result
	 *            要保存的匹配结果
	 */
	void saveResult(TextResults result);
}
