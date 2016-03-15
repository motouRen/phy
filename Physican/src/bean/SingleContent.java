package bean;

public class SingleContent {
	private int id;

	private String content;

	private String lastModify;

	private int serviceID;

	private int gravidaID;

	private int contentType;

	private String createDate;

	private int recNo;

	private int activity;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}

	public String getLastModify() {
		return this.lastModify;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public int getServiceID() {
		return this.serviceID;
	}

	public void setGravidaID(int gravidaID) {
		this.gravidaID = gravidaID;
	}

	public int getGravidaID() {
		return this.gravidaID;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public int getContentType() {
		return this.contentType;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public int getRecNo() {
		return this.recNo;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getActivity() {
		return this.activity;
	}
}
