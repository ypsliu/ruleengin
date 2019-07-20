package cn.rongcapital.ruleengine.executor.plugins;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rongcapital.ruleengine.exception.InvalidParameterException;
import cn.rongcapital.ruleengine.executor.ExecutionPlugin;



/**
 * @author hill
 * check if contacts are valid and the valid number greater than 20
 */
public class CheckContacts implements ExecutionPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckContacts.class);
	private String pluginName = "CheckContacts";
	
	/**
	 * the validation pattern
	 */
	private String cellPhonevalidationPattern = "^((\\+86)|(86))?((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
	
	private String fixedPhonevalidationPattern = "(^(\\d{3,4})?\\d{7,8})$";


	public String getCellPhonevalidationPattern() {
		return cellPhonevalidationPattern;
	}

	public void setCellPhonevalidationPattern(String cellPhonevalidationPattern) {
		this.cellPhonevalidationPattern = cellPhonevalidationPattern;
	}

	public String getFixedPhonevalidationPattern() {
		return fixedPhonevalidationPattern;
	}

	public void setFixedPhonevalidationPattern(String fixedPhonevalidationPattern) {
		this.fixedPhonevalidationPattern = fixedPhonevalidationPattern;
	}

	/**
	 * the pattern
	 */
	private Pattern cellphonePattern = Pattern.compile(this.cellPhonevalidationPattern);
	private Pattern fixedphonePattern = Pattern.compile(this.fixedPhonevalidationPattern);
	
	@Override
	public String pluginName() {
		return this.pluginName;
	}

	@Override
	public void setPluginName(String pluginName) {
		if(pluginName !=null){
			this.pluginName = pluginName;
		}

	}

	/* *
	 *the first param is contact-info, for example: 
	 * 姓名|手机号;姓名|手机号
	 *the second param is the number of the contacts, for example:
	 * 20
	 */
	@Override
	public Object exec(Object... params) {
		// check
		if (params == null || params.length < 1 || params[0] == null || !(params[0] instanceof String) 
				|| params[1] == null || !(params[1] instanceof Integer) || params[2] == null || !(params[2] instanceof String) ) {
			LOGGER.error("invalid params");
			throw new InvalidParameterException("invalid params");
		}
		final String contacts = (String) params[0];
		final Integer contactsNum = (Integer) params[1];
		final String blackKeys = (String) params[2];
		String flag = "REJECT";
		
		if (contacts == null || contacts.isEmpty()) {
//			LOGGER.error("invalid stringParam: {}", contacts);
//			throw new InvalidParameterException("invalid stringParam: " + contacts);
			return flag;
		}
		if (contactsNum == null ) {
			LOGGER.error("invalid stringParam: {}", contactsNum);
			throw new InvalidParameterException("invalid stringParam: " + contactsNum);
		}
		
		
		if (contacts.indexOf(";")<0 || contacts.indexOf("|")<0) {
			LOGGER.error("contacts should be splitted by ; and | : {}", contacts);
			throw new InvalidParameterException("contacts should be splitted by , and |,for example:name1|cellphone1;name2|cellphone2:" + contacts);
		}
		
		if (blackKeys.indexOf("|")<0) {
			LOGGER.error("blackKeys should be splitted by | : {}", blackKeys);
			throw new InvalidParameterException("blackKeys should be splitted by |,for example:key1|key2|key3|key4:" + blackKeys);
		}
		
		
		int count = 1;
		int blackKeyCount = 1;
		String[] contactsInfo = contacts.split(";");
		for(String contact : contactsInfo){
			if (contacts.indexOf("|")<0) {
				LOGGER.error("contact should be splitted by | : {}", contact);
				throw new InvalidParameterException("contact should be splitted by |,for example:name1|cellphone1:" + contacts);
			}
			
			String[] info = contact.split("\\|");
			if(info.length ==2){
				String name = info[0];
				String cellphone = info[1];
				//remove the space
				if(cellphone.indexOf(" ") > -1){
					cellphone = cellphone.replaceAll(" ", "");
				}
				//remove the separator -
				if(cellphone.indexOf("-") > -1){
					cellphone = cellphone.replaceAll("-", "");
				}
				if(name == null || name.isEmpty()){
					continue;
				}else{
					if(name.indexOf(blackKeys) > -1){
						blackKeyCount++;
						continue;
					}
				}
				
				if(cellphone ==null || cellphone.isEmpty()){
					continue;
				}else{
					if(this.cellphonePattern.matcher(cellphone).matches()){
						count++;
					}
				}
				
				if(count >= contactsNum.intValue() && blackKeyCount == 1){
					flag = "ACCEPT";
					break;
				}else if(count >= contactsNum.intValue() && blackKeyCount <= 3){
					flag = "CAREFUL";
					break;
				}
			}
			
		}
		return flag;
		
	}

}
