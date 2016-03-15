package com.winning.pregnancy.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorInfo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5493283725503099L;
	private int activity;
	private int assistantID;
	private String birthday;
	private int coinPay;
	private String createDate;
	private String departmentName;
	private String doctorHeadPhoto;
	private int doctorID;
	private String doctorMobile;
	private String doctorName;
	private String dueDate;
	private int grade;
	private String gravidaHeadPhoto;
	private int gravidaID;
	private String gravidaMobile;
	private String gravidaName;
	private String hospitalName;
	private int id;
	private String lastModify;
	private String nick;
	private int price;
	private String requestDate;
	private String responseDate;
	private String shortName;
	private int state;
	private String summary;
	private String title;
	private String titleName;

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getAssistantID() {
		return assistantID;
	}

	public void setAssistantID(int assistantID) {
		this.assistantID = assistantID;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getCoinPay() {
		return coinPay;
	}

	public void setCoinPay(int coinPay) {
		this.coinPay = coinPay;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDoctorHeadPhoto() {
		return doctorHeadPhoto;
	}

	public void setDoctorHeadPhoto(String doctorHeadPhoto) {
		this.doctorHeadPhoto = doctorHeadPhoto;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

	public String getDoctorMobile() {
		return doctorMobile;
	}

	public void setDoctorMobile(String doctorMobile) {
		this.doctorMobile = doctorMobile;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getGravidaHeadPhoto() {
		return gravidaHeadPhoto;
	}

	public void setGravidaHeadPhoto(String gravidaHeadPhoto) {
		this.gravidaHeadPhoto = gravidaHeadPhoto;
	}

	public int getGravidaID() {
		return gravidaID;
	}

	public void setGravidaID(int gravidaID) {
		this.gravidaID = gravidaID;
	}

	public String getGravidaMobile() {
		return gravidaMobile;
	}

	public void setGravidaMobile(String gravidaMobile) {
		this.gravidaMobile = gravidaMobile;
	}

	public String getGravidaName() {
		return gravidaName;
	}

	public void setGravidaName(String gravidaName) {
		this.gravidaName = gravidaName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastModify() {
		return lastModify;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

}
