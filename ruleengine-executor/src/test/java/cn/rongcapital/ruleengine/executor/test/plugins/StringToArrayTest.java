/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.StringToArray;

/**
 * the unit test for StringToArray
 * 
 * @author hill
 *
 */
public class StringToArrayTest {

	@Test
	public void testStringToArray() {
		final StringToArray sta = new StringToArray();

		// test-1: null
		try {
			sta.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: null passed");

		//test-2:"a|b|c"
		Assert.assertEquals("['a','b','c']", sta.exec("a|b|c"));
		System.out.println("test-2:a|b|c passed");
		
	}

}
