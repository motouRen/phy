package com.winning.pregnancy.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InquiryMessage implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2996421038471173595L;

	private int activity;
	private int assistantID;
	private String content;
	private int contentType;
	private String createDate;
	private int id;
	private String lastModify;
	private int recNo;
	private int serviceID;
	private int gravidaID;
	private int doctorID;

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public int getGravidaID() {
		return gravidaID;
	}

	public void setGravidaID(int gravidaID) {
		this.gravidaID = gravidaID;
	}

}
