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

import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataCondition;

/**
 * 参考数据API
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Path("reference")
public interface ReferenceDataResource {

	/**
	 * 根据条件查询参考数据
	 * 
	 * @param condition
	 *            条件
	 * @return 参考数据，如果返回null则表示参考数据爬取中
	 */
	@Path("/")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
    ReferenceData getReferenceData(@Valid ReferenceDataCondition condition);

}
