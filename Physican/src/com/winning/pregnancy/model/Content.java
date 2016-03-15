package com.winning.pregnancy.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.winning.pregnancy.ahibernate.annotation.Column;
import com.winning.pregnancy.ahibernate.annotation.Id;
import com.winning.pregnancy.ahibernate.annotation.Table;

@Table(name = "Content")
public class Content implements Serializable
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -523560502084166623L;

    @Id
    @Column(name = "id")
    @JSONField(serialize = false)
    private int id;
    @Column(name = "serviceID")
    private int serviceID;
    @Column(name = "content")
    private String content;
    @Column(name = "contentType")
    private int contentType;
    @Column(name = "recNo")
    private int recNo;
    @Column(name = "assistantID")
    private int assistantID;
    @Column(name = "gravidaID")
    private int gravidaID;
    @Column(name = "doctorID")
    private int doctorID;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getServiceID()
    {
        return serviceID;
    }

    public void setServiceID(int serviceID)
    {
        this.serviceID = serviceID;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getContentType()
    {
        return contentType;
    }

    public void setContentType(int contentType)
    {
        this.contentType = contentType;
    }

    public int getRecNo()
    {
        return recNo;
    }

    public void setRecNo(int recNo)
    {
        this.recNo = recNo;
    }

    public int getAssistantID()
    {
        return assistantID;
    }

    public void setAssistantID(int assistantID)
    {
        this.assistantID = assistantID;
    }

    public int getGravidaID()
    {
        return gravidaID;
    }

    public void setGravidaID(int gravidaID)
    {
        this.gravidaID = gravidaID;
    }

    public int getDoctorID()
    {
        return doctorID;
    }

    public void setDoctorID(int doctorID)
    {
        this.doctorID = doctorID;
    }
    
    

}
