/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.plugins;

import org.junit.Assert;
import org.junit.Test;

import cn.rongcapital.ruleengine.enums.Acceptance;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.plugins.CheckContacts;

/**
 * the unit test for CheckContactsTest
 * 
 * @author hill
 *
 */
public class CheckContactsTest {

	@Test
	public void testCheckContactsTest() {
		final CheckContacts sta = new CheckContacts();

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

		//test-2:"安妮|18565004232;沧泽|13049697722;coco|13143517051;alen|18620846065;dwen|18122046952;jay|18818871271;mazi|15089632258;根|13609777674;老豆|13542397052;护照美女|13790905578;leo|13143511678;飞|18825044851;瑞|18582085595;黄山|13227887377;斌|13148986766;晁牙医|186-6627-4984;湛江鸡|62369407;蒸百味|66680958;杨水旺|13417261814;建铭|13926721966"
		String contacts="邹威(乾景)|15872374776;就业管理局|13871458705;刘剑(乾景)|13007126053;王|18986254733;袁智(江岸联|18607184467;新视野|13517117641;汉川|13789990777;沈颖|18672701051;永兴|13545888833;罗姐|18986064599;将军四路|13437155676;罗姐老公|15172415691;刘丹丹|18971284933;黄老师(华传|13871458705;臻纪化公司|18062427775;阳逻三中|13871458705;潜在客户1|18008640892;何金兰|493167703;徐奇飞|13476258304;王佳|13297001511";
		Object check =sta.exec(contacts,20,"融资|贷款|小额|抵押|担保|信用|借钱|套现|宜信|捷越|恒昌|捷信|诺远|信而富|小牛");
//		System.out.println("check:"+check);
		Assert.assertEquals(Acceptance.ACCEPT.name(), check);
		System.out.println("test-2  passed");
		
	}

}
