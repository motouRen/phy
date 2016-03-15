package com.winning.pregnancy.model;

public class CoinSave implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 112552118430150664L;
	private String summary="";
	private int id;
	private String amount="";
	private String lastModify="";
	private int coinValue;
	private String alipaySignture="";
	private String rechargeNo="";
	private int gravidaID;
	private int coinRuleID;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
	public int getCoinValue() {
		return coinValue;
	}
	public void setCoinValue(int coinValue) {
		this.coinValue = coinValue;
	}
	public String getAlipaySignture() {
		return alipaySignture;
	}
	public void setAlipaySignture(String alipaySignture) {
		this.alipaySignture = alipaySignture;
	}
	public String getRechargeNo() {
		return rechargeNo;
	}
	public void setRechargeNo(String rechargeNo) {
		this.rechargeNo = rechargeNo;
	}
	public int getGravidaID() {
		return gravidaID;
	}
	public void setGravidaID(int gravidaID) {
		this.gravidaID = gravidaID;
	}
	public int getCoinRuleID() {
		return coinRuleID;
	}
	public void setCoinRuleID(int coinRuleID) {
		this.coinRuleID = coinRuleID;
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
