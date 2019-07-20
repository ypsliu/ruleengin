/**
 * 
 */
package cn.rongcapital.ruleengine.web.webitest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ruixue.serviceplatform.commons.testing.TestingDatabaseHelper;
import com.ruixue.serviceplatform.commons.web.DefaultJacksonJaxbJsonProvider;

/**
 * the base class for ResourceITest
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public abstract class BaseWebITest {

	/**
	 * the resteasy client
	 */
	private final ResteasyClient client = new ResteasyClientBuilder().httpEngine(
			new ApacheHttpClient4Engine(HttpClientBuilder.create().build())).build();

	/**
	 * the resteasy target
	 */
	private static ResteasyWebTarget target;

	private static boolean initialized = false;

	/**
	 * the resources cache
	 */
	private static final Map<Class<?>, Object> resourcesCache = new HashMap<Class<?>, Object>();

	private static final String APP_HOME = "/Users/Kruce/PROJECTS/RONGCAPITAL/rule-engine/ruleengine-web";

	private static final String BASE_URL = "http://localhost:8080/v1/";

	protected static TestingDatabaseHelper dbHelper;

	protected static Properties coreProperties = new Properties();

	protected static Properties webProperties = new Properties();

	/**
	 * to initialize the test case
	 * 
	 * @param baseUrl
	 *            the URL
	 */
	protected synchronized void initialize(final String baseUrl) {
		if (initialized) {
			return;
		}
		// jackson provider
		final DefaultJacksonJaxbJsonProvider jacksonJaxbJsonProvider = new DefaultJacksonJaxbJsonProvider();
		client.register(jacksonJaxbJsonProvider);
		// target
		target = client.target(baseUrl);
		initialized = true;
	}

	/**
	 * to get the proxy resource
	 * 
	 * @param resourceClazz
	 *            the class of the resource
	 * @return the proxy resource
	 */
	@SuppressWarnings("unchecked")
	public final synchronized <T> T getResource(final Class<T> resourceClazz) {
		if (!initialized) {
			throw new IllegalStateException("the target not initialized");
		}
		T resource = (T) resourcesCache.get(resourceClazz);
		if (resource == null) {
			resource = target.proxy(resourceClazz);
			resourcesCache.put(resourceClazz, resource);
		}
		return resource;
	}

	@Before
	public final void setup() {
		initialize(BASE_URL);
		// clear
		try {
			clearDatabase();
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@BeforeClass
	public static void setupTest() {
		if (java.lang.System.getProperty("APP_HOME") == null) {
			java.lang.System.setProperty("APP_HOME", APP_HOME);
		}
		InputStream is1 = null;
		InputStream is2 = null;
		try {
			// load core config
			is1 = new FileInputStream(java.lang.System.getProperty("APP_HOME") + File.separator + "conf"
					+ File.separator + "ruleengine-core.properties");
			coreProperties.load(is1);
			// load web config
			is1 = new FileInputStream(java.lang.System.getProperty("APP_HOME") + File.separator + "conf"
					+ File.separator + "ruleengine-web.properties");
			webProperties.load(is1);
			// create the database helper
			dbHelper = new TestingDatabaseHelper(coreProperties.getProperty("db.main.driverName"),
					coreProperties.getProperty("db.main.jdbcUrl"), coreProperties.getProperty("db.main.user"),
					coreProperties.getProperty("db.main.password"));
			// clear db
			clearDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			if (is1 != null) {
				try {
					is1.close();
				} catch (Exception e2) {
					//
				}
			}
			if (is2 != null) {
				try {
					is2.close();
				} catch (Exception e2) {
					//
				}
			}
		}
	}

	@AfterClass
	public static void clearTest() {
		try {
			clearDatabase();
		} catch (Exception e) {
			//
		}
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	private static void clearDatabase() throws Exception {
		dbHelper.executeUpdateSql("delete from re_datasource");
		dbHelper.executeUpdateSql("delete from re_biztype");
		dbHelper.executeUpdateSql("delete from re_rule");
		dbHelper.executeUpdateSql("delete from re_rule_history");
		dbHelper.executeUpdateSql("delete from re_result");
		dbHelper.executeUpdateSql("delete from re_extractsql");
		dbHelper.executeUpdateSql("delete from re_extractsql_history");
		dbHelper.executeUpdateSql("delete from re_reference_data");
		dbHelper.executeUpdateSql("delete from re_reference_data_status");
		dbHelper.executeUpdateSql("delete from re_ruleset");
		dbHelper.executeUpdateSql("delete from re_ruleset_history");
		dbHelper.executeUpdateSql("delete from re_match_stage");
	}

}
