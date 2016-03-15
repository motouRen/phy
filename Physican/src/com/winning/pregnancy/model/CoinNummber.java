package com.winning.pregnancy.model;

public class CoinNummber implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1984359099262753556L;
	private int id;
	private String amount="";
	private String lastModify="";
	private String  coinValue="";
	private String remark="";
	private String createDate="";
	private String activity="";
	public String getAmount() {
		return amount;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getCoinValue() {
		return coinValue;
	}
	public void setCoinValue(String coinValue) {
		this.coinValue = coinValue;
	}

}
