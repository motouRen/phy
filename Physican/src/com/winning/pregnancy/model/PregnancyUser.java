package com.winning.pregnancy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 
 * 项目名称：PregnancyAndroid 类名称：User 类描述： 创建人：Administrator 创建时间：2015-9-29
 * 上午10:19:49 修改人：Administrator 修改时间：2015-9-29 上午10:19:49 修改备注：
 * 
 * @version
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PregnancyUser implements Serializable, Cloneable
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2996421038471173595L;

    private Integer activity;
    private String createDate = "";
    private String dueDate = "";
    private Integer id;
    private String lastModify = "";
    private Integer integralBalance = 0;
    private Integer lastPregnancyID;
    private String mobile = "";
    private String nick = "";
    private Integer yxtUserID;
    private String headPhoto = "";
    private Integer defaultPatientID;
    private String height = "";
    private String hospitalCode = "";
    private String hospitalHost = "";
    private Integer hospitalID;
    private String hospitalName = "";
    private String idNo = "";
    private String address = "";
    private String birthday = "";
    private String cardNo = "";
    private String cardTypeDis = "";
    private Integer coinBalance = 0;
    private Integer lastMonitorID;
    private String medicalNo = "";
    private Integer monitorScore;
    private String name = "";
    private Integer patientID=0;
    private String patientIdno = "";
    private String patientMobile = "";
    private String patientName = "";
    private String weight = "";
    private Integer yxtMemberID;
    private List<CareDoctor> careDoctor = new ArrayList<PregnancyUser.CareDoctor>();
    private List<Inspect> inspect = new ArrayList<PregnancyUser.Inspect>();

    public class CareDoctor
    {
        private Integer activity;
        private String createDate;
        private Integer doctorID;
        private Integer gravidaID;
        private Integer id;
        private String lastModify;

        public Integer getActivity()
        {
            return activity;
        }

        public void setActivity(Integer activity)
        {
            this.activity = activity;
        }

        public String getCreateDate()
        {
            return createDate;
        }

        public void setCreateDate(String createDate)
        {
            this.createDate = createDate;
        }

        public Integer getDoctorID()
        {
            return doctorID;
        }

        public void setDoctorID(Integer doctorID)
        {
            this.doctorID = doctorID;
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

    }

    public class Inspect
    {
        private Integer activity;
        private String alertDate = "";
        private String code = "";
        private String createDate = "";
        private Integer gravidaID;
        private Integer id;
        private String lastModify = "";
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

    public Integer getActivity()
    {
        return activity;
    }

    public void setActivity(Integer activity)
    {
        this.activity = activity;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
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

    public Integer getIntegralBalance()
    {
        return integralBalance;
    }

    public void setIntegralBalance(Integer integralBalance)
    {
        this.integralBalance = integralBalance;
    }

    public Integer getLastPregnancyID()
    {
        return lastPregnancyID;
    }

    public void setLastPregnancyID(Integer lastPregnancyID)
    {
        this.lastPregnancyID = lastPregnancyID;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public Integer getYxtUserID()
    {
        return yxtUserID;
    }

    public void setYxtUserID(Integer yxtUserID)
    {
        this.yxtUserID = yxtUserID;
    }

    public String getHeadPhoto()
    {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto)
    {
        this.headPhoto = headPhoto;
    }

    public Integer getDefaultPatientID()
    {
        return defaultPatientID;
    }

    public void setDefaultPatientID(Integer defaultPatientID)
    {
        this.defaultPatientID = defaultPatientID;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getHospitalCode()
    {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode)
    {
        this.hospitalCode = hospitalCode;
    }

    public String getHospitalHost()
    {
        return hospitalHost;
    }

    public void setHospitalHost(String hospitalHost)
    {
        this.hospitalHost = hospitalHost;
    }

    public Integer getHospitalID()
    {
        return hospitalID;
    }

    public void setHospitalID(Integer hospitalID)
    {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName()
    {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName)
    {
        this.hospitalName = hospitalName;
    }

    public String getIdNo()
    {
        return idNo;
    }

    public void setIdNo(String idNo)
    {
        this.idNo = idNo;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getCardNo()
    {
        return cardNo;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public String getCardTypeDis()
    {
        return cardTypeDis;
    }

    public void setCardTypeDis(String cardTypeDis)
    {
        this.cardTypeDis = cardTypeDis;
    }

    public Integer getCoinBalance()
    {
        return coinBalance;
    }

    public void setCoinBalance(Integer coinBalance)
    {
        this.coinBalance = coinBalance;
    }

    public Integer getLastMonitorID()
    {
        return lastMonitorID;
    }

    public void setLastMonitorID(Integer lastMonitorID)
    {
        this.lastMonitorID = lastMonitorID;
    }

    public String getMedicalNo()
    {
        return medicalNo;
    }

    public void setMedicalNo(String medicalNo)
    {
        this.medicalNo = medicalNo;
    }

    public Integer getMonitorScore()
    {
        return monitorScore;
    }

    public void setMonitorScore(Integer monitorScore)
    {
        this.monitorScore = monitorScore;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getPatientID()
    {
        return patientID;
    }

    public void setPatientID(Integer patientID)
    {
        this.patientID = patientID;
    }

    public String getPatientIdno()
    {
        return patientIdno;
    }

    public void setPatientIdno(String patientIdno)
    {
        this.patientIdno = patientIdno;
    }

    public String getPatientMobile()
    {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile)
    {
        this.patientMobile = patientMobile;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public Integer getYxtMemberID()
    {
        return yxtMemberID;
    }

    public void setYxtMemberID(Integer yxtMemberID)
    {
        this.yxtMemberID = yxtMemberID;
    }

    public List<CareDoctor> getCareDoctor()
    {
        return careDoctor;
    }

    public void setCareDoctor(List<CareDoctor> careDoctor)
    {
        this.careDoctor = careDoctor;
    }

    public List<Inspect> getInspect()
    {
        return inspect;
    }

    public void setInspect(List<Inspect> inspect)
    {
        this.inspect = inspect;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        // TODO Auto-generated method stub
        return super.clone();
    }

}
