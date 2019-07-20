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

import cn.rongcapital.ruleengine.model.Datasource;

/**
 * the DatasourceManager mybaits DAO
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface DatasourceManagerDao {

	@Insert("insert into re_datasource(`code`, `name`, `comment`, `creationTime`, `updateTime`, `enabled`, "
			+ " `driverClass`, `url`, `user`, `password`, `maxPoolSize`, `validationSql`) "
			+ " values(#{code}, #{name}, #{comment}, #{creationTime}, #{updateTime}, 1, #{driverClass}, "
			+ " #{url}, #{user}, #{password}, #{maxPoolSize}, #{validationSql})")
	void createDatasource(Datasource datasource);

	@Update("update re_datasource set `name` = #{name}, `comment` = #{comment}, `updateTime` = #{updateTime}, "
			+ " `driverClass` = #{driverClass}, `url` = #{url}, `user` = #{user}, `password` = #{password}, "
			+ " `maxPoolSize` = #{maxPoolSize}, `validationSql` = #{validationSql} where `code` = #{code}")
	void updateDatasource(Datasource datasource);

	@Update("update re_datasource set `code` = concat('_removed_', `code`), `enabled` = 0, "
			+ " `updateTime` = #{updateTime} where `code` = #{code}")
	void removeDatasource(@Param("code") String code, @Param("updateTime") Date updateTime);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, "
			+ " `driverClass`, `url`, `user`, `password`, `maxPoolSize`, `validationSql` "
			+ " from re_datasource where `code` = #{code} and `enabled` = 1")
	Datasource getDatasource(@Param("code") String code);

	@Select("select `code`, `name`, `comment`, `creationTime`, `updateTime`, "
			+ " `driverClass`, `url`, `user`, `password`, `maxPoolSize`, `validationSql` "
			+ " from re_datasource where `enabled` = 1")
	List<Datasource> getDatasources();

	@Select("select count(1) from re_datasource where `code` = #{code} and `enabled` = 1")
	long countByCode(@Param("code") String code);

}
