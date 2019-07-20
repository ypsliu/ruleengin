/**
 * 
 */
package cn.rongcapital.ruleengine.tools.test.sync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.ws.rs.NotFoundException;

import cn.rongcapital.ruleengine.tools.sync.ContentFormatType;
import cn.rongcapital.ruleengine.tools.sync.ResourceHolder;
import cn.rongcapital.ruleengine.tools.sync.TextFileReader;
import cn.rongcapital.ruleengine.tools.sync.data.RuleSets;
import cn.rongcapital.ruleengine.tools.sync.data.Rules;
import cn.rongcapital.ruleengine.tools.sync.impl.RulesDatasSyncer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.rongcapital.ruleengine.api.RuleResource;
import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;
import cn.rongcapital.ruleengine.tools.sync.DataContentParser;
import cn.rongcapital.ruleengine.tools.sync.DatasSyncSettings;
import cn.rongcapital.ruleengine.tools.sync.DatasSyncer;
import cn.rongcapital.ruleengine.tools.sync.FileVerifier;
import cn.rongcapital.ruleengine.tools.sync.data.BizTypes;
import cn.rongcapital.ruleengine.tools.sync.data.Datasources;
import cn.rongcapital.ruleengine.tools.sync.data.RuleSetAssignment;
import cn.rongcapital.ruleengine.tools.sync.data.RuleSetAssignments;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@Configuration
@SpringApplicationConfiguration(RulesDatasSyncerTest.class)
public class RulesDatasSyncerTest {

	private static final String DATAS_PATH = "test-datas-path";

	@Autowired
	private DatasSyncer datasSyncer;

	@Autowired
	private ResourceHolder resourceHolder;

	@Autowired
	private TextFileReader textFileReader;

	@Autowired
	private DataContentParser jsonDataContentParser;

	@Autowired
	private DataContentParser yamlDataContentParser;

	@Autowired
	private FileVerifier fileVerifier;

	@Test
	public void testSyncBizTypes() {
		// reset
		Mockito.reset(this.fileVerifier);
		Mockito.reset(this.textFileReader);
		// the settings
		final DatasSyncSettings settings = new DatasSyncSettings();
		settings.setAutoCreateEnabled(true);
		settings.setAutoRemoveEnabled(false);
		settings.setDatasPath(DATAS_PATH);
		settings.setContentFormatType(ContentFormatType.JSON);

		// datasources
		final BizTypes bizTypes = new BizTypes();
		bizTypes.setBizTypes(new ArrayList<BizType>());
		bizTypes.getBizTypes().add(new BizType());
		bizTypes.getBizTypes().get(0).setCode("test-code-1");
		bizTypes.getBizTypes().get(0).setName("test-name-1");
		bizTypes.getBizTypes().get(0).setComment("test-comment-1");

		// datasPath
		final File datasPath = new File(DATAS_PATH);
		// datasource file
		final File datasourceFile = new File(datasPath, settings.getBizTypesFileName()
				+ settings.getContentFormatType().getFileExtName());

		// mock fileVerifier.canReadPath()
		Mockito.when(this.fileVerifier.canReadPath(datasPath)).thenReturn(true);
		Mockito.when(this.fileVerifier.canReadFile(datasourceFile)).thenReturn(true);

		// mock textFileReader.readContent()
		Mockito.when(this.textFileReader.readContent(datasourceFile)).thenReturn("test-json-biztypes-content");

		// mock jsonDataContentParser.parseContent()
		Mockito.when(this.jsonDataContentParser.parseContent("test-json-biztypes-content", BizTypes.class)).thenReturn(
				bizTypes);

		// mock RuleResource
		final RuleResource resource = Mockito.mock(RuleResource.class);
		// mock resourceHolder.getResource()
		Mockito.when(this.resourceHolder.getResource(RuleResource.class)).thenReturn(resource);

		// test-1: create
		// mock resource.getBizType() -> NotFoundException
		Mockito.when(resource.getBizType(bizTypes.getBizTypes().get(0).getCode())).thenThrow(new NotFoundException());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser).parseContent("test-json-biztypes-content", BizTypes.class);
		Mockito.verify(resource).getBizType(bizTypes.getBizTypes().get(0).getCode());
		Mockito.verify(resource).createBizType(bizTypes.getBizTypes().get(0));
		Mockito.verify(resource, Mockito.never()).updateBizType(Mockito.anyString(), Mockito.any(BizType.class));
		Mockito.verify(resource, Mockito.never()).getAllBizTypes();
		Mockito.verify(resource, Mockito.never()).removeBizType(Mockito.anyString());
		System.out.println("test-1: create passed");

