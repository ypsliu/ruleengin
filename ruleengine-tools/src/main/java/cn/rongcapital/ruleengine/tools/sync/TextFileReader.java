/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

import java.io.File;

/**
 * the text file reader
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface TextFileReader {

	/**
	 * to read the file
	 * 
	 * @param file
	 *            the file to read
	 * @return the text content
	 */
	String readContent(File file);

	/**
	 * to set the content encoding, the default is "UTF-8"
	 * 
	 * @param contentEncoding
	 *            the encoding
	 */
	void setContentEncoding(String contentEncoding);

}
