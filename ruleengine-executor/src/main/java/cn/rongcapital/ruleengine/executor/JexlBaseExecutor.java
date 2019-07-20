/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.utils.DatetimeProvider;

/**
 * JEXL的执行超类
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public abstract class JexlBaseExecutor {

	/**
	 * the context key of the runtime execution plugins
	 */
	public static final String PLUGINS_KEY = "_plugins";

	/**
	 * logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * the JEXL engine
	 */
	protected static final JexlEngine jexl = new JexlBuilder().create();

	/**
	 * the DatetimeProvider
	 */
	private DatetimeProvider datetimeProvider;

	/**
	 * the plugins
	 */
	private final Map<String, ExecutionPlugin> plugins = new HashMap<String, ExecutionPlugin>();

	/**
	 * the execution tracer
	 */
	protected ExecutionTracer executionTracer;

	/**
	 * the rule caller
	 */
	private RuleCaller ruleCaller;

	/**
	 * to execute the script
	 * 
	 * @param script
	 *            the script to execute
	 * @param params
	 *            the parameters map
	 * @return the result
	 */
	protected final Object execute(final String script, final Map<String, String> params) {
		// context
		final JexlContext context = new MapContext();
		// set the plugins
		context.set(PLUGINS_KEY, plugins);
		// set rule caller
		context.set(RuleCaller.CONTEXT_KEY, this.ruleCaller);
		// set the tracer
		context.set(ExecutionTracer.CONTEXT_KEY, this.executionTracer);
		// set the parameters
		if (params != null) {
			for (final String key : params.keySet()) {
				context.set(key, params.get(key));
			}
		}
		// execute
		return jexl.createScript(script).execute(context);
	}

	/**
	 * to build the JEXL script
	 * 
	 * @param expression
	 *            the JEXL expression with Segments
	 * @param segments
	 *            the Segments values
	 * @return the script
	 */
	protected final String buildScript(final String expression, final Map<String, String> segments) {
		String expr = expression;
		if (segments != null) {
			for (final String key : segments.keySet()) {
				final String value = segments.get(key);
				if (value != null) {
					expr = expr.replaceAll("\\$\\{" + key + "\\}", value);
				} else {
					expr = expr.replaceAll("\\$\\{" + key + "\\}", "");
				}
			}
		}
		return expr;
	}

	/**
	 * to get the now time
	 * 
	 * @return the time
	 */
	protected final Date getNowTime() {
		if (this.datetimeProvider != null) {
			return this.datetimeProvider.nowTime();
		}
		return new Date();
	}

	/**
	 * @param datetimeProvider
	 *            the datetimeProvider to set
	 */
	public final void setDatetimeProvider(final DatetimeProvider datetimeProvider) {
		this.datetimeProvider = datetimeProvider;
	}

	/**
	 * to set the plugins
	 * 
	 * @param plugins
	 *            the plugins
	 */
	public final void setPlugins(final List<ExecutionPlugin> plugins) {
		if (plugins != null) {
			this.plugins.clear();
			for (final ExecutionPlugin plugin : plugins) {
				if (plugin.pluginName() != null) {
					this.plugins.put(plugin.pluginName(), plugin);
				}
			}
		}
	}

	/**
	 * @param executionTracer
	 *            the executionTracer to set
	 */
	public final void setExecutionTracer(final ExecutionTracer executionTracer) {
		this.executionTracer = executionTracer;
	}

	/**
	 * @param ruleCaller
	 *            the ruleCaller to set
	 */
	public final void setRuleCaller(final RuleCaller ruleCaller) {
		this.ruleCaller = ruleCaller;
	}

}
