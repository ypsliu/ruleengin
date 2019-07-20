/**
 * 
 */
package cn.rongcapital.ruleengine.web.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.rongcapital.ruleengine.api.ResultResource;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.model.AcceptanceResult;
import cn.rongcapital.ruleengine.model.AcceptanceResults;
import cn.rongcapital.ruleengine.model.BaseResult;
import cn.rongcapital.ruleengine.model.MatchResult;
import cn.rongcapital.ruleengine.model.ScoreResult;
import cn.rongcapital.ruleengine.model.ScoreResults;
import cn.rongcapital.ruleengine.model.TextResult;
import cn.rongcapital.ruleengine.model.TextResults;
import cn.rongcapital.ruleengine.service.ResultService;

/**
 * the implementation for ResultResource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Controller
public final class ResultResourceImpl implements ResultResource {

	@Autowired
	private ResultService resultService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getMatchResult(java.lang.String, java.lang.String)
	 */
	@Override
	public MatchResult getMatchResult(final String bizTypeCode, final String bizCode) {
		final MatchResult mr = this.resultService.getMatchResult(bizTypeCode, bizCode);
		if (mr == null) {
			throw new NotFoundException("the match result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(mr.getAcceptance());
		this.removeInternalInfos(mr.getScore());
		this.removeInternalInfos(mr.getText());
		return mr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getMatchResult(java.lang.String, java.lang.String, long,
	 * java.lang.String)
	 */
	@Override
	public MatchResult getMatchResult(final String bizTypeCode, final String ruleSetCode, final long ruleSetVersion,
			final String bizCode) {
		final MatchResult mr = this.resultService.getMatchResult(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode);
		if (mr == null) {
			throw new NotFoundException("the match result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(mr.getAcceptance());
		this.removeInternalInfos(mr.getScore());
		this.removeInternalInfos(mr.getText());
		return mr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getAcceptanceResult(java.lang.String, java.lang.String)
	 */
	@Override
	public AcceptanceResults getAcceptanceResult(final String bizTypeCode, final String bizCode) {
		final AcceptanceResults ars = this.resultService.getAcceptanceResult(bizTypeCode, bizCode);
		if (ars == null) {
			throw new NotFoundException("the acceptance result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(ars);
		return ars;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getAcceptanceResult(java.lang.String, java.lang.String,
	 * long, java.lang.String)
	 */
	@Override
	public AcceptanceResults getAcceptanceResult(final String bizTypeCode, final String ruleSetCode,
			final long ruleSetVersion, final String bizCode) {
		final AcceptanceResults ars = this.resultService.getAcceptanceResult(bizTypeCode, ruleSetCode, ruleSetVersion,
				bizCode);
		if (ars == null) {
			throw new NotFoundException("the acceptance result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(ars);
		return ars;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getScoreResult(java.lang.String, java.lang.String)
	 */
	@Override
	public ScoreResults getScoreResult(final String bizTypeCode, final String bizCode) {
		final ScoreResults srs = this.resultService.getScoreResult(bizTypeCode, bizCode);
		if (srs == null) {
			throw new NotFoundException("the score result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(srs);
		return srs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getScoreResult(java.lang.String, java.lang.String, long,
	 * java.lang.String)
	 */
	@Override
	public ScoreResults getScoreResult(final String bizTypeCode, final String ruleSetCode, final long ruleSetVersion,
			final String bizCode) {
		final ScoreResults srs = this.resultService.getScoreResult(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode);
		if (srs == null) {
			throw new NotFoundException("the score result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(srs);
		return srs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getTextResult(java.lang.String, java.lang.String)
	 */
	@Override
	public TextResults getTextResult(final String bizTypeCode, final String bizCode) {
		final TextResults trs = this.resultService.getTextResult(bizTypeCode, bizCode);
		if (trs == null) {
			throw new NotFoundException("the text result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(trs);
		return trs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.ResultResource#getTextResult(java.lang.String, java.lang.String, long,
	 * java.lang.String)
	 */
	@Override
	public TextResults getTextResult(String bizTypeCode, String ruleSetCode, long ruleSetVersion, String bizCode) {
		final TextResults trs = this.resultService.getTextResult(bizTypeCode, ruleSetCode, ruleSetVersion, bizCode);
		if (trs == null) {
			throw new NotFoundException("the text result is NOT existed, bizCode: " + bizCode);
		}
		this.removeInternalInfos(trs);
		return trs;
	}

	private void removeInternalInfos(final AcceptanceResults acceptanceResults) {
		if (acceptanceResults == null) {
			return;
		}
		acceptanceResults.setBizType(null);
		if (acceptanceResults.getResults() != null) {
			for (final AcceptanceResult ar : acceptanceResults.getResults()) {
				this.removeInternalInfos(ar);
			}
		}
	}

	private void removeInternalInfos(final ScoreResults scoreResults) {
		if (scoreResults == null) {
			return;
		}
		scoreResults.setBizType(null);
		if (scoreResults.getResults() != null) {
			for (final ScoreResult sr : scoreResults.getResults()) {
				this.removeInternalInfos(sr);
			}
		}
	}

	private void removeInternalInfos(final TextResults textResults) {
		if (textResults == null) {
			return;
		}
		textResults.setBizType(null);
		if (textResults.getResults() != null) {
			for (final TextResult tr : textResults.getResults()) {
				this.removeInternalInfos(tr);
			}
		}
	}

	private void removeInternalInfos(final BaseResult<?> result) {
		if (result != null) {
			result.setParams(null);
			result.setRule(null);
		}
	}

	/**
	 * @param resultService
	 *            the resultService to set
	 */
	public void setResultService(final ResultService resultService) {
		this.resultService = resultService;
	}

}
