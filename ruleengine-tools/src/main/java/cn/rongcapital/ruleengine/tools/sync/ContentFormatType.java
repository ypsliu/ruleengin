/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

/**
 * the source data format type
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public enum ContentFormatType {

	/**
	 * the JSON format
	 */
	JSON(".json"),

	/**
	 * the YAML format
	 */
	YAML(".yaml");

	private final String fileExtName;

	private ContentFormatType(final String fileExtName) {
		this.fileExtName = fileExtName;
	}

	/**
	 * to get the file ext name
	 * 
	 * @return the name
	 */
	public String getFileExtName() {
		return this.fileExtName;
	}

}
