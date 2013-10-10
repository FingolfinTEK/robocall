package com.fingy.robocall.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CallRequest {

    private Long id;
    private String sid;
    private String text;
    private String language;
    private Date redialTime;

    public CallRequest() {
    }

    public CallRequest(String sid, String text, String language) {
        this.sid = sid;
        this.text = text;
        this.language = language;
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

    @Temporal(TemporalType.TIMESTAMP)
    public Date getRedialTime() {
        return redialTime;
    }

    public void setRedialTime(Date redialTime) {
        this.redialTime = redialTime;
    }
}
