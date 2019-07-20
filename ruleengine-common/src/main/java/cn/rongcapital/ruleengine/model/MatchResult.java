/**
 * 
 */
package cn.rongcapital.ruleengine.model;

/**
 * 匹配结果
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class MatchResult {

	/**
	 * 是否接受类型的规则 的匹配结果
	 */
	private AcceptanceResults acceptance;

	/**
	 * 评分类型的规则 的匹配结果
	 */
	private ScoreResults score;

	/**
	 * 文本类型的规则 的匹配结果
	 */
	private TextResults text;

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

	/**
	 * @return the text
	 */
	public TextResults getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(TextResults text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MatchResult [acceptance=" + acceptance + ", score=" + score + ", text=" + text + "]";
	}

}
