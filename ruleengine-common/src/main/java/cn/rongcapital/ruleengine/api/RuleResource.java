/**
 * 
 */
package cn.rongcapital.ruleengine.api;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;

/**
 * 规则管理API
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("rule")
public interface RuleResource {

	/**
	 * 创建业务类型
	 * 
	 * @param bizType
	 *            要创建的业务类型信息
	 * @return 新创建的业务类型信息
	 */
	@Path("/bizType")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
    BizType createBizType(@Valid BizType bizType);

	/**
	 * 变更业务类型
	 * 
	 * @param code
	 *            要变更的业务类型code
	 * @param bizType
	 *            要变更的业务类型信息
	 */
	@Path("/bizType/{code}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	void updateBizType(@PathParam("code") String code, @Valid BizType bizType);

	/**
	 * 删除业务类型
	 * 
	 * @param code
	 *            要删除的业务类型code
	 */
	@Path("/bizType/{code}")
	@DELETE
	void removeBizType(@PathParam("code") String code);

	/**
	 * 获取全部业务类型列表
	 * 
	 * @return 业务类型列表
	 */
	@Path("/bizType")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<BizType> getAllBizTypes();

	/**
	 * 根据code获取业务类型
	 * 
	 * @param code
	 *            业务类型code
	 * @return 业务类型
	 */
	@Path("/bizType/{code}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	BizType getBizType(@PathParam("code") String code);

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
	@Path("/bizType/{code}/ruleSet/{ruleSetCode}/{ruleSetVersion}")
	@PUT
	void assignRuleSet(@PathParam("code") String bizTypecode, @PathParam("ruleSetCode") String ruleSetCode,
			@PathParam("ruleSetVersion") long ruleSetVersion);

	/**
	 * 创建规则
	 * 
	 * @param rule
	 *            要创建的规则信息
	 * @return 新创建的规则信息
	 */
	@Path("/")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	Rule createRule(@Valid Rule rule);

	/**
	 * 变更规则
	 * 
	 * @param code
	 *            要变更的规则code
	 * @param rule
	 *            要变更的规则信息
	 */
	@Path("/{code}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	void updateRule(@PathParam("code") String code, @Valid Rule rule);

	/**
	 * 删除规则
	 * 
	 * @param code
	 *            要删除的规则code
	 */
	@Path("/{code}")
	@DELETE
	void removeRule(@PathParam("code") String code);

	/**
	 * 根据业务类型code获取规则列表
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @return 规则列表
	 */
	@Path("/bizType/{bizTypeCode}/rule")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<Rule> getRulesByBizType(@PathParam("bizTypeCode") String bizTypeCode);

	/**
	 * 获取全部规则列表
	 * 
	 * @return 规则列表
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<Rule> getRules();

	/**
	 * 根据规则code获取规则信息
	 * 
	 * @param code
	 *            规则code
	 * @return 规则信息
	 */
	@Path("/{code}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	Rule getRule(@PathParam("code") String code);

	/**
	 * 根据规则code获取规则历史信息列表
	 * 
	 * @param code
	 *            规则code
	 * @return 规则列表
	 */
	@Path("/{code}/history")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<Rule> getRuleHistories(@PathParam("code") String code);

	/**
	 * 根据规则code和版本号获取规则历史信息
	 * 
	 * @param code
	 *            规则code
	 * @param version
	 *            版本信息
	 * @return 规则历史信息
	 */
	@Path("/{code}/history/{version}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	Rule getRuleHistory(@PathParam("code") String code, @PathParam("version") long version);

	/**
	 * 创建数据源信息
	 * 
	 * @param datasource
	 *            要创建的数据源信息
	 * @return 新创建的数据源信息
	 */
	@Path("/datasource")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	Datasource createDatasource(@Valid Datasource datasource);

	/**
	 * 变更数据源信息
	 * 
	 * @param datasource
	 *            要变更的数据源信息
	 */
	@Path("/datasource/{code}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	void updateDatasource(@PathParam("code") String code, @Valid Datasource datasource);

	/**
	 * 根据数据源code获取数据源信息
	 * 
	 * @param code
	 *            要获取的数据源code
	 * @return 数据源信息
	 */
	@Path("/datasource/{code}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	Datasource getDatasource(@PathParam("code") String code);

	/**
	 * 获取全部数据源信息列表
	 * 
	 * @return 数据源信息列表
	 */
	@Path("/datasource")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<Datasource> getDatasources();

	/**
	 * 删除数据源
	 * 
	 * @param code
	 *            要删除的数据源code
	 */
	@Path("/datasource/{code}")
	@DELETE
	void removeDatasource(@PathParam("code") String code);

	/**
	 * 创建规则集合
	 * 
	 * @param ruleSet
	 *            要创建的规则集合信息
	 * @return 新创建的规则集合信息
	 */
	@Path("/ruleset")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
    RuleSet createRuleSet(@Valid RuleSet ruleSet);

	/**
	 * 变更规则集合
	 * 
	 * @param code
	 *            要变更的规则集合code
	 * @param ruleSet
	 *            要变更的规则集合信息
	 */
	@Path("/ruleset/{code}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	void updateRuleSet(@PathParam("code") String code, @Valid RuleSet ruleSet);

	/**
	 * 删除规则集合
	 * 
	 * @param code
	 *            要删除的规则集合code
	 */
	@Path("/ruleset/{code}")
	@DELETE
	void removeRuleSet(@PathParam("code") String code);

	/**
	 * 根据业务类型code获取规则集合列表
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @return 规则集合列表
	 */
	@Path("/bizType/{bizTypeCode}/ruleset")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<RuleSet> getRuleSetsByBizType(@PathParam("bizTypeCode") String bizTypeCode);

	/**
	 * 根据规则集合code获取规则集合信息
	 * 
	 * @param code
	 *            规则集合code
	 * @return 规则集合信息
	 */
	@Path("/ruleset/{code}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	RuleSet getRuleSet(@PathParam("code") String code);

	/**
	 * 根据规则集合code获取规则集合历史信息列表
	 * 
	 * @param code
	 *            规则集合code
	 * @return 规则集合列表
	 */
	@Path("/ruleset/{code}/history")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<RuleSet> getRuleSetHistories(@PathParam("code") String code);

	/**
	 * 根据规则集合code和版本号获取规则集合历史信息
	 * 
	 * @param code
	 *            规则集合code
	 * @param version
	 *            版本号
	 * @return 规则集合历史信息
	 */
	@Path("/ruleset/{code}/history/{version}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	RuleSet getRuleSetHistory(@PathParam("code") String code, @PathParam("version") long version);

	/**
	 * 获取全部规则集合信息列表
	 *
	 * @return 规则集合列表
	 */
	@Path("/ruleset")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	List<RuleSet> getRuleSets();

}
