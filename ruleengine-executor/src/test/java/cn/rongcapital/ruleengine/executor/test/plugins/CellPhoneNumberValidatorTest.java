/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.CellPhoneNumberValidator;

/**
 * the unit test for CellPhoneNumberValidator
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class CellPhoneNumberValidatorTest {

	@Test
	public void testValidateCellPhoneNumber() {
		// CellPhoneNumberValidator
		final CellPhoneNumberValidator cpnv = new CellPhoneNumberValidator();

		// test-1: null
		try {
			cpnv.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: null passed");

		// test-2: too short
		Assert.assertFalse((Boolean) cpnv.exec("1"));
		System.out.println("test-2: too short passed");

		// test-3: too long
		Assert.assertFalse((Boolean) cpnv.exec("123456789012345"));
		System.out.println("test-3: too short passed");

		// test-4: invalid
		Assert.assertFalse((Boolean) cpnv.exec("12345678901"));
		System.out.println("test-4: too short passed");

		// test-5: good
		Assert.assertTrue((Boolean) cpnv.exec("13545678901"));
		System.out.println("test-5: good passed");
		
		// test-6: good
		Assert.assertTrue((Boolean) cpnv.exec("8613545678901"));
		System.out.println("test-6: good passed");
		
		// test-7: good
		Assert.assertTrue((Boolean) cpnv.exec("+8613545678901"));
		System.out.println("test-7: good passed");
		
		// test-8: good
		Assert.assertTrue((Boolean) cpnv.exec("+86-13545678901"));
		System.out.println("test-8: good passed");
		
		// test-9: good
		Assert.assertTrue((Boolean) cpnv.exec("+86-135-4567-8901"));
		System.out.println("test-9: good passed");
	}

}
