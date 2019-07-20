/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.List;

import cn.rongcapital.ruleengine.model.ResultPojo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.rongcapital.ruleengine.model.MatchStage;

/**
 * the result mybatis DAO
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ResultDao {

	@Select("select bizCode, bizTypeCode, ruleCode, matchType, inputParams, errorMessage, "
			+ " `time`, `result`, `ruleVersion`, `executionId`, `ruleSetCode`, `ruleSetVersion` from re_result "
			+ " where bizCode = #{bizCode} and matchType = 'ACCEPTANCE' and `bizTypeCode` = #{bizTypeCode} "
			+ " and `ruleSetCode` = #{ruleSetCode} and `ruleSetVersion` = #{ruleSetVersion} "
			+ " order by `sortId`, `time` desc")
	List<ResultPojo> getAcceptanceResults(@Param("bizTypeCode") String bizTypeCode,
										  @Param("ruleSetCode") String ruleSetCode, @Param("ruleSetVersion") long ruleSetVersion,
										  @Param("bizCode") String bizCode);

	@Select("select bizCode, bizTypeCode, ruleCode, matchType, inputParams, errorMessage, "
			+ " `time`, `result`, `ruleVersion`, `executionId`, `ruleSetCode`, `ruleSetVersion` from re_result "
			+ " where bizCode = #{bizCode} and matchType = 'SCORE' and `bizTypeCode` = #{bizTypeCode} "
			+ " and `ruleSetCode` = #{ruleSetCode} and `ruleSetVersion` = #{ruleSetVersion} "
			+ " order by `sortId`, `time` desc")
	List<ResultPojo> getScoreResults(@Param("bizTypeCode") String bizTypeCode, @Param("ruleSetCode") String ruleSetCode,
									 @Param("ruleSetVersion") long ruleSetVersion, @Param("bizCode") String bizCode);

	@Select("select bizCode, bizTypeCode, ruleCode, matchType, inputParams, errorMessage, "
			+ " `time`, `result`, `ruleVersion`, `executionId`, `ruleSetCode`, `ruleSetVersion` from re_result "
			+ " where bizCode = #{bizCode} and matchType = 'TEXT' and `bizTypeCode` = #{bizTypeCode} "
			+ " and `ruleSetCode` = #{ruleSetCode} and `ruleSetVersion` = #{ruleSetVersion} "
			+ " order by `sortId`, `time` desc")
	List<ResultPojo> getTextResults(@Param("bizTypeCode") String bizTypeCode, @Param("ruleSetCode") String ruleSetCode,
									@Param("ruleSetVersion") long ruleSetVersion, @Param("bizCode") String bizCode);

	@Insert("insert into re_result(bizCode, bizTypeCode, ruleCode, matchType, inputParams, errorMessage, "
			+ " `time`, `result`, `ruleVersion`, `executionId`, `ruleSetCode`, `ruleSetVersion`, `sortId`) "
			+ " values(#{bizCode}, #{bizTypeCode}, #{ruleCode}, #{matchType}, #{inputParams}, #{errorMessage}, "
			+ " #{time}, #{result}, #{ruleVersion}, #{executionId}, #{ruleSetCode}, #{ruleSetVersion}, #{sortId})")
	void saveResult(ResultPojo result);

	@Insert("<script> insert into re_result(bizCode, bizTypeCode, ruleCode, matchType, inputParams, errorMessage, "
			+ " `time`, `result`, `ruleVersion`, `executionId`, `ruleSetCode`, `ruleSetVersion`, `sortId`) values "
			+ " <foreach collection=\"results\" item=\"result\" index=\"index\" separator=\",\">"
			+ " (#{result.bizCode}, #{result.bizTypeCode}, #{result.ruleCode}, #{result.matchType}, "
			+ " #{result.inputParams}, #{result.errorMessage}, #{result.time}, #{result.result},  #{result.ruleVersion},"
			+ " #{result.executionId}, #{result.ruleSetCode}, #{result.ruleSetVersion}, #{result.sortId}) "
			+ " </foreach> </script>")
	void saveResults(@Param("results") List<ResultPojo> results);

	@Insert("insert into `re_match_stage`(executionId, stageId, parentStageId, ruleCode, ruleVersion, tracesValue, `result`, "
			+ " errorMessage, timeInMs) values(#{executionId}, #{stage.stageId}, #{stage.parentStageId}, #{stage.ruleCode}, "
			+ " #{stage.ruleVersion}, #{stage.tracesValue}, #{stage.result}, #{stage.errorMessage}, #{stage.timeInMs})")
	void saveMatchStage(@Param("executionId") String executionId, @Param("stage") MatchStage stage);

	@Insert("<script> insert into `re_match_stage`(executionId, stageId, parentStageId, ruleCode, "
			+ " ruleVersion, tracesValue, `result`, errorMessage, timeInMs) values "
			+ " <foreach collection=\"stages\" item=\"stage\" index=\"index\" separator=\",\">"
			+ " (#{executionId}, #{stage.stageId}, #{stage.parentStageId}, #{stage.ruleCode}, "
			+ " #{stage.ruleVersion}, #{stage.tracesValue}, #{stage.result}, #{stage.errorMessage}, #{stage.timeInMs})"
			+ " </foreach> </script>")
	void saveMatchStages(@Param("executionId") String executionId, @Param("stages") List<MatchStage> stages);

	@Select("select executionId, stageId, parentStageId, ruleCode, ruleVersion, tracesValue, `result`, errorMessage, "
			+ " timeInMs from `re_match_stage` where executionId = #{executionId}")
	List<MatchStage> getMatchStages(@Param("executionId") String executionId);

}
