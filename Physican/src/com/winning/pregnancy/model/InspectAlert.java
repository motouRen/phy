package com.winning.pregnancy.model;

import com.winning.pregnancy.ahibernate.annotation.Column;
import com.winning.pregnancy.ahibernate.annotation.Id;

public class InspectAlert
{
    @Column(name = "activity")
    private Integer activity;
    @Column(name = "alertDate")
    private String alertDate = "";
    @Column(name = "code")
    private String code = "";
    @Column(name = "createDate")
    private String createDate = "";
    @Column(name = "gravidaID")
    private Integer gravidaID;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "lastModify")
    private String lastModify = "";
    @Column(name = "recNo")
    private Integer recNo;

    public Integer getActivity()
    {
        return activity;
    }

    public void setActivity(Integer activity)
    {
        this.activity = activity;
    }

    public String getAlertDate()
    {
        return alertDate;
    }

    public void setAlertDate(String alertDate)
    {
        this.alertDate = alertDate;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public Integer getGravidaID()
    {
        return gravidaID;
    }

    public void setGravidaID(Integer gravidaID)
    {
        this.gravidaID = gravidaID;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getLastModify()
    {
        return lastModify;
    }

    public void setLastModify(String lastModify)
    {
        this.lastModify = lastModify;
    }

    public Integer getRecNo()
    {
        return recNo;
    }

    public void setRecNo(Integer recNo)
    {
        this.recNo = recNo;
    }

}
