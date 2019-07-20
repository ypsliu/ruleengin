/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import java.util.List;
import java.util.Map;

import cn.rongcapital.ruleengine.model.MatchStage;
import cn.rongcapital.ruleengine.model.Rule;

/**
 * the execution tracer
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ExecutionTracer {

	/**
	 * the context key
	 */
	String CONTEXT_KEY = "tracer";

	/**
	 * to set the parameters
	 * 
	 * @param params
	 *            the parameters
	 */
	void setParams(Map<String, String> params);

	/**
	 * to get the parameters
	 * 
	 * @return the parameters
	 */
	Map<String, String> getParams();

	/**
	 * to get the execution id
	 * 
	 * @return the id
	 */
	String getExecutionId();

	/**
	 * to save the trace message
	 * 
	 * @param message
	 *            the message
	 */
	void trace(String message);

	/**
	 * to set the current stage result
	 * 
	 * @param result
	 *            the result
	 */
	void setResult(String result);

	/**
	 * to set the current stage error message
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	void setErrorMessage(String errorMessage);

	/**
	 * to begin a new stage
	 * 
	 * @param rule
	 *            the rule
	 */
	void beginStage(Rule rule);

	/**
	 * to end the current stage
	 */
	void endStage();

	/**
	 * to get the current stage
	 * 
	 * @return the current stage
	 */
	MatchStage currentStage();

	/**
	 * to get all stages
	 * 
	 * @return the stages
	 */
	List<MatchStage> getStages();

}
