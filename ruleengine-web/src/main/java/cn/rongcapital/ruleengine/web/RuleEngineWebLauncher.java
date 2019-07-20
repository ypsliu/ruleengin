/**
 * 
 */
package cn.rongcapital.ruleengine.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

import java.io.File;

/**
 * the sample web launcher
 * @author shangchunming@rongcapital.cn
 *
 */
@SpringBootApplication
@ImportResource(value = {"classpath:ruleengine-web.xml"})
public class RuleEngineWebLauncher extends SpringBootServletInitializer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// logback.configurationFile
		System.setProperty("logging.config", System.getProperty("APP_HOME") + File.separator + "conf" + File.separator
				+ "ruleengine-web-logback.xml");

		SpringApplication.run(RuleEngineWebLauncher.class, args);
	}

}
