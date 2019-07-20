/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

/**
 * the execution id generator
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public interface ExecutionIdGenerator {

	/**
	 * to generate a new execution id
	 * 
	 * @return the new id
	 */
	String generate();

}
