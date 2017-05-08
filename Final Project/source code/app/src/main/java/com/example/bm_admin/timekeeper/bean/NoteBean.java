package com.example.bm_admin.timekeeper.bean;

import java.util.Date;

/**
 * Created by bm-admin on 28/3/17.
 */
public class NoteBean {

    private Integer id;
    private String notes;
    private Date createdDate;
    private String label1;
    private String label2;
    private String email;

    public NoteBean(String title, String notes) {
        this.title = title;
        this.notes = notes;
    }
    public  NoteBean(){

    }

    private String title;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
