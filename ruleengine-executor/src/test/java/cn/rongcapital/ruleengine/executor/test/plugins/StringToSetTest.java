/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.StringToSet;

/**
 * the unit test for StringToMap
 * 
 * @author hill
 *
 */
public class StringToSetTest {

	@Test
	public void testStringToArray() {
		final StringToSet sta = new StringToSet();

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
		HashSet<Object> result = new HashSet<Object>();
		result.add("中中");
		result.add("大大");
		result.add("小小");
//		System.out.println("expect:"+result.toString());
		Object check =sta.exec("中中|大大|小小");
//		System.out.println("check:"+check);
		Assert.assertEquals(result, check);
		System.out.println("test-2:a|b|c passed");
		
	}

}
