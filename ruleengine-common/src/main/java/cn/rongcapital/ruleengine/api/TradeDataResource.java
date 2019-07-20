/**
 * 
 */
package cn.rongcapital.ruleengine.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.rongcapital.ruleengine.rop.RopResponse;
import org.jboss.resteasy.annotations.Form;

import cn.rongcapital.ruleengine.rop.TradeDataQueryForm;

/**
 * the trade resource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("/")
public interface TradeDataResource {

	/**
	 * the API name of query
	 */
	String QUERY_API_NAME = "xxx_query_response";

	/**
	 * to query the trade data
	 * 
	 * @param request
	 *            the request
	 * @return the result
	 */
	@Path("ropapi")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
    RopResponse query(@Form TradeDataQueryForm request);

}
