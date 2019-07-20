/**
 * 
 */
package cn.rongcapital.ruleengine.api;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.rongcapital.ruleengine.model.EvalRequest;
import cn.rongcapital.ruleengine.model.EvalResult;
import cn.rongcapital.ruleengine.model.MatchRequestData;

/**
 * 规则匹配API
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("match")
public interface MatchResource {

	/**
	 * 使用请求数据触发规则匹配
	 *
	 * @param request
	 *            匹配请求数据
	 */
	@Path("/")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	void match(@Valid MatchRequestData request);

	/**
	 * 触发规则匹配
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务编码
	 */
	@Path("/{bizTypeCode}/{bizCode}")
	@POST
	void match(@PathParam("bizTypeCode") String bizTypeCode, @PathParam("bizCode") String bizCode);

	/**
	 * 触发规则匹配
	 * 
	 * @param bizTypeCode
	 *            业务类型code
	 * @param bizCode
	 *            业务编码
	 * @param ruleSetCode
	 *            规则集合code
	 * @param ruleSetVersion
	 *            规则集合版本号
	 */
	@Path("/{bizTypeCode}/{bizCode}/{ruleSetCode}/{ruleSetVersion}")
	@POST
	void match(@PathParam("bizTypeCode") String bizTypeCode, @PathParam("bizCode") String bizCode,
			@PathParam("ruleSetCode") String ruleSetCode, @PathParam("ruleSetVersion") long ruleSetVersion);

	/**
	 * 测试规则匹配表达式
	 * 
	 * @param request
	 *            测试请求信息
	 * @return 测试结果
	 */
	@Path("/eval")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
    EvalResult evaluate(@Valid EvalRequest request);

}
