/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import cn.rongcapital.ruleengine.core.w2d.DataCaptureRequestForm;
import cn.rongcapital.ruleengine.core.w2d.DataCaptureResponse;
import cn.rongcapital.ruleengine.core.w2d.W2dResourceAgent;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class W2dResourceAgentProxy implements W2dResourceAgent {

	private W2dResourceAgent w2dResourceAgent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.core.w2d.W2dResource#capture(cn.rongcapital.ruleengine.core.w2d.
	 * DataCaptureRequestForm)
	 */
	@Override
	public DataCaptureResponse capture(final DataCaptureRequestForm condition) {
		return this.w2dResourceAgent.capture(condition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.core.w2d.W2dResource#capture(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DataCaptureResponse capture(final String name, final String id, final String mobileNumber,
			final String bizLicenceNumber, final String companyName) {
		return this.w2dResourceAgent.capture(name, id, mobileNumber, bizLicenceNumber, companyName);
	}

	/**
	 * @param w2dResourceAgent
	 *            the w2dResourceAgent to set
	 */
	public void setW2dResourceAgent(W2dResourceAgent w2dResourceAgent) {
		this.w2dResourceAgent = w2dResourceAgent;
	}

}
