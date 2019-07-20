/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.IdCardNumberValidator;

/**
 * the unit test for IdCardNumberValidator
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class IdCardNumberValidatorTest {

	@Test
	public void testValidateIdCardNumber() {
		// IdCardNumberValidator
		final IdCardNumberValidator icnv = new IdCardNumberValidator();

		// test-1: null
		try {
			icnv.exec(new Object[] {});
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: null passed");

		// test-2: blank
		try {
			icnv.exec(" ");
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-2: blank passed");

		// test-3: invalid 15
		Assert.assertFalse((Boolean) icnv.exec("123456789012345"));
		System.out.println("test-3: invalid 15 passed");

		// test-4: invalid 18
		Assert.assertFalse((Boolean) icnv.exec("123456789012345678"));
		System.out.println("test-4: invalid 18 passed");

		// test-5: good 15
		Assert.assertTrue((Boolean) icnv.exec("320902750507012"));
		System.out.println("test-5: good 15 passed");

		// test-6: good 18
		Assert.assertTrue((Boolean) icnv.exec("32090219750507123X"));
		Assert.assertTrue((Boolean) icnv.exec("32090219750507123x"));
		System.out.println("test-6: good 18 passed");
	}

}
