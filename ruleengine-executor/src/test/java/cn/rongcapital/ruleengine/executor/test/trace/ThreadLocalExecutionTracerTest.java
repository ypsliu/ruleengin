/**
 * 
 */
package cn.rongcapital.ruleengine.executor.test.trace;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cn.rongcapital.ruleengine.executor.ExecutionIdGenerator;
import cn.rongcapital.ruleengine.executor.trace.ThreadLocalExecutionTracer;
import cn.rongcapital.ruleengine.model.MatchStage;

/**
 * the unit test for ThreadLocalExecutionTracer
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public class ThreadLocalExecutionTracerTest {

	@Test
	public void testInSingleThread() {
		final String executionId = "test-execution-id";

		// mock ExecutionIdGenerator
		final ExecutionIdGenerator executionIdGenerator = Mockito.mock(ExecutionIdGenerator.class);
		Mockito.when(executionIdGenerator.generate()).thenReturn(executionId);

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer t = new ThreadLocalExecutionTracer();
		t.setExecutionIdGenerator(executionIdGenerator);

		this.test(executionId, t);
		this.test(executionId, t);

		System.out.println("testInSingleThread passed");
	}

	@Test
	public void testInMultiThreads() {
		// execution ids
		final AtomicInteger executionId = new AtomicInteger(1);

		// mock ExecutionIdGenerator
		final ExecutionIdGenerator executionIdGenerator = Mockito.mock(ExecutionIdGenerator.class);
		Mockito.when(executionIdGenerator.generate()).thenAnswer(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return "exection-" + executionId.getAndIncrement();
			}

		});

		// ThreadLocalExecutionTracer
		final ThreadLocalExecutionTracer t = new ThreadLocalExecutionTracer();
		t.setExecutionIdGenerator(executionIdGenerator);

		final int threadCount = 100;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; i++) {
			new Thread() {

				public void run() {
					try {
						final String executionId = t.getExecutionId();
						ThreadLocalExecutionTracerTest.this.test(executionId, t);
					} finally {
						latch.countDown();
					}
				}

			}.start();
		}

		// wait for the latch
		try {
			if (latch.await(10, TimeUnit.SECONDS)) {
				System.out.println("testInMultiThreads passed");
			} else {
				System.out.println("testInMultiThreads wait latch timeout");
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void test(final String executionId, final ThreadLocalExecutionTracer t) {
		// stage-1
		t.beginStage(null);
		t.trace("1-1");
		t.trace("1-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(1, t.currentStage().getStageId());
		Assert.assertEquals(0, t.currentStage().getParentStageId());

		// stage-2
		t.beginStage(null);
		t.trace("2-1");
		t.trace("2-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(2, t.currentStage().getStageId());
		Assert.assertEquals(1, t.currentStage().getParentStageId());

		// stage-3
		t.beginStage(null);
		t.trace("3-1");
		t.trace("3-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(3, t.currentStage().getStageId());
		Assert.assertEquals(2, t.currentStage().getParentStageId());
		// end stage-3, back to stage-2
		t.endStage();
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(2, t.currentStage().getStageId());
		Assert.assertEquals(1, t.currentStage().getParentStageId());
		// end stage-2, back to stage-1
		t.endStage();
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(1, t.currentStage().getStageId());
		Assert.assertEquals(0, t.currentStage().getParentStageId());

		// stage-4
		t.beginStage(null);
		t.trace("4-1");
		t.trace("4-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(4, t.currentStage().getStageId());
		Assert.assertEquals(1, t.currentStage().getParentStageId());

		// stage-5
		t.beginStage(null);
		t.trace("5-1");
		t.trace("5-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(5, t.currentStage().getStageId());
		Assert.assertEquals(4, t.currentStage().getParentStageId());

		// stage-6
		t.beginStage(null);
		t.trace("6-1");
		t.trace("6-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(6, t.currentStage().getStageId());
		Assert.assertEquals(5, t.currentStage().getParentStageId());
		// end stage-6, back to stage-5
		t.endStage();
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(5, t.currentStage().getStageId());
		Assert.assertEquals(4, t.currentStage().getParentStageId());
		// end stage-5, back to stage-4
		t.endStage();
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(4, t.currentStage().getStageId());
		Assert.assertEquals(1, t.currentStage().getParentStageId());

		// end stage-4, back to stage-1
		t.endStage();
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(1, t.currentStage().getStageId());
		Assert.assertEquals(0, t.currentStage().getParentStageId());

		// stage-7
		t.beginStage(null);
		t.trace("7-1");
		t.trace("7-2");
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(7, t.currentStage().getStageId());
		Assert.assertEquals(1, t.currentStage().getParentStageId());
		// end stage-7, back to stage-1
		t.endStage();
		Assert.assertEquals(executionId, t.getExecutionId());
		Assert.assertEquals(1, t.currentStage().getStageId());
		Assert.assertEquals(0, t.currentStage().getParentStageId());

		// get values before end stage-1
		final String executionId1 = t.getExecutionId();
		final List<MatchStage> stages = t.getStages();

		// end stage-1
		t.endStage();

		MatchStage stage = null;

		// check
		Assert.assertEquals(executionId1, executionId);

		// check stages
		Assert.assertNotNull(stages);
		Assert.assertEquals(7, stages.size());

		// check stage-1
		stage = stages.get(0);
		Assert.assertEquals(1, stage.getStageId());
		Assert.assertEquals(0, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("1-1", stage.getTraces().get(0));
		Assert.assertEquals("1-2", stage.getTraces().get(1));

		// check stage-2
		stage = stages.get(1);
		Assert.assertEquals(2, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("2-1", stage.getTraces().get(0));
		Assert.assertEquals("2-2", stage.getTraces().get(1));

		// check stage-3
		stage = stages.get(2);
		Assert.assertEquals(3, stage.getStageId());
		Assert.assertEquals(2, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("3-1", stage.getTraces().get(0));
		Assert.assertEquals("3-2", stage.getTraces().get(1));

		// check stage-4
		stage = stages.get(3);
		Assert.assertEquals(4, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("4-1", stage.getTraces().get(0));
		Assert.assertEquals("4-2", stage.getTraces().get(1));

		// check stage-5
		stage = stages.get(4);
		Assert.assertEquals(5, stage.getStageId());
		Assert.assertEquals(4, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("5-1", stage.getTraces().get(0));
		Assert.assertEquals("5-2", stage.getTraces().get(1));

		// check stage-6
		stage = stages.get(5);
		Assert.assertEquals(6, stage.getStageId());
		Assert.assertEquals(5, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("6-1", stage.getTraces().get(0));
		Assert.assertEquals("6-2", stage.getTraces().get(1));

		// check stage-7
		stage = stages.get(6);
		Assert.assertEquals(7, stage.getStageId());
		Assert.assertEquals(1, stage.getParentStageId());
		Assert.assertNotNull(stage.getTraces());
		Assert.assertEquals(2, stage.getTraces().size());
		Assert.assertEquals("7-1", stage.getTraces().get(0));
		Assert.assertEquals("7-2", stage.getTraces().get(1));
	}

}
