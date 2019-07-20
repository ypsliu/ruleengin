/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.exception.DuplicateException;
import cn.rongcapital.ruleengine.exception.NotFoundException;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.service.BizTypeService;

/**
 * the ITest for BizTypeServiceImpl
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/biztype-itest.xml" })
public class BizTypeServiceITest {

	@Autowired
	private BizTypeService bizTypeService;

	@Autowired
	private TestingService testingService;

	@Test
	public void test() {
		// test-1: create bizType
		final BizType b1 = new BizType();
		b1.setCode("code-1");
		b1.setName("name-1");
		b1.setComment("comment-1");
		this.bizTypeService.createBizType(b1);
		final BizType b2 = new BizType();
		b2.setCode("code-1");
		b2.setName("name-2");
		b2.setComment("comment-2");

		// duplicate code
		try {
			this.bizTypeService.createBizType(b2);
			Assert.fail("why no exception thrown?");
		} catch (DuplicateException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}

		b2.setCode("code-2");
		this.bizTypeService.createBizType(b2);
		System.out.println("test-1: create bizType passed");

		// test-2: get bizType
		final BizType b11 = this.bizTypeService.getSimpleBizType(b1.getCode());
		final BizType b21 = this.bizTypeService.getSimpleBizType(b2.getCode());
		// check
		Assert.assertNotNull(b11);
		Assert.assertEquals(b1.getCode(), b11.getCode());
		Assert.assertEquals(b1.getName(), b11.getName());
		Assert.assertEquals(b1.getComment(), b11.getComment());
		Assert.assertNotNull(b11.getCreationTime());
		Assert.assertNull(b11.getUpdateTime());
		Assert.assertNotNull(b21);
		Assert.assertEquals(b2.getCode(), b21.getCode());
		Assert.assertEquals(b2.getName(), b21.getName());
		Assert.assertEquals(b2.getComment(), b21.getComment());
		Assert.assertNotNull(b21.getCreationTime());
		Assert.assertNull(b21.getUpdateTime());
		System.out.println("test-2: get bizType passed");

		// test-3: get all bizTypes
		List<BizType> bizTypes = this.bizTypeService.getAllBizTypes();
		// check
		Assert.assertNotNull(bizTypes);
		int checked = 0;
		for (final BizType b : bizTypes) {
			if (b1.getCode().equals(b.getCode())) {
				checked++;
			} else if (b2.getCode().equals(b.getCode())) {
				checked++;
			}
		}
		Assert.assertEquals(2, checked);
		System.out.println("test-3: get all bizTypes passed");

		// test-4: update bizType
		b1.setName("name-1-1");
		b1.setComment("comment-1-1");

		// old not found
		String oldCode = b1.getCode();
		try {
			b1.setCode("_x_x_x_");
			this.bizTypeService.updateBizType(b1);
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}

		b1.setCode(oldCode);
		this.bizTypeService.updateBizType(b1);
		final BizType b12 = this.bizTypeService.getBizType("code-1");
		// check
		Assert.assertNotNull(b12);
		Assert.assertEquals("code-1", b12.getCode());
		Assert.assertEquals("name-1-1", b12.getName());
		Assert.assertEquals("comment-1-1", b12.getComment());
		Assert.assertNotNull(b12.getUpdateTime());
		System.out.println("test-4: update bizType passed");

		// test-5: remove bizType
		oldCode = b2.getCode();
		try {
			b2.setCode("_x_x_x_");
			this.bizTypeService.removeBizType(b2.getCode());
			Assert.fail("why no exception thrown?");
		} catch (NotFoundException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why other exception thrown?");
		}

		b2.setCode(oldCode);
		this.bizTypeService.removeBizType(b2.getCode());
		// check
		Assert.assertNull(this.bizTypeService.getBizType(b2.getCode()));
		Assert.assertNotNull(this.bizTypeService.getBizType(b1.getCode()));
		bizTypes = this.bizTypeService.getAllBizTypes();
		Assert.assertNotNull(bizTypes);
		Assert.assertEquals(1, bizTypes.size());
		Assert.assertEquals(b1.getCode(), bizTypes.get(0).getCode());
		System.out.println("test-5: remove bizType passed");

		// test-6: existed
		Assert.assertTrue(this.bizTypeService.bizTypeExisted(b1.getCode()));
		Assert.assertFalse(this.bizTypeService.bizTypeExisted(b2.getCode()));
		Assert.assertFalse(this.bizTypeService.bizTypeExisted("xxxxxxx"));
		System.out.println("test-6: existed passed");
	}

	@Before
	public void setup() {
		this.clearDatas();
	}

	@After
	public void teardown() {
		this.clearDatas();
	}

	private void clearDatas() {
		this.testingService.clearBizTypes();
	}

	/**
	 * @param bizTypeService
	 *            the bizTypeService to set
	 */
	public void setBizTypeService(BizTypeService bizTypeService) {
		this.bizTypeService = bizTypeService;
	}

	/**
	 * @param testingService
	 *            the testingService to set
	 */
	public void setTestingService(TestingService testingService) {
		this.testingService = testingService;
	}

}
