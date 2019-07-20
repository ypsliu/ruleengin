package cn.rongcapital.ruleengine.core.activator;

import cn.rongcapital.ruleengine.model.ResultPojo;
import cn.rongcapital.ruleengine.model.RuleEntity;
import cn.rongcapital.ruleengine.service.ResultService;
import cn.rongcapital.ruleengine.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lilupeng on 16/11/5.
 *
 */
public class PersistenceServiceActivator {

    @Value("${si.taskExecutor.corePoolSize}")
    private String corePoolSize;

    @Value("${si.db.persistence.rate}")
    private String rateHitBatchSave;

    @Value("${si.db.persistence.hit.size}")
    private String listSizeHitBatchSave;

    @Autowired
    private ResultService resultService;

    private ReentrantLock lock = new ReentrantLock();
    private volatile LongAdder adder = new LongAdder();

    private ScheduledExecutorService executor;
    private ScheduledExecutorService singleExecutor;
    private volatile AtomicBoolean inited = new AtomicBoolean(false);
    private volatile AtomicBoolean singleInited = new AtomicBoolean(false);

    private volatile List<ResultPojo> resultsList = Collections.synchronizedList(new LinkedList<ResultPojo>());

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceServiceActivator.class);

    /**
     * @param message
     *
     */
    public void persistOrderRules(Message<String> message) {

        if (!this.singleInited.get()) {
            singleExecutor = Executors.newSingleThreadScheduledExecutor();
            singleExecutor.scheduleAtFixedRate(new Scheduler(), 0, Integer.valueOf(rateHitBatchSave), TimeUnit.MILLISECONDS);
            this.singleInited.set(true);
        }

        try {
            List<RuleEntity> ruleEntityList = Arrays.asList(JsonHelper.toObject(message.getPayload(), RuleEntity[].class));

            for (RuleEntity ruleEntity : ruleEntityList) {
                resultsList.addAll(resultService.getResult(ruleEntity.getAcceptanceResult()));
                resultsList.addAll(resultService.getResult(ruleEntity.getScoreResult()));
                resultsList.addAll(resultService.getResult(ruleEntity.getTextResult()));
                adder.increment();
            }

            if (adder.intValue() >= Integer.valueOf(listSizeHitBatchSave)) {
                persistence();
            }
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from json to RuleEntity, message: {}", message);
        }
    }

    /**
     * @param message
     *
     */
    public void persistRule(Message<String> message) {

        if (!this.inited.get()) {
            executor = Executors.newScheduledThreadPool(Integer.valueOf(corePoolSize));
            executor.scheduleAtFixedRate(new Scheduler(), 0, Integer.valueOf(rateHitBatchSave), TimeUnit.MILLISECONDS);
            this.inited.set(true);
        }

        try {
            RuleEntity ruleEntity = JsonHelper.toObject(message.getPayload(), RuleEntity.class);
            LOGGER.debug("saving the results,  acceptanceResult: {}, scoreResult: {}, textResult: {}",
                    ruleEntity.getAcceptanceResult(), ruleEntity.getScoreResult(), ruleEntity.getTextResult());

            resultsList.addAll(this.resultService.getResult(ruleEntity.getAcceptanceResult()));
            resultsList.addAll(this.resultService.getResult(ruleEntity.getScoreResult()));
            resultsList.addAll(this.resultService.getResult(ruleEntity.getTextResult()));

            adder.increment();
            if (adder.intValue() >= Integer.valueOf(listSizeHitBatchSave)) {
                persistence();
            }
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from json to RuleEntity, message: {}", message);
        }
    }

    /**
     *
     */
    private void persistence() {
        lock.lock();
        // precondition checking
        if (resultsList == null || resultsList.size() == 0) {
            lock.unlock();
            return;
        }

        List<ResultPojo> pojos = new LinkedList<ResultPojo>();
        pojos.addAll(resultsList);
        resultsList.clear();
        adder.reset();
        resultService.saveResult(pojos);
        lock.unlock();
    }

    /**
     *
     */
    class Scheduler extends TimerTask {
        @Override
        public void run() {
            persistence();
        }
    }

    /**
     *
     * @return
     */
    public String getListSizeHitBatchSave() {
        return listSizeHitBatchSave;
    }

    /**
     *
     * @param listSizeHitBatchSave
     */
    public void setListSizeHitBatchSave(String listSizeHitBatchSave) {
        this.listSizeHitBatchSave = listSizeHitBatchSave;
    }

    /**
     * @param resultService
     *            the resultService to set
     */
    public void setResultService(final ResultService resultService) {
        this.resultService = resultService;
    }
}
