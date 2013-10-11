package com.fingy.robocall.model.dto;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Play {
    @XmlValue
    private String value;

    @XmlAttribute(required = false)
    private Integer loop;

    public Play() {
    }

    public Play(String value, Integer loop) {
        this.value = value;
        this.loop = loop;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLoop() {
        return loop;
    }

    public void setLoop(Integer loop) {
        this.loop = loop;
    }
}
