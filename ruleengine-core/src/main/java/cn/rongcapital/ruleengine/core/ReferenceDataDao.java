/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataStatus;

/**
 * the the reference data mybatis DAO
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ReferenceDataDao {

	@Select("select `id`, `bizTypeCode`, `done`, `providerTaskId`, `conditionsValue`, `insertTime` from "
			+ " `re_reference_data_status` where `conditionsValue` = #{conditionsValue} "
			+ " order by `insertTime` desc limit 1 for update")
	ReferenceDataStatus loadStatusWithLock(@Param("conditionsValue") String conditionsValue);

	@Insert("insert into `re_reference_data_status`(`bizTypeCode`, `done`, `providerTaskId`, `conditionsValue`, "
			+ " `insertTime`) values(#{bizTypeCode}, #{done}, #{providerTaskId}, #{conditionsValue}, #{insertTime})")
	void createStatus(ReferenceDataStatus status);

	@Insert("update `re_reference_data_status` set `done` = #{done}, `updateTime` = #{updateTime} "
			+ " where `id` = #{id}")
	void updateStatus(ReferenceDataStatus status);

	@Select("select `bizTypeCode`, `responseValue`, `providerTaskId`, `conditionsValue`, `time`, "
			+ " `errorCode`, `errorMessage` from `re_reference_data` where `conditionsValue` = #{conditionsValue} "
			+ " order by `time` desc limit 1")
	ReferenceData loadReferenceData(@Param("conditionsValue") String conditionsValue);

	@Insert("insert into `re_reference_data`(`bizTypeCode`, `providerTaskId`, `time`, `conditionsValue`, "
			+ " `responseValue`, `responseRaw`, `errorCode`, `errorMessage`) values(#{bizTypeCode}, "
			+ " #{providerTaskId}, #{time}, #{conditionsValue}, #{responseValue}, #{responseRaw}, #{errorCode}, "
			+ " #{errorMessage})")
	void createReferenceData(ReferenceData referenceData);

}
