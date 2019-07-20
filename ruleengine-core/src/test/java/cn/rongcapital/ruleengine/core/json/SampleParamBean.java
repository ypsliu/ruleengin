/**
 * 
 */
package cn.rongcapital.ruleengine.core.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * @author hill
 * sample input param for Test
 *
 */
@JsonTypeName("sampleParamBean")
public class SampleParamBean {

	public static final String TYPE_NAME = "SampleParamBean";

	@JsonProperty("APPLY_TYPE")
	private String applyType;
	
	@JsonProperty("INST")
	private String inst;

	@JsonProperty("CONTACT")
	private String contact;
	
	@JsonProperty("VALIDCONTACTS")
	private String validContacts;
	
	
	@JsonProperty("NAME")
	private String name;
	
	@JsonProperty("CERTIFICATE_NUMBER")
	private String certificateNumber;
	
	@JsonProperty("CELLPHONE")
	private String cellphone;
	
	@JsonProperty("BANK_ACCOUNT")
	private String bankAccount;
	
	@JsonProperty("NAME_REF")
	private String nameRef;
	
	@JsonProperty("CERTIFICATE_NUMBER_REF")
	private String certificateNumberRef;
	
	@JsonProperty("CELLPHONE_REF")
	private String cellphoneRef;
	
	@JsonProperty("BANK_ACCOUNT_REF")
	private String bankAccountRef;
	
	@JsonProperty("CONTACTS")
	private String contacts;
	
	@JsonProperty("FIRST_CONTACT")
	private String firstContact;
	
	@JsonProperty("FIRST_CELLPHONE")
	private String firstCellphone;
	
	@JsonProperty("FIRST_CONTACT_REF")
	private String firstContactRef;
	
	@JsonProperty("FIRST_CELLPHONE_REF")
	private String firstCellphoneRef;

	
	
	public String getInst() {
		return inst;
	}



	public void setInst(String inst) {
		this.inst = inst;
	}



	public String getApplyType() {
		return applyType;
	}



	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}



	public String getContact() {
		return contact;
	}



	public void setContact(String contact) {
		this.contact = contact;
	}



	public String getValidContacts() {
		return validContacts;
	}



	public void setValidContacts(String validContacts) {
		this.validContacts = validContacts;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCertificateNumber() {
		return certificateNumber;
	}



	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}



	public String getCellphone() {
		return cellphone;
	}



	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}



	public String getBankAccount() {
		return bankAccount;
	}



	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}



	public String getNameRef() {
		return nameRef;
	}



	public void setNameRef(String nameRef) {
		this.nameRef = nameRef;
	}



	public String getCertificateNumberRef() {
		return certificateNumberRef;
	}



	public void setCertificateNumberRef(String certificateNumberRef) {
		this.certificateNumberRef = certificateNumberRef;
	}



	public String getCellphoneRef() {
		return cellphoneRef;
	}



	public void setCellphoneRef(String cellphoneRef) {
		this.cellphoneRef = cellphoneRef;
	}



	public String getBankAccountRef() {
		return bankAccountRef;
	}



	public void setBankAccountRef(String bankAccountRef) {
		this.bankAccountRef = bankAccountRef;
	}



	public String getContacts() {
		return contacts;
	}



	public void setContacts(String contacts) {
		this.contacts = contacts;
	}



	public String getFirstContact() {
		return firstContact;
	}



	public void setFirstContact(String firstContact) {
		this.firstContact = firstContact;
	}



	public String getFirstCellphone() {
		return firstCellphone;
	}



	public void setFirstCellphone(String firstCellphone) {
		this.firstCellphone = firstCellphone;
	}



	public String getFirstContactRef() {
		return firstContactRef;
	}



	public void setFirstContactRef(String firstContactRef) {
		this.firstContactRef = firstContactRef;
	}



	public String getFirstCellphoneRef() {
		return firstCellphoneRef;
	}



	public void setFirstCellphoneRef(String firstCellphoneRef) {
		this.firstCellphoneRef = firstCellphoneRef;
	}



	public static String getTypeName() {
		return TYPE_NAME;
	}



	@Override
	public String toString() {
		return "SampleParamBean [applyType=" + applyType + ", inst=" + inst + ", contact=" + contact
				+ ", validContacts=" + validContacts + ", name=" + name + ", certificateNumber=" + certificateNumber
				+ ", cellphone=" + cellphone + ", bankAccount=" + bankAccount + ", nameRef=" + nameRef
				+ ", certificateNumberRef=" + certificateNumberRef + ", cellphoneRef=" + cellphoneRef
				+ ", bankAccountRef=" + bankAccountRef + ", contacts=" + contacts + ", firstContact=" + firstContact
				+ ", firstCellphone=" + firstCellphone + ", firstContactRef=" + firstContactRef + ", firstCellphoneRef="
				+ firstCellphoneRef + "]";
	}



	
	
	

	

}
