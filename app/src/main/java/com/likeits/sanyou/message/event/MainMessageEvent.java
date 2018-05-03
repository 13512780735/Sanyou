package com.likeits.sanyou.message.event;



public class MainMessageEvent {
    public final static int OPEN_USERINFO=1;
    public final static int LOGIN_OUT=2;


    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
