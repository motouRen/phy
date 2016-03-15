package com.winning.pregnancy.model;

public class DoctorList implements java.io.Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 2977582551056677041L;
    private String headPhoto;
    private String departmentName = "";// 科室
    private String hospitalName = "";// 医院名称
    private String titleName = "";// 职位
    private String doctorName = "";// 医生姓名
    private String summary = "";// 详情
    private String textServicePrice = "";// 货比值
    private String id = "";// 医生id
    private String mobile;

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getHospitalName()
    {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName)
    {
        this.hospitalName = hospitalName;
    }

    public String getTitleName()
    {
        return titleName;
    }

    public void setTitleName(String titleName)
    {
        this.titleName = titleName;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getTextServicePrice()
    {
        return textServicePrice;
    }

    public void setTextServicePrice(String textServicePrice)
    {
        this.textServicePrice = textServicePrice;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getHeadPhoto()
    {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto)
    {
        this.headPhoto = headPhoto;
    }

}
