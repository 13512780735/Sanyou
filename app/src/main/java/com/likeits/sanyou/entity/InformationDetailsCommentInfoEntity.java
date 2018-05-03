package com.likeits.sanyou.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/4.
 */

public class InformationDetailsCommentInfoEntity implements Serializable{

    /**
     * id : 1
     * uid : 1
     * easemob_id : 1
     * nickname : 三友钓鱼官网
     * headimg : http://semyooapp.wbteam.cn//Uploads/Avatar/1/123.jpg
     * content : 321
     * status : 1
     * floor : 1
     * interval : 1天前
     */

    private String id;
    private String uid;
    private String easemob_id;
    private String nickname;
    private String headimg;
    private String content;
    private String status;
    private String floor;
    private String interval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
