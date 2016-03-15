package bean;

/**
 * Copyright 2015 aTool.org 
 */

import java.util.List;

/**
 * Auto-generated: 2015-11-30 11:23:20
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class Doctor {

	private String err;
	private List<DoctorData> data;
	private int success;

	public void setErr(String err) {
		this.err = err;
	}

	public String getErr() {
		return err;
	}

	public void setData(List<DoctorData> data) {
		this.data = data;
	}

	public List<DoctorData> getData() {
		return data;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return success;
	}

}