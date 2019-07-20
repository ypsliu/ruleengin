/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import java.util.Date;
import java.util.Map;

/**
 * 参考数据状态
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ReferenceDataStatus {

	/**
	 * 流水号
	 */
	private Long id;

	/**
	 * 业务类型code
	 */
	private String bizTypeCode;

	/**
	 * 是否处理结束
	 */
	private boolean done;

	/**
	 * 参考数据来源的任务Id
	 */
	private String providerTaskId;

	/**
	 * 请求数据
	 */
	private Map<String, String> conditions;

	/**
	 * 请求数据，JSON格式
	 */
	private String conditionsValue;

	/**
	 * 请求时间
	 */
	private Date insertTime;

	/**
	 * 完成时间
	 */
	private Date updateTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the bizTypeCode
	 */
	public String getBizTypeCode() {
		return bizTypeCode;
	}

	/**
	 * @param bizTypeCode
	 *            the bizTypeCode to set
	 */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	/**
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * @param done
	 *            the done to set
	 */
	public void setDone(boolean done) {
		this.done = done;
	}

	/**
	 * @return the providerTaskId
	 */
	public String getProviderTaskId() {
		return providerTaskId;
	}

	/**
	 * @param providerTaskId
	 *            the providerTaskId to set
	 */
	public void setProviderTaskId(String providerTaskId) {
		this.providerTaskId = providerTaskId;
	}

	/**
	 * @return the conditions
	 */
	public Map<String, String> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(Map<String, String> conditions) {
		this.conditions = conditions;
	}

	/**
	 * @return the conditionsValue
	 */
	public String getConditionsValue() {
		return conditionsValue;
	}

	/**
	 * @param conditionsValue
	 *            the conditionsValue to set
	 */
	public void setConditionsValue(String conditionsValue) {
		this.conditionsValue = conditionsValue;
	}

	/**
	 * @return the insertTime
	 */
	public Date getInsertTime() {
		return insertTime;
	}

	/**
	 * @param insertTime
	 *            the insertTime to set
	 */
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReferenceDataStatus [id=" + id + ", bizTypeCode=" + bizTypeCode + ", done=" + done
				+ ", providerTaskId=" + providerTaskId + ", conditions=" + conditions + ", conditionsValue="
				+ conditionsValue + ", insertTime=" + insertTime + ", updateTime=" + updateTime + "]";
	}

}
