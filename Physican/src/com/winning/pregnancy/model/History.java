package com.winning.pregnancy.model;

public class History implements java.io.Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -7007397850352461430L;
    private String doctorName;
    private String departmentName;
    private String titleName;
    private String title;
    private String state;
    private String mobile;
    private String summary = "";// 详情
    private int id;
    private int assistantID;
    private String doctorMobile;
    private int doctorID;
    private int gravidaID;
    private String commentDate = "";
    private int commentLevel;
    private String commentTag = "";
    private String commentText = "";
    private String doctorHeadPhoto;

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getTitleName()
    {
        return titleName;
    }

    public void setTitleName(String titleName)
    {
        this.titleName = titleName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAssistantID()
    {
        return assistantID;
    }

    public void setAssistantID(int assistantID)
    {
        this.assistantID = assistantID;
    }

    public String getDoctorMobile()
    {
        return doctorMobile;
    }

    public void setDoctorMobile(String doctorMobile)
    {
        this.doctorMobile = doctorMobile;
    }

    public int getDoctorID()
    {
        return doctorID;
    }

    public void setDoctorID(int doctorID)
    {
        this.doctorID = doctorID;
    }

    public int getGravidaID()
    {
        return gravidaID;
    }

    public void setGravidaID(int gravidaID)
    {
        this.gravidaID = gravidaID;
    }

    public String getCommentDate()
    {
        return commentDate;
    }

    public void setCommentDate(String commentDate)
    {
        this.commentDate = commentDate;
    }

    public int getCommentLevel()
    {
        return commentLevel;
    }

    public void setCommentLevel(int commentLevel)
    {
        this.commentLevel = commentLevel;
    }

    public String getCommentTag()
    {
        return commentTag;
    }

    public void setCommentTag(String commentTag)
    {
        this.commentTag = commentTag;
    }

    public String getCommentText()
    {
        return commentText;
    }

    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }

    public String getDoctorHeadPhoto()
    {
        return doctorHeadPhoto;
    }

    public void setDoctorHeadPhoto(String doctorHeadPhoto)
    {
        this.doctorHeadPhoto = doctorHeadPhoto;
    }

    
    
}
