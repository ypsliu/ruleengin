/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

/**
 * the data content parser
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface DataContentParser {

	/**
	 * to parse the content to type
	 * 
	 * @param content
	 *            the text content
	 * @param type
	 *            the data type
	 * @return the data entity
	 */
	<T> T parseContent(String content, Class<T> type);

}
