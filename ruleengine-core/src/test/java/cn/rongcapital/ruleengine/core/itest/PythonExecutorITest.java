/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.core.PythonExecutorImpl;

/**
 * the ITest for PythonExecutor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class PythonExecutorITest {

	@Test
	public void test() {
		// PythonExecutor
		final PythonExecutorImpl e = new PythonExecutorImpl();

		// test-1: validation
		try {
			e.execute(null, null);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e2) {
			// OK
		} catch (Exception e2) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-1: validation passed");

		// test-2: invalid pythonPath
		e.setPythonPath("xxx");
		try {
			e.execute("111", null);
			Assert.fail("why no exception thrown?");
		} catch (RuntimeException e2) {
			// OK
		} catch (Exception e2) {
			Assert.fail("why other exception thrown?");
		}
		System.out.println("test-2: invalid pythonPath passed");

		// test-3: execute without args
		e.setPythonPath("/usr/bin/pythonw");
		List<String> result = e
				.execute(
						"/Users/Kruce/PROJECTS/RONGCAPITAL/rule-engine/ruleengine-core/src/test/resources/python/without-args.py",
						null);
		// check
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("3", result.get(0));
		System.out.println("test-3: execute without args passed");

		// test-4: execute with args
		result = e.execute(
				"/Users/Kruce/PROJECTS/RONGCAPITAL/rule-engine/ruleengine-core/src/test/resources/python/with-args.py",
				new String[] { "3", "4" });
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("7", result.get(0));
		System.out.println("test-4: execute with args passed");
	}
}
