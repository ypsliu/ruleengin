/**
 * 
 */
package cn.rongcapital.ruleengine.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.rongcapital.ruleengine.model.AcceptanceResults;
import cn.rongcapital.ruleengine.model.MatchResult;
import cn.rongcapital.ruleengine.model.ScoreResults;
import cn.rongcapital.ruleengine.model.TextResults;

/**
 * 规则匹配结果API
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("result")
public interface ResultResource {

	/**
	 * 根据业务编号获取匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{bizCode}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	MatchResult getMatchResult(@PathParam("bizTypeCode") String bizTypeCode, @PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{ruleSetCode}/{ruleSetVersion}/{bizCode}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	MatchResult getMatchResult(@PathParam("bizTypeCode") String bizTypeCode,
			@PathParam("ruleSetCode") String ruleSetCode, @PathParam("ruleSetVersion") long ruleSetVersion,
			@PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取 是否接受类型的规则 的匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{bizCode}/acceptance")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	AcceptanceResults getAcceptanceResult(@PathParam("bizTypeCode") String bizTypeCode,
			@PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取 是否接受类型的规则 的匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{ruleSetCode}/{ruleSetVersion}/{bizCode}/acceptance")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	AcceptanceResults getAcceptanceResult(@PathParam("bizTypeCode") String bizTypeCode,
			@PathParam("ruleSetCode") String ruleSetCode, @PathParam("ruleSetVersion") long ruleSetVersion,
			@PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取 评分类型的规则 的匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{bizCode}/score")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
    ScoreResults getScoreResult(@PathParam("bizTypeCode") String bizTypeCode, @PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取 评分类型的规则 的匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{ruleSetCode}/{ruleSetVersion}/{bizCode}/score")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	ScoreResults getScoreResult(@PathParam("bizTypeCode") String bizTypeCode,
			@PathParam("ruleSetCode") String ruleSetCode, @PathParam("ruleSetVersion") long ruleSetVersion,
			@PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取 文本类型的规则 的匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{bizCode}/text")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	TextResults getTextResult(@PathParam("bizTypeCode") String bizTypeCode, @PathParam("bizCode") String bizCode);

	/**
	 * 根据业务编号获取 文本类型的规则 的匹配结果
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 * @param bizCode
	 *            业务编号
	 * @return 匹配结果
	 */
	@Path("/{bizTypeCode}/{ruleSetCode}/{ruleSetVersion}/{bizCode}/text")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	TextResults getTextResult(@PathParam("bizTypeCode") String bizTypeCode,
			@PathParam("ruleSetCode") String ruleSetCode, @PathParam("ruleSetVersion") long ruleSetVersion,
			@PathParam("bizCode") String bizCode);

}
