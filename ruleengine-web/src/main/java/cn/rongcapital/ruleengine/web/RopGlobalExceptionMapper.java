/**
 * 
 */
package cn.rongcapital.ruleengine.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.rop.RopErrorResponse;

import com.ruixue.serviceplatform.commons.web.ErrorInfo;
import com.ruixue.serviceplatform.commons.web.GlobalExceptionMapper;

/**
 * the GlobalExceptionMapper for risk
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Provider
public final class RopGlobalExceptionMapper extends GlobalExceptionMapper {

	@Context
	private UriInfo uri;

	private List<String> ropApiPaths = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruixue.serviceplatform.commons.web.GlobalExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(final Throwable exception) {
		final Response errorResponse = super.toResponse(exception);
		if (this.uri != null) {
			if (this.ropApiPaths.contains(this.uri.getPath())) {
				// it is the ROP API, return the ROP error response
				final RopErrorResponse ropErrorResponse = new RopErrorResponse();
				if (errorResponse.getEntity() instanceof ErrorInfo) {
					// copy global error info
					final ErrorInfo ei = (ErrorInfo) errorResponse.getEntity();
					ropErrorResponse.setCode(ei.getErrorCode());
					ropErrorResponse.setMsg(ei.getErrorMessage());
				} else {
					// default
					ropErrorResponse.setCode("" + this.getDefaultStatusCode());
					ropErrorResponse.setMsg(this.getDefaultMessage());
					if (!StringUtils.isEmpty(exception.getMessage())) {
						ropErrorResponse.setMsg(exception.getMessage());
					}
				}
				return Response.status(Response.Status.OK).entity(ropErrorResponse)
						.type(MediaType.APPLICATION_JSON_TYPE).build();
			}
		}
		return errorResponse;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(final UriInfo uri) {
		this.uri = uri;
	}

	@Value("${rop.api.paths}")
	public void setRopApiPaths(final String ropApiPaths) {
		if (ropApiPaths == null) {
			return;
		}
		final String[] a = ropApiPaths.split("\\,", -1);
		if (a != null && a.length > 0) {
			this.ropApiPaths.clear();
			for (final String url : a) {
				this.ropApiPaths.add(url);
			}
		}
	}

}
