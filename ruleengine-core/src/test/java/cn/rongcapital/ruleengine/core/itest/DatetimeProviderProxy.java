/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.Date;

import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class DatetimeProviderProxy implements DatetimeProvider {

	private DatetimeProvider datetimeProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatetimeProvider#nowTime()
	 */
	@Override
	public Date nowTime() {
		return this.datetimeProvider.nowTime();
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

}
