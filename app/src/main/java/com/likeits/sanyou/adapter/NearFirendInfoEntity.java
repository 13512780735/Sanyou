package com.likeits.sanyou.adapter;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/4.
 */

public class NearFirendInfoEntity implements Serializable{

    /**
     * id : 3
     * lat : 39.967820
     * lon : 116.408293
     * uid : 113
     * km : 1.874
     * nickname : 13800138000
     * headimg :
     * easemob_id : 13800138000
     */

    private String id;
    private String lat;
    private String lon;
    private String uid;
    private double km;
    private String nickname;
    private String headimg;
    private String easemob_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }
}
