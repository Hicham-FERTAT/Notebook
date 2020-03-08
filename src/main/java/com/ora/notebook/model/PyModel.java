package com.ora.notebook.model;

import java.util.Objects;

public class PyModel {

    private String code;
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public PyModel(String code, String sessionId) {
        this.code = code;
        this.sessionId = sessionId;
    }

    public PyModel() {
    }

       public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PyModel)) return false;
        PyModel pyModel = (PyModel) o;
        return Objects.equals(getCode(), pyModel.getCode()) &&
                Objects.equals(getSessionId(), pyModel.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getSessionId());
    }

    @Override
    public String toString() {
        return "PyModel{" +
                "code='" + code + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
