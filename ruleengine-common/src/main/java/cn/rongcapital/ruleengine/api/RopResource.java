/**
 * 
 */
package cn.rongcapital.ruleengine.api;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.Form;

import cn.rongcapital.ruleengine.rop.RopMatchRequestForm;
import cn.rongcapital.ruleengine.rop.RopMatchResultForm;
import cn.rongcapital.ruleengine.rop.RopReferenceDataForm;
import cn.rongcapital.ruleengine.rop.RopResponse;

/**
 * ROP相关API
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("/rop")
public interface RopResource {

	/**
	 * the API name of match
	 */
	String MATCH_API_NAME = "fengchao_risk_match_response";

	/**
	 * the API name of result
	 */
	String RESULT_API_NAME = "fengchao_risk_result_response";

	/**
	 * the API name of reference
	 */
	String REFERENCE_API_NAME = "fengchao_risk_reference_response";

	/**
	 * 触发规则匹配
	 * 
	 * @param request
	 *            匹配请求数据
	 * @return 匹配请求响应
	 */
	@Path("/match")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
	RopResponse match(@Valid @Form RopMatchRequestForm request);

	/**
	 * 获取匹配结果
	 * 
	 * @param request
	 *            请求表单
	 * @return 匹配结果
	 */
	@Path("/result")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
	RopResponse getResults(@Valid @Form RopMatchResultForm request);

	/**
	 * 获取参考资源数据
	 * 
	 * @param request
	 *            请求表单
	 * @return 参考资源数据
	 */
	@Path("/reference")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
	RopResponse getReferenceData(@Valid @Form RopReferenceDataForm request);

}
