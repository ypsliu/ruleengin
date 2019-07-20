/**
 * 
 */
package cn.rongcapital.ruleengine.rop;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * ROP的返回信息的超类
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({ @JsonSubTypes.Type(value = RopErrorResponse.class),
		@JsonSubTypes.Type(value = RopMatchRequestResponse.class),
		@JsonSubTypes.Type(value = RopMatchResultResponse.class),
		@JsonSubTypes.Type(value = RopReferenceDataResponse.class),
		@JsonSubTypes.Type(value = TradeDataQueryResponse.class) })
public abstract class RopResponse {

}
