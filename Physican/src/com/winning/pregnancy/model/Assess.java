package com.winning.pregnancy.model;

import java.io.Serializable;

public class Assess implements Serializable
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -523560502084166623L;

    private int serviceID;
    private int level;
    private String tags;
    private String text;

    public int getServiceID()
    {
        return serviceID;
    }

    public void setServiceID(int serviceID)
    {
        this.serviceID = serviceID;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

}
