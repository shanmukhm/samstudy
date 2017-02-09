package com.samstudy.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class FormSubmissionData {

    @Id
    private String id;
    private String userId;
    private String name;
    private int version;
    private String x;
    private Date submittedDate;
    private String submittedFormJson;

    public FormSubmissionData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getSubmittedFormJson() {
        return submittedFormJson;
    }

    public void setSubmittedFormJson(String submittedFormJson) {
        this.submittedFormJson = submittedFormJson;
    }
}
