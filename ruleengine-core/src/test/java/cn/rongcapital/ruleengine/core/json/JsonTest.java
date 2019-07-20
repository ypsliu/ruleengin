/**
 * 
 */
package cn.rongcapital.ruleengine.core.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author shangchunming@rongcapital.cn
 *
 */
public class JsonTest {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		// mapper
		final ObjectMapper jsonMapper = new ObjectMapper();

		// bean1
		final BaseBean b1 = new SubBean1();
		((SubBean1) b1).setValue11("value-11");
		((SubBean1) b1).setValue12("value-12");

		// bean2
		final BaseBean b2 = new SubBean2();
		((SubBean2) b2).setValue21("value-21");
		((SubBean2) b2).setValue22("value-22");

		// serialize
		final String json1 = jsonMapper.writeValueAsString(b1);
		final String json2 = jsonMapper.writeValueAsString(b2);
		System.out.println("b1: " + json1);
		System.out.println("b2: " + json2);
		System.out.println("============================");

		// deserialize
		final SubBean1 b11 = jsonMapper.readValue(json1, SubBean1.class);
		final SubBean2 b21 = jsonMapper.readValue(json2, SubBean2.class);
		System.out.println("b1: " + b11);
		System.out.println("b2: " + b21);
		System.out.println("============================");

		// super class
		final BaseBean b12 = jsonMapper.readValue(json1, BaseBean.class);
		final BaseBean b22 = jsonMapper.readValue(json2, BaseBean.class);
		System.out.println("b1: " + b12);
		System.out.println("b2: " + b22);
		System.out.println("============================");
		
		SampleParamBean spb = new SampleParamBean();
		spb.setApplyType("it");
		spb.setContact("小明");
		spb.setValidContacts("小明|小红");
		spb.setInst("10065");
		spb.setName("小王");
		spb.setCertificateNumber("130406198201220344");
		spb.setCellphone("13932086660");
		spb.setBankAccount("6214850100169082");
		spb.setNameRef("小王");
		spb.setCertificateNumberRef("130406198201220344");
		spb.setCellphoneRef("13932086660");
		spb.setBankAccountRef("6214850100169082");
		spb.setContacts("小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小李|18611976866;小红|18611976866");
		spb.setFirstContact("小明");
		spb.setFirstContactRef("小明");
		spb.setFirstCellphone("13932095588");
		spb.setFirstCellphoneRef("13932095588");
		
		final String spbStr = jsonMapper.writeValueAsString(spb);
		System.out.println("spbStr: " + spbStr);
		
		final SampleParamBean spbBean = jsonMapper.readValue(spbStr, SampleParamBean.class);
		System.out.println("spbStr: " + spbBean);
		
		
	}

}
