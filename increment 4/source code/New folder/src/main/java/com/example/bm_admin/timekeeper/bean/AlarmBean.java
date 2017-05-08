package com.example.bm_admin.timekeeper.bean;

import java.util.Date;

/**
 * Created by bm-admin on 28/3/17.
 */
public class AlarmBean {
    private Integer id;
    private Integer reqCode;
    private Integer isRepeat;
    private String desc;
    private Date time;
    private String interval;
    private String snooze;
    private Date createdDate;
    private String label1;
    private String label2;
    private Integer maxCount;
    private String email;

    public AlarmBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReqCode() {
        return reqCode;
    }

    public void setReqCode(Integer reqCode) {
        this.reqCode = reqCode;
    }

    public Integer getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Integer isRepeat) {
        this.isRepeat = isRepeat;
    }

    public Integer isRepeat() {
        return isRepeat;
    }

    public void setRepeat(Integer repeat) {
        isRepeat = repeat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getSnooze() {
        return snooze;
    }

    public void setSnooze(String snooze) {
        this.snooze = snooze;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
