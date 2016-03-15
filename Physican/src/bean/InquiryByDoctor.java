package bean;

import java.util.List;

public class InquiryByDoctor {

	private String err;

	private List<InquiryByDoctorDetail> data;

	private int success;

	public void setErr(String err) {
		this.err = err;
	}

	public String getErr() {
		return this.err;
	}

	public void setData(List<InquiryByDoctorDetail> data) {
		this.data = data;
	}

	public List<InquiryByDoctorDetail> getData() {
		return this.data;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return this.success;
	}

}
