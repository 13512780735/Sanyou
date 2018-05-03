package com.likeits.sanyou.message.model;

/**
 * Created by Administrator on 2017/9/29.
 */

public class EaseUserBean {

    /**
     * uid : 173
     * nickname : test
     * headimg : http://liuxueapp.wbteam.cn//Uploads/Avatar/173/59b25d6d549b0.png
     * easemob_id : 13656788888
     */

    private String uid;
    private String nickname;
    private String headimg;
    private String easemob_id;
    private String pinYinName;

    public String getPinYinName() {
        return pinYinName;
    }

    public void setPinYinName(String pinYinName) {
        this.pinYinName = pinYinName;
    }

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
