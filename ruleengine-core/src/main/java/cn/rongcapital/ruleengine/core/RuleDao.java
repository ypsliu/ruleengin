/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;

/**
 * the rule mybatis DAO
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface RuleDao {

	@Insert("insert into re_rule(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`, "
			+ " `bizTypeCode`, `inputParams`, `expression`, `matchType`, `executorClass`, `datasourceCodes`, "
			+ " `exprSegments`, `version`, `designValue`)  values(#{code}, #{name}, #{comment}, #{creationTime}, #{updateTime}, 1, "
			+ " #{bizTypeCode}, #{inputParams}, #{expression}, #{matchType}, #{executorClass}, "
			+ " #{datasourceCodes}, #{exprSegments}, #{version}, #{designValue})")
	void createRule(Rule rule);

	@Insert("insert into re_rule_history(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`, "
			+ " `bizTypeCode`, `inputParams`, `expression`, `matchType`, `executorClass`, `datasourceCodes`, "
			+ " `exprSegments`, `version`, `designValue`)  values(#{code}, #{name}, #{comment}, #{creationTime}, #{updateTime}, 1, "
			+ " #{bizTypeCode}, #{inputParams}, #{expression}, #{matchType}, #{executorClass}, "
			+ " #{datasourceCodes}, #{exprSegments}, #{version}, #{designValue})")
	void createRuleHistory(Rule rule);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `inputParams`, "
			+ " `expression`, `matchType`, `executorClass`, `datasourceCodes`, `exprSegments`, `version`, `designValue` "
			+ " from re_rule where `code` = #{code} and `enabled` = 1")
	Rule getRule(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `inputParams`, "
			+ " `expression`, `matchType`, `executorClass`, `datasourceCodes`, `exprSegments`, `version`, `designValue` "
			+ " from re_rule_history where `code` = #{code} and `enabled` = 1 order by `version`")
	List<Rule> getRuleHistories(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `inputParams`, "
			+ " `expression`, `matchType`, `executorClass`, `datasourceCodes`, `exprSegments`, `version`, `designValue` "
			+ " from re_rule_history where `code` = #{code} and `version` = #{version}")
	Rule getRuleHistory(@Param("code") String code, @Param("version") long version);

	@Update("update re_rule set `name` = #{name}, `comment` = #{comment}, `updateTime` = #{updateTime}, "
			+ " `bizTypeCode` = #{bizTypeCode}, `inputParams` = #{inputParams}, `expression` = #{expression}, "
			+ " `matchType` = #{matchType}, `executorClass` = #{executorClass}, "
			+ " `datasourceCodes` = #{datasourceCodes}, `exprSegments` = #{exprSegments}, "
			+ " `version` = `version` + 1, `designValue` = #{designValue} where `code` = #{code} and `version` = #{version}")
	int updateRule(Rule rule);

	@Update("update re_rule set `code` = concat('_removed_', `code`), `enabled` = 0, "
			+ " `updateTime` = #{updateTime} where `code` = #{code}")
	void removeRule(@Param("code") String code, @Param("updateTime") Date updateTime);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `inputParams`, "
			+ " `expression`, `matchType`, `executorClass`, `datasourceCodes`, `exprSegments`, `version`, `designValue` "
			+ " from re_rule where `bizTypeCode` = #{bizTypeCode} and `enabled` = 1")
	List<Rule> getRulesByBizType(@Param("bizTypeCode") String bizTypeCode);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `inputParams`, "
			+ " `expression`, `matchType`, `executorClass`, `datasourceCodes`, `exprSegments`, `version`, `designValue` "
			+ " from re_rule where `enabled` = 1")
	List<Rule> getAllRules();

	@Select("select count(1) from re_rule where `code` = #{code} and `enabled` = 1")
	long countByCode(@Param("code") String code);

	@Delete("delete from re_extractsql where `ruleCode` = #{ruleCode}")
	void removeExtractSqls(@Param("ruleCode") String ruleCode);

	@Insert("insert into re_extractsql(`ruleCode`, `datasourceCode`, `inputParams`, `sql`, `tableName`, `columnsValue`, "
			+ " `conditionsValue`, `ruleVersion`) values(#{ruleCode}, #{datasourceCode}, #{inputParams}, #{sql}, #{tableName}, "
			+ " #{columnsValue}, #{conditionsValue}, #{ruleVersion})")
	void createExtractSql(ExtractSql extractSql);

	@Insert("insert into re_extractsql_history(`ruleCode`, `datasourceCode`, `inputParams`, `sql`, `tableName`, "
			+ " `columnsValue`, `conditionsValue`, `ruleVersion`) values(#{ruleCode}, #{datasourceCode}, "
			+ " #{inputParams}, #{sql}, #{tableName}, #{columnsValue}, #{conditionsValue}, #{ruleVersion})")
	void createExtractSqlHistory(ExtractSql extractSql);

	@Select("select `ruleCode`, `datasourceCode`, `inputParams`, `sql`, `tableName`, `columnsValue`, `conditionsValue`, "
			+ " `ruleVersion` from re_extractsql where `ruleCode` = #{ruleCode}")
	List<ExtractSql> getExtractSqls(@Param("ruleCode") String ruleCode);

	@Select("select `ruleCode`, `datasourceCode`, `inputParams`, `sql`, `tableName`, `columnsValue`, `conditionsValue` "
			+ " from re_extractsql_history where `ruleCode` = #{ruleCode} and `ruleVersion` = #{ruleVersion}")
	List<ExtractSql> getExtractSqlFromHistory(@Param("ruleCode") String ruleCode, @Param("ruleVersion") long ruleVersion);

	@Insert("insert into re_ruleset(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`, "
			+ " `bizTypeCode`, `rulesValue`, `ordered`, `version`)  values(#{code}, #{name}, #{comment}, #{creationTime}, "
			+ " #{updateTime}, 1, #{bizTypeCode}, #{rulesValue}, #{ordered}, #{version})")
	void createRuleSet(RuleSet ruleSet);

	@Insert("insert into re_ruleset_history(`code`, `name`, `comment`, `creationTime`, `updateTime`, "
			+ " `bizTypeCode`, `rulesValue`, `ordered`, `version`)  values(#{code}, #{name}, #{comment}, #{creationTime}, "
			+ " #{updateTime}, #{bizTypeCode}, #{rulesValue}, #{ordered}, #{version})")
	void createRuleSetHistory(RuleSet ruleSet);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `rulesValue`, `ordered`, `version` "
			+ " from re_ruleset where `code` = #{code} and `enabled` = 1")
	RuleSet getRuleSet(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `rulesValue`, `ordered`, `version` "
			+ " from re_ruleset_history where `code` = #{code} order by `version`")
	List<RuleSet> getRuleSetHistories(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `rulesValue`, `ordered`, `version` "
			+ " from re_ruleset_history where `code` = #{code} and `version` = #{version}")
	RuleSet getRuleSetHistory(@Param("code") String code, @Param("version") long version);

	@Update("update re_ruleset set `name` = #{name}, `comment` = #{comment}, `updateTime` = #{updateTime}, "
			+ " `bizTypeCode` = #{bizTypeCode}, `rulesValue` = #{rulesValue}, `ordered` = #{ordered}, "
			+ " `version` = `version` + 1 where `code` = #{code} and `version` = #{version}")
	int updateRuleSet(RuleSet rule);

	@Update("update re_ruleset set `code` = concat('_removed_', `code`), `enabled` = 0, "
			+ " `updateTime` = #{updateTime} where `code` = #{code}")
	void removeRuleSet(@Param("code") String code, @Param("updateTime") Date updateTime);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `rulesValue`, `ordered`, `version` "
			+ " from re_ruleset where `bizTypeCode` = #{bizTypeCode} and `enabled` = 1")
	List<RuleSet> getRuleSetsByBizType(@Param("bizTypeCode") String bizTypeCode);

	@Select("select count(1) from re_ruleset where `code` = #{code} and `enabled` = 1")
	long countRuleSetByCode(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `bizTypeCode`, `rulesValue`, `ordered`, `version` "
			+ " from re_ruleset where `enabled` = 1")
	List<RuleSet> getAllRuleSets();

}
