package com.example.bm_admin.timekeeper.bean;

import java.util.Date;

/**
 * Created by bm-admin on 28/3/17.
 */
public class RegisterBean {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Date createdDate;
    private String label1;
    private String label2;

    public RegisterBean() {
    }

    public RegisterBean(Integer id, String name, String email, String password, Date createdDate, String label1, String label2) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.label1 = label1;
        this.label2 = label2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
