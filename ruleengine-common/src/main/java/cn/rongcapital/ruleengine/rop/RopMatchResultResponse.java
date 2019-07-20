/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import cn.rongcapital.ruleengine.api.RopResource;
import cn.rongcapital.ruleengine.model.ScoreResults;
import org.codehaus.jackson.annotate.JsonTypeName;

import cn.rongcapital.ruleengine.model.AcceptanceResults;

/**
 * ROP的匹配结果响应信息
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeName(RopResource.RESULT_API_NAME)
public final class RopMatchResultResponse extends RopResponse {

	/**
	 * 是否接受类型的规则 的匹配结果
	 */
	private AcceptanceResults acceptance;

	/**
	 * 评分类型的规则 的匹配结果
	 */
	private ScoreResults score;

	/**
	 * @return the acceptance
	 */
	public AcceptanceResults getAcceptance() {
		return acceptance;
	}

	/**
	 * @param acceptance
	 *            the acceptance to set
	 */
	public void setAcceptance(AcceptanceResults acceptance) {
		this.acceptance = acceptance;
	}

	/**
	 * @return the score
	 */
	public ScoreResults getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(ScoreResults score) {
		this.score = score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RopMatchResultResponse [acceptance=" + acceptance + ", score=" + score + "]";
	}

}
