/**
 * 
 */
package cn.rongcapital.ruleengine.service;

import java.util.List;

/**
 * the python executor
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface PythonExecutor {

	/**
	 * to execute the python command
	 * 
	 * @param command
	 *            the python command
	 * @param args
	 *            the arguments
	 * @return the execution results
	 */
	List<String> execute(String command, String[] args);

}
