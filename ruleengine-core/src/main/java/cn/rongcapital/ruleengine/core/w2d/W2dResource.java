/**
 * 
 */
package cn.rongcapital.ruleengine.core.w2d;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * the w2d API resource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("/crawler")
public interface W2dResource {

	/**
	 * to capture
	 * 
	 * @param condition
	 *            the capture condition
	 * @return the response
	 */
	@Path("CaptureData")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON })
	DataCaptureResponse capture(DataCaptureRequestForm condition);

	/**
	 * to capture
	 * 
	 * @param name
	 *            姓名
	 * @param id
	 *            身份证号码
	 * @param mobileNumber
	 *            手机号
	 * @param bizLicenceNumber
	 *            营业执照号
	 * @param companyName
	 *            公司名称
	 * @return 任务id
	 */
	@Path("CaptureData")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	DataCaptureResponse capture(@QueryParam("name") String name, @QueryParam("id") String id,
			@QueryParam("mobileNumber") String mobileNumber, @QueryParam("bizLicenceNumber") String bizLicenceNumber,
			@QueryParam("companyName") String companyName);

}
