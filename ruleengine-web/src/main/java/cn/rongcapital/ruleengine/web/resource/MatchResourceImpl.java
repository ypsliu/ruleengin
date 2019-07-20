/**
 * 
 */
package cn.rongcapital.ruleengine.web.resource;

import cn.rongcapital.ruleengine.api.MatchResource;
import cn.rongcapital.ruleengine.model.EvalRequest;
import cn.rongcapital.ruleengine.model.EvalResult;
import cn.rongcapital.ruleengine.model.MatchRequestData;
import cn.rongcapital.ruleengine.model.MatchRequestMethod;
import cn.rongcapital.ruleengine.service.MatchService;
import cn.rongcapital.ruleengine.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.UUID;

/**
 * the implementation for MatchResource
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@Controller
public final class MatchResourceImpl implements MatchResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(MatchResourceImpl.class);

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private MatchService matchService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    // failed to autowire MessageChannel requestGateway(int:chain),
    // believe it is spring's bug in 4.3.3 but fixed in 5.0.0.SNAPSHOT
    //@Autowired
    //@Qualifier("requestGateway")
    //private Message requestGateway;

	/*
	 * (non-Javadoc)
	 *
	 * @see cn.rongcapital.ruleengine.api.MatchResource#match(cn.rongcapital.ruleengine.model.MatchRequestData)
	 */
    public void match(final MatchRequestData request) {
        //this.matchService.match(request);
        MessageChannel requestGateway = context.getBean("requestGateway", MessageChannel.class);
        try {
            Message<String> message = MessageBuilder.withPayload(JsonHelper.toJsonString(request))
                    .setHeader("ruleengine_match_resource_redis_queue", "matchRequestData")
                    .build();
            requestGateway.send(message);
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from MatchRequestData to json, MatchRequestData: {}", request);
        }
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.MatchResource#match(java.lang.String, java.lang.String)
	 */
	@Override
	public void match(final String bizTypeCode, final String bizCode) {
        //this.matchService.match(bizTypeCode, bizCode);
        MatchRequestData request = new MatchRequestData();
        request.setMatchRequestMethod(MatchRequestMethod.BIZTYPECODE_BIZCODE);
        request.setBizTypeCode(bizTypeCode);
        request.setBizCode(bizCode);
        match(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.MatchResource#match(java.lang.String, java.lang.String, java.lang.String,
	 * long)
	 */
	@Override
	public void match(final String bizTypeCode, final String bizCode, final String ruleSetCode, final long ruleSetVersion) {
		//this.matchService.match(bizTypeCode, bizCode, ruleSetCode, ruleSetVersion);
        MatchRequestData request = new MatchRequestData();
        request.setMatchRequestMethod(MatchRequestMethod.BIZTYPECODE_BIZCODE_RULESETCODE_RULESETVERSION);
        request.setBizTypeCode(bizTypeCode);
        request.setBizCode(bizCode);
        request.setRuleSetCode(ruleSetCode);
        request.setRuleSetVersion(ruleSetVersion);
        match(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.rongcapital.ruleengine.api.MatchResource#evaluate(cn.rongcapital.ruleengine.model.EvalRequest)
	 */
	@Override
	public EvalResult evaluate(final EvalRequest request) {
        EvalResult returnVal = null;
        try {
            // use uuid as queue name
            UUID uuid = UUID.randomUUID();
            Message<String> message = MessageBuilder.withPayload(JsonHelper.toJsonString(request)).build();

            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
            redisTemplate.setConnectionFactory(this.connectionFactory);
            redisTemplate.setEnableDefaultSerializer(false);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.afterPropertiesSet();
            redisTemplate.boundListOps(String.valueOf(uuid)).leftPush(message);

            PollableChannel outputChannel = new QueueChannel();
            PollableChannel errorChannel = new QueueChannel();
            RedisQueueMessageDrivenEndpoint endpoint = new RedisQueueMessageDrivenEndpoint(String.valueOf(uuid), this.connectionFactory);
            endpoint.setBeanFactory(this.beanFactory);
            endpoint.setTaskExecutor(taskExecutor);
            endpoint.setErrorChannel(errorChannel);
            endpoint.setOutputChannel(outputChannel);
            endpoint.setReceiveTimeout(1000);
            endpoint.setExpectMessage(true);
            endpoint.afterPropertiesSet();
            endpoint.start();

            Message<Object> receive = (Message<Object>) outputChannel.receive(1000);
            returnVal = matchService.evaluate(JsonHelper.toObject((String)receive.getPayload(), EvalRequest.class));
        } catch (IOException e) {
            LOGGER.error("failed to parse request message from EvalRequest to json, EvalRequest: {}", request);
        }

        return returnVal;
	}
}
