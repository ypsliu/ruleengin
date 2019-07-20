package cn.rongcapital.ruleengine.core.activator;

import cn.rongcapital.ruleengine.enums.MatchType;
import cn.rongcapital.ruleengine.match.AcceptanceExecutor;
import cn.rongcapital.ruleengine.match.ScoreExecutor;
import cn.rongcapital.ruleengine.match.TextExecutor;
import cn.rongcapital.ruleengine.model.*;
import cn.rongcapital.ruleengine.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lilupeng on 16/11/5.
 *
 */
public class RuleExeServiceActivator {

    @Autowired
    private TextExecutor textExecutor;

    @Autowired
    private ScoreExecutor scoreExecutor;

    @Autowired
    private AcceptanceExecutor acceptanceExecutor;

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExeServiceActivator.class);

    /**
     *
     * @param message
     */
    public Message<String> ruleSetActivator(Message<String> message) {
        Message<String> returnVal = null;
        List<RuleEntity> ruleEntityList = new LinkedList<RuleEntity>();
        try {
            RuleSetEntity ruleSetEntity = JsonHelper.toObject(message.getPayload(), RuleSetEntity.class);

            int sortId = 1;
            for (final Rule rule : ruleSetEntity.getRuleSet().getRules()) {
                RuleEntity ruleEntity = new RuleEntity(rule, ruleSetEntity.getParams(),
                        ruleSetEntity.getAcceptanceResult(), ruleSetEntity.getScoreResult(), ruleSetEntity.getTextResult(), sortId++);

                updateResult(rule, ruleEntity.getParams(),
                        ruleEntity.getAcceptanceResult(), ruleEntity.getScoreResult(), ruleEntity.getTextResult(), ruleEntity.getSortedId());

                ruleEntityList.add(ruleEntity);
            }

            // all executed results were ready to redis queue and waiting for persistence
            returnVal = MessageBuilder.withPayload(JsonHelper.toJsonString(ruleEntityList.toArray())).build();
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from json to RuleEntity, message: {}", message);
        }

        return returnVal;
    }

    /**
     *
     * @param message
     */
    public Message<String> rulesActivator(Message<String> message) {
        Message<String> returnVal = null;
        try {
            RuleEntity ruleEntity = JsonHelper.toObject(message.getPayload(), RuleEntity.class);
            updateResult(ruleEntity.getRule(), ruleEntity.getParams(),
                    ruleEntity.getAcceptanceResult(), ruleEntity.getScoreResult(), ruleEntity.getTextResult(), ruleEntity.getSortedId());

            // all executed results were ready to redis queue and waiting for persistence
            returnVal = MessageBuilder.withPayload(JsonHelper.toJsonString(ruleEntity)).build();
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from json to RuleEntity, message: {}", message);
        }

        return returnVal;
    }

    /**
     *
     * @param rule
     * @param params
     * @param acceptanceResult
     * @param scoreResult
     * @param textResult
     * @param sortId
     */
    private void updateResult(final Rule rule, final Map<String, String> params,
                         final AcceptanceResults acceptanceResult, final ScoreResults scoreResult, final TextResults textResult, final int sortId) {

        if (MatchType.ACCEPTANCE == rule.getMatchType()) {
            // acceptance
            final AcceptanceResult ar = this.acceptanceExecutor.execute(rule, params);
            ar.setRuleVersion(rule.getVersion());
            ar.setSortId(sortId);
            acceptanceResult.getResults().add(ar);
        }
        else if (MatchType.SCORE == rule.getMatchType()) {
            // score
            final ScoreResult sr = this.scoreExecutor.execute(rule, params);
            sr.setRuleVersion(rule.getVersion());
            sr.setSortId(sortId);
            scoreResult.getResults().add(sr);
        }
        else if (MatchType.TEXT == rule.getMatchType()) {
            // score
            final TextResult tr = this.textExecutor.execute(rule, params);
            tr.setRuleVersion(rule.getVersion());
            tr.setSortId(sortId);
            textResult.getResults().add(tr);
        }
    }

    /**
     * @param acceptanceExecutor
     *            the acceptanceExecutor to set
     */
    public void setAcceptanceExecutor(final AcceptanceExecutor acceptanceExecutor) {
        this.acceptanceExecutor = acceptanceExecutor;
    }

    /**
     * @param scoreExecutor
     *            the scoreExecutor to set
     */
    public void setScoreExecutor(final ScoreExecutor scoreExecutor) {
        this.scoreExecutor = scoreExecutor;
    }

    /**
     * @param textExecutor
     *            the textExecutor to set
     */
    public void setTextExecutor(final TextExecutor textExecutor) {
        this.textExecutor = textExecutor;
    }
}
