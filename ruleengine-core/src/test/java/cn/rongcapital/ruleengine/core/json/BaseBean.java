/**
 * 
 */
package cn.rongcapital.ruleengine.core.json;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({ @JsonSubTypes.Type(value = SubBean1.class), @JsonSubTypes.Type(value = SubBean2.class) })
public abstract class BaseBean {

}
