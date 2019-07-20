/**
 * 
 */
package cn.rongcapital.ruleengine.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.yaml.snakeyaml.Yaml;

/**
 * the yaml utils
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class YamlUtils {

	private static final Yaml yaml = new Yaml();

	/**
	 * to load the data from yaml file
	 * 
	 * @param yamlFile
	 *            the yaml file
	 * @param clazz
	 *            the result class
	 * @return the data
	 * @throws IOException
	 */
	public static <T> T loadFrom(final File yamlFile, final Class<T> clazz) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(yamlFile);
			return yaml.loadAs(is, clazz);
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					//
				}
			}
		}
	}

	/**
	 * to load the data from yaml file
	 * 
	 * @param yamlContent
	 *            the yaml content
	 * @param clazz
	 *            the result class
	 * @return the data
	 */
	public static <T> T loadFrom(final String yamlContent, final Class<T> clazz) {
		return yaml.loadAs(yamlContent, clazz);
	}

	/**
	 * to save the settings to yaml file
	 * 
	 * @param yamlFile
	 *            the yaml file
	 * @param settings
	 *            the settings
	 * @throws IOException
	 */
	public static void saveTo(final File yamlFile, final Object settings) throws IOException {
		Writer output = null;
		try {
			output = new FileWriter(yamlFile);
			final Yaml yaml = new Yaml();
			yaml.dump(settings, output);
		} catch (IOException e) {
			throw e;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e2) {
					//
				}
			}
		}
	}

}
