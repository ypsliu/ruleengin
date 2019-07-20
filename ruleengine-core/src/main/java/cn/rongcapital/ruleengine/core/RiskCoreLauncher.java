/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * the core launcher
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ImportResource(value = { "file:${APP_HOME}/conf/ruleengine-core.xml" })
public class RiskCoreLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// logback.configurationFile
		System.setProperty("logging.config", System.getProperty("APP_HOME") + File.separator + "conf" + File.separator
				+ "ruleengine-core-logback.xml");

		// run
		SpringApplication.run(RiskCoreLauncher.class, args);

		// TODO

		// latch
		final CountDownLatch latch = new CountDownLatch(1);

		try {
			latch.await();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
