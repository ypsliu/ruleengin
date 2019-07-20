/**
 * 
 */
package cn.rongcapital.ruleengine.mock.w2d;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;

import cn.rongcapital.ruleengine.core.w2d.DataCaptureRequestForm;
import cn.rongcapital.ruleengine.core.w2d.DataCaptureResponse;
import cn.rongcapital.ruleengine.core.w2d.W2dResource;

/**
 * the mock W2dResource launcher
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ImportResource(value = { "classpath:mock-w2d-web.xml" })
@Controller
public final class MockW2dResourceLauncher extends SpringBootServletInitializer implements W2dResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockW2dResourceLauncher.class);

	private volatile AtomicInteger index = new AtomicInteger(1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.core.w2d.W2dResource#capture(cn.rongcapital.ruleengine.core.w2d.
	 * DataCaptureRequestForm)
	 */
	@Override
	public DataCaptureResponse capture(final DataCaptureRequestForm condition) {
		LOGGER.info("condition: {}", condition);
		final DataCaptureResponse response = new DataCaptureResponse();
		response.setCode("200");
		response.setTaskId("test-taskId-" + this.index.getAndIncrement());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.core.w2d.W2dResource#capture(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DataCaptureResponse capture(final String name, final String id, final String mobileNumber,
			final String bizLicenceNumber, final String companyName) {
		LOGGER.info("name: {}, id: {}, mobileNumber: {}, bizLicenceNumber: {}, companyName: {}", name, id,
				mobileNumber, bizLicenceNumber, companyName);
		final DataCaptureResponse response = new DataCaptureResponse();
		response.setCode("200");
		response.setTaskId("test-taskId-" + this.index.getAndIncrement());
		return response;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MockW2dResourceLauncher.class, args);
	}

}