		// test-2: no changed
		// mock resource.getBizType()
		Mockito.doReturn(bizTypes.getBizTypes().get(0)).when(resource)
				.getBizType(bizTypes.getBizTypes().get(0).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader, Mockito.times(2)).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser, Mockito.times(2)).parseContent("test-json-biztypes-content",
				BizTypes.class);
		Mockito.verify(resource, Mockito.times(2)).getBizType(bizTypes.getBizTypes().get(0).getCode());
		Mockito.verify(resource).createBizType(bizTypes.getBizTypes().get(0));
		Mockito.verify(resource, Mockito.never()).updateBizType(Mockito.anyString(), Mockito.any(BizType.class));
		Mockito.verify(resource, Mockito.never()).getAllBizTypes();
		Mockito.verify(resource, Mockito.never()).removeBizType(Mockito.anyString());
		System.out.println("test-2: no changed passed");

		// test-3: update
		final BizType oldBizType = new BizType();
		oldBizType.setCode("test-cdoe-1");
		oldBizType.setName("test-name-1-old");
		oldBizType.setComment("test-comment-1-old");
		// mock resource.getBizType()
		Mockito.doReturn(oldBizType).when(resource).getBizType(bizTypes.getBizTypes().get(0).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader, Mockito.times(3)).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser, Mockito.times(3)).parseContent("test-json-biztypes-content",
				BizTypes.class);
		Mockito.verify(resource, Mockito.times(3)).getBizType(bizTypes.getBizTypes().get(0).getCode());
		Mockito.verify(resource).createBizType(bizTypes.getBizTypes().get(0));
		Mockito.verify(resource).updateBizType(oldBizType.getCode(), bizTypes.getBizTypes().get(0));
		Mockito.verify(resource, Mockito.never()).getAllBizTypes();
		Mockito.verify(resource, Mockito.never()).removeBizType(Mockito.anyString());
		System.out.println("test-3: update passed");

		// test-4: update and remove
		final List<BizType> olds = new ArrayList<BizType>(2);
		olds.addAll(bizTypes.getBizTypes());
		final BizType oldBizType2 = new BizType();
		oldBizType2.setCode("test-code-2");
		oldBizType2.setName("test-name-2-old");
		oldBizType2.setComment("test-comment-2-old");
		olds.add(oldBizType2);
		// mock resource.getAllBizTypes()
		Mockito.when(resource.getAllBizTypes()).thenReturn(olds);
		settings.setAutoRemoveEnabled(true);
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader, Mockito.times(4)).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser, Mockito.times(4)).parseContent("test-json-biztypes-content",
				BizTypes.class);
		Mockito.verify(resource, Mockito.times(4)).getBizType(bizTypes.getBizTypes().get(0).getCode());
		Mockito.verify(resource).createBizType(bizTypes.getBizTypes().get(0));
		Mockito.verify(resource, Mockito.times(2)).updateBizType(oldBizType.getCode(), bizTypes.getBizTypes().get(0));
		Mockito.verify(resource).getAllBizTypes();
		Mockito.verify(resource).removeBizType(oldBizType2.getCode());
		System.out.println("test-4: remove passed");
	}

	@Test
	public void testSyncDatasources() {
		// reset
		Mockito.reset(this.fileVerifier);
		Mockito.reset(this.textFileReader);
		// the settings
		final DatasSyncSettings settings = new DatasSyncSettings();
		settings.setAutoCreateEnabled(true);
		settings.setAutoRemoveEnabled(false);
		settings.setDatasPath(DATAS_PATH);
		settings.setContentFormatType(ContentFormatType.JSON);

		// datasources
		final Datasources datasources = new Datasources();
		datasources.setDatasources(new ArrayList<Datasource>());
		datasources.getDatasources().add(new Datasource());
		datasources.getDatasources().get(0).setCode("test-code-1");
		datasources.getDatasources().get(0).setName("test-name-1");
		datasources.getDatasources().get(0).setComment("test-comment-1");
		datasources.getDatasources().get(0).setDriverClass("test-driverClass-1");
		datasources.getDatasources().get(0).setUrl("test-url-1");
		datasources.getDatasources().get(0).setUser("test-user-1");
		datasources.getDatasources().get(0).setPassword("test-password-1");
		datasources.getDatasources().add(new Datasource());
		datasources.getDatasources().get(1).setCode("test-code-2");
		datasources.getDatasources().get(1).setName("test-name-2");
		datasources.getDatasources().get(1).setComment("test-comment-2");
		datasources.getDatasources().get(1).setDriverClass("test-driverClass-2");
		datasources.getDatasources().get(1).setUrl("test-url-2");
		datasources.getDatasources().get(1).setUser("test-user-2");
		datasources.getDatasources().get(1).setPassword("test-password-2");

		// datasPath
		final File datasPath = new File(DATAS_PATH);
		// datasource file
		final File datasourceFile = new File(datasPath, settings.getDatasourcesFileName()
				+ settings.getContentFormatType().getFileExtName());

		// mock fileVerifier.canReadPath()
		Mockito.when(this.fileVerifier.canReadPath(datasPath)).thenReturn(true);
		Mockito.when(this.fileVerifier.canReadFile(datasourceFile)).thenReturn(true);

		// mock textFileReader.readContent()
		Mockito.when(this.textFileReader.readContent(datasourceFile)).thenReturn("test-json-datasources-content");

		// mock jsonDataContentParser.parseContent()
		Mockito.when(this.jsonDataContentParser.parseContent("test-json-datasources-content", Datasources.class))
				.thenReturn(datasources);

		// mock RuleResource
		final RuleResource resource = Mockito.mock(RuleResource.class);
		// mock resourceHolder.getResource()
		Mockito.when(this.resourceHolder.getResource(RuleResource.class)).thenReturn(resource);

		// test-1: create
		// mock resource.getDatasource() -> NotFoundException
		Mockito.when(resource.getDatasource(datasources.getDatasources().get(0).getCode())).thenThrow(
				new NotFoundException());
		Mockito.when(resource.getDatasource(datasources.getDatasources().get(1).getCode())).thenThrow(
				new NotFoundException());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser).parseContent("test-json-datasources-content", Datasources.class);
		Mockito.verify(resource).getDatasource(datasources.getDatasources().get(0).getCode());
		Mockito.verify(resource).getDatasource(datasources.getDatasources().get(1).getCode());
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(0));
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(1));
		Mockito.verify(resource, Mockito.never()).updateDatasource(Mockito.anyString(), Mockito.any(Datasource.class));
		Mockito.verify(resource, Mockito.never()).getDatasources();
		Mockito.verify(resource, Mockito.never()).removeDatasource(Mockito.anyString());
		System.out.println("test-1: create passed");

		// test-2: no changed
		// mock resource.getDatasource()
		Mockito.doReturn(datasources.getDatasources().get(0)).when(resource)
				.getDatasource(datasources.getDatasources().get(0).getCode());
		Mockito.doReturn(datasources.getDatasources().get(1)).when(resource)
				.getDatasource(datasources.getDatasources().get(1).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader, Mockito.times(2)).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser, Mockito.times(2)).parseContent("test-json-datasources-content",
				Datasources.class);
		Mockito.verify(resource, Mockito.times(2)).getDatasource(datasources.getDatasources().get(0).getCode());
		Mockito.verify(resource, Mockito.times(2)).getDatasource(datasources.getDatasources().get(1).getCode());
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(0));
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(1));
		Mockito.verify(resource, Mockito.never()).updateDatasource(Mockito.anyString(), Mockito.any(Datasource.class));
		Mockito.verify(resource, Mockito.never()).getDatasources();
		Mockito.verify(resource, Mockito.never()).removeDatasource(Mockito.anyString());
		System.out.println("test-2: no changed passed");

		// test-3: update
		final Datasource oldDs1 = new Datasource();
		oldDs1.setCode("test-cdoe-1");
		oldDs1.setName("test-name-1-old");
		oldDs1.setComment("test-comment-1-old");
		oldDs1.setDriverClass("test-driverClass-1-old");
		oldDs1.setUrl("test-url-1-old");
		oldDs1.setUser("test-user-1-old");
		oldDs1.setPassword("test-password-1-old");
		final Datasource oldDs2 = new Datasource();
		oldDs1.setCode("test-code-2");
		oldDs2.setName("test-name-2-old");
		oldDs2.setComment("test-comment-2-old");
		oldDs2.setDriverClass("test-driverClass-2-old");
		oldDs2.setUrl("test-url-2-old");
		oldDs2.setUser("test-user-2-old");
		oldDs2.setPassword("test-password-2-old");
		// mock resource.getDatasource()
		Mockito.doReturn(oldDs1).when(resource).getDatasource(datasources.getDatasources().get(0).getCode());
		Mockito.doReturn(oldDs2).when(resource).getDatasource(datasources.getDatasources().get(1).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader, Mockito.times(3)).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser, Mockito.times(3)).parseContent("test-json-datasources-content",
				Datasources.class);
		Mockito.verify(resource, Mockito.times(3)).getDatasource(datasources.getDatasources().get(0).getCode());
		Mockito.verify(resource, Mockito.times(3)).getDatasource(datasources.getDatasources().get(1).getCode());
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(0));
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(1));
		Mockito.verify(resource).updateDatasource(oldDs1.getCode(), datasources.getDatasources().get(0));
		Mockito.verify(resource).updateDatasource(oldDs2.getCode(), datasources.getDatasources().get(1));
		Mockito.verify(resource, Mockito.never()).getDatasources();
		Mockito.verify(resource, Mockito.never()).removeDatasource(Mockito.anyString());
		System.out.println("test-3: update passed");

		// test-4: update and remove
		final List<Datasource> olds = new ArrayList<Datasource>(3);
		olds.addAll(datasources.getDatasources());
		final Datasource oldDs3 = new Datasource();
		oldDs3.setCode("test-code-3");
		oldDs3.setName("test-name-3-old");
		oldDs3.setComment("test-comment-3-old");
		oldDs3.setDriverClass("test-driverClass-3-old");
		oldDs3.setUrl("test-url-3-old");
		oldDs3.setUser("test-user-3-old");
		oldDs3.setPassword("test-password-3-old");
		olds.add(oldDs3);
		// mock resource.getDatasources()
		Mockito.when(resource.getDatasources()).thenReturn(olds);
		settings.setAutoRemoveEnabled(true);
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadFile(datasourceFile);
		Mockito.verify(this.textFileReader, Mockito.times(4)).readContent(datasourceFile);
		Mockito.verify(this.jsonDataContentParser, Mockito.times(4)).parseContent("test-json-datasources-content",
				Datasources.class);
		Mockito.verify(resource, Mockito.times(4)).getDatasource(datasources.getDatasources().get(0).getCode());
		Mockito.verify(resource, Mockito.times(4)).getDatasource(datasources.getDatasources().get(1).getCode());
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(0));
		Mockito.verify(resource).createDatasource(datasources.getDatasources().get(1));
		Mockito.verify(resource, Mockito.times(2)).updateDatasource(oldDs1.getCode(),
				datasources.getDatasources().get(0));
		Mockito.verify(resource, Mockito.times(2)).updateDatasource(oldDs2.getCode(),
				datasources.getDatasources().get(1));
		Mockito.verify(resource).getDatasources();
		Mockito.verify(resource).removeDatasource(oldDs3.getCode());
		System.out.println("test-4: remove passed");
	}

	@Test
	public void testSyncRules() {
		// reset
		Mockito.reset(this.fileVerifier);
		Mockito.reset(this.textFileReader);
		// the settings
		final DatasSyncSettings settings = new DatasSyncSettings();
		settings.setAutoCreateEnabled(true);
		settings.setAutoRemoveEnabled(false);
		settings.setDatasPath(DATAS_PATH);
		settings.setContentFormatType(ContentFormatType.YAML);

		// rules
		final Rules rules = new Rules();
		rules.setRules(new ArrayList<Rule>(2));
		rules.getRules().add(new Rule());
		rules.getRules().get(0).setCode("test-code-1");
		rules.getRules().get(0).setName("test-name-1");
		rules.getRules().get(0).setComment("test-comment-1");
		rules.getRules().get(0).setBizTypeCode("test-bizTypeCode-1");
		rules.getRules().get(0).setParams(new ArrayList<String>(2));
		rules.getRules().get(0).getParams().add("test-param-1-1");
		rules.getRules().get(0).getParams().add("test-param-1-2");
		rules.getRules().get(0).setExpression("test-expression-1");
		rules.getRules().get(0).setMatchType(MatchType.ACCEPTANCE);
		rules.getRules().get(0).setDatasources(new ArrayList<Datasource>(2));
		rules.getRules().get(0).getDatasources().add(new Datasource());
		rules.getRules().get(0).getDatasources().get(0).setCode("test-ds-code-1");
		rules.getRules().get(0).getDatasources().add(new Datasource());
		rules.getRules().get(0).getDatasources().get(1).setCode("test-ds-code-2");
		rules.getRules().get(0).setExtractSqls(new ArrayList<ExtractSql>(2));
		rules.getRules().get(0).getExtractSqls().add(new ExtractSql());
		rules.getRules().get(0).getExtractSqls().get(0).setDatasourceCode("test-ds-code-1");
		rules.getRules().get(0).getExtractSqls().get(0).setParams(new ArrayList<String>(2));
		rules.getRules().get(0).getExtractSqls().get(0).getParams().add("test-param-1-1");
		rules.getRules().get(0).getExtractSqls().get(0).getParams().add("test-param-1-2");
		rules.getRules().get(0).getExtractSqls().get(0).setSql("test-sql-1");
		rules.getRules().get(0).getExtractSqls().add(new ExtractSql());
		rules.getRules().get(0).getExtractSqls().get(1).setDatasourceCode("test-ds-code-2");
		rules.getRules().get(0).getExtractSqls().get(1).setParams(new ArrayList<String>(2));
		rules.getRules().get(0).getExtractSqls().get(1).getParams().add("test-param-2-1");
		rules.getRules().get(0).getExtractSqls().get(1).getParams().add("test-param-2-2");
		rules.getRules().get(0).getExtractSqls().get(1).setSql("test-sql-2");
		rules.getRules().get(0).setExpressionSegments(new TreeMap<String, String>());
		rules.getRules().get(0).getExpressionSegments().put("key-1-1", "value-1-1");
		rules.getRules().get(0).getExpressionSegments().put("key-1-2", "value-1-2");

		rules.getRules().add(new Rule());
		rules.getRules().get(1).setCode("test-code-2");
		rules.getRules().get(1).setName("test-name-2");
		rules.getRules().get(1).setComment("test-comment-2");
		rules.getRules().get(1).setBizTypeCode("test-bizTypeCode-1");
		rules.getRules().get(1).setParams(new ArrayList<String>(2));
		rules.getRules().get(1).getParams().add("test-param-2-1");
		rules.getRules().get(1).getParams().add("test-param-2-2");
		rules.getRules().get(1).setExpression("test-expression-2");
		rules.getRules().get(1).setMatchType(MatchType.SCORE);
		rules.getRules().get(1).setDatasources(new ArrayList<Datasource>(2));
		rules.getRules().get(1).getDatasources().add(new Datasource());
		rules.getRules().get(1).getDatasources().get(0).setCode("test-ds-code-1");
		rules.getRules().get(1).getDatasources().add(new Datasource());
		rules.getRules().get(1).getDatasources().get(1).setCode("test-ds-code-2");
		rules.getRules().get(1).setExtractSqls(new ArrayList<ExtractSql>(2));
		rules.getRules().get(1).getExtractSqls().add(new ExtractSql());
		rules.getRules().get(1).getExtractSqls().get(0).setDatasourceCode("test-ds-code-1");
		rules.getRules().get(1).getExtractSqls().get(0).setParams(new ArrayList<String>(2));
		rules.getRules().get(1).getExtractSqls().get(0).getParams().add("test-param-1-1");
		rules.getRules().get(1).getExtractSqls().get(0).getParams().add("test-param-1-2");
		rules.getRules().get(1).getExtractSqls().get(0).setSql("test-sql-1");
		rules.getRules().get(1).getExtractSqls().add(new ExtractSql());
		rules.getRules().get(1).getExtractSqls().get(1).setDatasourceCode("test-ds-code-2");
		rules.getRules().get(1).getExtractSqls().get(1).setParams(new ArrayList<String>(2));
		rules.getRules().get(1).getExtractSqls().get(1).getParams().add("test-param-2-1");
		rules.getRules().get(1).getExtractSqls().get(1).getParams().add("test-param-2-2");
		rules.getRules().get(1).getExtractSqls().get(1).setSql("test-sql-2");
		rules.getRules().get(1).setExpressionSegments(new TreeMap<String, String>());
		rules.getRules().get(1).getExpressionSegments().put("key-2-1", "value-2-1");
		rules.getRules().get(1).getExpressionSegments().put("key-2-2", "value-2-2");

		// datasPath
		final File datasPath = new File(DATAS_PATH);
		// datasource file
		final File rulesFile = new File(datasPath, settings.getRulesFileName()
				+ settings.getContentFormatType().getFileExtName());

		// mock fileVerifier.canReadPath()
		Mockito.when(this.fileVerifier.canReadPath(datasPath)).thenReturn(true);
		Mockito.when(this.fileVerifier.canReadFile(rulesFile)).thenReturn(true);

		// mock textFileReader.readContent()
		Mockito.when(this.textFileReader.readContent(rulesFile)).thenReturn("test-yaml-rules-content");

		// mock jsonDataContentParser.parseContent()
		Mockito.when(this.yamlDataContentParser.parseContent("test-yaml-rules-content", Rules.class)).thenReturn(rules);

		// mock RuleResource
		final RuleResource resource = Mockito.mock(RuleResource.class);
		// mock resourceHolder.getResource()
		Mockito.when(this.resourceHolder.getResource(RuleResource.class)).thenReturn(resource);

		// test-1: create
		// mock resource.getRule() -> NotFoundException
		Mockito.when(resource.getRule(rules.getRules().get(0).getCode())).thenThrow(new NotFoundException());
		Mockito.when(resource.getRule(rules.getRules().get(1).getCode())).thenThrow(new NotFoundException());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier).canReadFile(rulesFile);
		Mockito.verify(this.textFileReader).readContent(rulesFile);
		Mockito.verify(this.yamlDataContentParser).parseContent("test-yaml-rules-content", Rules.class);
		Mockito.verify(resource).getRule(rules.getRules().get(0).getCode());
		Mockito.verify(resource).getRule(rules.getRules().get(1).getCode());
		Mockito.verify(resource).createRule(rules.getRules().get(0));
		Mockito.verify(resource).createRule(rules.getRules().get(1));
		Mockito.verify(resource, Mockito.never()).updateRule(Mockito.anyString(), Mockito.any(Rule.class));
		Mockito.verify(resource, Mockito.never()).getRules();
		Mockito.verify(resource, Mockito.never()).removeRule(Mockito.anyString());
		System.out.println("test-1: create passed");

		// test-2: no changed
		// mock resource.getRule()
		Mockito.doReturn(rules.getRules().get(0)).when(resource).getRule(rules.getRules().get(0).getCode());
		Mockito.doReturn(rules.getRules().get(1)).when(resource).getRule(rules.getRules().get(1).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadFile(rulesFile);
		Mockito.verify(this.textFileReader, Mockito.times(2)).readContent(rulesFile);
		Mockito.verify(this.yamlDataContentParser, Mockito.times(2)).parseContent("test-yaml-rules-content",
				Rules.class);
		Mockito.verify(resource, Mockito.times(2)).getRule(rules.getRules().get(0).getCode());
		Mockito.verify(resource, Mockito.times(2)).getRule(rules.getRules().get(1).getCode());
		Mockito.verify(resource).createRule(rules.getRules().get(0));
		Mockito.verify(resource).createRule(rules.getRules().get(1));
		Mockito.verify(resource, Mockito.never()).updateRule(Mockito.anyString(), Mockito.any(Rule.class));
		Mockito.verify(resource, Mockito.never()).getRules();
		Mockito.verify(resource, Mockito.never()).removeRule(Mockito.anyString());
		System.out.println("test-2: no changed passed");

		// test-3: update
		final Rule oldRule1 = new Rule();
		oldRule1.setCode("test-code-1");
		oldRule1.setName("test-name-1-old");
		oldRule1.setComment("test-comment-1-old");
		oldRule1.setBizTypeCode("test-bizTypeCode-1-old");
		oldRule1.setParams(new ArrayList<String>(2));
		oldRule1.getParams().add("test-param-1-1-old");
		oldRule1.getParams().add("test-param-1-2-old");
		oldRule1.setExpression("test-expression-1-old");
		oldRule1.setMatchType(MatchType.SCORE);
		oldRule1.setDatasources(new ArrayList<Datasource>(2));
		oldRule1.getDatasources().add(new Datasource());
		oldRule1.getDatasources().get(0).setCode("test-ds-code-1-old");
		oldRule1.getDatasources().add(new Datasource());
		oldRule1.getDatasources().get(1).setCode("test-ds-code-2-old");
		oldRule1.setExtractSqls(new ArrayList<ExtractSql>(2));
		oldRule1.getExtractSqls().add(new ExtractSql());
		oldRule1.getExtractSqls().get(0).setDatasourceCode("test-ds-code-1-old");
		oldRule1.getExtractSqls().get(0).setParams(new ArrayList<String>(2));
		oldRule1.getExtractSqls().get(0).getParams().add("test-param-1-1-old");
		oldRule1.getExtractSqls().get(0).getParams().add("test-param-1-2-old");
		oldRule1.getExtractSqls().get(0).setSql("test-sql-1-old");
		oldRule1.getExtractSqls().add(new ExtractSql());
		oldRule1.getExtractSqls().get(1).setDatasourceCode("test-ds-code-2-old");
		oldRule1.getExtractSqls().get(1).setParams(new ArrayList<String>(2));
		oldRule1.getExtractSqls().get(1).getParams().add("test-param-2-1-old");
		oldRule1.getExtractSqls().get(1).getParams().add("test-param-2-2-old");
		oldRule1.getExtractSqls().get(1).setSql("test-sql-2-old");
		oldRule1.setExpressionSegments(new TreeMap<String, String>());
		oldRule1.getExpressionSegments().put("key-1-1-old", "value-1-1-old");
		oldRule1.getExpressionSegments().put("key-1-2-old", "value-1-2-old");

		final Rule oldRule2 = new Rule();
		oldRule2.setCode("test-code-2");
		oldRule2.setName("test-name-2-old");
		oldRule2.setComment("test-comment-2-old");
		oldRule2.setBizTypeCode("test-bizTypeCode-2-old");
		oldRule2.setParams(new ArrayList<String>(2));
		oldRule2.getParams().add("test-param-1-1-old");
		oldRule2.getParams().add("test-param-1-2-old");
		oldRule2.setExpression("test-expression-2-old");
		oldRule2.setMatchType(MatchType.SCORE);
		oldRule2.setDatasources(new ArrayList<Datasource>(2));
		oldRule2.getDatasources().add(new Datasource());
		oldRule2.getDatasources().get(0).setCode("test-ds-code-1-old");
		oldRule2.getDatasources().add(new Datasource());
		oldRule2.getDatasources().get(1).setCode("test-ds-code-2-old");
		oldRule2.setExtractSqls(new ArrayList<ExtractSql>(2));
		oldRule2.getExtractSqls().add(new ExtractSql());
		oldRule2.getExtractSqls().get(0).setDatasourceCode("test-ds-code-1-old");
		oldRule2.getExtractSqls().get(0).setParams(new ArrayList<String>(2));
		oldRule2.getExtractSqls().get(0).getParams().add("test-param-1-1-old");
		oldRule2.getExtractSqls().get(0).getParams().add("test-param-1-2-old");
		oldRule2.getExtractSqls().get(0).setSql("test-sql-1-old");
		oldRule2.getExtractSqls().add(new ExtractSql());
		oldRule2.getExtractSqls().get(1).setDatasourceCode("test-ds-code-2-old");
		oldRule2.getExtractSqls().get(1).setParams(new ArrayList<String>(2));
		oldRule2.getExtractSqls().get(1).getParams().add("test-param-2-1-old");
		oldRule2.getExtractSqls().get(1).getParams().add("test-param-2-2-old");
		oldRule2.getExtractSqls().get(1).setSql("test-sql-2-old");
		oldRule2.setExpressionSegments(new TreeMap<String, String>());
		oldRule2.getExpressionSegments().put("key-2-1-old", "value-2-1-old");
		oldRule2.getExpressionSegments().put("key-2-2-old", "value-2-2-old");

		// mock resource.getRule()
		Mockito.doReturn(oldRule1).when(resource).getRule(rules.getRules().get(0).getCode());
		Mockito.doReturn(oldRule2).when(resource).getRule(rules.getRules().get(1).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadFile(rulesFile);
		Mockito.verify(this.textFileReader, Mockito.times(3)).readContent(rulesFile);
		Mockito.verify(this.yamlDataContentParser, Mockito.times(3)).parseContent("test-yaml-rules-content",
				Rules.class);
		Mockito.verify(resource, Mockito.times(3)).getRule(rules.getRules().get(0).getCode());
		Mockito.verify(resource, Mockito.times(3)).getRule(rules.getRules().get(1).getCode());
		Mockito.verify(resource).createRule(rules.getRules().get(0));
		Mockito.verify(resource).createRule(rules.getRules().get(1));
		Mockito.verify(resource).updateRule(rules.getRules().get(0).getCode(), rules.getRules().get(0));
		Mockito.verify(resource).updateRule(rules.getRules().get(1).getCode(), rules.getRules().get(1));
		Mockito.verify(resource, Mockito.never()).getRules();
		Mockito.verify(resource, Mockito.never()).removeRule(Mockito.anyString());
		System.out.println("test-3: update passed");

		// test-4: update and remove
		final List<Rule> olds = new ArrayList<Rule>(3);
		olds.addAll(rules.getRules());
		final Rule oldRule3 = new Rule();
		oldRule3.setCode("test-code-3");
		oldRule3.setName("test-name-3-old");
		oldRule3.setComment("test-comment-3-old");
		oldRule3.setBizTypeCode("test-bizTypeCode-3-old");
		oldRule3.setParams(new ArrayList<String>(2));
		oldRule3.getParams().add("test-param-1-1-old");
		oldRule3.getParams().add("test-param-1-2-old");
		oldRule3.setExpression("test-expression-2-old");
		oldRule3.setMatchType(MatchType.SCORE);
		oldRule3.setDatasources(new ArrayList<Datasource>(2));
		oldRule3.getDatasources().add(new Datasource());
		oldRule3.getDatasources().get(0).setCode("test-ds-code-1-old");
		oldRule3.getDatasources().add(new Datasource());
		oldRule3.getDatasources().get(1).setCode("test-ds-code-2-old");
		oldRule3.setExtractSqls(new ArrayList<ExtractSql>(2));
		oldRule3.getExtractSqls().add(new ExtractSql());
		oldRule3.getExtractSqls().get(0).setDatasourceCode("test-ds-code-1-old");
		oldRule3.getExtractSqls().get(0).setParams(new ArrayList<String>(2));
		oldRule3.getExtractSqls().get(0).getParams().add("test-param-1-1-old");
		oldRule3.getExtractSqls().get(0).getParams().add("test-param-1-2-old");
		oldRule3.getExtractSqls().get(0).setSql("test-sql-1-old");
		oldRule3.getExtractSqls().add(new ExtractSql());
		oldRule3.getExtractSqls().get(1).setDatasourceCode("test-ds-code-2-old");
		oldRule3.getExtractSqls().get(1).setParams(new ArrayList<String>(2));
		oldRule3.getExtractSqls().get(1).getParams().add("test-param-2-1-old");
		oldRule3.getExtractSqls().get(1).getParams().add("test-param-2-2-old");
		oldRule3.getExtractSqls().get(1).setSql("test-sql-2-old");
		oldRule3.setExpressionSegments(new TreeMap<String, String>());
		oldRule3.getExpressionSegments().put("key-3-1-old", "value-3-1-old");
		oldRule3.getExpressionSegments().put("key-3-2-old", "value-3-2-old");
		olds.add(oldRule3);
		// mock resource.getDatasources()
		Mockito.when(resource.getRules()).thenReturn(olds);
		settings.setAutoRemoveEnabled(true);
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadFile(rulesFile);
		Mockito.verify(this.textFileReader, Mockito.times(4)).readContent(rulesFile);
		Mockito.verify(resource, Mockito.times(4)).getRule(rules.getRules().get(0).getCode());
		Mockito.verify(resource, Mockito.times(4)).getRule(rules.getRules().get(1).getCode());
		Mockito.verify(resource).createRule(rules.getRules().get(0));
		Mockito.verify(resource).createRule(rules.getRules().get(1));
		Mockito.verify(resource, Mockito.times(2)).updateRule(rules.getRules().get(0).getCode(),
				rules.getRules().get(0));
		Mockito.verify(resource, Mockito.times(2)).updateRule(rules.getRules().get(1).getCode(),
				rules.getRules().get(1));
		Mockito.verify(resource).getRules();
		Mockito.verify(resource).removeRule(oldRule3.getCode());
		System.out.println("test-4: remove passed");
	}

	@Test
	public void testSyncRuleSets() {
		// reset
		Mockito.reset(this.fileVerifier);
		Mockito.reset(this.textFileReader);
		// the settings
		final DatasSyncSettings settings = new DatasSyncSettings();
		settings.setAutoCreateEnabled(true);
		settings.setAutoRemoveEnabled(false);
		settings.setDatasPath(DATAS_PATH);
		settings.setContentFormatType(ContentFormatType.YAML);

		// ruleSets
		final RuleSets ruleSets = new RuleSets();
		ruleSets.setRuleSets(new ArrayList<RuleSet>(2));
		ruleSets.getRuleSets().add(new RuleSet());
		ruleSets.getRuleSets().get(0).setCode("test-code-1");
		ruleSets.getRuleSets().get(0).setName("test-name-1");
		ruleSets.getRuleSets().get(0).setComment("test-comment-1");
		ruleSets.getRuleSets().get(0).setBizTypeCode("test-bizTypeCode-1");
		ruleSets.getRuleSets().get(0).setRules(new ArrayList<Rule>());
		ruleSets.getRuleSets().get(0).getRules().add(new Rule());
		ruleSets.getRuleSets().get(0).getRules().get(0).setCode("r11");
		ruleSets.getRuleSets().get(0).getRules().get(0).setBizTypeCode("test-bizTypeCode-1");
		ruleSets.getRuleSets().get(0).getRules().get(0).setVersion(11);
		ruleSets.getRuleSets().get(0).getRules().add(new Rule());
		ruleSets.getRuleSets().get(0).getRules().get(1).setCode("r12");
		ruleSets.getRuleSets().get(0).getRules().get(1).setBizTypeCode("test-bizTypeCode-1");
		ruleSets.getRuleSets().get(0).getRules().get(1).setVersion(12);
		ruleSets.getRuleSets().add(new RuleSet());
		ruleSets.getRuleSets().get(1).setCode("test-code-2");
		ruleSets.getRuleSets().get(1).setName("test-name-2");
		ruleSets.getRuleSets().get(1).setComment("test-comment-2");
		ruleSets.getRuleSets().get(1).setBizTypeCode("test-bizTypeCode-1");
		ruleSets.getRuleSets().get(1).setRules(new ArrayList<Rule>());
		ruleSets.getRuleSets().get(1).getRules().add(new Rule());
		ruleSets.getRuleSets().get(1).getRules().get(0).setCode("r21");
		ruleSets.getRuleSets().get(1).getRules().get(0).setBizTypeCode("test-bizTypeCode-1");
		ruleSets.getRuleSets().get(1).getRules().get(0).setVersion(21);
		ruleSets.getRuleSets().get(1).getRules().add(new Rule());
		ruleSets.getRuleSets().get(1).getRules().get(1).setCode("r22");
		ruleSets.getRuleSets().get(1).getRules().get(1).setBizTypeCode("test-bizTypeCode-1");
		ruleSets.getRuleSets().get(1).getRules().get(1).setVersion(22);

		// datasPath
		final File datasPath = new File(DATAS_PATH);
		// datasource file
		final File ruleSetsFile = new File(datasPath, settings.getRuleSetsFileName()
				+ settings.getContentFormatType().getFileExtName());

		// mock fileVerifier.canReadPath()
		Mockito.when(this.fileVerifier.canReadPath(datasPath)).thenReturn(true);
		Mockito.when(this.fileVerifier.canReadFile(ruleSetsFile)).thenReturn(true);

		// mock textFileReader.readContent()
		Mockito.when(this.textFileReader.readContent(ruleSetsFile)).thenReturn("test-yaml-ruleSets-content");

		// mock jsonDataContentParser.parseContent()
		Mockito.when(this.yamlDataContentParser.parseContent("test-yaml-ruleSets-content", RuleSets.class)).thenReturn(
				ruleSets);

		// mock RuleResource
		final RuleResource resource = Mockito.mock(RuleResource.class);
		// mock resourceHolder.getResource()
		Mockito.when(this.resourceHolder.getResource(RuleResource.class)).thenReturn(resource);

		// test-1: create
		// mock resource.getRuleSet() -> NotFoundException
		Mockito.when(resource.getRuleSet(ruleSets.getRuleSets().get(0).getCode())).thenThrow(new NotFoundException());
		Mockito.when(resource.getRuleSet(ruleSets.getRuleSets().get(1).getCode())).thenThrow(new NotFoundException());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier).canReadFile(ruleSetsFile);
		Mockito.verify(this.textFileReader).readContent(ruleSetsFile);
		Mockito.verify(this.yamlDataContentParser).parseContent("test-yaml-ruleSets-content", RuleSets.class);
		Mockito.verify(resource).getRuleSet(ruleSets.getRuleSets().get(0).getCode());
		Mockito.verify(resource).getRuleSet(ruleSets.getRuleSets().get(1).getCode());
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(0));
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(1));
		Mockito.verify(resource, Mockito.never()).updateRuleSet(Mockito.anyString(), Mockito.any(RuleSet.class));
		Mockito.verify(resource, Mockito.never()).getRuleSets();
		Mockito.verify(resource, Mockito.never()).removeRuleSet(Mockito.anyString());
		System.out.println("test-1: create passed");

		// test-2: no changed
		// mock resource.getRuleSet()
		Mockito.doReturn(ruleSets.getRuleSets().get(0)).when(resource)
				.getRuleSet(ruleSets.getRuleSets().get(0).getCode());
		Mockito.doReturn(ruleSets.getRuleSets().get(1)).when(resource)
				.getRuleSet(ruleSets.getRuleSets().get(1).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(2)).canReadFile(ruleSetsFile);
		Mockito.verify(this.textFileReader, Mockito.times(2)).readContent(ruleSetsFile);
		Mockito.verify(this.yamlDataContentParser, Mockito.times(2)).parseContent("test-yaml-ruleSets-content",
				RuleSets.class);
		Mockito.verify(resource, Mockito.times(2)).getRuleSet(ruleSets.getRuleSets().get(0).getCode());
		Mockito.verify(resource, Mockito.times(2)).getRuleSet(ruleSets.getRuleSets().get(1).getCode());
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(0));
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(1));
		Mockito.verify(resource, Mockito.never()).updateRuleSet(Mockito.anyString(), Mockito.any(RuleSet.class));
		Mockito.verify(resource, Mockito.never()).getRuleSets();
		Mockito.verify(resource, Mockito.never()).removeRuleSet(Mockito.anyString());
		System.out.println("test-2: no changed passed");

		// test-3: update
		final RuleSet oldRuleSet1 = new RuleSet();
		oldRuleSet1.setCode("test-code-1");
		oldRuleSet1.setName("test-name-1-old");
		oldRuleSet1.setComment("test-comment-1-old");
		oldRuleSet1.setBizTypeCode("test-bizTypeCode-1-old");
		oldRuleSet1.setRules(new ArrayList<Rule>());
		oldRuleSet1.getRules().add(new Rule());
		oldRuleSet1.getRules().get(0).setCode("r12");
		oldRuleSet1.getRules().get(0).setBizTypeCode("test-bizTypeCode-1");
		oldRuleSet1.getRules().get(0).setVersion(2);
		oldRuleSet1.getRules().add(new Rule());
		oldRuleSet1.getRules().get(1).setCode("r11");
		oldRuleSet1.getRules().get(1).setBizTypeCode("test-bizTypeCode-1");
		oldRuleSet1.getRules().get(1).setVersion(1);

		final RuleSet oldRuleSet2 = new RuleSet();
		oldRuleSet2.setCode("test-code-2");
		oldRuleSet2.setName("test-name-2-old");
		oldRuleSet2.setComment("test-comment-2-old");
		oldRuleSet2.setBizTypeCode("test-bizTypeCode-1-old");
		oldRuleSet2.setRules(new ArrayList<Rule>());
		oldRuleSet2.getRules().add(new Rule());
		oldRuleSet2.getRules().get(0).setCode("r22");
		oldRuleSet2.getRules().get(0).setBizTypeCode("test-bizTypeCode-1");
		oldRuleSet2.getRules().get(0).setVersion(4);
		oldRuleSet2.getRules().add(new Rule());
		oldRuleSet2.getRules().get(1).setCode("r21");
		oldRuleSet2.getRules().get(1).setBizTypeCode("test-bizTypeCode-1");
		oldRuleSet2.getRules().get(1).setVersion(3);

		// mock resource.getRuleSet()
		Mockito.doReturn(oldRuleSet1).when(resource).getRuleSet(ruleSets.getRuleSets().get(0).getCode());
		Mockito.doReturn(oldRuleSet2).when(resource).getRuleSet(ruleSets.getRuleSets().get(1).getCode());
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(3)).canReadFile(ruleSetsFile);
		Mockito.verify(this.textFileReader, Mockito.times(3)).readContent(ruleSetsFile);
		Mockito.verify(this.yamlDataContentParser, Mockito.times(3)).parseContent("test-yaml-ruleSets-content",
				RuleSets.class);
		Mockito.verify(resource, Mockito.times(3)).getRuleSet(ruleSets.getRuleSets().get(0).getCode());
		Mockito.verify(resource, Mockito.times(3)).getRuleSet(ruleSets.getRuleSets().get(1).getCode());
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(0));
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(1));
		Mockito.verify(resource).updateRuleSet(ruleSets.getRuleSets().get(0).getCode(), ruleSets.getRuleSets().get(0));
		Mockito.verify(resource).updateRuleSet(ruleSets.getRuleSets().get(1).getCode(), ruleSets.getRuleSets().get(1));
		Mockito.verify(resource, Mockito.never()).getRuleSets();
		Mockito.verify(resource, Mockito.never()).removeRuleSet(Mockito.anyString());
		System.out.println("test-3: update passed");

		// test-4: update and remove
		final List<RuleSet> olds = new ArrayList<RuleSet>(3);
		olds.addAll(ruleSets.getRuleSets());

		final RuleSet oldRuleSet3 = new RuleSet();
		oldRuleSet3.setCode("test-code-3");
		oldRuleSet3.setName("test-name-3-old");
		oldRuleSet3.setComment("test-comment-3-old");
		oldRuleSet3.setBizTypeCode("test-bizTypeCode-1-old");
		oldRuleSet3.setRules(new ArrayList<Rule>());
		oldRuleSet3.getRules().add(new Rule());
		oldRuleSet3.getRules().get(0).setCode("r31");
		oldRuleSet3.getRules().get(0).setBizTypeCode("test-bizTypeCode-1");
		oldRuleSet3.getRules().get(0).setVersion(5);
		oldRuleSet3.getRules().add(new Rule());
		oldRuleSet3.getRules().get(1).setCode("r32");
		oldRuleSet3.getRules().get(1).setBizTypeCode("test-bizTypeCode-1");
		oldRuleSet3.getRules().get(1).setVersion(6);
		olds.add(oldRuleSet3);
		// mock resource.getDatasources()
		Mockito.when(resource.getRuleSets()).thenReturn(olds);
		settings.setAutoRemoveEnabled(true);
		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier, Mockito.times(4)).canReadFile(ruleSetsFile);
		Mockito.verify(this.textFileReader, Mockito.times(4)).readContent(ruleSetsFile);
		Mockito.verify(resource, Mockito.times(4)).getRuleSet(ruleSets.getRuleSets().get(0).getCode());
		Mockito.verify(resource, Mockito.times(4)).getRuleSet(ruleSets.getRuleSets().get(1).getCode());
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(0));
		Mockito.verify(resource).createRuleSet(ruleSets.getRuleSets().get(1));
		Mockito.verify(resource, Mockito.times(2)).updateRuleSet(ruleSets.getRuleSets().get(0).getCode(),
				ruleSets.getRuleSets().get(0));
		Mockito.verify(resource, Mockito.times(2)).updateRuleSet(ruleSets.getRuleSets().get(1).getCode(),
				ruleSets.getRuleSets().get(1));
		Mockito.verify(resource).getRuleSets();
		Mockito.verify(resource).removeRuleSet(oldRuleSet3.getCode());
		System.out.println("test-4: remove passed");
	}

	@Test
	public void testSyncRuleSetAssignments() {
		// reset
		Mockito.reset(this.fileVerifier);
		Mockito.reset(this.textFileReader);

		// RuleSetAssignments
		final RuleSetAssignments ruleSetAssignments = new RuleSetAssignments();
		ruleSetAssignments.setRuleSetAssignments(new ArrayList<RuleSetAssignment>());
		ruleSetAssignments.getRuleSetAssignments().add(new RuleSetAssignment());
		ruleSetAssignments.getRuleSetAssignments().get(0).setBizTypeCode("test-bizTypeCode-1");
		ruleSetAssignments.getRuleSetAssignments().get(0).setRuleSetCode("test-ruleSetCode-1");
		ruleSetAssignments.getRuleSetAssignments().get(0).setRuleSetVersion(111);
		ruleSetAssignments.getRuleSetAssignments().add(new RuleSetAssignment());
		ruleSetAssignments.getRuleSetAssignments().get(1).setBizTypeCode("test-bizTypeCode-2");
		ruleSetAssignments.getRuleSetAssignments().get(1).setRuleSetCode("test-ruleSetCode-2");
		ruleSetAssignments.getRuleSetAssignments().get(1).setRuleSetVersion(222);

		// the settings
		final DatasSyncSettings settings = new DatasSyncSettings();
		settings.setAutoCreateEnabled(true);
		settings.setAutoRemoveEnabled(false);
		settings.setDatasPath(DATAS_PATH);
		settings.setContentFormatType(ContentFormatType.YAML);

		// datasPath
		final File datasPath = new File(DATAS_PATH);
		// datasource file
		final File ruleSetsFile = new File(datasPath, settings.getRuleSetAssignmentsFileName()
				+ settings.getContentFormatType().getFileExtName());

		// mock fileVerifier.canReadPath()
		Mockito.when(this.fileVerifier.canReadPath(datasPath)).thenReturn(true);
		Mockito.when(this.fileVerifier.canReadFile(ruleSetsFile)).thenReturn(true);

		// mock textFileReader.readContent()
		Mockito.when(this.textFileReader.readContent(ruleSetsFile)).thenReturn("test-yaml-ruleSetAssignments-content");

		// mock jsonDataContentParser.parseContent()
		Mockito.when(
				this.yamlDataContentParser.parseContent("test-yaml-ruleSetAssignments-content",
						RuleSetAssignments.class)).thenReturn(ruleSetAssignments);

		// mock RuleResource
		final RuleResource resource = Mockito.mock(RuleResource.class);
		// mock resourceHolder.getResource()
		Mockito.when(this.resourceHolder.getResource(RuleResource.class)).thenReturn(resource);

		// syncDatas
		this.datasSyncer.syncDatas(settings);
		// check
		Mockito.verify(this.fileVerifier).canReadPath(datasPath);
		Mockito.verify(this.fileVerifier).canReadFile(ruleSetsFile);
		Mockito.verify(this.textFileReader).readContent(ruleSetsFile);
		Mockito.verify(this.yamlDataContentParser).parseContent("test-yaml-ruleSetAssignments-content",
				RuleSetAssignments.class);
		Mockito.verify(resource).assignRuleSet(ruleSetAssignments.getRuleSetAssignments().get(0).getBizTypeCode(),
				ruleSetAssignments.getRuleSetAssignments().get(0).getRuleSetCode(),
				ruleSetAssignments.getRuleSetAssignments().get(0).getRuleSetVersion());
		Mockito.verify(resource).assignRuleSet(ruleSetAssignments.getRuleSetAssignments().get(1).getBizTypeCode(),
				ruleSetAssignments.getRuleSetAssignments().get(1).getRuleSetCode(),
				ruleSetAssignments.getRuleSetAssignments().get(1).getRuleSetVersion());
		System.out.println("testSyncRuleSetAssignments passed");
	}

	@Bean
	public DatasSyncer createRulesDatasSyncer() {
		return new RulesDatasSyncer();
	}

	@Bean
	public ResourceHolder mockResourceHolder() {
		return Mockito.mock(ResourceHolder.class);
	}

	@Bean
	public TextFileReader mockTextFileReader() {
		return Mockito.mock(TextFileReader.class);
	}

	@Bean(name = "jsonDataContentParser")
	public DataContentParser mockJsonDataContentParser() {
		return Mockito.mock(DataContentParser.class);
	}

	@Bean(name = "yamlDataContentParser")
	public DataContentParser mockYamlDataContentParser() {
		return Mockito.mock(DataContentParser.class);
	}

	@Bean
	public FileVerifier mockFileVerifier() {
		return Mockito.mock(FileVerifier.class);
	}

	/**
	 * @param datasSyncer
	 *            the datasSyncer to set
	 */
	public void setDatasSyncer(DatasSyncer datasSyncer) {
		this.datasSyncer = datasSyncer;
	}

}
