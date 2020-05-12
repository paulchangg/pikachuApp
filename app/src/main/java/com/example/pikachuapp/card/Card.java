package com.example.pikachuapp.card;

import java.io.Serializable;

public class Card implements Serializable {
    private	Integer	c_id;
    private	String c_name;
    private	String annlfee;
    private	Double fcb;
    private	Double dcb;

    public Card(Integer c_id, String c_name, String annlfee, Double fcb, Double dcb) {
        this.c_id = c_id;
        this.c_name = c_name;
        this.annlfee = annlfee;
        this.fcb = fcb;
        this.dcb = dcb;
    }

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getAnnlfee() {
        return annlfee;
    }

    public void setAnnlfee(String annlfee) {
        this.annlfee = annlfee;
    }

    public Double getFcb() {
        return fcb;
    }

    public void setFcb(Double fcb) {
        this.fcb = fcb;
    }

    public Double getDcb() {
        return dcb;
    }

    public void setDcb(Double dcb) {
        this.dcb = dcb;
    }
}
