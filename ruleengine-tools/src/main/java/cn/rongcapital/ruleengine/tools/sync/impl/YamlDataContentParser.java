/**
 * 
 */
package cn.rongcapital.ruleengine.tools.sync.impl;

import cn.rongcapital.ruleengine.tools.utils.YamlUtils;
import org.springframework.stereotype.Service;

import cn.rongcapital.ruleengine.tools.sync.DataContentParser;

/**
 * the YAML implementation for DataContentParser
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Service
public final class YamlDataContentParser implements DataContentParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see DataContentParser#parseContent(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T parseContent(final String content, final Class<T> type) {
		// check
		if (content == null || type == null) {
			return null;
		}
		return YamlUtils.loadFrom(content, type);
	}

}
