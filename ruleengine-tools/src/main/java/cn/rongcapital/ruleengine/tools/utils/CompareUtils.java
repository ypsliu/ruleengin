/**
 * 
 */
package cn.rongcapital.ruleengine.tools.utils;

import cn.rongcapital.ruleengine.model.Datasource;
import cn.rongcapital.ruleengine.model.ExtractSql;
import cn.rongcapital.ruleengine.model.Rule;
import cn.rongcapital.ruleengine.model.RuleSet;

/**
 * the compare utils
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class CompareUtils {

	/**
	 * is the datasource changed?
	 * 
	 * @param old
	 *            the old datasource
	 * @param current
	 *            the current datasource
	 * @param fullCompare
	 *            compare fully?
	 * @return true: changed
	 */
	public static boolean isChanged(final Datasource old, final Datasource current, final boolean fullCompare) {
		if (old == null && current == null) {
			return false;
		}
		if ((old == null && current != null) || (old != null && current == null)) {
			return true;
		}
		// code
		if (old.getCode() == null) {
			if (current.getCode() != null) {
				return true;
			}
		} else if (!old.getCode().equals(current.getCode())) {
			return true;
		}
		if (!fullCompare) {
			return false;
		}
		// name
		if (old.getName() == null) {
			if (current.getName() != null) {
				return true;
			}
		} else if (!old.getName().equals(current.getName())) {
			return true;
		}
		// comment
		if (old.getComment() == null) {
			if (current.getComment() != null) {
				return true;
			}
		} else if (!old.getComment().equals(current.getComment())) {
			return true;
		}
		// driverClass
		if (old.getDriverClass() == null) {
			if (current.getDriverClass() != null) {
				return true;
			}
		} else if (!old.getDriverClass().equals(current.getDriverClass())) {
			return true;
		}
		// maxPoolSize
		if (old.getMaxPoolSize() != current.getMaxPoolSize()) {
			return true;
		}
		// password
		if (old.getPassword() == null) {
			if (current.getPassword() != null) {
				return true;
			}
		} else if (!old.getPassword().equals(current.getPassword())) {
			return true;
		}
		// url
		if (old.getUrl() == null) {
			if (current.getUrl() != null) {
				return true;
			}
		} else if (!old.getUrl().equals(current.getUrl())) {
			return true;
		}
		// user
		if (old.getUser() == null) {
			if (current.getUser() != null) {
				return true;
			}
		} else if (!old.getUser().equals(current.getUser())) {
			return true;
		}
		// validationSql
		if (old.getValidationSql() == null) {
			if (current.getValidationSql() != null) {
				return true;
			}
		} else if (!old.getValidationSql().equals(current.getValidationSql())) {
			return true;
		}
		return false;
	}

	/**
	 * is the rule changed?
	 * 
	 * @param old
	 *            the old rule
	 * @param current
	 *            the current rule
	 * @return true: changed
	 */
	public static boolean isChanged(final Rule old, final Rule current) {
		if (old == null && current == null) {
			return false;
		}
		if ((old == null && current != null) || (old != null && current == null)) {
			return true;
		}
		// code
		if (old.getCode() == null) {
			if (current.getCode() != null) {
				return true;
			}
		} else if (!old.getCode().equals(current.getCode())) {
			return true;
		}
		// name
		if (old.getName() == null) {
			if (current.getName() != null) {
				return true;
			}
		} else if (!old.getName().equals(current.getName())) {
			return true;
		}
		// comment
		if (old.getComment() == null) {
			if (current.getComment() != null) {
				return true;
			}
		} else if (!old.getComment().equals(current.getComment())) {
			return true;
		}
		// bizTypeCode
		if (old.getBizTypeCode() == null) {
			if (current.getBizTypeCode() != null) {
				return true;
			}
		} else if (!old.getBizTypeCode().equals(current.getBizTypeCode())) {
			return true;
		}
		// datasources
		if ((old.getDatasources() == null && current.getDatasources() != null)
				|| (old.getDatasources() != null && current.getDatasources() == null)) {
			return true;
		} else if (old.getDatasources() != null && current.getDatasources() != null) {
			if (old.getDatasources().size() != current.getDatasources().size()) {
				return true;
			}
			for (int i = 0; i < old.getDatasources().size(); i++) {
				final Datasource oldDs = old.getDatasources().get(i);
				final Datasource currentDs = current.getDatasources().get(i);
				if (isChanged(oldDs, currentDs, false)) {
					return true;
				}
			}
		}
		// executorClass
		if (old.getExecutorClass() == null) {
			if (current.getExecutorClass() != null) {
				return true;
			}
		} else if (!old.getExecutorClass().equals(current.getExecutorClass())) {
			return true;
		}
		// expression
		if (old.getExpression() == null) {
			if (current.getExpression() != null) {
				return true;
			}
		} else if (!old.getExpression().equals(current.getExpression())) {
			return true;
		}
		// extractSqls
		if ((old.getExtractSqls() == null && current.getExtractSqls() != null)
				|| (old.getExtractSqls() != null && current.getExtractSqls() == null)) {
			return true;
		} else if (old.getExtractSqls() != null && current.getExtractSqls() != null) {
			if (old.getExtractSqls().size() != current.getExtractSqls().size()) {
				return true;
			}
			for (int i = 0; i < old.getExtractSqls().size(); i++) {
				final ExtractSql oldEs = old.getExtractSqls().get(i);
				final ExtractSql currentEs = current.getExtractSqls().get(i);
				if (isChanged(oldEs, currentEs, false)) {
					return true;
				}
			}
		}
		// matchType
		if (old.getMatchType() != current.getMatchType()) {
			return true;
		}
		// params
		if (old.getParams() == null) {
			if (current.getParams() != null) {
				return true;
			}
		} else if (!old.getParams().equals(current.getParams())) {
			return true;
		}
		// expression segments
		if (old.getExpressionSegments() == null) {
			if (current.getExpressionSegments() != null) {
				return true;
			}
		} else if (!old.getExpressionSegments().equals(current.getExpressionSegments())) {
			return true;
		}
		return false;
	}

	/**
	 * is the ruleSet changed?
	 * 
	 * @param old
	 *            the old ruleSet
	 * @param current
	 *            the current ruleSet
	 * @return true: changed
	 */
	public static boolean isChanged(final RuleSet old, final RuleSet current) {
		if (old == null && current == null) {
			return false;
		}
		if ((old == null && current != null) || (old != null && current == null)) {
			return true;
		}
		// code
		if (old.getCode() == null) {
			if (current.getCode() != null) {
				return true;
			}
		} else if (!old.getCode().equals(current.getCode())) {
			return true;
		}
		// name
		if (old.getName() == null) {
			if (current.getName() != null) {
				return true;
			}
		} else if (!old.getName().equals(current.getName())) {
			return true;
		}
		// comment
		if (old.getComment() == null) {
			if (current.getComment() != null) {
				return true;
			}
		} else if (!old.getComment().equals(current.getComment())) {
			return true;
		}
		// bizTypeCode
		if (old.getBizTypeCode() == null) {
			if (current.getBizTypeCode() != null) {
				return true;
			}
		} else if (!old.getBizTypeCode().equals(current.getBizTypeCode())) {
			return true;
		}
		// rules
		if ((old.getRules() == null && current.getRules() != null)
				|| (old.getRules() != null && current.getRules() == null)) {
			return true;
		} else if (old.getRules() != null && current.getRules() != null) {
			if (old.getRules().size() != current.getRules().size()) {
				return true;
			}
			for (int i = 0; i < old.getRules().size(); i++) {
				final Rule oldRule = old.getRules().get(i);
				final Rule currentRule = current.getRules().get(i);
				if (!oldRule.getCode().equals(currentRule.getCode())
						|| !oldRule.getBizTypeCode().equals(currentRule.getBizTypeCode())
						|| oldRule.getVersion() != currentRule.getVersion()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * is the extractSql changed?
	 * 
	 * @param old
	 *            the old extractSql
	 * @param current
	 *            the current extractSql
	 * @param fullCompare
	 *            fully compare?
	 * @return true: changed
	 */
	public static boolean isChanged(final ExtractSql old, final ExtractSql current, final boolean fullCompare) {
		if (old == null && current == null) {
			return false;
		}
		if ((old == null && current != null) || (old != null && current == null)) {
			return true;
		}
		// datasourceCode
		if (old.getDatasourceCode() == null) {
			if (current.getDatasourceCode() != null) {
				return true;
			}
		} else if (!old.getDatasourceCode().equals(current.getDatasourceCode())) {
			return true;
		}
		// params
		if (old.getParams() == null) {
			if (current.getParams() != null) {
				return true;
			}
		} else if (!old.getParams().equals(current.getParams())) {
			return true;
		}
		// sql
		if (old.getSql() == null) {
			if (current.getSql() != null) {
				return true;
			}
		} else if (!old.getSql().equals(current.getSql())) {
			return true;
		}
		// tableName
		if (old.getTableName() == null) {
			if (current.getTableName() != null) {
				return true;
			}
		} else if (!old.getTableName().equals(current.getTableName())) {
			return true;
		}
		// columns
		if (old.getColumns() == null) {
			if (current.getColumns() != null) {
				return true;
			}
		} else if (!old.getColumns().equals(current.getColumns())) {
			return true;
		}
		// conditions
		if (old.getConditions() == null) {
			if (current.getConditions() != null) {
				return true;
			}
		} else if (!old.getConditions().equals(current.getConditions())) {
			return true;
		}

		if (!fullCompare) {
			return false;
		}
		// ruleCode
		if (old.getRuleCode() == null) {
			if (current.getRuleCode() != null) {
				return true;
			}
		} else if (!old.getRuleCode().equals(current.getRuleCode())) {
			return true;
		}
		return false;
	}

}
