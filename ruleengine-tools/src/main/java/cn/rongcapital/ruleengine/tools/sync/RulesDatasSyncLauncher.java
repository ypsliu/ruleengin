/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import cn.rongcapital.ruleengine.tools.utils.YamlUtils;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "cn.rongcapital.ruleengine.tools.sync.impl")
public class RulesDatasSyncLauncher {

	/**
	 * the logger
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(RulesDatasSyncLauncher.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// check APP_HOME
		if (System.getProperty("APP_HOME") == null) {
			System.err.println("error: no APP_HOME");
			return;
		}

		// logback config
		System.setProperty("logging.config", System.getProperty("APP_HOME") + File.separator + "conf" + File.separator
				+ "ruleengine-tools-logback.xml");

		// check the settings file
		final File settingsFile = new File(System.getProperty("APP_HOME") + File.separator + "conf" + File.separator
				+ DatasSyncSettings.FILE_NAME);
		if (settingsFile == null || !settingsFile.isFile() || !settingsFile.canRead()) {
			LOGGER.error("can not read the settings from file : {}", settingsFile.getAbsolutePath());
			throw new RuntimeException("can not read the settings from file: " + settingsFile.getAbsolutePath());
		}
		// load settings
		DatasSyncSettings settings = null;
		try {
			settings = YamlUtils.loadFrom(settingsFile, DatasSyncSettings.class);
			LOGGER.info("the settings loaded from: {}, settings: {}", settingsFile.getAbsoluteFile(), settings);
		} catch (IOException e) {
			LOGGER.error(
					"load the settings failed, file: " + settingsFile.getAbsolutePath() + ", error: " + e.getMessage(),
					e);
			throw new RuntimeException("load the settings failed, file: " + settingsFile.getAbsolutePath()
					+ ", error: " + e.getMessage());
		}

		// start the spring boot
		final ApplicationContext context = SpringApplication.run(RulesDatasSyncLauncher.class, args);

		// the ResourceHolder
		final ResourceHolder resourceHolder = context.getBean(ResourceHolder.class);
		resourceHolder.init(settings);

		// the TextFileReader
		final TextFileReader reader = context.getBean(TextFileReader.class);
		// set the content encoding
		reader.setContentEncoding(settings.getContentEncoding());

		// the DatasSyncer
		final DatasSyncer syncer = context.getBean(DatasSyncer.class);

		try {
			// sync
			syncer.syncDatas(settings);
		} catch (Exception e) {
			LOGGER.error("sync the datas failed, error: " + e.getMessage(), e);
		}
	}

}
