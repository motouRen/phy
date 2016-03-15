package com.winning.pregnancy.model;

import java.util.Date;

public class AppVersion
{
    private Integer id;
    private Date createDate;
    private String lastModify;
    private Integer activity;
    private String appName;
    private String versionNo;
    private String summary;
    private String tagetUrl;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public String getLastModify()
    {
        return lastModify;
    }

    public void setLastModify(String lastModify)
    {
        this.lastModify = lastModify;
    }

    public Integer getActivity()
    {
        return activity;
    }

    public void setActivity(Integer activity)
    {
        this.activity = activity;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getVersionNo()
    {
        return versionNo;
    }

    public void setVersionNo(String versionNo)
    {
        this.versionNo = versionNo;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getTagetUrl()
    {
        return tagetUrl;
    }

    public void setTagetUrl(String tagetUrl)
    {
        this.tagetUrl = tagetUrl;
    }

}
