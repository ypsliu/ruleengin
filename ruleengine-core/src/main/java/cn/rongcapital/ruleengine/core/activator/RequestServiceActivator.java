package cn.rongcapital.ruleengine.core.activator;

import cn.rongcapital.ruleengine.model.MatchRequestData;
import cn.rongcapital.ruleengine.model.MatchRequestMethod;
import cn.rongcapital.ruleengine.service.MatchService;
import cn.rongcapital.ruleengine.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.io.IOException;

/**
 * Created by lilupeng on 16/11/4.
 *
 */
public class RequestServiceActivator {

    @Autowired
    private MatchService matchService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceActivator.class);

    /**
     *
     * @param message
     */
    public void matchRequestDataActivator(Message<String> message) {
        try {
            MatchRequestData matchRequestData = JsonHelper.toObject(message.getPayload(), MatchRequestData.class);
            MatchRequestMethod matchRequestMethod = matchRequestData.getMatchRequestMethod();
            switch (matchRequestMethod) {
                case REQUEST:
                    matchService.match(matchRequestData);
                    break;
                case BIZTYPECODE_BIZCODE:
                    matchService.match(matchRequestData.getBizTypeCode(), matchRequestData.getBizCode());
                    break;
                case BIZTYPECODE_BIZCODE_RULESETCODE_RULESETVERSION:
                    matchService.match(matchRequestData.getBizTypeCode(), matchRequestData.getBizCode(), matchRequestData.getRuleSetCode(), matchRequestData.getRuleSetVersion());
                    break;
            }
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from json to MatchRequestData, message: {}", message);
        }
    }

    /**
     * @param matchService
     *            the matchService to set
     */
    public void setMatchService(final MatchService matchService) {
        this.matchService = matchService;
    }
}
