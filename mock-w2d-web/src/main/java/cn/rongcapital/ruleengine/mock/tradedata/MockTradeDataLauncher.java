/**
 * 
 */
package cn.rongcapital.ruleengine.mock.tradedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotAuthorizedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import cn.rongcapital.ruleengine.api.TradeDataResource;
import cn.rongcapital.ruleengine.rop.RopResponse;
import cn.rongcapital.ruleengine.rop.RopUtils;
import cn.rongcapital.ruleengine.rop.TradeDataQueryForm;
import cn.rongcapital.ruleengine.rop.TradeDataQueryResponse;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ImportResource(value = { "classpath:mock-tradedata-web.xml" })
@Controller
public final class MockTradeDataLauncher extends SpringBootServletInitializer implements TradeDataResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockTradeDataLauncher.class);

	@Value("${tradedata.rop.app_key}")
	private String ropAppKey = "test-rop-app-key";

	@Value("${tradedata.rop.secret}")
	private String ropSecret = "test-rop-secret";

	@Autowired
	private MockTradeDataService mockTradeDataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * TradeDataResource#query(TradeDataQueryForm)
	 */
	@Override
	public RopResponse query(final TradeDataQueryForm request) {
		// verify sign
		if (!RopUtils.verifySign(request, this.ropSecret)) {
			LOGGER.error("verify match request sign failed, request: {}", request);
			// verify sign failed
			throw new NotAuthorizedException("sign无效");
		}
		// decode
		this.decodeRequest(request);
		final TradeDataQueryResponse response = new TradeDataQueryResponse();
		response.setTable(request.getTable());
		if ("table-TradeDataProviderAgentITest".equals(request.getTable())) {
			// the TradeDataProviderAgentITest request
			response.setResults(new ArrayList<Map<String, String>>());
			response.getResults().add(new HashMap<String, String>());
			for (final String col : request.getColumns()) {
				response.getResults().get(0).put(col, col + "-value");
			}
		} else {
			final List<Map<String, String>> results = this.mockTradeDataService.query(request.getTable(),
					request.getColumns(), request.getConditions());
			response.setResults(results);
		}
		return response;
	}

	private void decodeRequest(final TradeDataQueryForm request) {
		// columns
		if (!StringUtils.isEmpty(request.getColumnsValue())) {
			final String[] a = request.getColumnsValue().split("\\,", -1);
			if (a != null && a.length > 0) {
				request.setColumns(new ArrayList<String>());
				for (final String c : a) {
					request.getColumns().add(c);
				}
			}
		}
		// conditions
		if (!StringUtils.isEmpty(request.getConditionsValue())) {
			final String[] a = request.getConditionsValue().split("\\;", -1);
			if (a != null && a.length > 0) {
				request.setConditions(new HashMap<String, String>());
				for (final String c : a) {
					final String[] b = c.split("\\,", 2);
					if (b != null && b.length == 2) {
						request.getConditions().put(b[0], b[1]);
					}
				}
			}
		}
	}

	/**
	 * @param ropAppKey
	 *            the ropAppKey to set
	 */
	public void setRopAppKey(String ropAppKey) {
		this.ropAppKey = ropAppKey;
	}

	/**
	 * @param ropSecret
	 *            the ropSecret to set
	 */
	public void setRopSecret(String ropSecret) {
		this.ropSecret = ropSecret;
	}

	/**
	 * @param mockTradeDataService
	 *            the mockTradeDataService to set
	 */
	public void setMockTradeDataService(MockTradeDataService mockTradeDataService) {
		this.mockTradeDataService = mockTradeDataService;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MockTradeDataLauncher.class, args);
	}

}
