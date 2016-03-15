package bean;

import java.util.List;

public class SingleContentRoot {
	private String err;

	private List<SingleContent> data;

	private int success;

	public void setErr(String err) {
		this.err = err;
	}

	public String getErr() {
		return this.err;
	}

	public void setData(List<SingleContent> data) {
		this.data = data;
	}

	public List<SingleContent> getData() {
		return this.data;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return this.success;
	}

}