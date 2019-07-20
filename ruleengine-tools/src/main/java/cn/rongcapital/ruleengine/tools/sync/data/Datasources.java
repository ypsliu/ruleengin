/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.data;

import java.util.List;

import cn.rongcapital.ruleengine.model.Datasource;

/**
 * the datasources
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class Datasources {

	/**
	 * the datasource list
	 */
	private List<Datasource> datasources;

	/**
	 * @return the datasources
	 */
	public List<Datasource> getDatasources() {
		return datasources;
	}

	/**
	 * @param datasources
	 *            the datasources to set
	 */
	public void setDatasources(List<Datasource> datasources) {
		this.datasources = datasources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Datasources [datasources=" + datasources + "]";
	}

}
