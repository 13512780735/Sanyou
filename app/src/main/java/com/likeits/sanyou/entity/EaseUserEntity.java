package com.likeits.sanyou.entity;

/**
 * Created by Administrator on 2017/12/1.
 */

public class EaseUserEntity {

    /**
     * uid : 1
     * nickname : 三友钓鱼官网
     * headimg : http://semyooapp.wbteam.cn//Uploads/Avatar/1/123.jpg
     * easemob_id : 1
     */

    private String uid;
    private String nickname;
    private String headimg;
    private String easemob_id;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
