/**
 * 
 */
package cn.rongcapital.ruleengine.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.rop.RopErrorResponse;

import com.ruixue.serviceplatform.commons.exception.InvalidField;
import com.ruixue.serviceplatform.commons.web.ErrorInfo;
import com.ruixue.serviceplatform.commons.web.ValidationExceptionHandler;

/**
 * the validation exception handler for ROP
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Provider
public final class RopValidationExceptionHandler extends ValidationExceptionHandler {

	@Context
	private UriInfo uri;

	private List<String> ropApiPaths = new ArrayList<String>();

	@Value("${rop.validation.default.error.code}")
	private String defaultErrorCode;

	@Value("${rop.validation.default.error.message}")
	private String defaultErrorMessage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ruixue.serviceplatform.commons.web.ValidationExceptionHandler#toResponse(javax.validation.ValidationException
	 * )
	 */
	@Override
	public Response toResponse(ValidationException exception) {
		final Response errorResponse = super.toResponse(exception);
		if (this.uri != null) {
			if (this.ropApiPaths.contains(this.uri.getPath())) {
				// it is the ROP API, return the ROP error response
				final RopErrorResponse ropErrorResponse = new RopErrorResponse();
				if (errorResponse.getEntity() instanceof ErrorInfo) {
					// copy global error info
					final ErrorInfo ei = (ErrorInfo) errorResponse.getEntity();
					ropErrorResponse.setCode(ei.getErrorCode());
					ropErrorResponse.setMsg(this.defaultErrorMessage);
					if (ei.getErrorFields() != null) {
						ropErrorResponse.setMsg(ropErrorResponse.getMsg() + ": ");
						final StringBuilder buf = new StringBuilder();
						for (final InvalidField f : ei.getErrorFields()) {
							if (buf.length() > 0) {
								buf.append(",");
							}
							buf.append(f.getField());
						}
						ropErrorResponse.setMsg(ropErrorResponse.getMsg() + buf.toString());
					}
				} else {
					// default
					ropErrorResponse.setCode(this.defaultErrorCode);
					ropErrorResponse.setMsg(this.defaultErrorMessage);
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

	/**
	 * @param defaultErrorCode
	 *            the defaultErrorCode to set
	 */
	public void setDefaultErrorCode(final String defaultErrorCode) {
		this.defaultErrorCode = defaultErrorCode;
	}

	/**
	 * @param defaultErrorMessage
	 *            the defaultErrorMessage to set
	 */
	public void setDefaultErrorMessage(final String defaultErrorMessage) {
		this.defaultErrorMessage = defaultErrorMessage;
	}

}
