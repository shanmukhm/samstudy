package com.samstudy.model;

public class FormAndVersion {

    private String formId;
    private int version;

    public FormAndVersion() {
    }

    public FormAndVersion(String formId, int version) {
        this.formId = formId;
        this.version = version;
    }

    public String getFormId() {
        return formId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
