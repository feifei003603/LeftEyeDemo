package com.sus.lefteyecinema.bean;

import java.io.Serializable;

public class LeftEyeItem implements Cloneable, Serializable {
    
    private static final long serialVersionUID = 1L;

    private String cover_url;

    /** 每期的id */
    private String periodId;

    /** 每期里面视频项的id */
    private String id;

    private String title;

    private String desc;

    private String titleDate;

    private String site;

    private String threeD;

    private String has;

    public String getThreeD() {
        return threeD;
    }

    public void setThreeD(String threeD) {
        this.threeD = threeD;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setperiodId(String periodId) {
        this.periodId = periodId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitleDate() {
        return titleDate;
    }

    public void setTitleDate(String titleDate) {
        this.titleDate = titleDate;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getHas() {
        return has;
    }

    public void setHas(String has) {
        this.has = has;
    }

}
