package com.winning.pregnancy.model;

import com.winning.pregnancy.ahibernate.annotation.Column;
import com.winning.pregnancy.ahibernate.annotation.Id;
import com.winning.pregnancy.ahibernate.annotation.Table;

/**
 * YxtUsesr entity. @author MyEclipse Persistence Tools
 */
@Table(name = "EMMessageLocal")
public class EMMessageLocal implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6503432754724428242L;
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "acked")
	private String acked;
	@Column(name = "message")
	private String message;
	@Column(name = "chatType")
	private String chatType;
	@Column(name = "delivered")
	private String delivered;
	@Column(name = "direct")
	private String direct;
	@Column(name = "error")
	private String error;
	@Column(name = "fromUser")
	private String fromUser;
	@Column(name = "isAcked")
	private String isAcked;
	@Column(name = "isDelivered")
	private String isDelivered;
	@Column(name = "listened")
	private String listened;
	@Column(name = "msgId")
	private String msgId;
	@Column(name = "msgTime")
	private String msgTime;
	@Column(name = "status")
	private String status;
	@Column(name = "toUser")
	private String toUser;
	@Column(name = "type")
	private String type;
	@Column(name = "userName")
	private String userName;
	@Column(name = "isread")
	private String isread;
	@Column(name = "isshow")
	private String isshow;
	@Column(name = "serviceID")
	private String serviceID;
	@Column(name = "messageID")
	private String messageID;
	@Column(name = "doctorID")
	private String doctorID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAcked() {
		return acked;
	}

	public void setAcked(String acked) {
		this.acked = acked;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	public String getDelivered() {
		return delivered;
	}

	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getIsAcked() {
		return isAcked;
	}

	public void setIsAcked(String isAcked) {
		this.isAcked = isAcked;
	}

	public String getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(String isDelivered) {
		this.isDelivered = isDelivered;
	}

	public String getListened() {
		return listened;
	}

	public void setListened(String listened) {
		this.listened = listened;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

}