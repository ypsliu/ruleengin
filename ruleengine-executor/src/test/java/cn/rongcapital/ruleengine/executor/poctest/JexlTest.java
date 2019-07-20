/**
 * 
 */
package cn.rongcapital.ruleengine.executor.poctest;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import cn.rongcapital.ruleengine.executor.ExecutionPlugin;
import cn.rongcapital.ruleengine.executor.JexlBaseExecutor;
import cn.rongcapital.ruleengine.executor.plugins.IdCardNumberValidator;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class JexlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JexlEngine jexl = new JexlBuilder().create();
		final JexlContext context = new MapContext();
		final Map<String, ExecutionPlugin> plugins = new HashMap<String, ExecutionPlugin>();
		final ExecutionPlugin p = new IdCardNumberValidator();
		plugins.put(p.pluginName(), p);
		context.set(JexlBaseExecutor.PLUGINS_KEY, plugins);
		String script = JexlBaseExecutor.PLUGINS_KEY + ".IdCardNumberValidator.exec(\"123456199909091234\")";
		System.out.println(jexl.createScript(script).execute(context));
		script = "var sum = 0; var i = 1; while (i <= 100) { sum += i; i += 1; } return sum;";
		System.out.println(jexl.createScript(script).execute(context));
	}

}
