package com.likeits.sanyou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class CenterPosterBean implements Serializable{


    /**
     * id : 4
     * author : 13800138000
     * uid : 114
     * headimg : http://semyooapp.wbteam.cn/114/5a20bbada05be.png
     * easemob_id : 15918254234
     * title : 222
     * cover_list : ["http://semyooapp.wbteam.cn//Uploads/Picture/2017-12-14/5a32363f81c0e.jpg"]
     * interval : 31分钟前
     * view : 0
     * comment : 0
     * content : 222
     * status : 1
     */

    private String id;
    private String author;
    private String uid;
    private String headimg;
    private String easemob_id;
    private String title;
    private String interval;
    private String view;
    private String comment;
    private String content;
    private String status;
    private List<String> cover_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<String> getCover_list() {
        return cover_list;
    }

    public void setCover_list(List<String> cover_list) {
        this.cover_list = cover_list;
    }
}
