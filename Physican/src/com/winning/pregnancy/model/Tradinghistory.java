package com.winning.pregnancy.model;
/**
 * 交易历史
 * @author xj
 *
 */
public class Tradinghistory implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1942179491175219868L;
	private String summary;
	private String id;
	private String balance;
	private String occurValue;
	private String lastModify;
	private String refrenceID;
	private String gravidaID;
	private String occurDate;
	private String businessType;
	private String createDate;
	private String activity;
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getOccurValue() {
		return occurValue;
	}
	public void setOccurValue(String occurValue) {
		this.occurValue = occurValue;
	}
	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
	public String getRefrenceID() {
		return refrenceID;
	}
	public void setRefrenceID(String refrenceID) {
		this.refrenceID = refrenceID;
	}
	public String getGravidaID() {
		return gravidaID;
	}
	public void setGravidaID(String gravidaID) {
		this.gravidaID = gravidaID;
	}
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	
}
