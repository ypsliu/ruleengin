/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.FixedPhoneNumberValidator;

/**
 * the unit test for FixedPhoneNumberValidator
 * 
 * @author hill
 *
 */
public class FixedPhoneNumberValidatorTest {

	@Test
	public void testValidateCellPhoneNumber() {
		// FixedPhoneNumberValidator
		final FixedPhoneNumberValidator cpnv = new FixedPhoneNumberValidator();

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
		Assert.assertTrue((Boolean) cpnv.exec("12345678901"));
		System.out.println("test-4: invalid passed");

		// test-5: good
		Assert.assertTrue((Boolean) cpnv.exec("5014488"));
		System.out.println("test-5: good passed");
		
		// test-6: good
		Assert.assertTrue((Boolean) cpnv.exec("62369407"));
		System.out.println("test-6: good passed");
		
		// test-7: good
		Assert.assertTrue((Boolean) cpnv.exec("010-50144889"));
		System.out.println("test-7: good passed");
		
		// test-8: good
		Assert.assertTrue((Boolean) cpnv.exec("01050144889"));
		System.out.println("test-8: good passed");
		
		// test-9: good
		Assert.assertTrue((Boolean) cpnv.exec("0312-50144889"));
		System.out.println("test-9: good passed");
		
		// test-10: good
		Assert.assertTrue((Boolean) cpnv.exec("031250144889"));
		System.out.println("test-10: good passed");
	}

}
