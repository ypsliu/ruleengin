/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.model.ui.ReturnType;
import cn.rongcapital.ruleengine.model.ui.RuleCondition;
import cn.rongcapital.ruleengine.model.ui.RuleDesign;
import cn.rongcapital.ruleengine.model.ui.RuleSegment;
import cn.rongcapital.ruleengine.model.ui.RuleStep;
import cn.rongcapital.ruleengine.model.ui.RuleVar;
import cn.rongcapital.ruleengine.service.RuleExpressionBuilder;

/**
 * the JEXL implementation for RuleExpressionBuilder
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class JexlRuleExpressionBuilder implements RuleExpressionBuilder {

	private final static Logger LOGGER = LoggerFactory.getLogger(JexlRuleExpressionBuilder.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * RuleExpressionBuilder#build(RuleDesign
	 * )
	 */
	@Override
	public String build(final RuleDesign design) {
		// check
		if (design == null) {
			return null;
		}
		final StringBuilder buf = new StringBuilder();
		// result
		buf.append("var _result_ = '';\n");
		// step-1: process the vars
		if (design.getVars() != null) {
			for (final RuleVar var : design.getVars()) {
				buf.append("var ").append(var.getName()).append(" = ");
				switch (var.getType()) {
				case LET: {
					if (this.isSegment(var.getParams(), design.getSegments())) {
						// it is the segment
						buf.append(this.getSegmentKeyName(var.getParams()));
					} else {
						buf.append(var.getParams());
					}
					break;
				}
				case PLUGINS: {
					// invoke the plugins
					final String[] ps = var.getParams().split("\\,", -1);
					if (ps == null || ps.length == 0) {
						LOGGER.error("invalid PLUGINS var params, name: {}, type: {}, params: {}", var.getName(),
								var.getType(), var.getParams());
						throw new RuntimeException("invalid PLUGINS var params, build the expression failed");
					}
					buf.append(JexlBaseExecutor.PLUGINS_KEY).append(".").append(ps[0]).append(".exec(");
					if (ps.length > 1) {
						for (int i = 1; i < ps.length; i++) {
							if (i > 1) {
								buf.append(", ");
							}
							if (this.isSegment(ps[i], design.getSegments())) {
								// it is the segment
								buf.append(this.getSegmentKeyName(ps[i]));
							} else {
								buf.append(ps[i]);
							}
						}
					}
					buf.append(")");
					break;
				}
				case RULES: {
					// invoke other rule
					final String[] ps = var.getParams().split("\\,", 2);
					if (ps == null || ps.length != 2) {
						LOGGER.error("invalid RULES var params, name: {}, type: {}, params: {}", var.getName(),
								var.getType(), var.getParams());
						throw new RuntimeException("invalid RULES var params, build the expression failed");
					}
					buf.append(RuleCaller.CONTEXT_KEY).append(".call('").append(ps[0]).append("', ").append(ps[1])
							.append(")");
					break;
				}

				}
				buf.append(";\n");
			}
		}
		// step-2: process the steps
		ReturnType lastReturnType = null;
		for (final RuleStep step : design.getSteps()) {
			// comment
			if (step.getComment() != null && !step.getComment().isEmpty()) {
				buf.append("/* ").append(step.getComment()).append(" */\n");
			}
			// conditions
			if (ReturnType.NEXT_STEP == lastReturnType) {
				buf.append("else if (");
			} else {
				buf.append("if (");
			}
			for (int i = 0, size = step.getConditions().size(); i < size; i++) {
				final RuleCondition c = step.getConditions().get(i);
				if (i > 0) {
					buf.append(" ").append(step.getRelType().getCode()).append(" ");
				}
				buf.append(c.getVarName()).append(" ").append(c.getOperator().getCode()).append(" ");
				if (this.isSegment(c.getValue(), design.getSegments())) {
					buf.append(this.getSegmentKeyName(c.getValue()));
				} else {
					buf.append(c.getValue());
				}
			}
			buf.append(") {\n");
			// true
			if (step.getTrueResult().getTrace() != null) {
				// true.trace
				buf.append("\ttracer.trace('").append(step.getTrueResult().getTrace()).append("');\n");
				// true.result
				buf.append("\t_result_ = '");
				if (this.isSegment(step.getTrueResult().getResult(), design.getSegments())) {
					buf.append(this.getSegmentKeyName(step.getTrueResult().getResult()));
				} else {
					buf.append(step.getTrueResult().getResult());
				}
				buf.append("';\n");
			}
			// the last return type from false
			lastReturnType = step.getFalseResult().getReturnType();
			if (ReturnType.RESULT == lastReturnType) {
				// return result
				buf.append("} else {\n");
				// false
				if (step.getFalseResult().getTrace() != null) {
					// true.trace
					buf.append("\ttracer.trace('").append(step.getFalseResult().getTrace()).append("');\n");
					// true.result
					buf.append("\t_result_ = '");
					if (this.isSegment(step.getFalseResult().getResult(), design.getSegments())) {
						buf.append(this.getSegmentKeyName(step.getFalseResult().getResult()));
					} else {
						buf.append(step.getFalseResult().getResult());
					}
					buf.append("';\n");
				}
			}
			buf.append("}\n");
		}
		// step-3: result
		buf.append("return _result_;");
		return buf.toString();
	}

	private boolean isSegment(final String varName, final List<RuleSegment> segments) {
		String name = varName;
		if (name != null && name.startsWith("'") && name.endsWith("'")) {
			name = name.substring(1, name.length() - 1);
		}
		if (name != null && segments != null) {
			for (final RuleSegment rs : segments) {
				if (name.equals(rs.getKey())) {
					return true;
				}
			}
		}
		return false;
	}

	private String getSegmentKeyName(final String keyName) {
		if (keyName != null) {
			if (keyName.startsWith("'") && keyName.endsWith("'")) {
				return "'${" + keyName.substring(1, keyName.length() - 1) + "}'";
			} else {
				return "${" + keyName + "}";
			}
		}
		return keyName;
	}

}
