package com.zkzj.rtmp_terminal.bean;

import java.io.Serializable;

public class TvBean implements Serializable {
    private int tv_img;
    private String tv_name;
    private String tv_address;
    private String Type;

    public TvBean(int tv_img, String tv_name, String tv_address, String type) {
        this.tv_img = tv_img;
        this.tv_name = tv_name;
        this.tv_address = tv_address;
        Type = type;
    }

    public int getTv_img() {
        return tv_img;
    }

    public void setTv_img(int tv_img) {
        this.tv_img = tv_img;
    }

    public String getTv_name() {
        return tv_name;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }

    public String getTv_address() {
        return tv_address;
    }

    public void setTv_address(String tv_address) {
        this.tv_address = tv_address;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "TvBean{" +
                "tv_img=" + tv_img +
                ", tv_name='" + tv_name + '\'' +
                ", tv_address='" + tv_address + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}