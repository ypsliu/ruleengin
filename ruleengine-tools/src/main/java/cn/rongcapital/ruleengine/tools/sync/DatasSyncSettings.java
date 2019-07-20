/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync;

/**
 * the configuration for datas sync
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class DatasSyncSettings {

	/**
	 * the configuration file name
	 */
	public static final String FILE_NAME = "sync-rules.yaml";

	/**
	 * the API base URL
	 */
	private String apiBaseUrl = "http://localhost:8080/v1/";

	/**
	 * is autoRemove enabled? default is true
	 */
	private boolean autoRemoveEnabled = true;

	/**
	 * is autoCreate enabled? default is true
	 */
	private boolean autoCreateEnabled = true;

	/**
	 * the content format, default is YAML
	 */
	private ContentFormatType contentFormatType = ContentFormatType.YAML;

	/**
	 * the content encoding, default is "UTF-8"
	 */
	private String contentEncoding = "UTF-8";

	/**
	 * the file name of the datasources
	 */
	private String datasourcesFileName = "datasources";

	/**
	 * the file name of the bizTypes
	 */
	private String bizTypesFileName = "bizTypes";

	/**
	 * the file name of the rules
	 */
	private String rulesFileName = "rules";

	/**
	 * the file name of the ruleSets
	 */
	private String ruleSetsFileName = "ruleSets";

	/**
	 * the file name of the ruleSetAssignment
	 */
	private String ruleSetAssignmentsFileName = "ruleSetAssignments";

	/**
	 * the date time format, default is "yyyy-MM-dd HH:mm:ss"
	 */
	private String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * the sync datas path
	 */
	private String datasPath;

	/**
	 * @return the apiBaseUrl
	 */
	public String getApiBaseUrl() {
		return apiBaseUrl;
	}

	/**
	 * @param apiBaseUrl
	 *            the apiBaseUrl to set
	 */
	public void setApiBaseUrl(String apiBaseUrl) {
		this.apiBaseUrl = apiBaseUrl;
	}

	/**
	 * @return the autoRemoveEnabled
	 */
	public boolean isAutoRemoveEnabled() {
		return autoRemoveEnabled;
	}

	/**
	 * @param autoRemoveEnabled
	 *            the autoRemoveEnabled to set
	 */
	public void setAutoRemoveEnabled(boolean autoRemoveEnabled) {
		this.autoRemoveEnabled = autoRemoveEnabled;
	}

	/**
	 * @return the autoCreateEnabled
	 */
	public boolean isAutoCreateEnabled() {
		return autoCreateEnabled;
	}

	/**
	 * @param autoCreateEnabled
	 *            the autoCreateEnabled to set
	 */
	public void setAutoCreateEnabled(boolean autoCreateEnabled) {
		this.autoCreateEnabled = autoCreateEnabled;
	}

	/**
	 * @return the contentFormatType
	 */
	public ContentFormatType getContentFormatType() {
		return contentFormatType;
	}

	/**
	 * @param contentFormatType
	 *            the contentFormatType to set
	 */
	public void setContentFormatType(ContentFormatType contentFormatType) {
		this.contentFormatType = contentFormatType;
	}

	/**
	 * @return the bizTypesFileName
	 */
	public String getBizTypesFileName() {
		return bizTypesFileName;
	}

	/**
	 * @param bizTypesFileName
	 *            the bizTypesFileName to set
	 */
	public void setBizTypesFileName(String bizTypesFileName) {
		this.bizTypesFileName = bizTypesFileName;
	}

	/**
	 * @return the datasourcesFileName
	 */
	public String getDatasourcesFileName() {
		return datasourcesFileName;
	}

	/**
	 * @param datasourcesFileName
	 *            the datasourcesFileName to set
	 */
	public void setDatasourcesFileName(String datasourcesFileName) {
		this.datasourcesFileName = datasourcesFileName;
	}

	/**
	 * @return the rulesFileName
	 */
	public String getRulesFileName() {
		return rulesFileName;
	}

	/**
	 * @param rulesFileName
	 *            the rulesFileName to set
	 */
	public void setRulesFileName(String rulesFileName) {
		this.rulesFileName = rulesFileName;
	}

	/**
	 * @return the ruleSetsFileName
	 */
	public String getRuleSetsFileName() {
		return ruleSetsFileName;
	}

	/**
	 * @param ruleSetsFileName
	 *            the ruleSetsFileName to set
	 */
	public void setRuleSetsFileName(String ruleSetsFileName) {
		this.ruleSetsFileName = ruleSetsFileName;
	}

	/**
	 * @return the ruleSetAssignmentsFileName
	 */
	public String getRuleSetAssignmentsFileName() {
		return ruleSetAssignmentsFileName;
	}

	/**
	 * @param ruleSetAssignmentsFileName
	 *            the ruleSetAssignmentsFileName to set
	 */
	public void setRuleSetAssignmentsFileName(String ruleSetAssignmentsFileName) {
		this.ruleSetAssignmentsFileName = ruleSetAssignmentsFileName;
	}

	/**
	 * @return the datetimeFormat
	 */
	public String getDatetimeFormat() {
		return datetimeFormat;
	}

	/**
	 * @param datetimeFormat
	 *            the datetimeFormat to set
	 */
	public void setDatetimeFormat(String datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
	}

	/**
	 * @return the datasPath
	 */
	public String getDatasPath() {
		return datasPath;
	}

	/**
	 * @param datasPath
	 *            the datasPath to set
	 */
	public void setDatasPath(String datasPath) {
		this.datasPath = datasPath;
	}

	/**
	 * @return the contentEncoding
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	/**
	 * @param contentEncoding
	 *            the contentEncoding to set
	 */
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DatasSyncSettings [apiBaseUrl=" + apiBaseUrl + ", autoRemoveEnabled=" + autoRemoveEnabled
				+ ", autoCreateEnabled=" + autoCreateEnabled + ", contentFormatType=" + contentFormatType
				+ ", contentEncoding=" + contentEncoding + ", datasourcesFileName=" + datasourcesFileName
				+ ", bizTypesFileName=" + bizTypesFileName + ", rulesFileName=" + rulesFileName + ", ruleSetsFileName="
				+ ruleSetsFileName + ", ruleSetAssignmentsFileName=" + ruleSetAssignmentsFileName + ", datetimeFormat="
				+ datetimeFormat + ", datasPath=" + datasPath + "]";
	}

}
