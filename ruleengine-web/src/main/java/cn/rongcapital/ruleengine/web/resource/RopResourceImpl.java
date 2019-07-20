/**
 * 
 */
package cn.rongcapital.ruleengine.web.resource;

import java.util.Map;

import javax.ws.rs.NotAuthorizedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import cn.rongcapital.ruleengine.api.RopResource;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.MatchRequestData;
import cn.rongcapital.ruleengine.model.MatchResult;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.rop.RopMatchRequestForm;
import cn.rongcapital.ruleengine.rop.RopMatchRequestResponse;
import cn.rongcapital.ruleengine.rop.RopMatchResultForm;
import cn.rongcapital.ruleengine.rop.RopMatchResultResponse;
import cn.rongcapital.ruleengine.rop.RopReferenceDataForm;
import cn.rongcapital.ruleengine.rop.RopReferenceDataResponse;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.RopUtils;
import cn.rongcapital.ruleengine.service.MatchService;
import cn.rongcapital.ruleengine.service.ReferenceDataService;
import cn.rongcapital.ruleengine.service.ResultService;
import cn.rongcapital.ruleengine.utils.JsonHelper;

/**
 * the implementation for RopResource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Controller
public final class RopResourceImpl implements RopResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RopResourceImpl.class);

	@Value("${rop.secret}")
	private String ropSecret;

	@Autowired
	private MatchService matchService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private ReferenceDataService referenceDataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.RopResource#match(cn.rongcapital.ruleengine.rop.RopMatchRequestForm)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RopResponse match(final RopMatchRequestForm request) {
		// verify sign
		if (!RopUtils.verifySign(request, this.ropSecret)) {
			LOGGER.error("verify match request sign failed, request: {}", request);
			// verify sign failed
			throw new NotAuthorizedException("sign无效");
		}
		if (request.getDatas() != null && !request.getDatas().trim().isEmpty()) {
			// with datas
			Map<String, String> datas = null;
			try {
				datas = JsonHelper.toObject(request.getDatas(), Map.class);
			} catch (Exception e) {
				throw new InvalidParameterException("datas不是一个合法的JSON格式的MAP", e);
			}
			// convert to MatchRequestData
			final MatchRequestData mrd = new MatchRequestData();
			mrd.setBizCode(request.getBizCode());
			mrd.setBizTypeCode(request.getBizTypeCode());
			mrd.setDatas(datas);
			this.matchService.match(mrd);
		} else {
			// without datas
			this.matchService.match(request.getBizTypeCode(), request.getBizCode());
		}
		// return the ROP response
		return new RopMatchRequestResponse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.rongcapital.ruleengine.api.RopResource#getResults(cn.rongcapital.ruleengine.rop.RopMatchResultForm)
	 */
	@Override
	public RopResponse getResults(final RopMatchResultForm request) {
		// verify sign
		if (!RopUtils.verifySign(request, this.ropSecret)) {
			LOGGER.error("verify get match result sign failed, request: {}", request);
			// verify sign failed
			throw new NotAuthorizedException("sign无效");
		}
		// create the ROP response
		final RopMatchResultResponse response = new RopMatchResultResponse();
		// get result
		final MatchResult result = this.resultService.getMatchResult(request.getBizTypeCode(), request.getBizCode());
		if (result != null) {
			response.setAcceptance(result.getAcceptance());
			response.setScore(result.getScore());
		}
		// return the response
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.rongcapital.ruleengine.api.RopResource#getReferenceData(cn.rongcapital.ruleengine.rop.RopReferenceDataForm
	 * )
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RopResponse getReferenceData(final RopReferenceDataForm request) {
		// verify sign
		if (!RopUtils.verifySign(request, this.ropSecret)) {
			LOGGER.error("verify match request sign failed, request: {}", request);
			// verify sign failed
			throw new NotAuthorizedException("sign无效");
		}
		// conditions
		Map<String, String> conditions = null;
		try {
			conditions = JsonHelper.toObject(request.getConditions(), Map.class);
		} catch (Exception e) {
			throw new InvalidParameterException("conditions不是一个合法的JSON格式的MAP", e);
		}
		// get data
		final ReferenceData rd = this.referenceDataService.getReferenceData(request.getBizTypeCode(), conditions);
		RopReferenceDataResponse response = new RopReferenceDataResponse();
		if (rd == null) {
			// pending
			response.setPending(true);
			response.setResult(null);
			LOGGER.info("the get reference data request accepted, bizTypeCode: {}, conditions: {}",
					request.getBizTypeCode(), conditions);
		} else {
			// with result data
			response.setPending(null);
			response.setResult(rd);
		}
		return response;
	}

	/**
	 * @param ropSecret
	 *            the ropSecret to set
	 */
	public void setRopSecret(final String ropSecret) {
		this.ropSecret = ropSecret;
	}

	/**
	 * @param matchService
	 *            the matchService to set
	 */
	public void setMatchService(final MatchService matchService) {
		this.matchService = matchService;
	}

	/**
	 * @param resultService
	 *            the resultService to set
	 */
	public void setResultService(final ResultService resultService) {
		this.resultService = resultService;
	}

	/**
	 * @param referenceDataService
	 *            the referenceDataService to set
	 */
	public void setReferenceDataService(final ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

}
