/**
 * 
 */
package cn.rongcapital.ruleengine.executor.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.rongcapital.ruleengine.executor.ExecutionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.executor.ExecutionTracer;
import cn.rongcapital.ruleengine.model.MatchStage;
import cn.rongcapital.ruleengine.model.Rule;

/**
 * the thread local implementation for ExecutionTracer
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class ThreadLocalExecutionTracer implements ExecutionTracer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalExecutionTracer.class);

	private final ThreadLocal<ExecutionStatus> executionStatus = new ThreadLocal<ExecutionStatus>() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		public final ExecutionStatus initialValue() {
			final ExecutionStatus es = new ExecutionStatus(
					ThreadLocalExecutionTracer.this.executionIdGenerator.generate());
			LOGGER.info("new execution status created: {}", es);
			return es;
		}

	};

	private ExecutionIdGenerator executionIdGenerator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#setParams(java.util.Map)
	 */
	@Override
	public void setParams(final Map<String, String> params) {
		this.executionStatus.get().params = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#getParams()
	 */
	@Override
	public Map<String, String> getParams() {
		return this.executionStatus.get().params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#getExecutionId()
	 */
	@Override
	public String getExecutionId() {
		return this.executionStatus.get().executionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#trace(java.lang.String)
	 */
	@Override
	public void trace(final String message) {
		// current stags
		final MatchStage currentStage = this.currentStage();
		if (currentStage == null) {
			throw new RuntimeException("the current stage not found");
		}
		if (currentStage.getTraces() == null) {
			currentStage.setTraces(new ArrayList<String>());
		}
		currentStage.getTraces().add(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#setResult(java.lang.String)
	 */
	@Override
	public void setResult(final String result) {
		// current stags
		final MatchStage currentStage = this.currentStage();
		if (currentStage == null) {
			throw new RuntimeException("the current stage not found");
		}
		currentStage.setResult(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#setErrorMessage(java.lang.String)
	 */
	@Override
	public void setErrorMessage(final String errorMessage) {
		// current stags
		final MatchStage currentStage = this.currentStage();
		if (currentStage == null) {
			throw new RuntimeException("the current stage not found");
		}
		currentStage.setErrorMessage(errorMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#beginStage(Rule)
	 */
	@Override
	public void beginStage(final Rule rule) {
		// current status
		final ExecutionStatus status = this.executionStatus.get();
		// the parent stage id is current stage id
		final int parentStageId = status.currentStageId;
		// create a new stage
		final MatchStage stage = new MatchStage();
		stage.setBeginAt(System.currentTimeMillis()); // begin at
		stage.setRule(rule);
		stage.setStageId(status.nextStageId()); // next stage id
		stage.setParentStageId(parentStageId);
		// current stage id in status
		status.currentStageId = stage.getStageId();
		// add to stages
		status.stages.add(stage);
		LOGGER.debug("new stage begun: {}", stage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#endStage()
	 */
	@Override
	public void endStage() {
		// current status
		final ExecutionStatus status = this.executionStatus.get();
		// current stags
		final MatchStage currentStage = this.currentStage();
		if (currentStage == null) {
			throw new RuntimeException("the current stage not found");
		}
		// time
		currentStage.setTimeInMs((int) (System.currentTimeMillis() - currentStage.getBeginAt()));
		// set the current to the parent id
		status.currentStageId = currentStage.getParentStageId();
		LOGGER.debug("the stage ended: {}, back to: {}", currentStage, this.currentStage());
		if (status.currentStageId == 0) {
			// all stage is done, remove it
			LOGGER.debug("all stage is done, remove it");
			this.executionStatus.remove();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#currentStage()
	 */
	@Override
	public MatchStage currentStage() {
		return this.executionStatus.get().currentStage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ExecutionTracer#getStages()
	 */
	@Override
	public List<MatchStage> getStages() {
		return this.executionStatus.get().stages;
	}

	/**
	 * @param executionIdGenerator
	 *            the executionIdGenerator to set
	 */
	public void setExecutionIdGenerator(final ExecutionIdGenerator executionIdGenerator) {
		this.executionIdGenerator = executionIdGenerator;
	}

	private class ExecutionStatus {

		public final String executionId;

		public final List<MatchStage> stages = new ArrayList<MatchStage>();

		public int currentStageId = 0;

		private int nextStageId = 0;

		public Map<String, String> params;

		public ExecutionStatus(final String executionId) {
			this.executionId = executionId;
		}

		public int nextStageId() {
			return ++this.nextStageId;
		}

		/**
		 * to get the current stage
		 * 
		 * @return the stage
		 */
		public MatchStage currentStage() {
			for (final MatchStage stage : this.stages) {
				if (stage.getStageId() == this.currentStageId) {
					return stage;
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ExecutionStatus [executionId=" + executionId + ", stages=" + stages + ", currentStageId="
					+ currentStageId + "]";
		}

	}

}
