/**
 * 
 */
package cn.rongcapital.ruleengine.executor;

import java.util.UUID;

/**
 * the default implementation for ExecutionIdGenerator
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class DefaultExecutionIdGenerator implements ExecutionIdGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionIdGenerator#generate()
	 */
	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}

}
