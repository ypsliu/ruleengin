/**
 * 
 */
package cn.rongcapital.ruleengine.utils;

import java.util.Date;

/**
 * the local machine implementation for DatetimeProvider
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class LocalDatetimeProvider implements DatetimeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatetimeProvider#nowTime()
	 */
	@Override
	public Date nowTime() {
		return new Date();
	}

}
