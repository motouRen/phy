package bean;

import java.util.List;

public class Arrived {

	private String err;

	private List<ArrivedDetail> data;

	private int success;

	public void setErr(String err) {
		this.err = err;
	}

	public String getErr() {
		return this.err;
	}

	public void setData(List<ArrivedDetail> data) {
		this.data = data;
	}

	public List<ArrivedDetail> getData() {
		return this.data;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return this.success;
	}

}
