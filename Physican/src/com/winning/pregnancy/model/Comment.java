package com.winning.pregnancy.model;

public class Comment implements java.io.Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -212945153810597763L;
    private String gravidaName = "";
    private String commentLevel = "";
    private String dueDate = "";
    private String commentText = "";
    private String title = "";
    private String commentTag = "";
    private String mobile;
    private String summary = "";// 详情
    private int id;
    private int assistantID;
    private String doctorMobile;
    private int doctorID;
    private int gravidaID;
    private String doctorName;
    private String state;

    public String getGravidaName()
    {
        return gravidaName;
    }

    public void setGravidaName(String gravidaName)
    {
        this.gravidaName = gravidaName;
    }

    public String getCommentLevel()
    {
        return commentLevel;
    }

    public void setCommentLevel(String commentLevel)
    {
        this.commentLevel = commentLevel;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getCommentText()
    {
        return commentText;
    }

    public void setCommentText(String commentText)
    {
        this.commentText = commentText;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCommentTag()
    {
        return commentTag;
    }

    public void setCommentTag(String commentTag)
    {
        this.commentTag = commentTag;
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

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

}
