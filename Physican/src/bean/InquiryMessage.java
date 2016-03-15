package bean;

import java.util.List;

/**
 * Auto-generated: 2015-12-01 10:41:44
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class InquiryMessage {

	private String err;
	private List<InquiryMessageData> data;
	private int success;

	public void setErr(String err) {
		this.err = err;
	}

	public String getErr() {
		return err;
	}

	public void setData(List<InquiryMessageData> data) {
		this.data = data;
	}

	public List<InquiryMessageData> getData() {
		return data;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return success;
	}

}