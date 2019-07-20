/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.IdCardNumberAgeGetter;
import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * the unit test for IdCardNumberAgeGetter
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class IdCardNumberAgeGetterTest {

	@Test
	public void testGetAgeByIdCardNumber() {
		// date format
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// mock the DatetimeProvider
		final DatetimeProvider datetimeProvider = Mockito.mock(DatetimeProvider.class);
		try {
			Mockito.when(datetimeProvider.nowTime()).thenReturn(format.parse("1899-12-12"));
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}

		// IdCardNumberAgeGetter
		final IdCardNumberAgeGetter icnag = new IdCardNumberAgeGetter();
		icnag.setDatetimeProvider(datetimeProvider);

		// test-1: null
		try {
			icnag.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: null passed");

		// test-2: date after now
		Assert.assertEquals(-1, icnag.exec("123456200303031234"));
		System.out.println("test-3: same date now passed");

		// test-3: same date
		Assert.assertEquals(0, icnag.exec("123456189912121234"));
		System.out.println("test-2: date after now passed");

		// test-4: same month/day
		Assert.assertEquals(70, icnag.exec("123456182912121234"));
		System.out.println("test-4: same month/day passed");

		// test-5: before month/day
		Assert.assertEquals(70, icnag.exec("123456182912111234"));
		System.out.println("test-5: before month/day passed");

		// test-6: after month/day
		Assert.assertEquals(69, icnag.exec("123456182912131234"));
		System.out.println("test-6: after month/day passed");
	}

}
