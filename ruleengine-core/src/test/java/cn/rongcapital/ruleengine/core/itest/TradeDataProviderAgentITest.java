/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.core.trade.TradeDataProviderAgent;
import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.TradeDataQueryForm;
import cn.rongcapital.ruleengine.rop.TradeDataQueryResponse;

/**
 * the ITest for TradeDataProviderAgent
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/tradedata-agent-itest.xml" })
public class TradeDataProviderAgentITest {

	@Autowired
	private TradeDataProviderAgent tradeDataProviderAgent;

	@Test
	public void testValidation() {
		final TradeDataQueryForm request = new TradeDataQueryForm();
		try {
			this.tradeDataProviderAgent.query(null);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		try {
			this.tradeDataProviderAgent.query(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		request.setTable("table-1");
		try {
			this.tradeDataProviderAgent.query(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		request.setColumns(new ArrayList<String>());
		try {
			this.tradeDataProviderAgent.query(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		request.getColumns().add("column-1");
		request.getColumns().add("column-2");
		try {
			this.tradeDataProviderAgent.query(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		request.setConditions(new HashMap<String, String>());
		try {
			this.tradeDataProviderAgent.query(request);
			Assert.fail("why no exception thrown?");
		} catch (InvalidParameterException e) {
			// OK
		} catch (Exception e) {
			Assert.fail("why the other exception thrown?");
		}
		request.getConditions().put("column-1", "column-value-1");
		request.getConditions().put("column-2", "column-value-2");
		this.tradeDataProviderAgent.query(request);
		System.out.println("validation passed");
	}

	@Test
	public void test() {
		final TradeDataQueryForm request = new TradeDataQueryForm();
		request.setTable("table-TradeDataProviderAgentITest");
		request.setColumns(new ArrayList<String>());
		request.getColumns().add("column-1");
		request.getColumns().add("column-2");
		request.setConditions(new HashMap<String, String>());
		request.getConditions().put("column-1", "column-value-1");
		request.getConditions().put("column-2", "column-value-2");

		// query
		RopResponse response = this.tradeDataProviderAgent.query(request);
		// check
		Assert.assertNotNull(response);
		Assert.assertTrue(response instanceof TradeDataQueryResponse);
		final TradeDataQueryResponse r = (TradeDataQueryResponse) response;
		Assert.assertEquals(request.getTable(), r.getTable());
		Assert.assertNotNull(r.getResults());
		Assert.assertEquals(1, r.getResults().size());
		Assert.assertNotNull(r.getResults().get(0));
		Assert.assertEquals("column-1-value", r.getResults().get(0).get("column-1"));
		Assert.assertEquals("column-2-value", r.getResults().get(0).get("column-2"));

		System.out.println("test passed");
	}

	/**
	 * @param tradeDataProviderAgent
	 *            the tradeDataProviderAgent to set
	 */
	public void setTradeDataProviderAgent(final TradeDataProviderAgent tradeDataProviderAgent) {
		this.tradeDataProviderAgent = tradeDataProviderAgent;
	}

}
