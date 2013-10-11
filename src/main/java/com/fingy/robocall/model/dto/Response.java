package com.fingy.robocall.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Response {

    @XmlElement(name = "Play")
    private Play play;

    public Response() {
    }

    public Response(String url, Integer loop) {
        play = new Play(url, loop);
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }
}
