package bean;

public class VersionDetails {
	private String summary;

	private int id;

	private String appName;

	private String lastModify;

	private String tagetUrl;

	private String versionNo;

	private String createDate;

	private int activity;

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}

	public String getLastModify() {
		return this.lastModify;
	}

	public void setTagetUrl(String tagetUrl) {
		this.tagetUrl = tagetUrl;
	}

	public String getTagetUrl() {
		return this.tagetUrl;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getVersionNo() {
		return this.versionNo;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getActivity() {
		return this.activity;
	}
}
