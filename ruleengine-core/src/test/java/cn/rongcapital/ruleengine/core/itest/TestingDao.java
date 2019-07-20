/**
 * 
 */
package cn.rongcapital.ruleengine.core.itest;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ReferenceData;
import cn.rongcapital.ruleengine.model.ReferenceDataStatus;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public interface TestingDao {

	@Delete("delete from re_datasource")
	void clearDatasources();

	@Delete("delete from re_biztype")
	void clearBizTypes();

	@Delete("delete from re_rule")
	void clearRules();

	@Delete("delete from re_rule_history")
	void clearRuleHistories();

	@Delete("delete from re_ruleset")
	void clearRuleSets();

	@Delete("delete from re_ruleset_history")
	void clearRuleSetHistories();

	@Delete("delete from re_extractsql")
	void clearExtractSqls();

	@Delete("delete from re_extractsql_history")
	void clearExtractSqlHistoies();

	@Delete("delete from re_result")
	void clearResults();

	@Delete("delete from re_match_stage")
	void clearMatchStages();

	@Delete("delete from re_reference_data_status")
	void clearReferenceDataStatus();

	@Delete("delete from re_reference_data")
	void clearReferenceDatas();

	@Insert("insert into re_datasource(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`, "
			+ " `driverClass`, `url`, `user`, `password`, `maxPoolSize`, `validationSql`) "
			+ " values(#{code}, #{name}, #{comment}, #{creationTime}, #{updateTime}, 1, #{driverClass}, "
			+ " #{url}, #{user}, #{password}, #{maxPoolSize}, #{validationSql})")
	void createDatasource(Datasource datasource);

	@Insert("insert into re_biztype(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`) "
			+ " values(#{code}, #{name}, #{comment}, #{creationTime}, #{updateTime}, 1)")
	void createBizType(BizType bizType);

	@Select("select `id`, `bizTypeCode`, `done`, `providerTaskId`, `conditionsValue`, `insertTime` from "
			+ " `re_reference_data_status` where `conditionsValue` = #{conditionsValue} "
			+ " order by `insertTime` desc limit 1")
	ReferenceDataStatus loadStatus(@Param("conditionsValue") String conditionsValue);

	@Select("select `bizTypeCode`, `responseValue`, `providerTaskId`, `conditionsValue`, `time`, "
			+ " `errorCode`, `errorMessage` from `re_reference_data` where `conditionsValue` = #{conditionsValue} "
			+ " order by `time` desc limit 1")
	ReferenceData loadReferenceData(@Param("conditionsValue") String conditionsValue);

}
