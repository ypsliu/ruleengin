/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.data;

import java.util.List;

import cn.rongcapital.ruleengine.model.BizType;

/**
 * the bizTypes
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class BizTypes {

	/**
	 * the bizTypes
	 */
	private List<BizType> bizTypes;

	/**
	 * @return the bizTypes
	 */
	public List<BizType> getBizTypes() {
		return bizTypes;
	}

	/**
	 * @param bizTypes
	 *            the bizTypes to set
	 */
	public void setBizTypes(List<BizType> bizTypes) {
		this.bizTypes = bizTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BizTypes [bizTypes=" + bizTypes + "]";
	}

}
