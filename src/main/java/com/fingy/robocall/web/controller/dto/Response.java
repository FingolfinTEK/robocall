package com.fingy.robocall.web.controller.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Response {

    @XmlElement(name = "Play")
    private String play;

    public Response() {
    }

    public Response(String play) {
        this.play = play;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }
}
