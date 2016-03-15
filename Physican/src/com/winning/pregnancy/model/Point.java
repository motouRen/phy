package com.winning.pregnancy.model;
/**
 * 积分
 * @author xj
 *
 */
public class Point implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1283353460265864451L;
	private String summary="";
	private int id;
	private int balance;
	private int occurValue;
	private String lastModify;
	private int refrenceID;
	private int gravidaID;
	private String occurDate;
	private int businessType;
	private String createDate;
	private int activity;
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getOccurValue() {
		return occurValue;
	}
	public void setOccurValue(int occurValue) {
		this.occurValue = occurValue;
	}
	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
	public int getRefrenceID() {
		return refrenceID;
	}
	public void setRefrenceID(int refrenceID) {
		this.refrenceID = refrenceID;
	}
	public int getGravidaID() {
		return gravidaID;
	}
	public void setGravidaID(int gravidaID) {
		this.gravidaID = gravidaID;
	}
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getActivity() {
		return activity;
	}
	public void setActivity(int activity) {
		this.activity = activity;
	}
	
}
