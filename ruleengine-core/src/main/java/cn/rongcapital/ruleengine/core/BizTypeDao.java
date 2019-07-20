/**
 * 
 */
package cn.rongcapital.ruleengine.core;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.rongcapital.ruleengine.model.BizType;

/**
 * the bizType mybatis DAO
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface BizTypeDao {

	@Insert("insert into re_biztype(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`) "
			+ " values(#{code}, #{name}, #{comment}, #{creationTime}, #{updateTime}, 1)")
	void createBizType(BizType bizType);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `ruleSetCode`, `ruleSetVersion` "
			+ " from re_biztype where `code` = #{code} and `enabled` = 1")
	BizType getBizType(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, `ruleSetCode`, `ruleSetVersion` "
			+ " from re_biztype where `enabled` = 1")
	List<BizType> getAllBizTypes();

	@Update("update re_biztype set `name` = #{name}, `comment` = #{comment}, `updateTime` = #{updateTime} "
			+ " where `code` = #{code}")
	void updateBizType(BizType bizType);

	@Update("update re_biztype set `code` = concat('_removed_', `code`), `enabled` = 0, "
			+ " `updateTime` = #{updateTime} where `code` = #{code}")
	void removeBizType(@Param("code") String code, @Param("updateTime") Date updateTime);

	@Select("select count(1) from re_biztype where `code` = #{code} and `enabled` = 1")
	long countByCode(@Param("code") String code);

	@Update("update re_biztype set `ruleSetCode` = #{ruleSetCode}, `ruleSetVersion` = #{ruleSetVersion}, "
			+ " `updateTime` = #{updateTime} where `code` = #{code}")
	void assignRuleSet(@Param("code") String code, @Param("ruleSetCode") String ruleSetCode,
			@Param("ruleSetVersion") long ruleSetVersion, @Param("updateTime") Date updateTime);

}
