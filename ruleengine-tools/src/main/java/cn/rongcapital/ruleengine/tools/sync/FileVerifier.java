/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

import java.io.File;

/**
 * the file verifier
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface FileVerifier {

	/**
	 * to verify the file can be read
	 * 
	 * @param file
	 *            the file to verify
	 * @return true: can read
	 */
	boolean canReadFile(File file);

	/**
	 * to verify the path can be read
	 * 
	 * @param path
	 *            the path to verify
	 * @return true: can read
	 */
	boolean canReadPath(File path);

}
