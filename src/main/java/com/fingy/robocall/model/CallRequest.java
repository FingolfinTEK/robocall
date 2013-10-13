package com.fingy.robocall.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CallRequest {

    private Long id;
    private String sid;
    private String text;
    private String language;
    private String phoneNumber;
    private String rootUrl;
    private Date redialTime;
    private Integer redialCount = 0;

    public CallRequest() {
    }

    public CallRequest(String sid, String text, String language, String phoneNumber, String rootUrl) {
        this.sid = sid;
        this.text = text;
        this.language = language;
        this.phoneNumber = phoneNumber;
        this.rootUrl = rootUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(unique = true, nullable = false)
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Basic
    @Column(nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(nullable = false)
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(nullable = false)
    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getRedialTime() {
        return redialTime;
    }

    public void setRedialTime(Date redialTime) {
        this.redialTime = redialTime;
    }

    @Basic
    @Column(nullable = false)
    public Integer getRedialCount() {
        return redialCount;
    }

    public void setRedialCount(Integer redialCount) {
        this.redialCount = redialCount;
    }
}
