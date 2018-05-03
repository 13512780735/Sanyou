package com.likeits.sanyou.message.model;

/**
 * Created by Administrator on 2017/10/13.
 */

public class FriendBean {
    private String uid;
    private String headimg;
    private String easemob_id;
    private String name;
    private String letters;

    public FriendBean() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
