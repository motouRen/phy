package bean;

import java.util.List;

public class Done {
	private String err;

	private List<DoneDetail> data;

	private int success;

	public void setErr(String err) {
		this.err = err;
	}

	public String getErr() {
		return this.err;
	}

	public void setData(List<DoneDetail> data) {
		this.data = data;
	}

	public List<DoneDetail> getData() {
		return this.data;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return this.success;
	}
}
