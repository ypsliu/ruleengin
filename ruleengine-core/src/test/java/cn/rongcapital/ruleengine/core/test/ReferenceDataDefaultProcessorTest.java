/**
 * 
 */
package cn.rongcapital.ruleengine.core.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.core.ReferenceDataDefaultProcessor;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.model.ReferenceData;

/**
 * the unit test for ReferenceDataDefaultProcessor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class ReferenceDataDefaultProcessorTest {

	@Test
	public void test() {
		// ReferenceDataDefaultProcessor
		final ReferenceDataDefaultProcessor p = new ReferenceDataDefaultProcessor();

		// bizTypeCode
		final String bizTypeCode = "test-bizTypeCode";

		// conditions
		final Map<String, String> conditions = new HashMap<String, String>();

		// rawDatas
		final Map<String, List<Map<String, String>>> rawDatas = new HashMap<String, List<Map<String, String>>>();

		// validation
		try {
			p.process(null, null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		try {
			p.process(bizTypeCode, null, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		try {
			p.process(bizTypeCode, conditions, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		conditions.put("key-1", "value-1");
		try {
			p.process(bizTypeCode, conditions, null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		try {
			p.process(bizTypeCode, conditions, rawDatas);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}
		rawDatas.put("source-1", new ArrayList<Map<String, String>>());
		rawDatas.get("source-1").add(new HashMap<String, String>());
		rawDatas.get("source-1").get(0).put("data-key-1", "data-value-1");
		ReferenceData rd = null;
		try {
			rd = p.process(bizTypeCode, conditions, rawDatas);
		} catch (Exception e) {
			Assert.fail("why the exception thrown?");
		}
		// check
		Assert.assertNotNull(rd);
		Assert.assertEquals(bizTypeCode, rd.getBizTypeCode());
		Assert.assertEquals(conditions, rd.getConditions());
		Assert.assertEquals(rawDatas, rd.getResponse());
		System.out.println("test passed");
	}

}
