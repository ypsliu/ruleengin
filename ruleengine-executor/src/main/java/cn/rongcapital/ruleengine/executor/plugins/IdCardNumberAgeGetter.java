/**
 * 
 */
package cn.rongcapital.ruleengine.executor.plugins;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.ExecutionPlugin;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;
import cn.rongcapital.ruleengine.utils.LocalDatetimeProvider;

/**
 * the IdCard number age getter plugin
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class IdCardNumberAgeGetter implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdCardNumberAgeGetter.class);

	private DatetimeProvider datetimeProvider = new LocalDatetimeProvider();

	private final Calendar cal = Calendar.getInstance();

	private final SimpleDateFormat idCardNumberBirthdayDateFormat15 = new SimpleDateFormat("yyMMdd");

	private final SimpleDateFormat idCardNumberBirthdayDateFormat18 = new SimpleDateFormat("yyyyMMdd");

	private String pluginName = "IdCardNumberAgeGetter";

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#pluginName()
	 */
	@Override
	public String pluginName() {
		return this.pluginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#execute(java.lang.Object[])
	 */
	@Override
	public Object exec(final Object... params) {
		// check
		if (params == null || params.length < 1 || params[0] == null || !(params[0] instanceof String)) {
			LOGGER.error("invalid params");
			throw new InvalidParameterException("invalid params");
		}
		final String idCardNumber = (String) params[0];
		if (idCardNumber == null || (idCardNumber.length() != 15 && idCardNumber.length() != 18)) {
			LOGGER.error("invalid idCardNumber: {}", idCardNumber);
			throw new InvalidParameterException("invalid idCardNumber: " + idCardNumber);
		}
		Date birthday = null;
		try {
			if (idCardNumber.length() == 15) {
				birthday = this.idCardNumberBirthdayDateFormat15.parse(idCardNumber.substring(6, 12));
			} else {
				birthday = this.idCardNumberBirthdayDateFormat18.parse(idCardNumber.substring(6, 14));
			}
			return this.getAgeByDate(birthday);
		} catch (Exception e) {
			LOGGER.error(
					"get the birthday from idCardNumber failed, idCardNumber: " + idCardNumber + ", error: "
							+ e.getMessage(), e);
		}
		return -1;
	}

	/**
	 * to get the age by the birthday
	 * 
	 * @param birthday
	 *            the birthday
	 * @return the age
	 */
	private int getAgeByDate(final Date birthday) {
		// check
		if (birthday == null) {
			return -1;
		}
		// now
		this.cal.setTime(this.datetimeProvider.nowTime());
		final int nowYear = cal.get(Calendar.YEAR);
		final int nowMonth = cal.get(Calendar.MONTH) + 1;
		final int nowDay = cal.get(Calendar.DAY_OF_MONTH);
		// birthday
		cal.setTime(birthday);
		final int birthYear = cal.get(Calendar.YEAR);
		final int birthMonth = cal.get(Calendar.MONTH) + 1;
		final int birthDay = cal.get(Calendar.DAY_OF_MONTH);

		// check
		if (nowYear < birthYear) {
			// birthday after now
			return -1;
		}
		int age = nowYear - birthYear;
		if (nowMonth <= birthMonth) {
			if (nowMonth == birthMonth) {
				if (nowDay < birthDay) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		if (datetimeProvider != null) {
			this.datetimeProvider = datetimeProvider;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionPlugin#setPluginName(java.lang.String)
	 */
	@Override
	public void setPluginName(final String pluginName) {
		if (pluginName != null) {
			this.pluginName = pluginName;
		}
	}

}
