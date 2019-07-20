/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.impl;

import java.io.File;
import java.util.List;

import javax.ws.rs.NotFoundException;

import cn.rongcapital.ruleengine.tools.sync.ContentFormatType;
import cn.rongcapital.ruleengine.tools.sync.ResourceHolder;
import cn.rongcapital.ruleengine.tools.sync.TextFileReader;
import cn.rongcapital.ruleengine.tools.sync.data.RuleSets;
import cn.rongcapital.ruleengine.tools.sync.data.Rules;
import cn.rongcapital.ruleengine.tools.utils.CompareUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.api.RuleResource;
import cn.rongcapital.ruleengine.model.BizType;
import cn.rongcapital.ruleengine.model.Datasource;
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
 * the rules datas DatasSyncer
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class RulesDatasSyncer implements DatasSyncer {

	/**
	 * the logger
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(RulesDatasSyncer.class);

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

	private DatasSyncSettings settings;

	private File datasPath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see DatasSyncer#syncDatas(cn.rongcapital.ruleengine.tools.sync.
	 * DatasSyncSettings)
	 */
	@Override
	public void syncDatas(final DatasSyncSettings settings) {
		// check
		if (settings == null) {
			throw new RuntimeException("the settings is null");
		}
		if (settings.getDatasPath() == null) {
			throw new RuntimeException("the datasPath is null");
		}
		if (settings.getContentFormatType() == null) {
			throw new RuntimeException("the content format type is null");
		}
		if (settings.getDatasourcesFileName() == null) {
			throw new RuntimeException("the datasources file name is null");
		}
		if (settings.getRulesFileName() == null) {
			throw new RuntimeException("the rules file name is null");
		}
		if (settings.getRuleSetsFileName() == null) {
			throw new RuntimeException("the ruleSets file name is null");
		}
		if (settings.getRuleSetAssignmentsFileName() == null) {
			throw new RuntimeException("the ruleSetAssignments file name is null");
		}
		this.settings = settings;
		this.datasPath = new File(this.settings.getDatasPath());
		if (!this.fileVerifier.canReadPath(this.datasPath)) {
			throw new RuntimeException("can not read the datas path: " + this.datasPath);
		}
		try {
			// process the bizTypes
			this.processBizTypes();
		} catch (Exception e) {
			LOGGER.error("process the bizTypes failed, error: " + e.getMessage(), e);
		}
		try {
			// process the datasources
			this.processDatasources();
		} catch (Exception e) {
			LOGGER.error("process the datasources failed, error: " + e.getMessage(), e);
		}
		try {
			// process the rules
			this.processRules();
		} catch (Exception e) {
			LOGGER.error("process the rules failed, error: " + e.getMessage(), e);
		}
		try {
			// process the ruleSets
			this.processRuleSets();
		} catch (Exception e) {
			LOGGER.error("process the ruleSets failed, error: " + e.getMessage(), e);
		}
		try {
			// process the ruleSetAssignments
			this.processRuleSetAssignments();
		} catch (Exception e) {
			LOGGER.error("process the ruleSetAssignments failed, error: " + e.getMessage(), e);
		}
	}

	private void processBizTypes() {
		// file
		final File file = new File(this.datasPath, this.settings.getBizTypesFileName()
				+ this.settings.getContentFormatType().getFileExtName());
		LOGGER.info("processing the bizTypes, file: {}", file);
		// check
		if (!this.fileVerifier.canReadFile(file)) {
			LOGGER.warn("can not read the bizType datas file: {}, process bizTypes ignored", file);
			return;
		}
		// read bizTypes
		final String content = this.textFileReader.readContent(file);
		if (content == null) {
			LOGGER.error("read the bizType datas failed, file : {}, process bizTypes ignored", file);
			return;
		}
		final BizTypes bts = this.getDataContentParser().parseContent(content, BizTypes.class);
		if (bts == null || bts.getBizTypes() == null || bts.getBizTypes().isEmpty()) {
			LOGGER.warn("no bizType datas in file: {}, process bizTypes ignored", file);
			return;
		}
		LOGGER.debug("the bizTypes loaded: {}", bts);
		final RuleResource resource = this.resourceHolder.getResource(RuleResource.class);
		// update and create
		for (final BizType bt : bts.getBizTypes()) {
			if (bt.getCode() == null) {
				LOGGER.warn("the bizType has no code, ignored it: ", bt);
				continue;
			}
			BizType old = null;
			try {
				old = resource.getBizType(bt.getCode());
			} catch (NotFoundException e) {
				// not existed
			} catch (Exception e) {
				LOGGER.error("get the old bizType failed, code: " + bt.getCode() + ", error: " + e.getMessage(), e);
			}
			if (old != null) {
				if (!old.equals(bt)) {
					// update
					resource.updateBizType(old.getCode(), bt);
					LOGGER.info("the bizType updated: {}", bt);
				} else {
					LOGGER.info("the bizType is not changed, ignore it: {}", bt);
				}
			} else if (this.settings.isAutoCreateEnabled()) {
				// create
				resource.createBizType(bt);
				LOGGER.info("the new bizType created: {}", bt);
			}
		}
		// remove
		if (this.settings.isAutoRemoveEnabled()) {
			final List<BizType> olds = resource.getAllBizTypes();
			for (final BizType old : olds) {
				boolean existed = false;
				for (final BizType bt : bts.getBizTypes()) {
					if (old.getCode().equals(bt.getCode())) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					// remove
					resource.removeBizType(old.getCode());
					LOGGER.info("the bizType removed: {}", old);
				}
			}
		}
		LOGGER.info("all bizTypes processed");
	}

	private void processDatasources() {
		// file
		final File file = new File(this.datasPath, this.settings.getDatasourcesFileName()
				+ this.settings.getContentFormatType().getFileExtName());
		LOGGER.info("processing the datasources, file: {}", file);
		// check
		if (!this.fileVerifier.canReadFile(file)) {
			LOGGER.warn("can not read the datasource datas file: {}, process datasource ignored", file);
			return;
		}
		// read datasources
		final String content = this.textFileReader.readContent(file);
		if (content == null) {
			LOGGER.error("read the datasource datas failed, file : {}, process datasource ignored", file);
			return;
		}
		final Datasources dss = this.getDataContentParser().parseContent(content, Datasources.class);
		if (dss == null || dss.getDatasources() == null || dss.getDatasources().isEmpty()) {
			LOGGER.warn("no datasource datas in file: {}, process datasource ignored", file);
			return;
		}
		LOGGER.debug("the datasources loaded: {}", dss);
		final RuleResource resource = this.resourceHolder.getResource(RuleResource.class);
		// update and create
		for (final Datasource ds : dss.getDatasources()) {
			if (ds.getCode() == null) {
				LOGGER.warn("the datasource has no code, ignored it: ", ds);
				continue;
			}
			Datasource old = null;
			try {
				old = resource.getDatasource(ds.getCode());
			} catch (NotFoundException e) {
				// not existed
			} catch (Exception e) {
				LOGGER.error("get the old datasource failed, code: " + ds.getCode() + ", error: " + e.getMessage(), e);
			}
			if (old != null) {
				if (CompareUtils.isChanged(old, ds, true)) {
					// update
					resource.updateDatasource(old.getCode(), ds);
					LOGGER.info("the datasource updated: {}", ds);
				} else {
					LOGGER.info("the datasource is not changed, ignore it: {}", ds);
				}
			} else if (this.settings.isAutoCreateEnabled()) {
				// create
				resource.createDatasource(ds);
				LOGGER.info("the new datasource created: {}", ds);
			}
		}
		// remove
		if (this.settings.isAutoRemoveEnabled()) {
			final List<Datasource> olds = resource.getDatasources();
			for (final Datasource old : olds) {
				boolean existed = false;
				for (final Datasource ds : dss.getDatasources()) {
					if (old.getCode().equals(ds.getCode())) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					// remove
					resource.removeDatasource(old.getCode());
					LOGGER.info("the datasource removed: {}", old);
				}
			}
		}
		LOGGER.info("all datasources processed");
	}

	private void processRules() {
		// file
		final File file = new File(this.datasPath, this.settings.getRulesFileName()
				+ this.settings.getContentFormatType().getFileExtName());
		LOGGER.info("processing the rules, file: {}", file);
		// check
		if (!this.fileVerifier.canReadFile(file)) {
			LOGGER.warn("can not read the rule datas file: {}, process rules ignored", file);
			return;
		}
		// read rules
		final String content = this.textFileReader.readContent(file);
		if (content == null) {
			LOGGER.error("read the rules datas failed, file : {}, process rules ignored", file);
			return;
		}
		final Rules ruless = this.getDataContentParser().parseContent(content, Rules.class);
		if (ruless == null || ruless.getRules() == null || ruless.getRules().isEmpty()) {
			LOGGER.warn("no rule datas in file: {}, process rule ignored", file);
			return;
		}
		LOGGER.debug("the rules loaded: {}", ruless);
		final RuleResource resource = this.resourceHolder.getResource(RuleResource.class);
		// update and create
		for (final Rule rule : ruless.getRules()) {
			if (rule.getCode() == null) {
				LOGGER.warn("the rule has no code, ignore it: ", rule);
				continue;
			}
			Rule old = null;
			try {
				old = resource.getRule(rule.getCode());
			} catch (NotFoundException e) {
				// not existed
			} catch (Exception e) {
				LOGGER.error("get the old rule failed, code: " + rule.getCode() + ", error: " + e.getMessage(), e);
			}
			if (old != null) {
				if (CompareUtils.isChanged(old, rule)) {
					// update
					resource.updateRule(old.getCode(), rule);
					LOGGER.info("the rule updated: {}", rule);
				} else {
					LOGGER.info("the rule is not changed, ignore it: ", rule);
				}
			} else if (this.settings.isAutoCreateEnabled()) {
				// create
				resource.createRule(rule);
				LOGGER.info("the new rule created: {}", rule);
			}
		}
		// remove
		if (this.settings.isAutoRemoveEnabled()) {
			final List<Rule> olds = resource.getRules();
			for (final Rule old : olds) {
				boolean existed = false;
				for (final Rule ds : ruless.getRules()) {
					if (old.getCode().equals(ds.getCode())) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					// remove
					resource.removeRule(old.getCode());
					LOGGER.info("the rule removed: {}", old);
				}
			}
		}
		LOGGER.info("all rules processed");
	}

	private void processRuleSets() {
		// file
		final File file = new File(this.datasPath, this.settings.getRuleSetsFileName()
				+ this.settings.getContentFormatType().getFileExtName());
		LOGGER.info("processing the ruleSets, file: {}", file);
		// check
		if (!this.fileVerifier.canReadFile(file)) {
			LOGGER.warn("can not read the ruleSet datas file: {}, process ruleSets ignored", file);
			return;
		}
		// read ruleSets
		final String content = this.textFileReader.readContent(file);
		if (content == null) {
			LOGGER.error("read the ruleSets datas failed, file : {}, process ruleSets ignored", file);
			return;
		}
		final RuleSets ruleSetss = this.getDataContentParser().parseContent(content, RuleSets.class);
		if (ruleSetss == null || ruleSetss.getRuleSets() == null || ruleSetss.getRuleSets().isEmpty()) {
			LOGGER.warn("no ruleSet datas in file: {}, process ruleSet ignored", file);
			return;
		}
		LOGGER.debug("the ruleSets loaded: {}", ruleSetss);
		final RuleResource resource = this.resourceHolder.getResource(RuleResource.class);
		// update and create
		for (final RuleSet ruleSet : ruleSetss.getRuleSets()) {
			if (ruleSet.getCode() == null) {
				LOGGER.warn("the ruleSet has no code, ignore it: ", ruleSet);
				continue;
			}
			RuleSet old = null;
			try {
				old = resource.getRuleSet(ruleSet.getCode());
			} catch (NotFoundException e) {
				// not existed
			} catch (Exception e) {
				LOGGER.error("get the old ruleSet failed, code: " + ruleSet.getCode() + ", error: " + e.getMessage(), e);
			}
			if (old != null) {
				if (CompareUtils.isChanged(old, ruleSet)) {
					// update
					resource.updateRuleSet(old.getCode(), ruleSet);
					LOGGER.info("the ruleSet updated: {}", ruleSet);
				} else {
					LOGGER.info("the ruleSet is not changed, ignore it: ", ruleSet);
				}
			} else if (this.settings.isAutoCreateEnabled()) {
				// create
				resource.createRuleSet(ruleSet);
				LOGGER.info("the new ruleSet created: {}", ruleSet);
			}
		}
		// remove
		if (this.settings.isAutoRemoveEnabled()) {
			final List<RuleSet> olds = resource.getRuleSets();
			for (final RuleSet old : olds) {
				boolean existed = false;
				for (final RuleSet rs : ruleSetss.getRuleSets()) {
					if (old.getCode().equals(rs.getCode())) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					// remove
					resource.removeRuleSet(old.getCode());
					LOGGER.info("the ruleSet removed: {}", old);
				}
			}
		}
		LOGGER.info("all ruleSets processed");
	}

	private void processRuleSetAssignments() {
		// file
		final File file = new File(this.datasPath, this.settings.getRuleSetAssignmentsFileName()
				+ this.settings.getContentFormatType().getFileExtName());
		LOGGER.info("processing the ruleSetAssignments, file: {}", file);
		// check
		if (!this.fileVerifier.canReadFile(file)) {
			LOGGER.warn("can not read the ruleSetAssignment datas file: {}, process ruleSetAssignments ignored", file);
			return;
		}
		// read ruleSetAssignments
		final String content = this.textFileReader.readContent(file);
		if (content == null) {
			LOGGER.error("read the ruleSetAssignments datas failed, file : {}, process ruleSetAssignments ignored",
					file);
			return;
		}
		final RuleSetAssignments ruleSetAssignments = this.getDataContentParser().parseContent(content,
				RuleSetAssignments.class);
		if (ruleSetAssignments == null || ruleSetAssignments.getRuleSetAssignments() == null
				|| ruleSetAssignments.getRuleSetAssignments().isEmpty()) {
			LOGGER.warn("no ruleSetAssignment datas in file: {}, process ruleSetAssignments ignored", file);
			return;
		}
		LOGGER.debug("the ruleSetAssignments loaded: {}", ruleSetAssignments);
		final RuleResource resource = this.resourceHolder.getResource(RuleResource.class);

		// assignment
		for (final RuleSetAssignment rsa : ruleSetAssignments.getRuleSetAssignments()) {
			if (rsa.getBizTypeCode() == null || rsa.getRuleSetCode() == null) {
				LOGGER.warn("the ruleSetAssignment has no code, ignore it: ", rsa);
				continue;
			}
			try {
				resource.assignRuleSet(rsa.getBizTypeCode(), rsa.getRuleSetCode(), rsa.getRuleSetVersion());
				LOGGER.info("the ruleSet assigned, bizTypeCode: {}, ruleSetCode: {}, ruleSetVersion: {}",
						rsa.getBizTypeCode(), rsa.getRuleSetCode(), rsa.getRuleSetVersion());
			} catch (Exception e) {
				LOGGER.error(
						"assign ruleSet failed, bizTypeCode: " + rsa.getBizTypeCode() + ", ruleSetCode: "
								+ rsa.getRuleSetCode() + ", ruleSetVersion: " + rsa.getRuleSetVersion() + ", error: "
								+ e.getMessage(), e);
			}
		}
		LOGGER.info("all ruleSetAssigments processed");
	}

	private DataContentParser getDataContentParser() {
		if (ContentFormatType.JSON == this.settings.getContentFormatType()) {
			return this.jsonDataContentParser;
		} else {
			return this.yamlDataContentParser;
		}
	}

	/**
	 * @param resourceHolder
	 *            the resourceHolder to set
	 */
	public void setResourceHolder(final ResourceHolder resourceHolder) {
		this.resourceHolder = resourceHolder;
	}

	/**
	 * @param textFileReader
	 *            the textFileReader to set
	 */
	public void setTextFileReader(final TextFileReader textFileReader) {
		this.textFileReader = textFileReader;
	}

	/**
	 * @param jsonDataContentParser
	 *            the jsonDataContentParser to set
	 */
	public void setJsonDataContentParser(final DataContentParser jsonDataContentParser) {
		this.jsonDataContentParser = jsonDataContentParser;
	}

	/**
	 * @param yamlDataContentParser
	 *            the yamlDataContentParser to set
	 */
	public void setYamlDataContentParser(final DataContentParser yamlDataContentParser) {
		this.yamlDataContentParser = yamlDataContentParser;
	}

	/**
	 * @param fileVerifier
	 *            the fileVerifier to set
	 */
	public void setFileVerifier(final FileVerifier fileVerifier) {
		this.fileVerifier = fileVerifier;
	}

}
